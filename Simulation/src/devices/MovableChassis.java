package devices;

import agent.Movable;
import agent.MovingRobot;
import agent.Robot;
import devices.UpdatableDevice.DevicesIndex;
/**
 * Le chassis d'un <code>MovingRobot</code> qui lui permet de se déplacer.
 * @author GATTACIECCA Bastien
 *
 */
public class MovableChassis implements Movable {
	private MovingRobot robot;
	private BigMotor left, right;
	private double speed;
	public MovableChassis(MovingRobot robot) {
		if (robot == null)
			throw new IllegalArgumentException("One of the two motors refers to 'null'.");
		this.robot = robot;
		left = (BigMotor)robot.getComposant(DevicesIndex.INDEX_BIGMOTOR_LEFT);
		right = (BigMotor)robot.getComposant(DevicesIndex.INDEX_BIGMOTOR_RIGHT);
	}
	/**
	 * {@inheritDoc}
	 */
	public void update() {
		/*
		 * On récupère la vitesse des deux moteurs.
		 */
		double vitesseMoteur1 = left.getSpeed();
		double vitesseMoteur2 = right.getSpeed();
		double delta = vitesseMoteur1 - vitesseMoteur2;

		robot.setAngle(robot.getAngle() + Math.atan2(delta,Robot.CHASSIS));
		/*
		 * Détermine le point autour duquel le robot va tourner.
		 */
		speed =   (vitesseMoteur1 + vitesseMoteur2)/2;
		robot.setX(robot.getX() + Math.cos(robot.getAngle()) * speed);
		robot.setY(robot.getY() + Math.sin(robot.getAngle()) * speed);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void arreter() {
		left.setState(Motor.NULL_TURN);
		right.setState(Motor.NULL_TURN);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void immediateStop() {
		arreter();
		left.setImmediateSpeed(0);
		right.setImmediateSpeed(0);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void avancer(double v, double dist) {
		left.setSpeed(v);
		right.setSpeed(v);
		new Deplacement(1000L*(long)(Math.abs(dist)/v));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startRotating(double v, boolean dir) {
		left.setSpeed(dir ? v : -v);
		right.setSpeed(dir ? -v : v);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotation(double v, double angle) {
		startRotating(v, angle > 0);
		new Deplacement((long)(v*angle*0.839));
	}
	/**
	 * @return Retourne la vitesse.
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * @return Indique si le chassis est en mouvement.
	 */
	public boolean isMoving() {
		return left.getState() != Motor.NULL_TURN &&
				right.getState() != Motor.NULL_TURN;
	}
	/**
	 * @author GATTACIECCA Bastien
	 *
	 */
	class Deplacement extends Thread {
		private long time = 0;
		public Deplacement(long wait) {
			if (wait < 0) {
				arreter();
				return;
			}
			time = wait;
			start();
		}
		@Override
		public void run() {
			try { sleep((long) time); } catch (InterruptedException e) {}
			arreter();
			interrupt();
		}
	}
}
