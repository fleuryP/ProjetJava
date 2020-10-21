package devices;
import agent.*;
import environment.*;
/**
 * Classe qui décrit les caractéristiques du capteur tactile. Concrètement, un
 * balancier se situe dans les pinces du robot, lorsqu'un objet appuie sur celui-ci,
 * le bras du balancier appuie sur le capteur tactile. On a choisi de ne pas coder
 * les mouvements du balancier et de réduire l'information à "si les bornes d'un objet 
 * rencontrent la barre du balancier, alors celui-ci est déclenché". Les objets
 * qui sont présents sur le plateau et qui sont susceptibles de trigger le capteur sont des
 * sous-classes de <code>Objects</code> : on distingue les <code>Palet</code>.s et les <code>Robot</code>.s
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 * @see Palet
 */
public class TouchSensor extends Sensor {
	/**
	 * Constante du capteur relâché.
	 */
	private static final boolean TOUCH_RELEASED = false;
	/**
	 * Constante du capteur enfoncé.
	 */             
	private static final boolean TOUCH_PRESSED = true;
	/**
	 * Etat du capteur, prend la valeur des deux constantes <code>TOUCH_RELEASED</code> 
	 * ou <code>TOUCH_PRESSED</code>.
	 */
	private boolean state;
	
	public TouchSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		//rien n'appuie sur le balancier au départ
		state = TOUCH_RELEASED;
	}
	/**
	 * Vérifie si un Objects se trouve dans le balancier.
	 */
	@Override
	public void updateDevice() {
		//on récupère le tableau de palets
		Palet[] p = super.getEnvironnement().getPalets();
		//System.out.println(getState());
		for (int i = 0; i < p.length; i++) {
			if(p[i].getBounds().contains(getRobot().getPositionCapteurs()/*à changer, on veut la barre du balancier par la pos. du capteur*/)) {
				//si les bounds (les "bornes") d'un palet contient les coordonnées du capteur,
				//on sort directement et l'état change.
				state = TOUCH_PRESSED;
				return;
			}
		}
		//si le capteur n'a touché aucun objet
		state = TOUCH_RELEASED;
	}
	/**
	 * Retourne l'état du capteur.
	 * @return state
	 */
	public boolean getState() {
		return state;
	}
}