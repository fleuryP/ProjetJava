package agent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import devices.BigMotor;
import devices.ColorSensor;
import devices.Motor;
import devices.SonicSensor;
import devices.TouchSensor;
import devices.UpdatableDevice;
import environment.Plateau;
import environment.PlateauGraphique;
/**
 * Classe qui d�crit les caract�ristiques d'un <code>Robot</code>. Un Robot
 * est un <code>Objects</code> dans l'environnement. Un <code>Robot</code>
 * poss�de un ensemble de <code>UpdatableDevice</code> qui correspondent �
 * ses composants. Ils sont de deux types : les <code>Motor</code> qui lui
 * servent � interagir dans l'environnement et les <code>Sensor</code> qui
 * lui permettent de r�cup�rer de l'information de cet environnement.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class Robot extends Objects {
	private static Image img;
	static {
		try {
			img = ImageIO.read(new File("robot.png")); //on lit l'image 1cm = 4px
		} catch (IOException e) {
			System.out.println("Le fichier \"robot.png\" n'a pas pu �tre charg�.");
		}
	}
	/**
	 * Le nom du robot. 
	 * Les robots avec les m�mes noms sont dans la m�me �quipe mais cette fonctionnalit�
	 * n'est pas impl�ment�e pour le moment.
	 */
	private final String name;
	/**
	 * Liste des composants du robot. Moteurs & Senseurs.
	 */
	private final UpdatableDevice[] composants;
	/**
	 * Position des capteurs, rappelons que tous les senseurs se trouvent, sur le plan 
	 * (x,y), au m�me point ! (vu d'en haut)
	 */
	private Point2D.Double positionCapteurs;
	/**
	 * Position du point de rotation des pinces.
	 */
	private Point2D.Double positionPinces;
	/**
	 * Une paire de pinces pour notre joyeux robot :)
	 * Les pinces ne sont pas un composant du robot. Elles sont dirig�es par le petit moteur </code>Motor<code>
	 */
	private PairePince pp;
	/**
	 * Initialise les attributs d'instance du robot.
	 * @param pg l'environnement graphique dans lequel vit le robot.
	 * @param name le nom du robot.
	 * @param x coordonn�e x du robot.
	 * @param y coordonn�e y du robot.
	 */
	public Robot(PlateauGraphique pg, String name, double x, double y) {
		super(x,y);
		if (pg == null) throw new IllegalArgumentException("The robot must live in an environment.");
		if (name == null) throw new IllegalArgumentException("The robot must be named.");
		this.name = name;
		positionCapteurs = new Point2D.Double();
		positionPinces = new Point2D.Double();
		updatePosition();
		composants = new UpdatableDevice[] {
				new BigMotor(), 						//2 servo-moteurs pour les deux roues
				new BigMotor(), 
				new Motor(), 							//1 ptit moteur pour la paire de pinces
				new TouchSensor(pg,this),	//1 capteur tactile
				new SonicSensor(pg,this),	//1 capteur ultrasons
				new ColorSensor(pg,this)	//1 capteur couleur
		};
		pp = new PairePince(this); //1 paire de pince
	}
	/**
	 * Relocalise la position des capteurs et des pinces lorsque le robot se d�place.
	 */
	private void updatePosition() {
		double cos = Math.cos(getAngle());
		double sin = Math.sin(getAngle());
		int distanceCapteurs = 110 - getWidth()/2;
		positionCapteurs.setLocation(
				getX() + distanceCapteurs * cos, 
				getY() + distanceCapteurs * sin);
		positionPinces.setLocation(getX() + 20 * cos, getY() + 20 * sin);
	}
	@Override
	public void update() {
		//� actualiser, car quand le robot se d�place, ses capteurs aussi !
		updatePosition();
		//on update tous les composants du robot
		for (UpdatableDevice instru : composants) {
			instru.updateDevice();
		}
		pp.update();
	}
	@Override
	public void paint(Graphics g) {
		if (img == null) return;
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform Tx = new AffineTransform();
		Tx.translate(
				getX() - getWidth()/2,
				getY() - getHeight()/2);
		Tx.rotate(
				getAngle(),
				getWidth() /2,
				getHeight()/2);
		//on dessine les pinces et le robot par dessus
		pp.paint(g,g2);
		g2.drawImage(img, Tx, null);
		g2.setColor(Color.RED);
		g2.fillRect((int)getPositionCapteurs().x,(int)getPositionCapteurs().y,2,2);
	}
	/**
	 * Repr�sentation textuelle de l'�tat du robot.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("Tribot ").append(name).append("\nPosition = [x=").append(getX()).append(";y=").append(getY()).append("]").toString();
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
	 * @return la largeur de l'image du robot.
	 */
	public int getWidth() {
		return img.getWidth(null);
	}
	/**
	 * @return la hauteur de l'image du robot.
	 */
	public int getHeight() {
		return img.getHeight(null);
	}
	/**
	 * Construis un carr� inclu dans le robot, quelque soit sa direction.
	 * Il s'agit de bounds tr�s g�n�rales pour le robot et ne sera utilis�
	 * que pour pouvoir drag la position du robot � la souris lorqu'on sera
	 * dans le mode ad�quat.
	 * @param p 
	 * @return
	 */
	public Rectangle getSquareBounds() {
		return new Rectangle(
				(int)getX()-getHeight()/2,
				(int)getY()-getHeight()/2,getHeight(),getHeight());
	}
	/**
	 * Red�fini la position du robot sur l'axe des x.
	 */
	public void setX(double x) {
		if (x + Math.cos(getAngle()) * getWidth()/2 > 0 
				&& x + Math.cos(getAngle()) * getWidth()/2 < Plateau.X
				&& x > getWidth()/2 - 10
				&& x < Plateau.X - getWidth()/2 + 10) {
			super.setX(x);
		}
	}
	/**
	 * Red�fini la position du robot sur l'axe des y.
	 */
	public void setY(double y) {
		if (y + Math.sin(getAngle()) * getWidth()/2 > 0 
				&& y + Math.sin(getAngle()) * getWidth()/2 < Plateau.Y
				&& y > getWidth()/2 - 10
				&& y < Plateau.Y - getWidth()/2 + 10) {
			super.setY(y);
		}
	}
	/**
	 * R�cup�re le composant � l�indice en param�tre. Cet indice est une 
	 * constante vu que les composants du robot sont non modifiables (et 
	 * on instancie les composants dans un ordre pr�cis).
	 * @param INDEX_CONTSANT Une des constantes d�finies dans l'interface
	 * UpdatableDevice
	 * @return le composant du robot � l'index sp�cifi�.
	 */
	public UpdatableDevice getComposant(byte INDEX_CONTSANT) {
		return composants[INDEX_CONTSANT];
	}
}