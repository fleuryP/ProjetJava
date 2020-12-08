package agent;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import environment.Plateau;
import utils.Geometry;
/**
 * Classe qui décrit les attributs de base d'un Objet. Un Objet est un
 * élément qui se situe dans l'environnement, comme un <code>Palet</code>
 * ou un <code>Robot</code>. Ces deux types possèdent des caractéristiques
 * en commun qui sont factorisées dans cette classe abstraite. On va trouver
 * la position (x,y) sur le plateau ainsi que l'angle de l'objet. On définit
 * deux méthodes abstraites <code>update</code> et <code>paint(Graphics g)</code>
 * pour le polymorphisme.
 * Il fallait trouver un nom pour cette classe suffisamment abstrait pour que
 * cela corresponde à un <code>Robot</code> ET à un <code>Palet</code>. A ne 
 * pas confondre avec la classe <code>Object</code> !!
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Objects {
	/**
	 * Les coordonnées et l'angle de l'Objects.
	 */
	private double x, y, angle;
	/**
	 * La shape (forme) de l'Objects.
	 */
	protected Shape shape;
	/**
	 * On instancie un Objects avec ses coordonnées, l'angle de départ est 0.
	 * @param x coordonnée x de l'objet
	 * @param y coordonnée y de l'objet
	 */
	public Objects(double x, double y) {
		if (!Plateau.contains(x,y))
			throw new IllegalArgumentException("The specified coordinates ("+x+","+y+") are illegal.");
		this.x = x;
		this.y = y;
		this.angle = 0;
	}
	/**
	 * Met à jour les caractéristiques de l'Objects à chaque itération.
	 */
	public abstract void update();
	/**
	 * Met à jour les caractéristiques graphiques de l'Objects à chaque itération.
	 * @param g2 le contexte graphique
	 */
	public abstract void paint(Graphics2D g2);
	/**
	 * Vérifie si les bounds de l'Objects de l'instance courante croisent les
	 * bounds de l'Objects en paramètre. La méthode est symétrique.
	 * @param o On teste si L'Objects o croise celui de l'instance courante.
	 * @return Si oui ou non l'Objects o croise celui de l'instance courante.
	 */
	public boolean intersects(Objects o) {
		return shape.intersects(o.getShape());
	}
	/**
	 * Vérifie si les bounds de l'Objects de l'instance courante contiennent le
	 * point2D en paramètre.
	 * @param o On teste si le point p est contenu dans la shape de l'Objects 
	 * de l'instance courante.
	 * @return Si oui ou non l'Objects courant contient le point en paramètre.
	 */
	public boolean contains(Point2D.Double p) {
		return shape.contains(p);
	}
	/**
	 * Représentation textuelle de l'Objects.
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