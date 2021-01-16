package agent;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utils.Cercle2D;
import utils.Form2D;
import utils.Maths;
import utils.Polygon2D;
/**
 * Classe abstraite qui caract�rise la shape (la forme) d'un <code>Objects</code>. Les <code>Shape
 * </code> permettent de g�rer sur le plateau les collisions entre <code>Objects</code> : c'est-�-dire
 * si un <code>Robot</code> en rencontre un autre, ou s'il rencontre un <code>Palet</code> ; mais aussi
 * invers�ment, il est n�cessaire de savoir lorsqu'un <code>Palet</code> rencontre un <code>Robot</code>
 * ou s'il rencontre un autre <code>Palet</code> car celui-ci est pouss� par un robot les pinces ferm�es !
 * <p>
 * Il n'y a que deux sous-classes d'<code>Objects</code> que sont <code>Palet</code> et <code>Robot</code>.
 * Leurs shapes respectives sont donc les deux classes static imbriqu�es de cette classe abstraite.
 * Les <code>Shape</code> poss�dent entre autres une m�thode <code>update(x,y,angle)</code> pour mettre � jour
 * la forme d'un Objects lorsque celui-ci se d�place. Une m�thode <code>intersects(Shape s)</code> pour savoir
 * quand les bounds d'un Objects croise les bounds d'un autre Objects. Ainsi qu'une m�thode 
 * <code>contains(Point2D p)</code> pour savoir si un point se situe dans les bounds.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Shape {
	/**
	 * Determine si les bounds de la <code>Shape</code> sont valides ou non.
	 * On utilise cet attribut avec les <code>Palet</code> notamment pour d�sactiver
	 * leur shape quand ils sont captur�s par un <code>Robot</code>.
	 */
	protected boolean invalidate;
	public boolean isValid() {return invalidate;}
	/**
	 * La forme g�om�trique qui caract�rise la forme de l'<code>Objects</code>.
	 */
	protected Form2D formeGeom;

	protected Shape() {
		invalidate = false;
	}
	/**
	 * Met � jour la forme d'un <code>Objects</code> lorsque celui-ci est mis � jour.
	 * @param x la coordonn�e x de l'objects.
	 * @param y la coordonn�e y de l'objects.
	 * @param angle l'angle de l'objects.
	 * @param ouverturePinces l'angle d'ouverture des pinces.
	 */
	public abstract void update(double x, double y, double angle, double ouverturePinces);
	/**
	 * Peint la forme g�om�trique qui correspond � la <code>Shape</code> de 
	 * l'<code>Objects</code>.
	 * @param g L'instance graphique courante.
	 */
	public void paint(Graphics2D g) {
		formeGeom.paint(g);
	}
	/**
	 * Permet de savoir quand la shape d'un <code>Objects</code> croise la shape de l'objects de
	 * l'instance courante. L�ve une IllegalArgumentException si l'objects en argument
	 * est aussi l'objects de l'instance courante : on refuse de tester si la forme d'un
	 * objects croise elle-m�me.
	 * @param s La shape de l'objects.
	 * @return true si la shape de l'objects croise celle de l'instance courante.
	 */
	public boolean intersects(Shape s) {
		if (this == s) throw new IllegalArgumentException("A shape cannot intersect itself.");
		if (s == null || invalidate || s.invalidate) return false;
		return formeGeom.intersects(s.formeGeom);
	}
	/**
	 * Permet de savoir si un point se situe dans la shape de l'<code>Objects</code>.
	 * @param p Un point <code>Point2D</code>.
	 * @return Si oui ou non il se situe dans la shape.
	 */
	public boolean contains(Point2D p) {
		if (p == null || invalidate) return false;
		return formeGeom.contains(p);
	}
	/**
	 * Retourne la forme g�om�trique de la <code>Shape</code>.
	 * @return une forme g�om�trique <code>Form2D</code>.
	 */
	public Form2D getForm() {
		return formeGeom;
	}
	/**
	 * Retourne la largeur de la shape de l'objects.
	 * @return la largeur.
	 */
	public abstract int getShapeWidth();
	/**
	 * Retourne la hauteur de la shape de l'objects.
	 * @return la hauteur.
	 */
	public abstract int getShapeHeight();
	/**
	 * D�crit la shape d'un <code>Robot</code>. Il s'agit d'un polygone qui correspond
	 * parfaitement � l'orientation du robot. Pour savoir si deux polygones se croisent (avec
	 * possiblement une orientation diff�rente), on regarde si au moins un de leurs segments 
	 * se croisent. Pour savoir si un polygone croise un cercle (la shape d'un <code>Palet</code>), 
	 * on regarde si l'une des lignes du polygone croise le cercle.
	 * @author GATTACIECCA Bastien
	 */
	public static class Robot extends Shape {
		/**
		 * Les points qui forment le polygone.
		 */
		protected ArrayList<Point2D> points;
		
		public Robot() {
			super();
			points = new ArrayList<Point2D>();
			/*
			 * Localisation relative des points que forment le robot en partant
			 * du centre.
			 */
			points.add(new Point2D.Double(-65,-24));
			points.add(new Point2D.Double(-30,-39));
			points.add(new Point2D.Double(-3,-39));
			points.add(new Point2D.Double()); //Point dynamique d�e � l'ouverture des pinces.
			points.add(new Point2D.Double()); //Idem.
			points.add(new Point2D.Double(-3,39));
			points.add(new Point2D.Double(-30,39));
			points.add(new Point2D.Double(-65,23));
			
			formeGeom = new Polygon2D(points);
		}
		/**
		 * {@inheritDoc}
		 */
		public void update(double x, double y, double angle, double ouverture) {
			double largeur = getShapeWidth() + Maths.givesSameScale(60d,-10d,PairePince.OUVERTURE_MIN,PairePince.OUVERTURE_MAX,ouverture);
			double anglePinces = Maths.givesSameScale(0,0.85d,PairePince.OUVERTURE_MIN,PairePince.OUVERTURE_MAX,ouverture);
			((Polygon2D)formeGeom).update(x, y, angle, anglePinces, largeur);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getShapeWidth() {
			return agent.Robot.getWidth();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getShapeHeight() {
			return agent.Robot.getHeight();
		}
	}
	/**
	 * Pour repr�senter la shape (la forme) d'un <code>Palet</code>, on utilise un cercle 
	 * <code>Cercle2D</code>.
	 * @author GATTACIECCA Bastien
	 */
	public static class Palet extends Shape {
		public Palet() {
			super();
			formeGeom = new Cercle2D(35);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update(double x, double y, double angle, double ouverturePinces) {
			formeGeom.update(x, y, angle);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getShapeWidth() {
			return agent.Palet.getWidth();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getShapeHeight() {
			return agent.Palet.getHeight();
		}		
	}
}