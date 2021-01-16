package devices;
import java.awt.Graphics2D;

import agent.Robot;
import environment.PlateauGraphique;
/**
 * Classe abstraite m�re de tous les capteurs. Les capteurs permettent au robot
 * de r�cup�rer de l'information de leur environnement. Notre robot dispose de
 * trois capteurs qui permettent d'�valuer la distance (capteur � ultra sons <code>SonicSensor</code>),
 * de reconna�tre une couleur au sol (capteur de couleur <code>ColorSensor</code>), et de savoir si un objet
 * se trouve dans le balancier (capteur tactile <code>TouchSensor</code>).
 * Pr�cisons que vu de dessus, sur le plan (x,y), les trois capteurs se trouvent aux
 * m�mes coordonn�es.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Sensor implements UpdatableDevice {
	/**
	 * Un capteur a acc�s � son environnement.
	 */
	protected PlateauGraphique environnement;
	/**
	 * Un capteur donne les informations au robot sur lequel il est install�.
	 */
	protected Robot robot;
	
	public Sensor(PlateauGraphique pg, Robot r) {
		if (pg == null) throw new IllegalArgumentException("A sensor needs an environment to be used.");
		if (r == null)	throw new IllegalArgumentException("A sensor needs a robot on which it is installed.");
		environnement = pg;
		robot = r;
	}
	/**
	 * {@inheritDoc}
	 */
	public abstract void updateDevice();
	/**
	 * Permet � un <code>Sensor</code> de dessiner sa fonctionnalit�. Par exemple, un
	 * appel � <code>paintDevice(Graphics2D)</code> sur un <code>SonicSensor</code> peindra
	 * le c�ne de vision du capteur ainsi que la distance qui le relie � l'objet le plus proche.
	 * @param g Le contexte graphique dans lequel on dessine.
	 */
	public abstract void paintDevice(Graphics2D g);
	/**
	 * Repr�sentation textuelle d'un <code>Sensor</code>.
	 */
	public String toString() {
		return robot.getName() + "'s " + getClass().getSimpleName();
	}
}