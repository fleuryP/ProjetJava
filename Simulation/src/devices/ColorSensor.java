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
	 * Liste des couleurs du plateau.
	 * @author GATTACIECCA Bastien
	 */
	public static enum ColorName {
		BLACK, DARKGREY, GREY, WHITE, YELLOW, GREEN, RED, BLUE}
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
		//System.out.println(getColor().name());
	}
	/**
	 * Retourne sous forme de chaîne de caractères la couleur perçue par
	 * le capteur.
	 * @return une des constantes String de couleur
	 */
	public ColorName getColor() {
		int sumrgb = currentColor.getRed() + currentColor.getGreen() + currentColor.getBlue();
		switch (sumrgb) {
		case 0: return ColorName.BLACK;
		case 3*114: return ColorName.DARKGREY;
		case 3*193: return ColorName.GREY;
		case 3*255: return ColorName.WHITE;
		}
		return (currentColor.getGreen() == 255) ? 
				(currentColor.getRed() == 255) ? ColorName.YELLOW : ColorName.GREEN
						: (currentColor.getRed() == 255) ? ColorName.RED : ColorName.BLUE;
	}
}