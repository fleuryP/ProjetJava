package agent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Classe qui d�crit les caract�ristiques d'un <code>Palet</code>. Les palets
 * sont les objets de l'environnement qui doivent �tre captur�s par les pinces
 * des robots pour �tre marqu�s. Ils sont ronds et poss�dent tous la m�me taille.
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
			System.out.println("Le fichier \"palet.png\" n'a pas pu �tre charg�.");
		}
	}
	/**
	 * @param x coordonn�e x du palet
	 * @param y coordonn�e y du palet
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
	 * Pour r�cup�rer les "bounds" (les bornes) du palet, on cr�� un objet
	 * <code>Polygon</code>. Le polygone est un octogone r�gulier, car il
	 * semble que 8 c�t�s sont assez pour �voquer les bords arrondis d'un palet.
	 * @return l'octogone r�gulier inscrit du cercle que forme un palet.
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