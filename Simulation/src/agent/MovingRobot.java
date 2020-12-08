package agent;

import devices.BigMotor;
import devices.Motor;
import devices.UpdatableDevice.DevicesIndex;
import environment.PlateauGraphique;
/**
 * Classe qui précise les caractéristiques d'un <code>Robot</code> s'il
 * se déplace via les paramètres donnés par ses moteurs. La différence
 * de puissance passée en paramètre entre le <code>BigMotor</code> droit
 * et le <code>BigMotor</code> gauche permet de déterminer l'angle mais
 * aussi la vitesse du robot.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class MovingRobot extends Robot implements Movable {
	/**
	 * Le <code>BigMotor</code> gauche.
	 */
	private BigMotor leftMotor;
	/**
	 * Le <code>BigMotor</code> droit.
	 */
	private BigMotor rightMotor;

	public MovingRobot(PlateauGraphique pg, String name, double x, double y) {
		super(pg, name, x, y);
		leftMotor 	= (BigMotor)getComposant(DevicesIndex.INDEX_BIGMOTOR_LEFT);
		rightMotor 	= (BigMotor)getComposant(DevicesIndex.INDEX_BIGMOTOR_RIGHT);
		setAngle(0.05);
		startRotating(true);
	}
	private double speed;
	/**
	 * Calcule la nouvelle position et le nouvel angle du <code>Robot</code> en   v = d/t  <=> t = d/v <=> 
	 * fonction des paramètres donnés à ses deux <code>BigMotor</code>.
	 */
	@Override
	public void update() {
		super.update();
		/*
		 * On récupère la force moteur des deux moteurs.
		 */
		double vitesseMoteur1 = leftMotor.getSpeed();
		double vitesseMoteur2 = rightMotor.getSpeed();
		double delta = vitesseMoteur1 - vitesseMoteur2;
		/*
		 * Distance entre les roues.
		 */
		int longueurChassis = 35; //35 vient du robot.png où on regarde la distance en px 
		
		setAngle(getAngle() + Math.atan2(delta,longueurChassis));
		/*
		 * Détermine le point autour duquel le robot va tourner.
		 */
		speed =   (vitesseMoteur1 + vitesseMoteur2)/2;
		setX(getX() + Math.cos(getAngle()) * speed);
		setY(getY() + Math.sin(getAngle()) * speed);
	}
	/**
	 * Représentation textuelle de l'état du robot.
	 */
	public String toString() {
		String str = super.toString();
		return str + "  -  Type : Movable";
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void arreter() {
		leftMotor.setState(Motor.NULL_TURN);
		rightMotor.setState(Motor.NULL_TURN);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void immediateStop() {
		arreter();
		leftMotor.setImmediateSpeed(0);
		rightMotor.setImmediateSpeed(0);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void avancer(double speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startRotating(boolean dir) {
		leftMotor.setSpeed(dir ? 10d : -10d);
		rightMotor.setSpeed(dir ? -10d : 10d);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotation(double angle) {
		startRotating(angle > 0);
		Thread t = new Thread() {
			public void run() {
				while (Math.abs(getAngle() - angle) > 0.02) {
					try {sleep(PlateauGraphique.DELAY_MILLIS);} catch (InterruptedException e) {}
				}
				arreter();
			}
		};
		t.start();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getSpeed() {
		return speed;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMoving() {
		return leftMotor.getState() != Motor.NULL_TURN 
				&& rightMotor.getState() != Motor.NULL_TURN;
	}
}