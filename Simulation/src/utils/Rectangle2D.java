package utils;

import java.awt.geom.Point2D;
import java.util.Collection;

public class Rectangle2D extends Polygon2D {
	private double width, height;
	/**
	 * Construit un <code>Rectangle2D</code> droit, donc avec un angle nul.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectangle2D(double x, double y, double width, double height) {
		super();
		this.width = width; this.height = height;
		
		points.add(new Point2D.Double(x,y));
		points.add(new Point2D.Double(x + width,y));
		points.add(new Point2D.Double(x + width,y + height));
		points.add(new Point2D.Double(x,y + height));
		
		for (int i = 0; i < points.size(); i++) {
			lignes.add(new Ligne2D());
		}
		relier();
	}
	public Rectangle2D(Collection<Point2D> p) {
		super(p);
		if (p.size() != 4) throw new IllegalArgumentException("This isn't a rectangle right?");
		
		for (int i = 0; i < lignes.size(); i++) {
			double scal = 0;
			try {
				scal = Geometry.computesScalaire(lignes.get(i),lignes.get(i+1));
				if (scal != 0) throw new IllegalArgumentException("This isn't a rectangle right?");
			}catch(IndexOutOfBoundsException e) {
				scal = Geometry.computesScalaire(lignes.get(i),lignes.get(0));
				if (scal != 0) throw new IllegalArgumentException("This isn't a rectangle right?");
			}
		}		
	}
	@Override
	public void update(double x, double y, double angle) {
		if (points.size() != 4) throw new IllegalArgumentException("This isn't a rectangle right?");
		super.update(x,y,angle);
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
}
