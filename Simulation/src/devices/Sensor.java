package devices;
import agent.Robot;
import environment.PlateauGraphique;
/**
 * Classe abstraite mère de tous les capteurs. Les capteurs permettent au robot
 * de récupérer de l'information de leur environnement. Notre robot dispose de
 * trois capteurs qui permettent d'évaluer la distance (capteur à ultra sons <code>SonicSensor</code>),
 * de reconnaître une couleur au sol (capteur de couleur <code>ColorSensor</code>), et de savoir si un objet
 * se trouve dans le balancier (capteur tactile <code>TouchSensor</code>).
 * Précisons que vu de dessus, sur le plan (x,y), les trois capteurs se trouvent aux
 * mêmes coordonnées.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Sensor implements UpdatableDevice {
	/**
	 * Un capteur a accès à son environnement.
	 */
	private PlateauGraphique environnement;
	/**
	 * Un capteur donne les informations au robot sur lequel il est installé.
	 */
	private Robot robot;
	
	public Sensor(PlateauGraphique pg, Robot r) {
		if (pg == null) throw new IllegalArgumentException("A sensor needs an environment to be used.");
		if (r == null)	throw new IllegalArgumentException("A sensor needs a robot on which it is installed.");
		environnement = pg;
		robot = r;
	}
	
	public abstract void updateDevice();
	/**
	 * Récupère l'environnement alentour.
	 * @return l'environnement
	 */
	public PlateauGraphique getEnvironnement() {
		return environnement;
	}
	/**
	 * Récupère le robot.
	 * @return le robot
	 */
	public Robot getRobot() {
		return robot;
	}
}