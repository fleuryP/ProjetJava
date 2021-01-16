package agent;

import devices.MovableChassis;
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
	 * Les <code>BigMotor</code> gauche et droit.
	 */
	private MovableChassis chassis;

	public MovingRobot(PlateauGraphique pg, String name, double x, double y) {
		super(pg, name, x, y);
		chassis = new MovableChassis(this);
		//chassis.rotation(20, 270);
	}
	/**
	 * Calcule la nouvelle position et le nouvel angle du <code>Robot</code> en
	 * fonction des paramètres donnés à ses deux <code>BigMotor</code>.
	 */
	@Override
	public void update() {
		super.update();
		chassis.update();
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
	public double getSpeed() {
		return chassis.getSpeed();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMoving() {
		return chassis.isMoving();
	}
}