package devices;

import agent.*;
import environment.*;
/**
 * 
 * Classe qui d�crit les caract�ristiques du capteur � ultrasons. Le capteur peut calculer
 * une distance en comptant le temps qu'a mis l'onde sonore pour partir et revenir, sachant
 * la vitesse de l'onde. Dans notre cas on fait partir un point de la position du capteur
 * dans la direction du robot jusqu'� ce qu'il rencontre un <code>Objects</code> ou alors
 * un des bords du plateau. On rappelle que le capteur a une port�e de 5-255cm et qu'on travaille
 * avec une �chelle de (1cm = 4px).
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class SonicSensor extends Sensor {
	/**
	 * Constante de la distance maximum per�ue.
	 */
	private static final int DISTANCE_MAX = 255	*4; //1020
	/**
	 * Constante de la distance minimum per�ue.
	 */
	private static final int DISTANCE_MIN = 5	*4; //20
	/**
	 * Distance entre le capteur et la position la plus proche d'un <code>Objects</code> ou 
	 * d'un bord du plateau 
	 */
	private int distance;
	
	public SonicSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		distance = DISTANCE_MIN;
	}
	/**
	 * Calcul de la distance.
	 */
	@Override
	public void updateDevice() {
		//System.out.println(getDistance());
		//on r�cup�re l'angle du robot
		double angle = getRobot().getAngle();
		for (int k = 0; k <= DISTANCE_MAX; k++) {
			/**
			 * Le point (posX,posY) suit la droite qui passe par la position du capteur
			 * � une distance k de celui-ci
			 */
			double posX = getRobot().getPositionCapteurs().x + k * Math.cos(angle);
			double posY = getRobot().getPositionCapteurs().y + k * Math.sin(angle);
			//si le point d�passe les bords du plateau, (ou un Objects : pas encore impl�ment�) ...
			if (posX <= 0 || posY <= 0 || posX >= Plateau.X || posY >= Plateau.Y) {
				distance = k;
				return;
			}
		}
		distance = DISTANCE_MAX;
	}
	/**
	 * Retourne la distance per�ue. <code>distance</code> ne peut pas �tre sup�rieur 
	 * � <code>DISTANCE_MAX</code>, le cas est d�j� g�r� dans la boucle.
	 * @return distance
	 */
	public int getDistance() {
		return (distance < DISTANCE_MIN) ? DISTANCE_MIN : distance;
	}
}