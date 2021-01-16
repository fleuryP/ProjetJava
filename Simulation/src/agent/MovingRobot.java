package agent;

import devices.MovableChassis;
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
	 * fonction des param�tres donn�s � ses deux <code>BigMotor</code>.
	 */
	@Override
	public void update() {
		super.update();
		chassis.update();
	}
	/**
	 * Repr�sentation textuelle de l'�tat du robot.
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