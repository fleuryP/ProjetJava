package agent;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import environment.Plateau;
import utils.Geometry;
/**
 * Classe qui d�crit les attributs de base d'un Objet. Un Objet est un
 * �l�ment qui se situe dans l'environnement, comme un <code>Palet</code>
 * ou un <code>Robot</code>. Ces deux types poss�dent des caract�ristiques
 * en commun qui sont factoris�es dans cette classe abstraite. On va trouver
 * la position (x,y) sur le plateau ainsi que l'angle de l'objet. On d�finit
 * deux m�thodes abstraites <code>update</code> et <code>paint(Graphics g)</code>
 * pour le polymorphisme.
 * Il fallait trouver un nom pour cette classe suffisamment abstrait pour que
 * cela corresponde � un <code>Robot</code> ET � un <code>Palet</code>. A ne 
 * pas confondre avec la classe <code>Object</code> !!
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Objects {
	/**
	 * Les coordonn�es et l'angle de l'Objects.
	 */
	private double x, y, angle;
	/**
	 * La shape (forme) de l'Objects.
	 */
	protected Shape shape;
	/**
	 * On instancie un Objects avec ses coordonn�es, l'angle de d�part est 0.
	 * @param x coordonn�e x de l'objet
	 * @param y coordonn�e y de l'objet
	 */
	public Objects(double x, double y) {
		if (!Plateau.contains(x,y))
			throw new IllegalArgumentException("The specified coordinates ("+x+","+y+") are illegal.");
		this.x = x;
		this.y = y;
		this.angle = 0;
	}
	/**
	 * Met � jour les caract�ristiques de l'Objects � chaque it�ration.
	 */
	public abstract void update();
	/**
	 * Met � jour les caract�ristiques graphiques de l'Objects � chaque it�ration.
	 * @param g2 le contexte graphique
	 */
	public abstract void paint(Graphics2D g2);
	/**
	 * V�rifie si les bounds de l'Objects de l'instance courante croisent les
	 * bounds de l'Objects en param�tre. La m�thode est sym�trique.
	 * @param o On teste si L'Objects o croise celui de l'instance courante.
	 * @return Si oui ou non l'Objects o croise celui de l'instance courante.
	 */
	public boolean intersects(Objects o) {
		return shape.intersects(o.getShape());
	}
	/**
	 * V�rifie si les bounds de l'Objects de l'instance courante contiennent le
	 * point2D en param�tre.
	 * @param o On teste si le point p est contenu dans la shape de l'Objects 
	 * de l'instance courante.
	 * @return Si oui ou non l'Objects courant contient le point en param�tre.
	 */
	public boolean contains(Point2D.Double p) {
		return shape.contains(p);
	}
	/**
	 * Repr�sentation textuelle de l'Objects.
	 */
	public abstract String toString();
	//-----------------------------------------------
	//--------------setters--&--getters--------------
	//-----------------------------------------------
	public void setX(double x) {
		if (Plateau.contains(x,y)) {
			if (x + Math.cos(angle) * shape.getShapeWidth()/2 > 0 
					&& x + Math.cos(angle) * shape.getShapeWidth()/2 < Plateau.X
					&& x > shape.getShapeWidth()/2 - 10
					&& x < Plateau.X - shape.getShapeWidth()/2 + 10) {
				this.x = x;
			}
		}
	}
	public void setY(double y) {
		if (Plateau.contains(x,y)) {
			if (y + Math.sin(angle) * shape.getShapeWidth()/2 > 0 
					&& y + Math.sin(angle) * shape.getShapeWidth()/2 < Plateau.Y
					&& y > shape.getShapeWidth()/2 - 10
					&& y < Plateau.Y - shape.getShapeWidth()/2 + 10) {
				this.y = y;
			}
		}
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getAngle() {
		return Geometry.computesAngle(angle);
	}
	public Shape getShape() {
		return shape;
	}
}