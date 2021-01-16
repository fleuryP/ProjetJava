package agent;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import environment.Plateau;
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
	 * On instancie un Objects avec ses coordonn�es, l'angle de d�part est 0. Si l'on souhaite
	 * un angle diff�rent, il est toujours possible d'appeler <code>setAngle(double)</code> sur
	 * l'objet apr�s son instanciation.
	 * @param x coordonn�e x de l'objet.
	 * @param y coordonn�e y de l'objet.
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
	 * @param g2 le contexte graphique.
	 */
	public abstract void paint(Graphics2D g2);
	/**
	 * V�rifie si les bounds de l'Objects de l'instance courante croisent les
	 * bounds de l'Objects en param�tre. La m�thode est sym�trique.
	 * @param o On teste si L'Objects o croise celui de l'instance courante.
	 * @return Si oui ou non l'Objects o croise celui de l'instance courante.
	 */
	public boolean intersects(Objects o) {
		return shape.intersects(o.shape);
	}
	/**
	 * V�rifie si les bounds de l'Objects de l'instance courante contiennent le
	 * point2D en param�tre.
	 * @param p On teste si le point p est contenu dans la shape de l'Objects 
	 * de l'instance courante.
	 * @return Si oui ou non l'Objects courant contient le point en param�tre.
	 */
	public boolean contains(Point2D p) {
		return shape.contains(p);
	}
	/**
	 * V�rifie si les bounds de l'Objects de l'instance courante contiennent le
	 * point aux coordonn�es en param�tre.
	 * @param x la coordonn�e x du point.
	 * @param y la coordonn�e y du point.
	 * @return Si oui ou non l'Objects courant contient les coordonn�es du point 
	 * en param�tre.
	 */
	public boolean contains(double x, double y) {
		return shape.contains(x,y);
	}
	/**
	 * Permet de savoir si les bounds de l'<code>Objects</code> en param�tre sont
	 * incluses dans les bounds de l'<code>Objects</code> de l'instance courante 
	 * compte tenu de son orientation.
	 * @param o On teste si L'Objects o est contenu dans celui de l'instance courante.
	 * @return true si l'objects est inclus int�gralement dans l'objects courant,
	 * faux sinon.
	 */
	public boolean includes(Objects o) {
		return shape.includes(o.shape);
	}
	/**
	 * Lorsqu'un objet a atteint une position innatendue, non voulue ou non pr�vue, 
	 * vis-�-vis d'un autre <code>Objects</code>, on l'�jecte. Ejecter un objet
	 * consiste � le d�placer d'une grande distance le temps d'une it�ration pour
	 * d�bugguer la situation impr�vue.
	 * @param o L'objects avec lequel la position fut innatendue et qui doit �tre
	 * �jecter.
	 */
	public void eject(Objects o) {
		o.setX(shape.getForm().getCenterX() + shape.getForm().getRectangularBounds().getWidth()/2);
		o.setY(shape.getForm().getCenterY() + shape.getForm().getRectangularBounds().getHeight()/2);
	}
	/**
	 * Repr�sentation textuelle de l'Objects.
	 */
	public abstract String toString();
	//-----------------------------------------------
	//--------------setters--&--getters--------------
	//-----------------------------------------------
	public void setX(double x) {
		if (Plateau.contains(x,y))
			if (x + Math.cos(angle) * shape.getShapeWidth()/2 > 0 
					&& x + Math.cos(angle) * shape.getShapeWidth()/2 < Plateau.X
					&& x > shape.getShapeWidth()/2 - 10
					&& x < Plateau.X - shape.getShapeWidth()/2 + 10) {
				this.x = x;
			}
	}
	public void setY(double y) {
		if (Plateau.contains(x,y))
			if (y + Math.sin(angle) * shape.getShapeWidth()/2 > 0 
					&& y + Math.sin(angle) * shape.getShapeWidth()/2 < Plateau.Y
					&& y > shape.getShapeWidth()/2 - 10
					&& y < Plateau.Y - shape.getShapeWidth()/2 + 10) {
				this.y = y;
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
		return angle;
	}
	public Shape getShape() {
		return shape;
	}
}