package environment;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import agent.*;
/**
 * Classe qui décrit une instance "jouable" de <code>Plateau</code>. Ainsi,
 * un <code>PlateauGraphique</code> est capable de se rafraîchir pour mettre
 * à jour (appel itératif des méthodes update() et paint()) tous les objets 
 * qui la composent. C’est dans un <code>PlateauGraphique</code> que l’on va 
 * instancier les différents <code>Objects</code> présents sur le plateau.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 *
 */
public class PlateauGraphique extends Plateau implements	ActionListener,
															MouseMotionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * L'image du curseur lorsqu'on est en mode pointeur pour diriger
	 * un </code>ControlledRobot<code>.
	 */
	private static ImageIcon curseurIcon;
	static {
		curseurIcon = new ImageIcon("curseur.png");
	}
	/**
	 * Tous les </code>Objects<code> présents sur le plateau.
	 */
	private Objects[] objects;
	/**
	 * Sous-ensemble de objects, tous les </code>Palets<code> présents 
	 * sur le plateau.
	 */
	private Palet[] palets;
	/**
	 * Sous-ensemble de objects, tous les </code>Robots<code> présents
	 * sur le plateau.
	 */
	private Robot[] robots;
	/**
	 * Indique si on continue d'update les objects présents sur le plateau.
	 */
	private boolean pause;
	/**
	 * Indique si l'utilisateur souhaite utiliser un curseur pour diriger
	 * un </code>ControlledRobot<code>.
	 */
	private boolean needCursor;
	/**
	 * Timer pour boucler :)
	 */
	private boolean needDrag;
	private Timer timer;
	
	public PlateauGraphique() {
		/*
		 * On instancie dans l'ordre : les Palets, les ControlledRobot puis les MovingRobot
		 */
		objects = new Objects[] {
				new Palet(700,560),
				new Palet(500,280),
				new Palet(240,60),
				new MovingRobot(this,"Cedric",600,400),
				/*new Robot(this,"Adam",230,650)*/};
		
		initBothTabs(); //init palets et robots
		pause = false;
		needCursor = false;
		needDrag = false;
		/*
		 * Un Timer s'utilise sur un objet qui a le type "ActionListener". Ainsi,
		 * toutes les 20 ms, on va faire appel à la méthode implémentée ActionPerformed.
		 */
		timer = new Timer(10, this); 
		timer.start();
	}
	/**
	 * Update toutes les 20ms.
	 */
	public void update() {
		for (Objects obj : objects) {
			obj.update();
		}
	}
	/**
	 * Repaint toutes les 20ms.
	 */
	@Override
	public void paint(Graphics g) {
		if (plateau == null) return;
		g.drawImage(plateau,0,0,this);
		
		for (Objects obj : objects) {
			obj.paint(g);
		}
		/*
		 * Dessine le curseur lorsque l'on souhaite l'utiliser.
		 */
		if (needCursor) {
			curseurIcon.paintIcon(this,g,
					curseur.x-curseurIcon.getIconWidth()/2,
					curseur.y-curseurIcon.getIconHeight()/2);
		}
		/*
		 * Libère de l'espace mémoire puisqu'on n'utilise plus "l'ancienne image".
		 */
		g.dispose();
	}
	/**
	 * Permet d'initialiser les deux tableaux de <code>Palet</code> et de <code>Robot</code>.
	 * On sait simplement que dans le constructeur, on initialise d'abord les palets et
	 * ensuite les robots dans le tableau d'Objects.
	 */
	private void initBothTabs() {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Robot) {
				palets = new Palet[i];
				robots = new Robot[objects.length - i];
				for (int k = 0; k < i; k++) {
					palets[k] = (Palet)objects[k];
				}
				for (int k = i; k < objects.length; k++) {
					robots[k - i] = (Robot)objects[k];
				}
				return;
			}
		} //s'il n'y a pas de robots dans la liste Objects
		palets = new Palet[objects.length];
		for (int k = 0; k < objects.length; k++) {
			palets[k] = (Palet)objects[k];
		}
	}
	/**
	 * Retourne le tableau de <code>Palet</code>.
	 * @return tous les <code>Palet</code> du tableau.
	 */
	public Palet[] getPalets() {
		return palets;
	}
	/**
	 * Retourne le tableau de <code>Robot</code>.
	 * @return tous les <code>Robot</code> du tableau.
	 */
	public Robot[] getRobots() {
		return robots;
	}
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		curseur.setLocation(e.getX(),e.getY());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!pause) { //on update et on repaint tant qu'on est pas en pause
			update();
			repaint(); //la méthode repaint fait appel à la méthode repaint() de Component.
		}
	}
	/**
	 * Redéfini si on met la boucle itérative en pause ou non.
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	/**
	 * Renvoi si la boucle est en pause ou non.
	 */
	public boolean isInPause() {
		return pause;
	}
	/**
	 * Lorsque l'utilisateur demande un curseur, la fctnalité "drag" pour
	 * déplacer la fenêtre est momentanément indisponible puisqu'on utilise
	 * un <code>MouseMotionListener</code> pour déterminer la nouvelle position
	 * du curseur.
	 * @param needCursor si oui ou non l'utilisateur souhaite un curseur pour
	 * déplacer le <code>ControlledRobot</code>.
	 */
	public void setNeedCursor(boolean needCursor) {
		if (needCursor) {
			addMouseMotionListener(this);
			setDrag(false);
		}
		else removeMouseMotionListener(this);
		this.needCursor = needCursor;
	}
	/**
	 * Indique si oui ou non l'utilisateur est en train d'utiliser un curseur pour
	 * contrôler un <code>ControlledRobot</code>.
	 * @return
	 */
	public boolean isCursorDisplayed() {
		return needCursor;
	}
	public void setDrag(boolean needDrag) {
		if (needDrag) {
			setNeedCursor(false);
		}
		this.needDrag = needDrag;
	}
	public boolean isDragNeeded() {
		return needDrag;
	}
}