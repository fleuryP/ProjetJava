package agent;

import devices.BigMotor;
import devices.UpdatableDevice;
import environment.PlateauGraphique;
/**
 * Classe qui pr�cise les caract�ristiques d'un <code>Robot</code> s'il
 * se d�place via les param�tres donn�s par ses moteurs. La diff�rence
 * de puissance pass�e en param�tre entre le <code>BigMotor</code> droit
 * et le <code>BigMotor</code> gauche permet de d�terminer l'angle mais
 * aussi la vitesse du robot.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class MovingRobot extends Robot {
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
		leftMotor = (BigMotor)getComposant(UpdatableDevice.INDEX_BIGMOTOR_LEFT);
		rightMotor = (BigMotor)getComposant(UpdatableDevice.INDEX_BIGMOTOR_RIGHT);
		//leftMotor.setForce(0.2);
		//rightMotor.setForce(1);
	}
	@Override
	public void update() {
		super.update();
		/*
		 * On r�cup�re la force moteur des deux moteurs.
		 */
		double forceMoteur1 = leftMotor.getForce();
		double forceMoteur2 = rightMotor.getForce();
		double delta = forceMoteur1 - forceMoteur2;
		/*
		 * Distance entre les roues.
		 */
		int longueurChassis = getHeight() - 40; //40 vient du robot.png o� on regarde la distance en px 
		setAngle(getAngle() + Math.atan(delta / longueurChassis));
		/*
		 * D�termine le point autour duquel le robot va tourner.
		 */
		double sum =   forceMoteur1 + forceMoteur2;
		setX(getX() + Math.cos(getAngle()) * sum);
		setY(getY() + Math.sin(getAngle()) * sum);
	}
}