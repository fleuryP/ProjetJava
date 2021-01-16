package agent;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import devices.BigMotor;
import devices.ColorSensor;
import devices.Motor;
import devices.Sensor;
import devices.SonicSensor;
import devices.TouchSensor;
import devices.UpdatableDevice;
import devices.UpdatableDevice.DevicesIndex;
import environment.PlateauGraphique;
/**
 * Classe abstraite qui décrit les caractéristiques d'un <code>Robot</code>. 
 * Un Robot est un <code>Objects</code> dans l'environnement. Un <code>Robot</code>
 * possède un ensemble de <code>UpdatableDevice</code> qui correspondent à
 * ses composants. Ils sont de deux types : les <code>Motor</code> qui lui
 * servent à interagir dans l'environnement et les <code>Sensor</code> qui
 * lui permettent de récupérer de l'information de cet environnement. La classe
 * est abstraite car on ne peut encore déterminer de quelle manière se déplace
 * ce <code>Robot</code>.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Robot extends Objects {
	private static Image img;
	static {
		try {
			img = ImageIO.read(new File("robot.png")); //on lit l'image 1cm = 4px
		} catch (IOException e) {
			System.out.println("Le fichier \"robot.png\" n'a pas pu être chargé.");
		}
	}
	/**
	 * Distance en px, entre le centre du robot et le centre du palet qu'il aggripe.
	 */
	public static final double CENTRE_PALET = 60.0d;
	/**
	 * Distance en px, entre la position des capteurs et une extrémité du balancier
	 * du robot. Peu importe l'extrémité, il y a équidistance : les deux extrémités
	 * et le capteur forment un triangle isocèle.
	 */
	public static final double CAPTEUR_BALANCIER = 16.0d;
	/**
	 * Distance en px, entre le centre du robot et la position de tous les capteurs.
	 * Rappelons que l'ensemble des capteurs d'un robot sont superposés : cela implique
	 * qu'ils ont tous les même position sur le plan (x,y).
	 */
	public static final double CENTRE_CAPTEURS = 51.0d;
	/**
	 * Distance en px, entre le centre du robot et la position où les pinces sont fixées.
	 */
	public static final double CENTRE_PINCES = 20.0d;
	/**
	 * Distance en px, entre le centre du robot et le centre de la shape générale du robot
	 * (où les pinces sont incluses).
	 */
	public static final double CENTRE_SHAPE = 25.0d;
	/**
	 * Distance en px, qui représente la largeur du chassis du robot.
	 */
	public static final double CHASSIS = 35.0d;
	/**
	 * Le nom du robot. 
	 * Les robots avec les mêmes noms sont dans la même équipe mais cette fonctionnalité
	 * n'est pas implémentée pour le moment.
	 */
	private final String name;
	/**
	 * Liste des composants du robot. Moteurs et Senseurs. dans le bon ordre.
	 * L'ordre est celui des constantes définies dans <code>UpdatableDevice</code>.
	 * Les propriétés physiques du robot ne sont pas modifiables. Il est interdit de
	 * rajouter des moteurs ou des capteurs : donc déclaré final.
	 */
	private final ArrayList<UpdatableDevice> composants;
	/**
	 * Position des capteurs, rappelons que tous les senseurs se trouvent, sur le plan 
	 * (x,y), au même point ! (vu d'en haut)
	 */
	private final Point2D.Double positionCapteurs;
	/**
	 * Position du point de rotation des pinces.
	 */
	private final Point2D.Double positionPinces;
	/**
	 * Une paire de pinces pour notre joyeux robot :)
	 * Les pinces ne sont pas un composant du robot. Elles sont dirigées par le petit moteur <code>Motor</code>.
	 */
	private final PairePince pairePinces;
	/**
	 * Initialise les attributs d'instance du robot.
	 * @param pg l'environnement graphique dans lequel vit le robot.
	 * @param name le nom du robot.
	 * @param x coordonnée x du robot.
	 * @param y coordonnée y du robot.
	 */
	public Robot(PlateauGraphique pg, String name, double x, double y) {
		super(x,y);
		if (pg == null) throw new IllegalArgumentException("The robot must live in an environment.");
		if (name == null) throw new IllegalArgumentException("The robot must be named.");
		this.name = name;
		shape = new Shape.Robot();
		positionCapteurs = new Point2D.Double();
		positionPinces = new Point2D.Double();
		updatePositions();

		composants = new ArrayList<UpdatableDevice>();
		composants.add(new BigMotor());
		composants.add(new BigMotor());
		composants.add(new Motor());
		composants.add(new TouchSensor(pg,this));
		composants.add(new SonicSensor(pg,this));
		composants.add(new ColorSensor(pg,this));

		pairePinces = new PairePince(this); //1 paire de pince
	}
	/**
	 * Relocalise la position des capteurs et des pinces lorsque le robot se déplace.
	 */
	private void updatePositions() {
		double cos = Math.cos(getAngle()), sin = Math.sin(getAngle());
		positionCapteurs.setLocation(
				getX() + CENTRE_CAPTEURS * cos, 
				getY() + CENTRE_CAPTEURS * sin);
		positionPinces.setLocation(
				getX() + CENTRE_PINCES * cos, 
				getY() + CENTRE_PINCES * sin);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		shape.update(
				getX() + CENTRE_SHAPE * Math.cos(getAngle()), 
				getY() + CENTRE_SHAPE * Math.sin(getAngle()), 
				getAngle(),
				pairePinces.getOuverture());
		/*
		 * à actualiser, car quand le robot se déplace, ses capteurs aussi !
		 */
		updatePositions();
		/*
		 * on update tous les composants du robot
		 */
		for (UpdatableDevice instru : composants) {
			instru.updateDevice();
		}
		pairePinces.update();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics2D g) {
		if (img == null) return;
		AffineTransform Tx = new AffineTransform();
		/*
		 * translation de vecteur image. 
		 */
		Tx.translate(getX() - getWidth()/2, getY() - getHeight()/2);
		/*
		 * rotation de vecteur image.
		 */
		Tx.rotate(getAngle(), getWidth()/2, getHeight()/2);
		/*
		 * on dessine les pinces d'abord puis le robot par dessus
		 */
		pairePinces.paint(g);
		g.drawImage(img, Tx, null);

		for (UpdatableDevice comp : composants)
			if (comp instanceof Sensor) ((Sensor)comp).paintDevice(g);

		shape.paint(g);
	}
	/**
	 * @return La vitesse du <code>Robot</code>.
	 */
	public abstract double getSpeed();
	/**
	 * @return Si le <code>Robot</code> est en mouvement.
	 */
	public abstract boolean isMoving();
	/**
	 * @return le nom du robot.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Représentation textuelle de l'état du robot.
	 */
	public String toString() {
		return "Tribot "+name+"("+getX()+";"+getY()+")";
	}
	/**
	 * @return la position des capteurs.
	 */
	public Point2D.Double getPositionCapteurs() {
		return positionCapteurs;
	}
	/**
	 * @return la position des pinces.
	 */
	public Point2D.Double getPositionPinces() {
		return positionPinces;
	}
	/**
	 * @return la paire de pinces du robot.
	 */
	public PairePince getPinces() {
		return pairePinces;
	}
	/**
	 * @return la largeur de l'image d'un robot.
	 */
	public static int getWidth() {
		return img.getWidth(null);
	}
	/**
	 * @return la hauteur de l'image d'un robot.
	 */
	public static int getHeight() {
		return img.getHeight(null);
	}
	/**
	 * Retourne le type du composant à l'index en paramètre.
	 * @param <U> Un sous-type de <code>UpdatableDevice</code>.
	 * @param cste Une des constantes définies dnas l'énumération.
	 * @return Retourne le composant du robot correspondant à la constante en paramètre.
	 */
	@SuppressWarnings("unchecked")
	public <U extends UpdatableDevice> U getComposant(DevicesIndex cste) {
		if (cste == null) throw new IllegalArgumentException("The argument must be a constant among existing UpdatableDevice constants.");
		return (U)composants.get(cste.ordinal());
	}
}