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
	 */
	public void paint(Graphics2D g) {
		g.setColor(Color.MAGENTA);
	}
	/**
	 * Indique si la forme contient le point en paramètre.
	 * @param p Un Point.
	 * @return true si la forme contient le point, faux sinon.
	 */
	public abstract boolean contains(Point2D p);
	/**
	 * Indique si la forme de l'instance courante croise la forme
	 * en paramètre. Commutative.
	 * @param s Une forme géométrique.
	 * @return true si les deux formes se croisent.
	 */
	public abstract boolean intersects(Form2D s);
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
}