package devices;

import environment.PlateauGraphique;

/**
 * Classe qui d�crit les fonctionnalit�s du gros moteur. Ce moteur est utilis�
 * pour d�placer une roue du robot. Un robot en poss�de 2, et un stabilisateur fixe � l'arri�re.
 * Le moteur a pour caract�ristiques d'avoir de l'inertie (il prend un certain temps � atteindre
 * la vitesse souhait�e) et de tourner moins vite qu'un Motor simple. 
 * On opte alors, en plus des variables d�crites dans <code>Motor</code>, pour deux variables 
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
	 * Fonction du mod�le computationnel, elle d�crit l'acc�l�ration du moteur.
	 * Elle prend en param�tre le temps t en SECONDE et la vitesse que le moteur doit
	 * atteindre.
	 * @param t l'instant t de l'acc�l�ration en seconde.
	 * @param speedToReach la vitesse en px/update.
	 */
	public static final double speedFunction(double t, double speedToReach) {
		return speedToReach == 0 ? 0 : speedToReach + (t - 0.15) / (2 * Math.pow(100,t - 0.15));
	}
	/**
	 * Attribut qui s'incr�mente de 1 � chaque it�ration. Il permet de d�finir les
	 * variations de la vitesse avec la m�thode <code>speedFunction(double,double)</code>
	 * � intervalle de temps r�gulier. 
	 */
	private double linearAcceleration;
	/**
	 * Double de la vitesse actuelle du moteur en px/update.
	 */
	private double currentSpeed;
	/**
	 * Double de la vitesse � atteindre en px/update.
	 */
	private double speedToReach;
	public BigMotor() {
		super();
		linearAcceleration = 0;
		currentSpeed = 0;
		speedToReach = 0;
	}
	/**
	 * On met � jour la vitesse actuelle <code>currentSpeed</code>.
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
		 * Incr�mentations :
		 * On divise linearAcceleration par 50, car cet attribut s'incr�mente de 1 � chaque it�ration. Or
		 * on it�re toutes les 20ms, donc en multipliant linearAcceleration par 1/50 (0.020/1000) on donne bien
		 * en param�tre de <code>speedFunction(double,double)</code> un temps en <b>seconde</b>.
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
	 * Accesseur � <code>speedToReach</code> pour red�finir la vitesse du moteur � atteindre.
	 * Si <code>speedToReach</code> d�passe l'intervalle, l�ve une <code>IllegalArgumentException</code>.
	 * Lorsque l'on red�finit la vitesse, on r�initialise l'acc�l�ration du moteur � 0.
	 * @param speedMoteurToReach La vitesse du moteur � atteindre en px/s.
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
	 * Accesseur � <code>currentSpeed</code> pour red�finir la vitesse du moteur � atteindre de mani�re DIRECTE.
	 * Si <code>currentSpeed</code> d�passe l'intervalle, l�ve une <code>IllegalArgumentException</code> dans l'appel 
	 * de m�thode setSpeed(double) : void. L'acc�s � cette m�thode ne devrait �tre autoris� que pour le handler et en 
	 * aucun cas l'utilisateur.
	 * @param currentSpeed La vitesse moteur � atteindre directement.
	 */
	public void setImmediateSpeed(double currentSpeed) {
		setSpeed(currentSpeed);
		this.currentSpeed = currentSpeed;
	}
	/**
	 * Accesseur � <code>speedToReach</code> pour red�finir l'�tat du moteur. Si <code>state</code> 
	 * n'est pas une des constantes d'�tat du moteur, l�ve une <code>IllegalArgumentException</code>.
	 * Cette m�thode red�finit </code>setState(byte)</code> de </code>Motor</code> pour (ici) d�finir 
	 * directement la vitesse moteur � atteindre � </code>SPEED_MIN</code>, 0 ou </code>SPEED_MAX</code>.
	 * @param state Le nouvel �tat du moteur.
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
	 * Repr�sentation textuelle du <code>BigMoteur</code>.
	 */
	public String toString() {
		return "BigMotor[reachSpeed = "+speedToReach * PlateauGraphique.SPEED_FACTOR+"; speed = " + currentSpeed * PlateauGraphique.SPEED_FACTOR + "]";
	}
}