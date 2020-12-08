package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
/**
 * Une <code>Form2D</code> est une forme g�om�trique. Elles ont la particularit�
 * d'avoir une orientation diff�rente, leur impl�mentation en tient compte.
 * @author GATTACIECCA Bastien
 */
public abstract class Form2D {
	/**
	 * Le centre de gravit� du polygone.
	 */
	protected final Point2D centre;
	/**
	 * Construit une <code>Form2D</code>.
	 */
	public Form2D() {
		centre = new Point2D.Double();
	}
	/**
	 * Met � jour la forme.
	 * @param x La nouvelle coordonn�e x.
	 * @param y La nouvelle coordonn�e y.
	 * @param angle Le nouvel angle.
	 */
	public abstract void update(double x, double y, double angle);
	/**
	 * Tourne la forme sur elle-m�me d'un angle en radian.
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
	 * Indique si la forme contient le point en param�tre.
	 * @param p Un Point.
	 * @return true si la forme contient le point, faux sinon.
	 */
	public abstract boolean contains(Point2D p);
	/**
	 * Indique si la forme de l'instance courante croise la forme
	 * en param�tre. Commutative.
	 * @param s Une forme g�om�trique.
	 * @return true si les deux formes se croisent.
	 */
	public abstract boolean intersects(Form2D s);
	/**
	 * Retourne un rectangle de type <code>Rectangle2D</code> qui inclu la
	 * <code>Form2D</code>. Ce rectangle est n�cessairement repr�sent� avec
	 * un angle nul (en d'autres termes, un rectangle droit, non orient�).
	 * @return un <code>Rectangle2D</code> circonscrit � la forme.
	 */
	public abstract Rectangle2D getRectangularBounds();
	/**
	 * Retourne le centre de gravit� de la forme.
	 * @return Le centre de la forme.
	 */
	public Point2D getCenter() {
		return centre;
	}
}