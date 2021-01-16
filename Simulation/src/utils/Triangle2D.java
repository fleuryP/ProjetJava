package utils;

import java.awt.geom.Point2D;
import java.util.Collection;
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
	/**
	 * Construis un <code>Triangle2D</code> à partir de 3 points.
	 * @param p Une collection de 3 points.
	 */
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
	/**
	 * @return Retourne l'origine de ce triangle. Il s'agit d'un point remarquable que l'on note 
	 * à son instanciation.
	 */
	public Point2D getOrigine() {
		return origine;
	}
	/**
	 * @return Retourne la base de ce triangle, soit le premier tracé qui relie deux points.
	 */
	public Ligne2D getBase() {
		return lignes.get(0);
	}
	/**
	 * @return Retourne la hauteur de ce triangle.
	 */
	public double getHauteur() {
		return (2 * getAire()) / getBase().getDistance();
	}
	/**
	 * @return Retourne la mediane de ce triangle.
	 */
	public double getMediane() {
		/*
		 * 2*mediane² +1/2 BC² = AB² + AC²
		 * voir th. de la médiane sur wikipedia.
		 */
		double AB = lignes.get(1).getDistance();
		double AC = lignes.get(2).getDistance();
		double BC = getBase().getDistance();
		return Math.sqrt(-Math.pow(BC, 2)/4 + (Math.pow(AB, 2) + Math.pow(AC, 2))/2);
	}
	/**
	 * @return Retourne l'aire de ce triangle.
	 */
	public double getAire() {
		return getRectangularBounds().getAire()/2;
	}
}