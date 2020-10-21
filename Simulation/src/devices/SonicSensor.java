package devices;

import agent.*;
import environment.*;
/**
 * 
 * Classe qui décrit les caractéristiques du capteur à ultrasons. Le capteur peut calculer
 * une distance en comptant le temps qu'a mis l'onde sonore pour partir et revenir, sachant
 * la vitesse de l'onde. Dans notre cas on fait partir un point de la position du capteur
 * dans la direction du robot jusqu'à ce qu'il rencontre un <code>Objects</code> ou alors
 * un des bords du plateau. On rappelle que le capteur a une portée de 5-255cm et qu'on travaille
 * avec une échelle de (1cm = 4px).
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class SonicSensor extends Sensor {
	/**
	 * Constante de la distance maximum perçue.
	 */
	private static final int DISTANCE_MAX = 255	*4; //1020
	/**
	 * Constante de la distance minimum perçue.
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
		//on récupère l'angle du robot
		double angle = getRobot().getAngle();
		for (int k = 0; k <= DISTANCE_MAX; k++) {
			/**
			 * Le point (posX,posY) suit la droite qui passe par la position du capteur
			 * à une distance k de celui-ci
			 */
			double posX = getRobot().getPositionCapteurs().x + k * Math.cos(angle);
			double posY = getRobot().getPositionCapteurs().y + k * Math.sin(angle);
			//si le point dépasse les bords du plateau, (ou un Objects : pas encore implémenté) ...
			if (posX <= 0 || posY <= 0 || posX >= Plateau.X || posY >= Plateau.Y) {
				distance = k;
				return;
			}
		}
		distance = DISTANCE_MAX;
	}
	/**
	 * Retourne la distance perçue. <code>distance</code> ne peut pas être supérieur 
	 * à <code>DISTANCE_MAX</code>, le cas est déjà géré dans la boucle.
	 * @return distance
	 */
	public int getDistance() {
		return (distance < DISTANCE_MIN) ? DISTANCE_MIN : distance;
	}
}