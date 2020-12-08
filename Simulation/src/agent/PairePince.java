package agent;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import devices.Motor;
import devices.UpdatableDevice.DevicesIndex;
/**
 * Classe qui décrit les caractéristiques d'une <code>PairePince</code>.
 * Une paire de pince est contrôlée par le seul <code>Moteur</code> du robot.
 * Chacune des pinces doit s'orienter (de base) en fonction de l'orientation
 * propre du robot plus de l'angle d'ouverture de la pince.
 * <p>
 * Pour qu'une Pince puisse s'ouvrir, il faut que le <code>Moteur</code> auquel
 * elle est reliée soit dans un état <code>Motor.POSITIVE_TURN</code>. En effet,
 * le moteur fait tourner une vis sans fin qui fait tourner deux roues dentées sur
 * ses côtés, ainsi on sait que les deux pinces s'ouvrent en même temps et avec le
 * même angle d'ouverture. Dans le cas où le moteur tourne à droite, les pinces
 * s'ouvrent ; et se ferment dans le cas contraire.
 * <p>
 * On ajoute également un angle d'ouverture maximum et minimum, au-delà duquel
 * l'ouverture des pinces "casserait" le robot (si elles s'ouvrent ou se ferment
 * trop). Pour éviter cela, on dit que les pinces sont fermées dans leur état par
 * défaut (donc à l'angle <code>OUVERTURE_MIN</code> = 0) et ne peuvent excéder une 
 * ouverture de <code>OUVERTURE_MAX = PI/6</code> qui ne doit permettre (au mieux)
 * de récupérer un <code>Palet</code>.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 *
 */
public class PairePince {
	private static Image pinceHaut;
	private static Image pinceBas;
	//	/**
	//	 * Longueur de l'image.
	//	 * On en a pas plus besoin que ça à priori ...
	//	 */
	//	private static int width;
	/**
	 * Largeur de l'image.
	 */
	private static int height;
	/**
	 * Angle d'ouverture maximum des pinces, en radians.
	 */
	public static final double OUVERTURE_MAX = Math.PI/6;
	/**
	 * Angle d'ouverture minimum des pinces, en radians.
	 */
	public static final double OUVERTURE_MIN = 0;
	static {
		try {
			pinceHaut = ImageIO.read(new File("pinceHaut.png"));
			pinceBas = ImageIO.read(new File("pinceBas.png"));
			//width = pinceHaut.getWidth(null);
			height = pinceHaut.getHeight(null);
		} catch (IOException e) {
			System.out.println("Les fichier \"pinceHaut.png\" et \"pinceBas.png\" n'ont pas pu être chargés.");
		}
	}
	/**
	 * Le <code>Robot</code> sur lequel on installe la paire de pince.
	 */
	private Robot robot;
	/**
	 * Le <code>Motor</code> du <code>Robot</code> qui va contrôler l'ouverture
	 * de la paire de pince.
	 */
	private Motor moteur;
	/**
	 * Le point qui détermine l'axe de roatation des deux pinces.
	 */
	private Point2D.Double posPinces;
	/**
	 * L'ouverture actuelle de la paire de pinces.
	 */
	private double currentOuverture;

	public PairePince(Robot r) {
		if (r == null) throw new IllegalArgumentException("The claws have to be fixed to a robot.");
		robot  = r;
		moteur = r.getComposant(DevicesIndex.INDEX_MOTOR);
		posPinces = r.getPositionPinces(); //inutile, mais histoire d'être sûr de pas se prendre une NullPointerException lors d'un test :(
		currentOuverture = 0;
	}
	public void update() {
		posPinces = robot.getPositionPinces();
		switch(moteur.getState()) {
		/*
		 * Si le moteur tourne à droite.
		 */
		case Motor.POSITIVE_TURN : 
			currentOuverture += Motor.SPEED_TURN;
			if (currentOuverture >= OUVERTURE_MAX) {
				currentOuverture = OUVERTURE_MAX;
				moteur.setState(Motor.NULL_TURN);
			}
			return;
			/*
			 * Si le moteur tourne à gauche.
			 */
		case Motor.NEGATIVE_TURN :
			currentOuverture -= Motor.SPEED_TURN;
			if (currentOuverture <= OUVERTURE_MIN) {
				currentOuverture = OUVERTURE_MIN;
				moteur.setState(Motor.NULL_TURN);
			}
			return;
		}
	}
	//Les instances de Graphics et Graphics2D proviennent de la classe Robot, c'est de là qu'on va appeler update() et paint(g,g2) ...
	public void paint(Graphics2D g) {
		if (pinceBas == null || pinceHaut == null) return;
		AffineTransform Tx1 = new AffineTransform(); //pince bas
		AffineTransform Tx2 = new AffineTransform(); //pince haut
		/*
		 * translation de vecteurs images. 
		 */
		Tx1.translate(posPinces.x,posPinces.y-1);
		Tx2.translate(posPinces.x,posPinces.y-height+1);
		/*
		 * rotation de vecteurs images.
		 */
		Tx1.rotate(robot.getAngle() + currentOuverture,	0, 0);
		Tx2.rotate(robot.getAngle() - currentOuverture, 0, height);
		/*
		 * dessin des vecteurs.
		 */
		g.drawImage(pinceBas,  Tx1, null);
		g.drawImage(pinceHaut, Tx2, null);
	}
	/**
	 * Ouvre les pinces en actionnant le moteur.
	 */
	public void open() {
		moteur.setState(Motor.POSITIVE_TURN);
	}
	/**
	 * Ferme les pinces en actionnant le moteur.
	 */
	public void close() {
		moteur.setState(Motor.NEGATIVE_TURN);
	}
	/**
	 * @return Si oui ou non les pinces sont entièrement ouvertes.
	 */
	public boolean isFullyOpen() {
		return currentOuverture == OUVERTURE_MAX;
	}	
	/**
	 * @return Si oui ou non les pinces sont entièrement fermées.
	 */
	public boolean isFullyClose() {
		return currentOuverture == OUVERTURE_MIN;
	}
	/**
	 * 
	 */
	public double getOuverture() {
		return currentOuverture;
	}
	//----------------------------------------------------------------------
	//--------------------------Actions-avec-Palet--------------------------
	//----------------------------------------------------------------------
	/**
	 * Le palet actuellement dans les pinces du robot.
	 */
	private Palet p = null;
	/**
	 * Les pinces aggripent un Palet !
	 * @param p le Palet à prendre dans la paire de pinces.
	 */
	public void takePalet(Palet p) {
		if (p == null) throw new IllegalArgumentException("The Palet you want to take refers 'null'.");
		if (this.p != null) throw new IllegalArgumentException("You cannot take a Palet if you are already taking one.");
		this.p = p;
		p.setCatcher(robot);
	}
	/**
	 * Les pinces qui aggripaient un Palet le pose.
	 * @param p le Palet à poser.
	 */
	public void dropPalet(Palet p) {
		if (p == null) throw new IllegalArgumentException("The Palet you want to drop refers 'null'.");
		if (this.p == null) throw new IllegalArgumentException("You cannot drop a Palet if you didn't catch any.");
		this.p = null;
		p.setCatcher(null);
	}
	/**
	 * @return le Palet qui est actuellement dans les pinces ; retroune 'null' 
	 * si aucun Palet ne s'y trouve.
	 */
	public Palet currentGrippedPalet() {
		return p;
	}
}