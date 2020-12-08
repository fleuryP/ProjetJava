package devices;

import environment.PlateauGraphique;

/**
 * Classe qui décrit les fonctionnalités du gros moteur. Ce moteur est utilisé
 * pour déplacer une roue du robot. Un robot en possède 2, et un stabilisateur fixe à l'arrière.
 * Le moteur a pour caractéristiques d'avoir de l'inertie (il prend un certain temps à atteindre
 * la vitesse souhaitée) et de tourner moins vite qu'un Motor simple. 
 * On opte alors, en plus des variables décrites dans <code>Motor</code>, pour deux variables 
 * Double qui indiquent la vitesse du moteur actuelle comprise entre deux constantes et celle que
 * le moteur doit atteindre.
 * La vitesse se note en px/s.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class BigMotor extends Motor {
	/**
	 * Constante de la vitesse minimum du moteur en px/s.
	 */
	private static final double SPEED_MIN = -300.0d;
	/**
	 * Constante de la vitesse maximum du moteur en px/s.
	 */
	private static final double SPEED_MAX =  300.0d;
	/**
	 * Fonction du modèle computationnel, elle décrit l'accélération du moteur.
	 * Elle prend en paramètre le temps t en SECONDE et la vitesse que le moteur doit
	 * atteindre.
	 * @param t l'instant t de l'accélération en seconde.
	 * @param speedToReach la vitesse en px/update.
	 */
	public static final double speedFunction(double t, double speedToReach) {
		return speedToReach == 0 ? 0 : speedToReach + (t - 0.15) / (2 * Math.pow(100,t - 0.15));
	}
	/**
	 * Attribut qui s'incrémente de 1 à chaque itération. Il permet de définir les
	 * variations de la vitesse avec la méthode <code>speedFunction(double,double)</code>
	 * à intervalle de temps régulier. 
	 */
	private double linearAcceleration;
	/**
	 * Double de la vitesse actuelle du moteur en px/update.
	 */
	private double currentSpeed;
	/**
	 * Double de la vitesse à atteindre en px/update.
	 */
	private double speedToReach;
	public BigMotor() {
		super();
		linearAcceleration = 0;
		currentSpeed = 0;
		speedToReach = 0;
	}
	/**
	 * On met à jour la vitesse actuelle <code>currentSpeed</code>.
	 */
	@Override
	public void updateDevice() {
		/**
		 * si currentSpeed > 0,  alors state = POSITIVE_TURN
		 * sinon si currentSpeed < 0, alors state = NEGATIVE_TURN
		 * sinon state = NULL_TURN
		 */
		byte b = currentSpeed > 0 ? POSITIVE_TURN : (currentSpeed < 0 ? NEGATIVE_TURN : NULL_TURN);
		super.setState(b);
		/**
		 * Incrémentations :
		 * On divise linearAcceleration par 50, car cet attribut s'incrémente de 1 à chaque itération. Or
		 * on itère toutes les 20ms, donc en multipliant linearAcceleration par 1/50 (0.020/1000) on donne bien
		 * en paramètre de <code>speedFunction(double,double)</code> un temps en <b>seconde</b>.
		 */
		currentSpeed = speedFunction(linearAcceleration / PlateauGraphique.SPEED_FACTOR,speedToReach);
		linearAcceleration ++;
	}
	/**
	 * @return La vitesse actuelle du moteur.
	 */
	public double getSpeed() {
		return currentSpeed;
	}
	/**
	 * Accesseur à <code>speedToReach</code> pour redéfinir la vitesse du moteur à atteindre.
	 * Si <code>speedToReach</code> dépasse l'intervalle, lève une <code>IllegalArgumentException</code>.
	 * Lorsque l'on redéfinit la vitesse, on réinitialise l'accélération du moteur à 0.
	 * @param speedMoteurToReach La vitesse du moteur à atteindre en px/s.
	 */
	public void setSpeed(double speedMoteurToReach) {
		if (speedMoteurToReach < SPEED_MIN || speedMoteurToReach > SPEED_MAX) {
			throw new IllegalArgumentException(
					"argument = "+speedMoteurToReach+"; out of range ["+SPEED_MIN+";"+SPEED_MAX+"].");
		}
		/*
		 * On convertit la vitesse en px/update ; autrement dit, en px/0.02s.
		 */
		speedMoteurToReach /= PlateauGraphique.SPEED_FACTOR;
		linearAcceleration = 0;
		speedToReach = speedMoteurToReach;
	}
	/**
	 * Accesseur à <code>currentSpeed</code> pour redéfinir la vitesse du moteur à atteindre de manière DIRECTE.
	 * Si <code>currentSpeed</code> dépasse l'intervalle, lève une <code>IllegalArgumentException</code> dans l'appel 
	 * de méthode setSpeed(double) : void. L'accès à cette méthode ne devrait être autorisé que pour le handler et en 
	 * aucun cas l'utilisateur.
	 * @param currentSpeed La vitesse moteur à atteindre directement.
	 */
	public void setImmediateSpeed(double currentSpeed) {
		setSpeed(currentSpeed);
		this.currentSpeed = currentSpeed;
	}
	/**
	 * Accesseur à <code>speedToReach</code> pour redéfinir l'état du moteur. Si <code>state</code> 
	 * n'est pas une des constantes d'état du moteur, lève une <code>IllegalArgumentException</code>.
	 * Cette méthode redéfinit </code>setState(byte)</code> de </code>Motor</code> pour (ici) définir 
	 * directement la vitesse moteur à atteindre à </code>SPEED_MIN</code>, 0 ou </code>SPEED_MAX</code>.
	 * @param state Le nouvel état du moteur.
	 */
	public void setState(byte state) {
		switch(state) {
		case POSITIVE_TURN: 
			speedToReach = SPEED_MAX;
			return;
		case NEGATIVE_TURN: 
			speedToReach = SPEED_MIN;
			return;
		case NULL_TURN: 
			speedToReach = 0;
			return;
		default: throw new IllegalArgumentException(
				"argument = "+state+"; must be "+NEGATIVE_TURN+", "+NULL_TURN+", or "+POSITIVE_TURN);
		}
	}
	/**
	 * Représentation textuelle du <code>BigMoteur</code>.
	 */
	public String toString() {
		return "BigMotor[reachSpeed = "+speedToReach * PlateauGraphique.SPEED_FACTOR+"; speed = " + currentSpeed * PlateauGraphique.SPEED_FACTOR + "]";
	}
}