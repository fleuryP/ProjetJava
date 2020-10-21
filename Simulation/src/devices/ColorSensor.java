package devices;
import java.awt.Color;
import agent.*;
import environment.*;
/**
 * Classe qui décrit les caractéristiques du capteur de couleur. 
 * Le capteur se situe face au sol pour récupérer une couleur (uniquement).
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class ColorSensor extends Sensor {
	/**
	 * Constante String pour la couleur Noir.
	 */
	public static final String BLACK	= "BLACK";
	/**
	 * Constante String pour la couleur Vert.
	 */
	public static final String GREEN	= "GREEN";
	/**
	 * Constante String pour la couleur Rouge.
	 */
	public static final String RED		= "RED";
	/**
	 * Constante String pour la couleur Bleu.
	 */
	public static final String BLUE		= "BLUE";
	/**
	 * Constante String pour la couleur Jaune.
	 */
	public static final String YELLOW	= "YELLOW";
	/**
	 * Constante String pour la couleur Blanc.
	 */
	public static final String WHITE	= "WHITE";
	/**
	 * Constante String pour la couleur Vert.
	 */
	public static final String GREY		= "GREY";
	/**
	 * Constante String pour la couleur Gris foncé qui indique les murs en plexiglas en fait ...
	 */
	public static final String DARKGREY	= "DARKGREY";
	/**
	 * Tableau de couleur récupéré depuis l'environnement.
	 */
	private Color[][]pixels;
	/**
	 * La couleur (r,g,b) actuellement sous le "nez" du capteur de couleur.
	 */
	private Color currentColor;
	
	public ColorSensor(PlateauGraphique pg, Robot r) {
		super(pg,r);
		pixels = getEnvironnement().getPixels();
		currentColor = new Color(0,0,0);
	}
	@Override
	public void updateDevice() {
		double x = getRobot().getPositionCapteurs().x;
		double y = getRobot().getPositionCapteurs().y;
		currentColor = pixels[(int)x][(int)y];
		//System.out.println(getColor());
	}
	/**
	 * Retourne sous forme de chaîne de caractères la couleur perçue par
	 * le capteur.
	 * @return une des constantes String de couleur
	 */
	public String getColor() {
		int sumrgb = currentColor.getRed() + currentColor.getGreen() + currentColor.getBlue();
		switch (sumrgb) {
		case 0: return BLACK;
		case 3*114: return DARKGREY;
		case 3*193: return GREY;
		case 3*255: return WHITE;
		}
		return (currentColor.getGreen() == 255) ? 
				(currentColor.getRed() == 255) ? YELLOW : GREEN
				: (currentColor.getRed() == 255) ? RED : BLUE;
	}
}