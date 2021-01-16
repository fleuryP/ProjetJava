package utils;

import java.awt.geom.Point2D;
import java.util.Collection;
/**
 * Un <code>Rectangle2D</code> est un <code>Polygon2D</code> à quatre côtés perpendiculaires.
 * @author GATTACIECCA Bastien
 */
public class Rectangle2D extends Polygon2D {
	private double width, height;
	/**
	 * Construit un <code>Rectangle2D</code> droit, donc avec un angle nul.
	 * @param x la coordonnée x du coin supérieur gauche du rectangle.
	 * @param y la coordonnée y du coin supérieur gauche du rectangle.
	 * @param width la largeur du rectangle.
	 * @param height la hauteur du rectangle.
	 */
	public Rectangle2D(double x, double y, double width, double height) {
		super();
		this.width = width; this.height = height;
		
		points.add(new Point2D.Double(x,y));
		points.add(new Point2D.Double(x + width,y));
		points.add(new Point2D.Double(x + width,y + height));
		points.add(new Point2D.Double(x,y + height));
		centre.setLocation(x + width/2, y + height/2);
		for (int i = 0; i < points.size(); i++) {
			lignes.add(new Ligne2D());
		}
		relier();
	}
	/**
	 * Construis un <code>Rectangle2D</code> à partir de 4 points. Vérifie que les côtés
	 * formés sont perpendiculaires.
	 * @param p Une collection de 4 points.
	 */
	public Rectangle2D(Collection<Point2D> p) {
		super(p);
		if (p.size() != 4) throw new IllegalArgumentException("This isn't a rectangle right?");
		
		for (int i = 0; i < lignes.size(); i++) {
			double scal = 0;
			try {
				scal = Geometry.computesScalaire(lignes.get(i),lignes.get(i+1));
			}catch(IndexOutOfBoundsException e) {
				scal = Geometry.computesScalaire(lignes.get(i),lignes.get(0));
			}
			if (scal != 0) throw new IllegalArgumentException("This isn't a rectangle right?");
		}		
	}
	/**
	 * @return Retourne l'aire du rectangle.
	 */
	public double getAire() {
		return width * height;
	}
	/**
	 * @return Retourne la largeur du rectangle.
	 */
	public double getWidth() {
		return width;
	}
	/**
	 * @return Retourne la hauteur du rectangle.
	 */
	public double getHeight() {
		return height;
	}
}
