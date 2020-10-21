package agent;
import java.awt.Graphics;

import environment.Plateau;
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
	 * Les coordonnées et l'angle de l'Objet.
	 */
	private double x, y, angle;
	/**
	 * On instancie un Objet avec ses coordonnées, l'angle de départ est 0.
	 * @param x coordonnée x de l'objet
	 * @param y coordonnée y de l'objet
	 */
	public Objects(double x, double y) {
		if (!Plateau.contains(x,y)) {
			throw new IllegalArgumentException("The specified coordinates ("+x+","+y+") are illegal.");
		}
		this.x = x;
		this.y = y;
		this.angle = 0;
	}
	/**
	 * Met à jour les caractéristiques de l'Objet à chaque itération.
	 */
	public abstract void update();
	/**
	 * Met à jour les caractéristiques graphiques de l'Objet à chaque itération.
	 * @param g le contexte graphique
	 */
	public abstract void paint(Graphics g);
	//-----------------------------------------------
	//--------------setters--&--getters--------------
	//-----------------------------------------------
	public void setX(double x) {
		if (!Plateau.contains(x,y)) {
			throw new IllegalArgumentException("The specified coordinate x = "+x+" is illegal.");
		}
		this.x = x;
	}
	public void setY(double y) {
		if (!Plateau.contains(x,y)) {
			throw new IllegalArgumentException("The specified coordinate y = "+y+" is illegal.");
		}
		this.y = y;
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
}