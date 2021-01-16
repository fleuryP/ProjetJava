package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Une <code>Form2D</code> est une forme géométrique. Elles ont la particularité
 * d'avoir une orientation différente, leur implémentation en tient compte.
 * @author GATTACIECCA Bastien
 */
public abstract class Form2D {
	/**
	 * Le centre de gravité du polygone.
	 */
	protected final Point2D centre;
	/**
	 * Construit une <code>Form2D</code>.
	 */
	public Form2D() {
		centre = new Point2D.Double();
	}
	/**
	 * Met à jour la forme.
	 * @param x La nouvelle coordonnée x.
	 * @param y La nouvelle coordonnée y.
	 * @param angle Le nouvel angle.
	 */
	public abstract void update(double x, double y, double angle);
	/**
	 * Tourne la forme sur elle-même d'un angle en radian.
	 * @param angle L'angle de rotation en radians.
	 */
	public abstract void rotation(double angle);
	/**
	 * Dessine la forme.
	 * @param g L'instance graphique courante.
	 * @param c La couleur utilisée pour peindre la forme.
	 */
	public abstract void paint(Graphics2D g, Color c);
	/**
	 * Indique si la forme contient le point en paramètre.
	 * @param p Un Point.
	 * @return true si la forme contient le point, faux sinon.
	 */
	public abstract boolean contains(Point2D p);
	/**
	 * Indique si la forme contient le point de coordonnées (x,y).
	 * @param x la coordonnée x du point.
	 * @param y la coordonnée y du point.
	 * @return true si la forme contient le point, faux sinon.
	 */
	public boolean contains(double x, double y) {
		return contains(new Point2D.Double(x,y));
	}
	/**
	 * Indique si la forme de l'instance courante croise la forme
	 * en paramètre. Commutative.
	 * @param s Une forme géométrique.
	 * @return true si les deux formes se croisent.
	 */
	public abstract boolean intersects(Form2D s);
	/**
	 * Indique si la <code>Form2D</code> en paramètre est strictement incluse 
	 * dans la <code>Form2D</code> de l'instance courante.
	 * @param s Une forme dont on teste l'inclusion.
	 * @return true si la forme est contenue dans la forme, faux sinon.
	 */
	public boolean includes(Form2D s) {
		if (s instanceof Polygon2D) {
			for (Point2D p : ((Polygon2D)s).points)
				if (!contains(p)) return false;
			return true;
		}
		if (s instanceof Ligne2D)
			/*
			 * Il faudrait également vérifier les intersections, mais condition suffisante dans notre application.
			 */
			return contains(((Ligne2D)s).getP1()) && contains(((Ligne2D)s).getP2());
		if (s instanceof Cercle2D)
			return includes(s.getRectangularBounds());
		return false;
	}
	/**
	 * Retourne un rectangle de type <code>Rectangle2D</code> qui inclu la
	 * <code>Form2D</code>. Ce rectangle est nécessairement représenté avec
	 * un angle nul (en d'autres termes, un rectangle droit, non orienté).
	 * @return un <code>Rectangle2D</code> circonscrit à la forme.
	 */
	public abstract Rectangle2D getRectangularBounds();
	/**
	 * Retourne le centre de gravité de la forme.
	 * @return Le centre de la forme.
	 */
	public Point2D getCenter() {
		return centre;
	}
	/**
	 * @return Retourne la coordonnée x du centre.
	 */
	public double getCenterX() {
		return centre.getX();
	}
	/**
	 * @return Retourne la coordonnée y du centre.
	 */
	public double getCenterY() {
		return centre.getY();
	}
}