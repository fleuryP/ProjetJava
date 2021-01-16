package devices;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import agent.Objects;
import agent.Robot;
import environment.PlateauGraphique;
import utils.Geometry;
import utils.Ligne2D;
import utils.Triangle2D;
/**
 * 
 * Classe qui décrit les caractéristiques du capteur à ultrasons. Le capteur peut calculer
 * une distance en comptant le temps qu'a mis l'onde sonore pour partir et revenir, sachant
 * la vitesse de l'onde. Dans notre cas on fait partir un point de la position du capteur
 * dans la direction du robot jusqu'à ce qu'il rencontre un <code>Objects</code> ou alors
 * un des bords du plateau. On rappelle que le capteur a une portée de 5-255cm et qu'on travaille
 * avec une échelle de (1cm = 4px).
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class SonicSensor extends Sensor {
	/**
	 * Constante de la distance maximum perçue.
	 * Soit 255cm x 4.
	 */
	public static final int DISTANCE_MAX = 1020;
	/**
	 * Constante de la distance minimum perçue.
	 * Soit 5cm x 4.
	 */
	public static final int DISTANCE_MIN = 20;
	/**
	 * La collection de tous les objects du plateau, suceptibles de renvoyer l'onde sonore du capteur.
	 */
	private Collection<Objects> allOthersObjects;
	/**
	 * Les trois points du cone.
	 */
	private final ArrayList<Point2D> points;
	/**
	 * Distance entre le capteur et la position la plus proche d'un <code>Objects</code> ou 
	 * d'un bord du plateau 
	 */
	private int distance;
	/**
	 * Le triangle formé par le cône de vision du capteur.
	 */
	private final Triangle2D cone;
	/**
	 * Genere la variabilite.
	 */
	private Random gaussian; 

	public SonicSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		points = new ArrayList<Point2D>();
		for (int i = 0; i < 3; i++) points.add(new Point2D.Double());
		cone = new Triangle2D(points);
		distance = DISTANCE_MIN;
		allOthersObjects = environnement.getObjects();
		gaussian = new Random();
	}
	/**
	 * {@inheritDoc}
	 * Calcul de la distance.
	 */
	@Override
	public void updateDevice() {
		
		double angle = robot.getAngle();
		/*
		 * L'ouverture du cône est de 10° gauche et 10° droite (20° total).
		 */
		double angleL1 = angle + Math.toRadians(10);
		double angleL2 = angle - Math.toRadians(10);
		Point2D origine = robot.getPositionCapteurs();
		points.get(0).setLocation(origine);
		points.get(1).setLocation(
				origine.getX() + DISTANCE_MAX * Math.cos(angleL1),
				origine.getY() + DISTANCE_MAX * Math.sin(angleL1));
		points.get(2).setLocation(
				origine.getX() + DISTANCE_MAX * Math.cos(angleL2),
				origine.getY() + DISTANCE_MAX * Math.sin(angleL2));
		cone.update(points);

		distance = Geometry.getFirstObject(cone,angleL1,angleL2,allOthersObjects);
		if (robot.isMoving()) distance = (int)(gaussian.nextGaussian() * 0.01 + distance);
	}
	/**
	 * Retourne la distance perçue <code>distance</code> + variabilité justifiée dans
	 * le code R joint. 
	 * @return La distance.
	 * Si inférieur à <code>DISTANCE_MIN</code>, renvoie NaN.
	 * Si supérieur à <code>DISTANCE_MAX</code>, renvoie Infinity.
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * Représentation textuelle du <code>SonicSensor</code>.
	 */
	public String toString() {
		String sensorString = super.toString();
		return sensorString + " = [distance = " + distance + "]";
	}
	/**
	 * {@inheritDoc}
	 * Méthode graphique qui permet de dessiner sur le plateau en temps réel
	 * la ligne que parcourt le trajet du capteur à ultra-son, et qui affiche 
	 * au milieu de ce segment la valeur de la distance retournée par celui-ci.
	 * @param g le contexte graphique.
	 */
	public void paintDevice(Graphics2D g) {
		cone.paint(g,Color.BLUE);
		Point2D origine = robot.getPositionCapteurs();
		Ligne2D ligne = new Ligne2D(origine, new Point2D.Double(
				origine.getX() + distance * Math.cos(robot.getAngle()),
				origine.getY() + distance * Math.sin(robot.getAngle())));
		Point2D p = ligne.getCentre();
		g.drawString(String.valueOf(distance),(int)p.getX(),(int)p.getY());
		ligne.paint(g,Color.CYAN);
	}
}