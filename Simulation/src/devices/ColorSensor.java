package devices;
import java.awt.Color;
import java.awt.Graphics2D;

import agent.*;
import environment.*;
/**
 * Classe qui d�crit les caract�ristiques du capteur de couleur. 
 * Le capteur se situe face au sol pour r�cup�rer une couleur. Aucun
 * autre mode du capteur de couleur n'est utilis�.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class ColorSensor extends Sensor {
	/**
	 * Liste des couleurs du plateau.
	 * @author GATTACIECCA Bastien
	 */
	public static enum ColorName {
		BLACK, DARKGREY, GREY, WHITE, YELLOW, GREEN, RED, BLUE}
	/**
	 * Tableau des constantes de couleur utilis�es de la classe Color je java.awt
	 */
	private static final Color[]used = new Color[] {
			Color.BLACK, 		Color.DARK_GRAY, 
			Color.LIGHT_GRAY, 	Color.WHITE, 
			Color.YELLOW, 		Color.GREEN, 
			Color.RED, 			Color.BLUE
	};
	/**
	 * Tableau de couleur r�cup�r� depuis l'environnement.
	 */
	private final Color[][]pixels;
	/**
	 * La couleur (r,g,b) actuellement sous le "nez" du capteur de couleur.
	 */
	private Color currentColor;

	public ColorSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		pixels = environnement.getPixels();
		currentColor = new Color(0,0,0);
	}
	@Override
	public void updateDevice() {
		double x = robot.getPositionCapteurs().x;
		double y = robot.getPositionCapteurs().y;
		try {
			currentColor = pixels[(int)x][(int)y];
		}catch(ArrayIndexOutOfBoundsException e) {
			/*
			 * Si en dehors du tableau il n'y a rien � faire.
			 */
		}
		//System.out.println(getColor().name());
	}
	/**
	 * Retourne sous forme de constante de couleur la couleur per�ue par
	 * le capteur. Cette m�thode ne devrait pas �tre utilis�e par l'utilisateur.
	 * @return une des constantes String de couleur
	 */
	public ColorName getColor() {
		ColorName[]values = ColorName.values();
		for (int p = 0; p < used.length; p++) {
			if (used[p].equals(currentColor)) return values[p];
		}
		throw new RuntimeException("Something went wrong with the \"plateau.png\" pixel color");
	}
	/**
	 * Retourne sous forme de tableau d'entier � trois cases les trois valeurs
	 * respectivement de rouge, vert, et bleu de l'attribut <code>currentColor</code>.
	 * C'est cette m�thode qui doit �tre visible par l'utilisateur pour r�cup�rer
	 * les donn�es du <code>ColorSensor</code>.
	 * @return un tableau d'entier contenant les valeurs RGB de la couleur per�ue.
	 */
	public int[] getRGB() {
		int R = currentColor.getRed();
		int G = currentColor.getGreen();
		int B = currentColor.getBlue();
		return new int[] {R,G,B};
	}
	/**
	 * {@inheritDoc}
	 */
	public void paintDevice(Graphics2D g) {
		g.setColor(currentColor);
		g.fillRect((int)robot.getPositionCapteurs().x-2, (int)robot.getPositionCapteurs().y-2, 6, 6);
	}
	/**
	 * Repr�sentation textuelle du <code>ColorSensor</code>.
	 */
	public String toString() {
		String sensorString = super.toString();
		return sensorString + " = [currentColor = " + getColor().name() + "]";
	}
}