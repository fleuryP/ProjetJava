package utils;

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
	Ellipse2D.Double cercle;
	/**
	 * Diamètre du cercle, inchangeable.
	 */
	private final int diameter;
	/**
	 * Les coordonnées du centre définit au-dessus.
	 */
	private double centreX,centreY;
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
		centreX = x; centreY = y;
		cercle.setFrame(x - diameter/2,y - diameter/2,diameter,diameter);
	}
	public void rotation(double angle) { }
	/**
	 * {@inheritDoc}
	 */
	public boolean contains(Point2D p) {
		return cercle.contains(p);
	}
	public boolean contains(double x, double y) {
		return cercle.contains(x,y);
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
			double distance = Math.hypot(centreX - C1.centreX, centreY - C1.centreY);
			return distance < getDiametre()/2 + C1.getDiametre()/2;
		}
		if (s instanceof Polygon2D)
			/*
			 * On a définit le croisement polygone/cercle dans la classe polygone, la méthode est commutative !
			 */
			return s.intersects(this);
		if (s instanceof Ligne2D)
			return s.intersects(this);
		return false;
	}
	@Override
	public Rectangle2D getRectangularBounds() {
		return new Rectangle2D(centreX - getDiametre()/2, centreY - getDiametre()/2, getDiametre(), getDiametre()); 
	}
	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics2D g) {
		super.paint(g);
		g.draw(cercle);
	}
	public double getDiametre() {
		return cercle.getWidth();
	}
	public Ellipse2D getCercle() {
		return cercle;
	}
}