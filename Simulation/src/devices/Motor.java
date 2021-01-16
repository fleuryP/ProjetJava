package devices;

import environment.PlateauGraphique;

/**
 * Classe qui d�crit les fonctionnalit�s du moteur moyen. Ce moteur est utilis�
 * pour ouvrir ou fermer les pinces. L'axe du moteur fait tourner une vis sans
 * fin qui fait tourner deux roues dent�es lat�rales (a pour effet l'ouverture 
 * sym�trique des deux pinces). La diff�rence de ratio fait que les pinces s'ouvrent 
 * assez lentement. Le moteur a pour caract�ristiques d'avoir tr�s peu d'inertie 
 * (il atteint la vitesse max quasi imm�diatement) et tourne assez vite. On 
 * opte alors pour des constantes qui indiquent le sens du mouvement du moteur uniquement.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class Motor implements UpdatableDevice {
	/**
	 * Constante mouvement moteur sens horaire.
	 */
	public static final byte POSITIVE_TURN =  1;
	/**
	 * Constante mouvement moteur sens anti-horaire.
	 */
	public static final byte NEGATIVE_TURN = -1;
	/**
	 * Constante sans mouvement moteur.
	 */
	public static final byte NULL_TURN =  0;
	/**
	 * Constante de la vitesse du moteur. 0.01 est purement arbitraire 
	 * pour le moment. Il s'agit de la vitesse d'ouverture des pinces.
	 */
	public static final double SPEED_TURN = 0.5 / PlateauGraphique.SPEED_FACTOR;
	/**
	 * Indique l'�tat courant du moteur {<code>POSITIVE_TURN</code>, 
	 * <code>NEGATIVE_TURN</code>, <code>NULL_TURN</code>}.
	 */
	private byte currentState;

	public Motor() {
		currentState = NULL_TURN;
	}
	@Override
	public void updateDevice() {
		
	}
	/**
	 * @return L'�tat courant du moteur.
	 */
	public byte getState() {
		return currentState;
	}
	/**
	 * Accesseur � <code>currentState</code> pour red�finir l'�tat 
	 * du moteur. Si <code>currentState</code> n'est pas une des 
	 * constantes d'�tat du moteur, l�ve une <code>IllegalArgumentException</code>.
	 * @param state Le nouvel �tat du moteur.
	 */
	public void setState(byte state) {
		switch(state) {
		case POSITIVE_TURN: 
			currentState = POSITIVE_TURN;
			return;
		case NEGATIVE_TURN: 
			currentState = NEGATIVE_TURN;
			return;
		case NULL_TURN: 
			currentState = NULL_TURN;
			return;
		default: throw new IllegalArgumentException(
				"argument = "+state+"; must be "+NEGATIVE_TURN+", "+NULL_TURN+", or "+POSITIVE_TURN);
		}
	}
	/**
	 * Repr�sentation textuelle du <code>Moteur</code>.
	 */
	public String toString() {
		String constantName = currentState == NULL_TURN ? "NULL_TURN" : currentState == POSITIVE_TURN ? "POSITIVE_TURN" : "NEGATIVE_TURN";
		return getClass().getSimpleName() + "[state = " + constantName + "]";
	}
}