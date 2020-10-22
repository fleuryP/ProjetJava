package agent;

import devices.BigMotor;
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
		leftMotor 	= (BigMotor)getComposant(DevicesIndex.INDEX_BIGMOTOR_LEFT);
		rightMotor 	= (BigMotor)getComposant(DevicesIndex.INDEX_BIGMOTOR_RIGHT);
		//leftMotor.setForce(1);
		//rightMotor.setForce(-0.5);
	}
	@Override
	public void update() {
		super.update();
		/*
		 * On récupère la force moteur des deux moteurs.
		 */
		double forceMoteur1 = leftMotor.getForce();
		double forceMoteur2 = rightMotor.getForce();
		double delta = forceMoteur1 - forceMoteur2;
		/*
		 * Distance entre les roues.
		 */
		int longueurChassis = getHeight() - 40; //40 vient du robot.png où on regarde la distance en px 
		setAngle(getAngle() + Math.atan(delta / longueurChassis));
		/*
		 * Détermine le point autour duquel le robot va tourner.
		 */
		double sum =   forceMoteur1 + forceMoteur2;
		setX(getX() + Math.cos(getAngle()) * sum);
		setY(getY() + Math.sin(getAngle()) * sum);
	}
}