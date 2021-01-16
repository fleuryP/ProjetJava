package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
/**
 * Un <code>Cercle2D</code> est une figure géométrique définie par son rayon.
 * Le cercle suivra l'emplacement de son centre dynamiquement.
 * @author GATTACIECCA Bastien
 */
public class Cercle2D extends Form2D {
	/**
	 * On utilise en réalité l'ellipse définie par la librairie java. Elle est
	 * définie incluse dans un rectangle, donc en redéfinissant le rectangle
	 * comme étant un carré, on a bien un cercle :)
	 */
	private Ellipse2D.Double cercle;
	/**
	 * Diamètre du cercle, inchangeable.
	 */
	private final int diameter;
	/**
	 * Construit un <code>Cercle2D</code> selon son diamètre.
	 * @param d Le diamètre du cercle.
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
	 * On va quand même pas faire tourner un cercle ...
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
			 * que la distance qui sépare leur centre.
			 */
			Cercle2D C1 = (Cercle2D)s;
			double distance = Math.hypot(getCenterX() - C1.getCenterX(), getCenterY() - C1.getCenterY());
			return distance < getRayon() + C1.getRayon();
		}
		if (s instanceof Polygon2D || s instanceof Ligne2D)
			/*
			 * On a définit le croisement polygone/cercle dans la classe polygone, la méthode est commutative !
			 * De même avec le croisement ligne/cercle
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