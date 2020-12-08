package devices;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import agent.Objects;
import agent.Robot;
import environment.PlateauGraphique;
import utils.Geometry;
import utils.Ligne2D;
import utils.Triangle2D;
/**
 * 
 * Classe qui d�crit les caract�ristiques du capteur � ultrasons. Le capteur peut calculer
 * une distance en comptant le temps qu'a mis l'onde sonore pour partir et revenir, sachant
 * la vitesse de l'onde. Dans notre cas on fait partir un point de la position du capteur
 * dans la direction du robot jusqu'� ce qu'il rencontre un <code>Objects</code> ou alors
 * un des bords du plateau. On rappelle que le capteur a une port�e de 5-255cm et qu'on travaille
 * avec une �chelle de (1cm = 4px).
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class SonicSensor extends Sensor {
	/**
	 * Constante de la distance maximum per�ue.
	 */
	public static final int DISTANCE_MAX = 255	*4; //1020
	/**
	 * Constante de la distance minimum per�ue.
	 */
	public static final int DISTANCE_MIN = 5	*4; //20
	/**
	 * La collection de tous les objects du plateau, suceptibles de renvoyer l'onde sonore du capteur.
	 * Cette collection ne contient pas le <code>Robot</code> qui porte le capteur � ultra-son.
	 */
	private Collection<Objects> allOthersObjects;
	/**
	 * Distance entre le capteur et la position la plus proche d'un <code>Objects</code> ou 
	 * d'un bord du plateau 
	 */
	private int distance;
	/**
	 * Les trois points du cone.
	 */
	private final ArrayList<Point2D> points;
	/**
	 * Le triangle form� par le c�ne de vision du capteur.
	 */
	private final Triangle2D cone;

	public SonicSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		points = new ArrayList<Point2D>();
		for (int i = 0; i < 3; i++) {points.add(new Point2D.Double());}
		cone = new Triangle2D(points);
		distance = DISTANCE_MIN;
		allOthersObjects = super.getEnvironnement().getObjects();
		allOthersObjects.remove(getRobot());
	}
	/**
	 * Calcul de la distance.
	 */
	@Override
	public void updateDevice() {
		//System.out.println(getDistance());
		double angle = getRobot().getAngle();
		double angleL1 = angle + Math.toRadians(10);
		double angleL2 = angle - Math.toRadians(10);
		points.clear();
		Point2D origine = getRobot().getPositionCapteurs();
		points.add(origine);
		points.add(new Point2D.Double(
				origine.getX() + DISTANCE_MAX * Math.cos(angleL1),
				origine.getY() + DISTANCE_MAX * Math.sin(angleL1)));
		points.add(new Point2D.Double(
				origine.getX() + DISTANCE_MAX * Math.cos(angleL2),
				origine.getY() + DISTANCE_MAX * Math.sin(angleL2)));
		cone.update(points);

		distance = Geometry.getFirstObject(cone,angleL1,angleL2,allOthersObjects);
	}
	/**
	 * Retourne la distance per�ue <code>distance</code>. 
	 * @return La distance.
	 * Si inf�rieur � <code>DISTANCE_MIN</code>, renvoie NaN.
	 * Si sup�rieur � <code>DISTANCE_MAX</code>, renvoie Infinity.
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * Repr�sentation textuelle du <code>SonicSensor</code>.
	 */
	public String toString() {
		String sensorString = super.toString();
		return sensorString + " = [distance = " + distance + "]";
	}
	
	/**
	 * M�thode graphique qui permet de dessiner sur le plateau en temps r�el
	 * la ligne que parcourt le trajet du capteur � ultra-son, et qui affiche 
	 * au milieu de ce segment la valeur de la distance retourn�e par celui-ci.
	 * @param g2 le contexte graphique.
	 */
	public void paint(Graphics2D g) {
		cone.paint(g);
		g.setColor(Color.BLUE);
		Ligne2D ligne = new Ligne2D(
				getRobot().getPositionCapteurs(),
		new Point2D.Double(
				getRobot().getPositionCapteurs().x + distance * Math.cos(getRobot().getAngle()),
				getRobot().getPositionCapteurs().y + distance * Math.sin(getRobot().getAngle())
				));
		Point2D p = Geometry.getMilieu(ligne);
		g.drawString(String.valueOf(distance),(int)p.getX(),(int)p.getY());
		ligne.paint(g);
	}
}