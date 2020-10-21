package devices;
/**
 * Classe qui d�crit les fonctionnalit�s du gros moteur. Ce moteur est utilis�
 * pour d�placer une roue du robot. Un robot en poss�de 2, et un stabilisateur fixe � l'arri�re.
 * Le moteur a pour caract�ristiques d'avoir de l'inertie (il prend un certain temps � atteindre
 * la vitesse souhait�e) et de tourner moins vite qu'un Motor simple. 
 * On opte alors, en plus des variables d�crites dans <code>Motor</code>, pour deux variables 
 * Double qui indiquent la force du moteur actuelle comprise entre deux constantes et celle que
 * le moteur doit atteindre.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class BigMotor extends Motor {
	/**
	 * Constante de la force moteur minimum.
	 */
	public static final double FORCE_MOTEUR_MIN = -1.0;
	/**
	 * Constante de la force moteur maximum.
	 */
	public static final double FORCE_MOTEUR_MAX =  1.0;
	/**
	 * Double de la force moteur actuelle, comprise entre <code>FORCE_MOTEUR_MIN</code> 
	 * et <code>FORCE_MOTEUR_MAX</code> inclus.
	 */
	private double currentForceMoteur;
	/**
	 * Double de la force moteur � atteindre.
	 */
	private double reachForceMoteur;

	public BigMotor() {
		super();
		currentForceMoteur = 0;
		reachForceMoteur = 0;
	}
	/**
	 * Pour le moment l'incr�mentation/d�cr�mentation de la puissance moteur est lin�aire.
	 * Si la force moteur actuelle <code>currentForceMoteur</code> est inf�rieure � la force moteur � 
	 * atteindre <code>reachForceMoteur</code>, on incr�mente ; sinon on d�cr�mente. On ajoute un palier 
	 * de [-0.005;0.005] pour avoir une zone d'arr�t s�re.
	 * Si la force moteur actuelle <code>currentForceMoteur</code> est �gale � la force moteur � atteindre 
	 * <code>reachForceMoteur</code> � +/- 0.005, alors on dit que <code>currentForceMoteur = reachForceMoteur</code>.
	 */
	@Override
	public void updateDevice() {
		if (currentForceMoteur < reachForceMoteur - 0.005) {
			currentForceMoteur += 0.01; //0.001 est purement arbitraire pour le moment
		}else if (currentForceMoteur > reachForceMoteur + 0.005) {
			currentForceMoteur -= 0.01;
		}else {
			currentForceMoteur = reachForceMoteur;
		}
		/**
		 * si currentForceMoteur � ]0,1],  alors state = POSITIVE_TURN
		 * sinon si currentForceMoteur � [-1,0[, alors state = NEGATIVE_TURN
		 * sinon state = NULL_TURN
		 */
		byte b = currentForceMoteur > 0 ? POSITIVE_TURN : (currentForceMoteur < 0 ? NEGATIVE_TURN : NULL_TURN);
		setState(b);
	}
	/**
	 * @return La force moteur actuelle.
	 */
	public double getForce() {
		return currentForceMoteur;
	}
	/**
	 * Accesseur � <code>reachForceMoteur</code> pour red�finir la force du moteur � atteindre.
	 * Si <code>forceMoteurToReach</code> d�passe l'intervalle [-1,1], l�ve une <code>IllegalArgumentException</code>.
	 * @param forceMoteurToReach La Force moteur � atteindre.
	 */
	public void setForce(double forceMoteurToReach) {
		if (forceMoteurToReach < FORCE_MOTEUR_MIN || forceMoteurToReach > FORCE_MOTEUR_MAX) {
			throw new IllegalArgumentException(
					"argument = "+forceMoteurToReach+"; out of range ["+FORCE_MOTEUR_MIN+";"+FORCE_MOTEUR_MAX+"].");
		}
		reachForceMoteur = forceMoteurToReach;
	}
}