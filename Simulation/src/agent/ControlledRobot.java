package agent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import environment.Plateau;
import environment.PlateauGraphique;
/**
 * Classe qui d�crit les fonctionnalit�s d'un robot particulier : le <code>ControlledRobot</code>.
 * Un robot contr�l� est un robot qui utilise ses capteurs et ses moteurs via les fonctionnalit�s
 * qu'offrent la barre d'outils situ�e en bas de l'�cran. Il est �vident qu'un tel robot ne simule
 * absolument pas le comportement d'un robot r�el ; mais il est toutefois tr�s utile pour d'autres
 * situations. D'abord pour les d�veloppeurs de l'application pour pouvoir tester l'ensemble du
 * projet (tester facilement la prise de palet par les pinces, rel�cher un palet, la rotation des images,
 * les tests de collision graphique, etc). Ensuite pour les futurs utilisateurs de l'application qui
 * devront par exemple mettre en place une strat�gie de r�cup�ration de palet et en tester plusieurs.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 *
 */
public class ControlledRobot extends Robot {
	/**
	 * L'environnement graphique est r�cup�r� pour d�terminer quelles fonctionnalit�s sont demand�es.
	 */
	private PlateauGraphique pg;
	/**
	 * Indique lorsque l'utilisateur d�place le robot via une des fonctionnalit�s.
	 */
	private boolean moving;
	/**
	 * Construit un <code>ControlledRobot</code> en faisant un appel � super et en r�cup�rant
	 * l'environnement graphique. De plus, ajoute un listener pour la souris, pour pouvoir
	 * diriger le robot lorsqu'un mode de contr�le � la souris est demand� depuis l'environnement.
	 * @param pg l'environnement graphique dans lequel vit le robot.
	 * @param name le nom du robot.
	 * @param x coordonn�e x du robot.
	 * @param y coordonn�e y du robot.
	 */
	public ControlledRobot(PlateauGraphique pg, String name, double x, double y) {
		super(pg, name, x, y);
		this.pg = pg;
		moving = false;
		deplacerRobot();
	}
	/**
	 * Calcul la nouvelle position et le nouvel angle du robot en fonction de la position
	 * de la souris lorsque l'environnement graphique demande cette fonctionnalit�.
	 */
	@Override
	public void update() {
		super.update();
		if (pg.isCursorDisplayed()) {
			double distance = Math.hypot(
					Plateau.curseur.y - getY(), 
					Plateau.curseur.x - getX());
			if (distance > 10) {
				moving = true;
				setAngle(Math.atan2(
						Plateau.curseur.y - getY(), 
						Plateau.curseur.x - getX()));
				setX(getX() + Math.cos(getAngle()));
				setY(getY() + Math.sin(getAngle()));
			}
		}
	}
	/**
	 * @return La vitesse du <code>Robot</code>.
	 */
	public double getSpeed() {
		return 2;  
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMoving() {
		return moving;
	}
	/**
	 * Repr�sentation textuelle de l'�tat du robot.
	 */
	public String toString() {
		String str = super.toString();
		return str + "  -  Type : Controlled";
	}
	/**
	 * Ajoute un listener pour la souris sur le plateau. Lorsque la fonctionnalit� de drag
	 * est demand�e sur le plateau, elle repositionne le robot de cette instance en conservant
	 * son angle.
	 */
	private void deplacerRobot() {
		pg.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {moving = false;}
			public void mouseDragged(MouseEvent e) {
				if (!pg.isDragNeeded()) return;
				moving = true;
				setX(e.getX());
				setY(e.getY());
			}
		});
	}
}