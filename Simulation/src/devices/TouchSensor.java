package devices;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import agent.Palet;
import agent.Robot;
import environment.PlateauGraphique;
import utils.Cercle2D;
import utils.Form2D;
import utils.Geometry;
import utils.Ligne2D;
/**
 * Classe qui décrit les caractéristiques du capteur tactile. Concrètement, un
 * balancier se situe dans les pinces du robot, lorsqu'un objet appuie sur celui-ci,
 * le bras du balancier appuie sur le capteur tactile. On a choisi de ne pas coder
 * les mouvements du balancier et de réduire l'information à "si les bornes d'un objet 
 * rencontrent la barre du balancier, alors celui-ci est déclenché". Les objets
 * qui sont présents sur le plateau et qui sont susceptibles de trigger le capteur sont des
 * sous-classes de <code>Palet</code>.s. On met à jour la position du balancier dans cette 
 * classe et non dans <code>Robot</code> car on n'envisage pas un robot avec un balancier 
 * mais sans capteur tactile.
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
	/**
	 * Le balancier est définit par une <code>Line2D</code>.
	 */
	private final Ligne2D balancier;
	/**
	 * Le palet qu'on récupère lorsque le capteur tactile détecte quelque chose. 
	 * La question est : quel Palet a t-il détecté ?
	 */
	private Palet source;

	public TouchSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		balancier = new Ligne2D();
		/**
		 * rien n'appuie sur le balancier au départ.
		 */
		state = TOUCH_RELEASED;
		source = null;
	}
	/**
	 * Met à jour la position du balancier.
	 * Vérifie si un Objects se trouve dans le balancier.
	 */
	@Override
	public void updateDevice() {
		//System.out.println(getState());
		/**
		 * Met à jour la position du balancier en fonction de la position
		 * du capteur tactile.
		 */
		Point2D.Double capteurs = robot.getPositionCapteurs();
		double angle = robot.getAngle() + Math.PI/2;
		balancier.update(
				capteurs.x + Robot.CAPTEUR_BALANCIER * Math.cos(angle),
				capteurs.y + Robot.CAPTEUR_BALANCIER * Math.sin(angle),
				capteurs.x - Robot.CAPTEUR_BALANCIER * Math.cos(angle),
				capteurs.y - Robot.CAPTEUR_BALANCIER * Math.sin(angle));
		/**
		 * Si les pinces sont fermées et que :
		 * - le robot n'a pas de palet : alors dans aucun cas le capteur tactile peut renvoyer une valeur vraie.
		 * - le robot aggripe un palet : alors les bounds de ce palet sont 'null' et le capteur ne touche rien.
		 * donc qqsoit la situation ; si les pinces sont fermées, le capteur renvoie RELEASED.
		 */
		if (robot.getPinces().isFullyClose()) {
			state = TOUCH_RELEASED;
			return;
		}
		/**
		 * On récupère tous les Palets ; susceptibles de trigger le capteur
		 * tactile.
		 */
		Collection<Palet> palets = environnement.getPalets();
		for (Palet p : palets) {
			Form2D c = p.getShape().getForm();
			if (Geometry.intersects(balancier,(Cercle2D)c)) {
				source = p;
				state = TOUCH_PRESSED;
				return;
			}
		}
		source = null;
		state = TOUCH_RELEASED;
	}
	/**
	 * Retourne l'état du capteur.
	 * @return state
	 */
	public boolean getState() {
		return state;
	}
	/**
	 * Retourne le Palet "source". Celui-ci est le palet qui touche
	 * le balancier.
	 * @return le Palet qui touche le balancier.
	 */
	public Palet getSource() {
		return source;
	}
	/**
	 * {@inheritDoc}
	 * Dessine le balancier en rouge lorsque rien n'appuie dessus, en vert
	 * lorsqu'un palet appuie dessus.
	 */
	public void paintDevice(Graphics2D g) {
		balancier.paint(g,state ? Color.GREEN : Color.RED);
	}
	/**
	 * Représentation textuelle du <code>TouchSensor</code>.
	 */
	public String toString() {
		String sensorString = super.toString();
		return sensorString + " = [pressed? = " + (state ? "yes]" : "no]");
	}
}
