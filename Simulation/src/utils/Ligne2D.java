package utils;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Ligne2D extends Form2D {
	protected Line2D L;
	protected Point2D centre;
	private double distance;
	private double angle;

	public String toString() {
		return "x1="+getX1()+" y1="+getY1()+" x2="+getX2()+" y2="+getY2()+" centre=("+getCentreX()+";"+getCentreY()+") distance= "+getDistance()+" angle="+getAngle();
	}
	public Ligne2D() {
		this(0,0,1,1);
	}
	public Ligne2D(Point2D P1, Point2D P2) {
		this(P1.getX(),P1.getY(),P2.getX(),P2.getY());
	}
	public Ligne2D(double x1, double y1, double x2, double y2) {
		L = new Line2D.Double(x1,y1,x2,y2);
		centre = new Point2D.Double();
		centre.setLocation((x1+x2)/2,(y1+y2)/2);
		distance = Math.hypot(getDx(), getDy());
		angle = Math.atan2(getDy(), getDx());
	}
	@Override
	public void update(double x, double y, double angle) {
		this.angle = angle;
		L.setLine(x,y, x + distance * Math.cos(angle), y + distance * Math.sin(angle));
		centre.setLocation((getX1()+getX2())/2,(getY1()+getY2())/2);
		rotation(angle);
	}
	public void update(double x1, double y1, double x2, double y2) {
		L.setLine(x1,y1,x2,y2);
		centre.setLocation((x1+x2)/2,(y1+y2)/2);
		distance = Math.hypot(getDx(), getDy());
		angle = Math.atan2(getDy(), getDx());
	}
	public void update(Point2D P1, Point2D P2) {
		update(P1.getX(),P1.getY(),P2.getX(),P2.getY());
	}

	@Override
	public void rotation(double angle) {
		double x = getX1() - getCentreX();
		double y = getY1() - getCentreY();
		double dist = Math.hypot(x, y);
		getP1().setLocation(getCentreX() + dist * Math.cos(angle), getCentreY() + dist * Math.sin(angle));
		getP2().setLocation(getCentreX() + dist * Math.cos(Math.PI + angle), getCentreY() + dist * Math.sin(Math.PI + angle));
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		g.draw(L);
	}

	@Override
	public boolean contains(Point2D p) {
		return false;
	}

	@Override
	public boolean intersects(Form2D s) {
		if (s == null) throw new IllegalArgumentException("The form refers to 'null'.");
		if (s instanceof Ligne2D)
			return Geometry.intersectsOf(this,(Ligne2D)s);
		if (s instanceof Polygon2D)
			return s.intersects(this);
		if (s instanceof Cercle2D)
			return Geometry.intersectsOf(this, (Cercle2D)s) != null;
		return false;
	}

	@Override
	public Rectangle2D getRectangularBounds() {
		Vector<Point2D> fourPoints = new Vector<Point2D>();
		fourPoints.add(new Point2D.Double(getX1(),getY1()));
		fourPoints.add(new Point2D.Double(getX2(),getY1()));
		fourPoints.add(new Point2D.Double(getX2(),getY2()));
		fourPoints.add(new Point2D.Double(getX1(),getY2()));
		return new Rectangle2D(fourPoints);
	}
	public Point2D getP1() {
		return L.getP1();
	}
	public Point2D getCentre() {
		return centre;
	}
	public Point2D getP2() {
		return L.getP2();
	}
	public double getX1() {
		return getP1().getX();
	}
	public double getX2() {
		return getP2().getX();
	}
	public double getCentreX() {
		return centre.getX();
	}
	public double getCentreY() {
		return centre.getY();
	}
	public double getY1() {
		return getP1().getY();
	}
	public double getY2() {
		return getP2().getY();
	}
	public double getDx() {
		return getX1() - getX2();
	}
	public double getDy() {
		return getY1() - getY2();
	}
	public double getDistance() {
		return distance;
	}
	public double getAngle() {
		return angle;
	}

}
