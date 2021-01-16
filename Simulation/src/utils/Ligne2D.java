package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;
/**
 * Une <code>Ligne2D</code> est une forme géométrique définie par ses deux extrémités.
 * Elle apporte davantage de fonctionnalités qu'une simple <code>Line2D</code> de la
 * librairie java notamment avec ses nombreux getters. On utilisera le mot "ligne" pour
 * en réalité signifier un segment.
 * @author GATTACIECCA Bastien
 */
public class Ligne2D extends Form2D {
	/**
	 * La ligne qui est représentée par cette classe.
	 */
	protected Line2D L;
	/**
	 * La longueur du segment.
	 */
	private double distance;
	/**
	 * La direction du vecteur directeur, qui part de la première extrémité du segment
	 * et qui se dirige vers la deuxième extrémité de celui-ci.
	 */
	private double angle;
	/**
	 * Construit un segment de longueur sqrt(2) et partant de l'origine de repère.
	 */
	public Ligne2D() {
		this(0,0,1,1);
	}
	/**
	 * Construit un segment à partir de deux points en paramètres.
	 * @param P1 Un point.
	 * @param P2 Un autre point.
	 */
	public Ligne2D(Point2D P1, Point2D P2) {
		this(P1.getX(),P1.getY(),P2.getX(),P2.getY());
	}
	/**
	 * Construit un segment à partir des coordonnées de deux points en paramètre.
	 * @param x1 la coordonnée x du premier point.
	 * @param y1 la coordonnée y du premier point.
	 * @param x2 la coordonnée x du deuxième point.
	 * @param y2 la coordonnée y du deuxième point.
	 */
	public Ligne2D(double x1, double y1, double x2, double y2) {
		L = new Line2D.Double(x1,y1,x2,y2);
		centre.setLocation((x1+x2)/2,(y1+y2)/2);
		distance = Math.hypot(getDx(), getDy());
		angle = Math.atan2(getDy(), getDx());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(double x, double y, double angle) {
		this.angle = angle;
		L.setLine(x,y, x + distance * Math.cos(angle), y + distance * Math.sin(angle));
		centre.setLocation((getX1()+getX2())/2,(getY1()+getY2())/2);
		rotation(angle);
	}
	/**
	 * Relocalise le segment avec de nouvelles coordonnées pour ses deux extrémités.
	 * @param x1 la coordonnée x du premier point.
	 * @param y1 la coordonnée y du premier point.
	 * @param x2 la coordonnée x du deuxième point.
	 * @param y2 la coordonnée y du deuxième point.
	 */
	public void update(double x1, double y1, double x2, double y2) {
		L.setLine(x1,y1,x2,y2);
		centre.setLocation((x1+x2)/2,(y1+y2)/2);
		distance = Math.hypot(getDx(), getDy());
		angle = Math.atan2(getDy(), getDx());
	}
	/**
	 * Relocalise le segment avec de nouvelles extrémités.
	 * @param P1 Un point.
	 * @param P2 Un autre point.
	 */
	public void update(Point2D P1, Point2D P2) {
		update(P1.getX(),P1.getY(),P2.getX(),P2.getY());
	}
	/**
	 * Tourne ce segment sur son centre de l'angle fournit en paramètre.
	 * @param angle L'angle de rotation du segment.
	 */
	@Override
	public void rotation(double angle) {
		double x = getX1() - getCentreX();
		double y = getY1() - getCentreY();
		double dist = Math.hypot(x, y);
		getP1().setLocation(getCentreX() + dist * Math.cos(angle), getCentreY() + dist * Math.sin(angle));
		getP2().setLocation(getCentreX() + dist * Math.cos(Math.PI + angle), getCentreY() + dist * Math.sin(Math.PI + angle));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics2D g, Color c) {
		g.setColor(c);
		g.draw(L);
	}
	/**
	 * {@inheritDoc}
	 * Un segment ne peut pas contenir un point.
	 */
	@Override
	public boolean contains(Point2D p) {
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Form2D s) {
		if (s == null) throw new IllegalArgumentException("The form refers to 'null'.");
		if (s instanceof Ligne2D)
			return Geometry.intersects(this,(Ligne2D)s);
		if (s instanceof Polygon2D)
			return s.intersects(this);
		if (s instanceof Cercle2D)
			return Geometry.intersects(this, (Cercle2D)s);
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle2D getRectangularBounds() {
		Vector<Point2D> fourPoints = new Vector<Point2D>();
		fourPoints.add(new Point2D.Double(getX1(),getY1()));
		fourPoints.add(new Point2D.Double(getX2(),getY1()));
		fourPoints.add(new Point2D.Double(getX2(),getY2()));
		fourPoints.add(new Point2D.Double(getX1(),getY2()));
		return new Rectangle2D(fourPoints);
	}
	/**
	 * @return Retourne le <code>Point2D</code> de la première extrémité
	 * du segment.
	 */
	public Point2D getP1() {
		return L.getP1();
	}
	/**
	 * @return Retourne le <code>Point2D</code> du centre du segment.
	 */
	public Point2D getCentre() {
		return centre;
	}
	/**
	 * @return Retourne le <code>Point2D</code> de la deuxième extrémité
	 * du segment.
	 */
	public Point2D getP2() {
		return L.getP2();
	}
	/**
	 * @return Retourne la coordonnée x de la première extrémité du segment.
	 */
	public double getX1() {
		return getP1().getX();
	}
	/**
	 * @return Retourne la coordonnée x de la deuxième extrémité du segment.
	 */
	public double getX2() {
		return getP2().getX();
	}
	/**
	 * @return Retourne la coordonnée x du centre du segment.
	 */
	public double getCentreX() {
		return centre.getX();
	}
	/**
	 * @return Retourne la coordonnée y du centre du segment.
	 */
	public double getCentreY() {
		return centre.getY();
	}
	/**
	 * @return Retourne la coordonnée y de la première extrémité du segment.
	 */
	public double getY1() {
		return getP1().getY();
	}
	/**
	 * @return Retourne la coordonnée y de la deuxième extrémité du segment.
	 */
	public double getY2() {
		return getP2().getY();
	}
	/**
	 * @return Retourne le delta x des deux extrémités du segment.
	 */
	public double getDx() {
		return getX2() - getX1();
	}
	/**
	 * @return Retourne le delta y des deux extrémités du segment.
	 */
	public double getDy() {
		return getY2() - getY1();
	}
	/**
	 * @return Retourne la longueur du segment.
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * @return Retourne l'angle du segment.
	 */
	public double getAngle() {
		return angle;
	}
}