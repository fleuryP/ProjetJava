package environment;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import agent.ControlledRobot;
import agent.Movable;
import agent.MovingRobot;
import agent.Objects;
import agent.Palet;
import agent.Robot;
import utils.Polygon2D;

/**
 * Classe qui décrit une instance "jouable" de <code>Plateau</code>. Ainsi,
 * un <code>PlateauGraphique</code> est capable de se rafraîchir pour mettre
 * à jour (appel itératif des méthodes update(), handler(), et paint()) tous les objets 
 * qui la composent. C’est dans un <code>PlateauGraphique</code> que l’on va 
 * instancier les différents <code>Objects</code> présents sur le plateau.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
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
	private LinkedList<Objects> objects;
	/**
	 * L'itérateur de objects.
	 */
	private ListIterator<Objects> objectsIterator;
	/**
	 * Sous-ensemble de objects, l'unique </code>ControlledRobot<code> présent
	 * sur le plateau. On pourrait en même plusieurs mais on n'en souhaite qu'un
	 * seul.
	 */
	private ControlledRobot controlledRobot;
	/**
	 * Sous-ensemble de objects, tous les </code>MovingRobots<code> présents
	 * sur le plateau.
	 */
	private LinkedList<MovingRobot> movingRobots;
	/**
	 * Sous-ensemble de objects, tous les </code>Palets<code> présents 
	 * sur le plateau.
	 */
	private LinkedList<Palet> palets;
	/**
	 * Indique si on continue d'update les Objects présents sur le plateau.
	 */
	private boolean pause;
	/**
	 * Indique si l'utilisateur souhaite utiliser un curseur pour diriger
	 * un </code>ControlledRobot<code>.
	 */
	private boolean needCursor;
	/**
	 * Indique si l'utilisateur souhaite utiliser la souris pour drag
	 * un </code>ControlledRobot<code>.
	 */
	private boolean needDrag;
	/**
	 * Timer pour boucler :)
	 */
	private Timer timer;
	/**
	 * Constante de délai.
	 */
	public static final int DELAY_MILLIS = 20;
	/**
	 * Constante par laquelle on doit multiplier les valeurs de vitesse en px/s en vitesses de px/0.02s.
	 */
	public static final double SPEED_FACTOR = 1000.0d / PlateauGraphique.DELAY_MILLIS;


	public PlateauGraphique() {
		objects = new LinkedList<Objects>();
		/*
		 * On instancie dans l'ordre : le ControlledRobot, les MovingRobot, puis les Palets
		 */		
		controlledRobot = new ControlledRobot(this,"Mbappe",1000,250);

		movingRobots = new LinkedList<MovingRobot>();
		//		movingRobots.add(new MovingRobot(this,"Riri",120,400));
		//		movingRobots.add(new MovingRobot(this,"Fifi",1000,250));
		//		movingRobots.add(new MovingRobot(this,"Loulou",250,250));

		palets = new LinkedList<Palet>();
		palets.add(new Palet(360,200));
		palets.add(new Palet(600,200));
		palets.add(new Palet(840,200));
		palets.add(new Palet(360,400));
		palets.add(new Palet(600,400));
		palets.add(new Palet(840,400));
		palets.add(new Palet(360,600));
		palets.add(new Palet(600,600));
		palets.add(new Palet(840,600));

		objects.add(controlledRobot);
		objects.addAll(movingRobots);
		objects.addAll(palets);

		objectsIterator = objects.listIterator();

		pause = false;
		needCursor = false;
		needDrag = false;
		/*
		 * Un Timer s'utilise sur un objet qui a le type "ActionListener". Ainsi,
		 * toutes les 20 ms, on va faire appel à la méthode implémentée ActionPerformed.
		 */
		timer = new Timer(DELAY_MILLIS, this); 
		timer.start();
	}
	/**
	 * Update toutes les 20ms.
	 */
	public void update() {
		while(objectsIterator.hasNext()) {
			objectsIterator.next().update();
		}
	}
	/**
	 * Gestionnaire de collision toutes les 20ms.
	 */
	public void handler() {
		/*
		 * Avec cette boucle on peut faire un test d'intersects sans repasser
		 * deux fois sur le même objects ET sans tester l'intersection de deux
		 * objects identiques.
		 * 		R1	P1	P2	P3
		 * R1		*	*	*
		 * P1			*	*
		 * P2				*
		 * P3
		 */
		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				Objects anObject = objects.get(i);
				Objects anOtherObject = objects.get(j);

				if (anObject.intersects(anOtherObject)) {

					if (anObject instanceof Robot && anOtherObject instanceof Robot) {
						if(anObject instanceof ControlledRobot || anOtherObject instanceof ControlledRobot) continue;
						((Movable)anObject).immediateStop();
						((Movable)anOtherObject).immediateStop();

					}else if (anObject instanceof Palet && anOtherObject instanceof Palet) {
						Palet p1 = (Palet)anObject;
						Palet p2 = (Palet)anOtherObject;
						double deltaX = p1.getX() - p2.getX();
						double deltaY = p1.getY() - p2.getY();
						double angle = Math.atan2(deltaY,deltaX);
						/*
						 * Direction opposée des deux Palets, on a bien :
						 * 'angle' et 'angle + PI' qui sont opposés sur le cercle
						 * trigonométrique.
						 */
						p1.setX(p1.getX() + 2*Math.cos(angle));
						p1.setY(p1.getY() + 2*Math.sin(angle));
						p2.setX(p2.getX() + 2*Math.cos(angle + Math.PI));
						p2.setY(p2.getY() + 2*Math.sin(angle + Math.PI));

					}else {
						/*
						 * Ne peut pas être l'inverse, un palet ne peut pas intersects un robot.
						 * Pas de ClassCastException !
						 */
						Robot r = (Robot)anObject;
						Palet p = (Palet)anOtherObject;


						Point2D hitPoint = ((Polygon2D)r.getShape().getForm()).hitPoint;
						double deltaX = p.getX() - hitPoint.getX();
						double deltaY = p.getY() - hitPoint.getY();
						double distance = Math.hypot(deltaX, deltaY);
						double angle = Math.atan2(deltaY,deltaX);
						do {
							p.setX(p.getX() + (r.getSpeed() + 1/distance) * Math.cos(angle));
							p.setY(p.getY() + (r.getSpeed() + 1/distance) * Math.sin(angle));
							p.update();
							//if (r.getShape().getForm().conta)
						}while(r.intersects(p));

					}
				}
			}
		}
	}
	/**
	 * Repaint toutes les 20ms.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D)g;
		while(objectsIterator.hasPrevious()) {
			objectsIterator.previous().paint(g2);
		}
		/*
		 * Dessine le curseur lorsque l'on souhaite l'utiliser.
		 */
		if (needCursor) {
			curseurIcon.paintIcon(this,g2,
					curseur.x - curseurIcon.getIconWidth()/2,
					curseur.y - curseurIcon.getIconHeight()/2);
		}
		/*
		 * Libère de l'espace mémoire puisqu'on n'utilise plus "l'ancienne image".
		 */
		g.dispose();
		g2.dispose();
	}
	/**
	 * Retourne le tableau de <code>Objects</code>.
	 * @return tous les <code>Objects</code> du plateau.
	 */
	public Collection<Objects> getObjects() {
		return objects;
	}
	/**
	 * Retourne l'unique <code>ControlledRobot</code>.
	 * @return le <code>ControlledRobot</code> du tableau.
	 */
	public ControlledRobot getControlledRobot() {
		return controlledRobot;
	}
	/**
	 * Retourne le tableau de <code>MovingRobot</code>.
	 * @return tous les <code>MovingRobot</code> du tableau.
	 */
	public Collection<MovingRobot> getMovingRobots() {
		return movingRobots;
	}
	/**
	 * Retourne le tableau de <code>Palet</code>.
	 * @return tous les <code>Palet</code> du tableau.
	 */
	public Collection<Palet> getPalets() {
		return palets;
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
			handler();
			repaint(); //la méthode repaint fait appel à la méthode paint(Graphics g) de Component.
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