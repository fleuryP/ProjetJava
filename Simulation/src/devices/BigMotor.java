package devices;
/**
 * Classe qui décrit les fonctionnalités du gros moteur. Ce moteur est utilisé
 * pour déplacer une roue du robot. Un robot en possède 2, et un stabilisateur fixe à l'arrière.
 * Le moteur a pour caractéristiques d'avoir de l'inertie (il prend un certain temps à atteindre
 * la vitesse souhaitée) et de tourner moins vite qu'un Motor simple. 
 * On opte alors, en plus des variables décrites dans <code>Motor</code>, pour deux variables 
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
	 * Double de la force moteur à atteindre.
	 */
	private double reachForceMoteur;

	public BigMotor() {
		super();
		currentForceMoteur = 0;
		reachForceMoteur = 0;
	}
	/**
	 * Pour le moment l'incrémentation/décrémentation de la puissance moteur est linéaire.
	 * Si la force moteur actuelle <code>currentForceMoteur</code> est inférieure à la force moteur à 
	 * atteindre <code>reachForceMoteur</code>, on incrémente ; sinon on décrémente. On ajoute un palier 
	 * de [-0.005;0.005] pour avoir une zone d'arrêt sûre.
	 * Si la force moteur actuelle <code>currentForceMoteur</code> est égale à la force moteur à atteindre 
	 * <code>reachForceMoteur</code> à +/- 0.005, alors on dit que <code>currentForceMoteur = reachForceMoteur</code>.
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
		 * si currentForceMoteur € ]0,1],  alors state = POSITIVE_TURN
		 * sinon si currentForceMoteur € [-1,0[, alors state = NEGATIVE_TURN
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
	 * Accesseur à <code>reachForceMoteur</code> pour redéfinir la force du moteur à atteindre.
	 * Si <code>forceMoteurToReach</code> dépasse l'intervalle [-1,1], lève une <code>IllegalArgumentException</code>.
	 * @param forceMoteurToReach La Force moteur à atteindre.
	 */
	public void setForce(double forceMoteurToReach) {
		if (forceMoteurToReach < FORCE_MOTEUR_MIN || forceMoteurToReach > FORCE_MOTEUR_MAX) {
			throw new IllegalArgumentException(
					"argument = "+forceMoteurToReach+"; out of range ["+FORCE_MOTEUR_MIN+";"+FORCE_MOTEUR_MAX+"].");
		}
		reachForceMoteur = forceMoteurToReach;
	}
}