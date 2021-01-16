package devices;
import java.awt.Color;
import java.awt.Graphics2D;

import agent.*;
import environment.*;
/**
 * Classe qui décrit les caractéristiques du capteur de couleur. 
 * Le capteur se situe face au sol pour récupérer une couleur. Aucun
 * autre mode du capteur de couleur n'est utilisé.
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
	 * Tableau des constantes de couleur utilisées de la classe Color je java.awt
	 */
	private static final Color[]used = new Color[] {
			Color.BLACK, 		Color.DARK_GRAY, 
			Color.LIGHT_GRAY, 	Color.WHITE, 
			Color.YELLOW, 		Color.GREEN, 
			Color.RED, 			Color.BLUE
	};
	/**
	 * Tableau de couleur récupéré depuis l'environnement.
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
			 * Si en dehors du tableau il n'y a rien à faire.
			 */
		}
		//System.out.println(getColor().name());
	}
	/**
	 * Retourne sous forme de constante de couleur la couleur perçue par
	 * le capteur. Cette méthode ne devrait pas être utilisée par l'utilisateur.
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
	 * Retourne sous forme de tableau d'entier à trois cases les trois valeurs
	 * respectivement de rouge, vert, et bleu de l'attribut <code>currentColor</code>.
	 * C'est cette méthode qui doit être visible par l'utilisateur pour récupérer
	 * les données du <code>ColorSensor</code>.
	 * @return un tableau d'entier contenant les valeurs RGB de la couleur perçue.
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
	 * Représentation textuelle du <code>ColorSensor</code>.
	 */
	public String toString() {
		String sensorString = super.toString();
		return sensorString + " = [currentColor = " + getColor().name() + "]";
	}
}