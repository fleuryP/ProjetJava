package utils;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Un <code>Polygon2D</code> est une figure géométrique composée de lignes droites
 * reliées entre elles à leurs éxtrémités. Les lignes sont tracées dans l'ordre dans
 * lequel sont ajoutés les points. Un <code>Polygone2D</code> possède au moins trois
 * lignes.
 * 
 * @author GATTACIECCA Bastien
 */
public class Polygon2D extends Form2D {
	/**
	 * La liste des points qui composent le polygone.
	 */
	protected final ArrayList<Point2D> points;
	/**
	 * La liste des lignes qui composent le polygone.
	 */
	protected final ArrayList<Ligne2D> lignes;
	/**
	 * Les coordonnées du centre définit au-dessus.
	 */
	protected double centreX,centreY;
	/**
	 * Construit un polygone vide. Doit être construit par la suite.
	 */
	public Polygon2D() {
		points = new ArrayList<Point2D>();
		lignes = new ArrayList<Ligne2D>();
	}
	/**
	 * Construit un polygone à partir d'une liste de points. Les lignes seront tracées
	 * en liant les points dans l'ordre dans lequel ils sont.
	 * @param p Une collection de <code>Point2D</code>.
	 */
	public Polygon2D(Collection<Point2D> p) {
		this();
		if (p.size() < 3) throw new IllegalArgumentException("A polygon must get 3 points at least.");
		points.addAll(p);
		for (int i = 0; i < points.size(); i++) {
			lignes.add(new Ligne2D());
		}
		relier();
	}
	public void update(Collection<Point2D> p) {
		if (p.size() < 3) throw new IllegalArgumentException("A polygon must get 3 points at least.");
		points.clear();
		points.addAll(p);
		relier();
	}
	private double previousAngle;
	public void update(double x, double y, double angle, double ouverturePinces, double largeur) {
		update(x,y,angle);
		points.get(3).setLocation(centreX + largeur/3 * Math.cos(angle - 0.2 - ouverturePinces), centreY + largeur/3 * Math.sin(angle - 0.2 - ouverturePinces));
		points.get(4).setLocation(centreX + largeur/3 * Math.cos(angle + 0.2 + ouverturePinces), centreY + largeur/3 * Math.sin(angle + 0.2 + ouverturePinces));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(double x, double y, double angle) {
		/*
		 * translation.
		 */
		double dx = x - centreX;
		double dy = y - centreY;
		for (Point2D p : points) {
			p.setLocation(p.getX() + dx, p.getY() + dy);
		}
		centre.setLocation(x,y);
		centreX = centre.getX();
		centreY = centre.getY();
		/*
		 * rotation.
		 */
		double dAngle = angle - previousAngle;
		rotation(dAngle);
		previousAngle = angle;
		relier();
	}
	/**
	 * Tourne le polygone, en tournant tous ses points autour de son centre.
	 * @param angle L'angle d'orientation finale du polygone.
	 */
	@Override
	public void rotation(double angle) {
		for (Point2D p : points) {
			double dx = p.getX() - centreX;
			double dy = p.getY() - centreY;
			double distance = Math.hypot(dx,dy);
			double alpha = Math.atan2(dy,dx);
			p.setLocation(
					centreX + distance * Math.cos(angle+alpha),
					centreY + distance * Math.sin(angle+alpha));
		}
	}
	/**
	 * Relie tous les points entre eux par des segments. Chaque point est relié au suivant.
	 * Dès que l'on capture l'exception du dernier élément, on le relie avec le premier et
	 * le tour est joué :)
	 */
	protected void relier() {
		for (int i = 0; i < lignes.size(); i++) {
			try {lignes.get(i).update(points.get(i),points.get(i+1));
			}catch(IndexOutOfBoundsException e) {
				lignes.get(i).update(points.get(i),points.get(0));}
		}
	}
	/**
	 * {@inheritDoc}
	 * Pour savoir si un point M est contenu dans un polygone avec une certaine
	 * orientation, on fait juste une petite technique :
	 * Soit I le centre de gravité (intersection des médianes) du polygone ; si IM
	 * ne coupe aucune des lignes du polygone, alors M est contenu dans celui-ci.
	 */
	@Override
	public boolean contains(Point2D p) {
		Ligne2D IM = new Ligne2D(centre,p);
		for (Ligne2D l : lignes) {
			if (IM.intersects(l)) return false;
		}
		return true;
	}
	/**
	 * Retourne le point du Robot que le Palet touche.
	 * L'attribut retourné est nécessairement différent de <code>null</code>
	 * lorsqu'il est appelé, puisqu'il prend une valeur au moment où un robot
	 * croise un palet et la condition pour qu'un palet touche un robot est que
	 * cette valeur soit différente de 'null'.
	 */
	public Point2D hitPoint = null;	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Form2D s) {
		if (s == null) throw new IllegalArgumentException("The form refers to 'null'.");
		if (s instanceof Polygon2D) {
			ArrayList<Ligne2D> lignes2 = ((Polygon2D)s).lignes;
			for (int i = 0; i < lignes.size(); i++) {
				for (int j = i; j < lignes2.size(); j++) {
					if (lignes.get(i).intersects(lignes2.get(j))) return true;
				}
			}
		}
		if (s instanceof Ligne2D) {
			Ligne2D otherLine = (Ligne2D)s;
			for (Ligne2D l : lignes) {
				if (Geometry.intersectsOf(l, otherLine)) return true;
			}
		}
		if (s instanceof Cercle2D) {
			Cercle2D palet = (Cercle2D)s;
			for (Ligne2D l : lignes) {
				if ((hitPoint = Geometry.intersectsOf(l,palet)) != null) {
					return true;
				}
			}
		}
		hitPoint = null;
		return false;
	}
	@Override
	public Rectangle2D getRectangularBounds() {
		double minX,minY,maxX,maxY;
		minX = minY = Double.MAX_VALUE;
		maxX = maxY = Double.MIN_VALUE;
		for (Point2D p : points) {
			minX = Math.min(minX, p.getX()); maxX = Math.max(maxX, p.getX());
			minY = Math.min(minY, p.getY()); maxY = Math.max(maxY, p.getY());
		}
		return new Rectangle2D(minX, minY, maxX - minX, maxY - minY); 
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		g.fillRect((int)centre.getX(),(int)centre.getY(), 4, 4);
		for (Ligne2D l : lignes) {
			l.paint(g);
		}
	}
}