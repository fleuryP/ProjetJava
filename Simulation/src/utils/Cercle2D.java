package utils;

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
	Ellipse2D.Double cercle;
	/**
	 * Diam�tre du cercle, inchangeable.
	 */
	private final int diameter;
	/**
	 * Les coordonn�es du centre d�finit au-dessus.
	 */
	private double centreX,centreY;
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
			 * que la distance qui s�pare leur centre.
			 */
			Cercle2D C1 = (Cercle2D)s;
			double distance = Math.hypot(centreX - C1.centreX, centreY - C1.centreY);
			return distance < getDiametre()/2 + C1.getDiametre()/2;
		}
		if (s instanceof Polygon2D)
			/*
			 * On a d�finit le croisement polygone/cercle dans la classe polygone, la m�thode est commutative !
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