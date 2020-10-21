package agent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Classe qui décrit les caractéristiques d'un <code>Palet</code>. Les palets
 * sont les objets de l'environnement qui doivent être capturés par les pinces
 * des robots pour être marqués. Ils sont ronds et possèdent tous la même taille.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class Palet extends Objects {
	private static Image img;
	static {
		try {
			img = ImageIO.read(new File("palet.png")); //on lit l'image 1cm = 4px
		} catch (IOException e) {
			System.out.println("Le fichier \"palet.png\" n'a pas pu être chargé.");
		}
	}
	/**
	 * @param x coordonnée x du palet
	 * @param y coordonnée y du palet
	 */
	public Palet(double x, double y) {
		super(x,y);
	}
	@Override
	public void update() {
		
	}
	@Override 
	public void paint(Graphics g) {
		if (img == null) return;
		g.drawImage(img,(int)getX(),(int)getY(),null);
	}
	/**
	 * Pour récupérer les "bounds" (les bornes) du palet, on créé un objet
	 * <code>Polygon</code>. Le polygone est un octogone régulier, car il
	 * semble que 8 côtés sont assez pour évoquer les bords arrondis d'un palet.
	 * @return l'octogone régulier inscrit du cercle que forme un palet.
	 */
	public Polygon getBounds() {
		Polygon p = new Polygon();
		int nbCote = 8;
		int rayon = img.getWidth(null)/2;
		double arc = 2 * Math.PI / nbCote;
		for (int i = 0; i < nbCote; i++) {
		    double x = rayon * Math.cos(arc * i);
		    double y = rayon * Math.sin(arc * i);
		    p.addPoint(
		    		(int)(getX() + x + rayon),
		    		(int)(getY() + y + rayon));
		}
		return p;
	}
}