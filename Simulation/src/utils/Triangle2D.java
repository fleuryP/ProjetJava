package utils;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import agent.Objects;
/**
 * Un <code>Triangle2D</code> est une forme géométrique. Il s'agit plus particulièrement
 * d'un <code>Polygone2D</code> avec la particularité de n'avoir que trois points (captain obvious).
 * 
 * @author GATTACIECCA Bastien
 *
 */
public class Triangle2D extends Polygon2D {
	private Point2D origine;
	/**
	 * Construit un <code>Triangle2D</code>.
	 * @param p Une collection de 3 points.
	 */
	public Triangle2D(Collection<Point2D> p) {
		super(p);
		if (p.size() != 3) throw new IllegalArgumentException("Are you sure that's a triangle? :thinking:");
		origine = new Point2D.Double();
	}
	public void update(Collection<Point2D> p) {
		if (p.size() != 3) throw new IllegalArgumentException("Are you sure that's a triangle? :thinking:");
		points.clear();
		points.addAll(p);
		origine.setLocation(points.get(0));
		double sumX=0,sumY=0;
		for (Point2D point : p) {
			sumX += point.getX(); sumY += point.getY();
		}
		centre.setLocation(sumX/3, sumY/3);
		relier();
	}
	public boolean contains(Objects o) {
		Point2D centreO = o.getShape().getForm().getCenter();//on récup le centre de la forme géom de la shape de l'objects
		Ligne2D IM = new Ligne2D(centreO,centre);
		for (Ligne2D l : lignes)
			if (Geometry.intersectsOf(IM, l)) return false;
		return true;
	}
	public Point2D getOrigine() {
		return origine;
	}
	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics2D g) {
		super.paint(g);
		g.fillRect((int)centre.getX(),(int)centre.getY(), 4, 4);
	}
}