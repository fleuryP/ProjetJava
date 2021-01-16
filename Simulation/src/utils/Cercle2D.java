package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
/**
 * Un <code>Cercle2D</code> est une figure g�om�trique d�finie par son rayon.
 * Le cercle suivra l'emplacement de son centre dynamiquement.
 * @author GATTACIECCA Bastien
 */
public class Cercle2D extends Form2D {
	/**
	 * On utilise en r�alit� l'ellipse d�finie par la librairie java. Elle est
	 * d�finie incluse dans un rectangle, donc en red�finissant le rectangle
	 * comme �tant un carr�, on a bien un cercle :)
	 */
	private Ellipse2D.Double cercle;
	/**
	 * Diam�tre du cercle, inchangeable.
	 */
	private final int diameter;
	/**
	 * Construit un <code>Cercle2D</code> selon son diam�tre.
	 * @param d Le diam�tre du cercle.
	 */
	public Cercle2D(int d) {
		cercle = new Ellipse2D.Double();
		diameter = d;
	}
	/**
	 * {@inheritDoc}
	 */
	public void update(double x, double y, double angle) {
		centre.setLocation(x,y);
		cercle.setFrame(x - diameter/2,y - diameter/2,diameter,diameter);
	}
	/**
	 * {@inheritDoc}
	 * On va quand m�me pas faire tourner un cercle ...
	 */
	public void rotation(double angle) { }
	/**
	 * {@inheritDoc}
	 */
	public boolean contains(Point2D p) {
		return cercle.contains(p);
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean intersects(Form2D s) {
		if (s == null) throw new IllegalArgumentException("One of the arguments refer to 'null'.");
		if (s instanceof Cercle2D) {
			/*
			 * Deux cercles se croisent si la somme de leur rayon respectif est plus grande
			 * que la distance qui s�pare leur centre.
			 */
			Cercle2D C1 = (Cercle2D)s;
			double distance = Math.hypot(getCenterX() - C1.getCenterX(), getCenterY() - C1.getCenterY());
			return distance < getRayon() + C1.getRayon();
		}
		if (s instanceof Polygon2D || s instanceof Ligne2D)
			/*
			 * On a d�finit le croisement polygone/cercle dans la classe polygone, la m�thode est commutative !
			 * De m�me avec le croisement ligne/cercle
			 */
			return s.intersects(this);
		return false;
	}
	@Override
	public Rectangle2D getRectangularBounds() {
		return new Rectangle2D(
				getCenterX() - getRayon(), 
				getCenterY() - getRayon(), 
				getRayon()*2, 
				getRayon()*2); 
	}
	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics2D g, Color c) {
		g.setColor(c);
		g.draw(cercle);
	}
	/**
	 * @return Retourne le rayon du cercle.
	 */
	public double getRayon() {
		return cercle.getWidth()/2;
	}
}