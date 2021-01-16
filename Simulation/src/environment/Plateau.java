package environment;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import agent.Objects;
import utils.Rectangle2D;
/**
 * Classe abstraite qui définit les caractéristiques d'un <code>Plateau</code>.
 * Cette classe présente des constantes utiles comme les dimensions du plateau,
 * ainsi qu'une <code>BufferedImage</code> pour avoir accès aux pixels de l'image !
 * On va pouvoir définir des méthodes de classe, comme <code>contains(Point p)</code>.
 * Une telle description d'un <code>Plateau</code> ne suffit pas pour pouvoir être
 * instancié tel quel. Ainsi, on utilise un <code>PlateauGraphique</code>, sous-classe
 * de <code>Plateau</code> pour pouvoir créer une instance "jouable" qui va pouvoir
 * se rafraîchir pour mettre à jour tous les objets qui la composent.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public abstract class Plateau extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Dimension x du plateau de jeu.
	 */
	public	static final int 		X = 1200;
	/**
	 * Dimension y du plateau de jeu.
	 */
	public	static final int 		Y = 800;
	/**
	 * Coordonées de la souris sur le plateau. Mise à jour que lorsque le 
	 * curseur est demandé.
	 */
	public 	static final Point	curseur = new Point();
	/**
	 * Image du plateau dont les pixels sont accessibles en lecture/écriture.
	 */
	protected	static BufferedImage	plateau;
	/**
	 * Tableau 2D qui contient la couleur de tous les pixels.
	 */
	private		static Color[][]		pixels;
	/**
	 * Le <code>Rectangle</code> de la surface du plateau.
	 */
	private static final Rectangle2D plateauRectangle = new Rectangle2D(0,0,X,Y);
	
	static {
		try {
			plateau = ImageIO.read(new File("plateau.png")); //on lit l'image 1cm = 4px
			pixels = new Color[X][Y];
			for (int i = 0; i < X; i++)
				for (int j = 0; j < Y; j++)
					pixels[i][j] = new Color(plateau.getRGB(i,j)); //on créé un tableau 2D qui récupère chaque pixel de l'image
		} catch (IOException e) {
			System.out.println("Le fichier \"plateau.png\" n'a pas pu être chargé.");
		}
	}
	public Plateau() {
		setSize(X,Y);
	}
	/**
	 * Dessine le plateau.
	 */
	public void paint(Graphics g) {
		if (plateau == null) return;
		g.drawImage(plateau,0,0,this);
	}
	/**
	 * Méthode de classe qui indique si un point aux coordonnées (x,y) se 
	 * situe dans le plateau.
	 * @param x coordonnée x du point.
	 * @param y coordonnée y du point.
	 * @return Plateau.contains(x,y)
	 */
	public static boolean contains(double x, double y) {
		return plateauRectangle.contains(x,y);
	}
	/**
	 * Méthode de classe qui indique si l'objet du plateau en paramètre se 
	 * situe dans le plateau.
	 * @param o un Objects (un palet ou un robot)
	 * @return Plateau.contains(x,y)
	 */
	public static boolean contains(Objects o) {
		return plateauRectangle.includes(o.getShape().getForm());
	}
	/**
	 * Retourne le tableau de <code>Color</code> de 2 dimensions. Cela permettra 
	 * au <code>ColorSensor</code> de déterminer la couleur sur le plateau qu'il 
	 * regarde.
	 * @return le tableau2D de Color
	 */
	public Color[][] getPixels() {
		return pixels;
	}
}