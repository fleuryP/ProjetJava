package agent;
import java.awt.Graphics2D;
import java.awt.Image;
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
	 * Indique quel <code>Robot</code> est en train de se d�placer avec ce
	 * palet. Vaut <code>null</code> si le palet est libre.
	 */
	private Robot catcher;
	/**
	 * @param x coordonn�e x du palet
	 * @param y coordonn�e y du palet
	 */
	public Palet(double x, double y) {
		super(x,y);
		shape = new Shape.Palet();
		catcher = null;
	}
	@Override
	public void update() {
		shape.update(getX(), getY(), getAngle(), 0);
		if (catcher != null) {
			shape.update(getX(), getY(), getAngle(), catcher.getPinces().getOuverture());
			setX(catcher.getX() + 60 * Math.cos(catcher.getAngle()));
			setY(catcher.getY() + 60 * Math.sin(catcher.getAngle()));
		}
	}
	@Override 
	public void paint(Graphics2D g) {
		if (img == null) return;
		g.drawImage(img,(int)(getX()-getWidth()/2),(int)(getY()-getHeight()/2),null);
		shape.paint(g);
		shape.getForm().getRectangularBounds().paint(g);
	}
	/**
	 * D�finit le robot r comme �tant celui qui capture le Palet de cette instance.
	 * Si r vaut null, le Palet est "libre", sinon, il se d�place avec le Robot.
	 * Par ailleurs, lorsqu'un Palet se d�place, on lui retire ses 'bounds' car il
	 * n'est plus consid�r� comme un Objet qui appuie sur le balancier. D�s qu'on le
	 * rel�che, il s'�carte de 100px pour ne plus �tre recaptur� par le Robot !
	 * @param r le Robot qui capture le Palet.
	 */
	public void setCatcher(Robot r) {
		if (r == null) {
			setX(catcher.getX() + 100 * Math.cos(catcher.getAngle()));
			setY(catcher.getY() + 100 * Math.sin(catcher.getAngle()));
		}
		shape.invalidate = r != null ? true : false;
		catcher = r;
	}
	/**
	 * @return le Robot qui capture le Palet. 'null' si aucun robot
	 * ne le capture.
	 */
	public Robot getCatcher() {
		return catcher;
	}
	/**
	 * Repr�sentation textuelle de l'�tat d'un Palet.
	 */
	public String toString() {
		return "Palet("+getX()+";"+getY()+")  -  Etat = " + (catcher == null ? "libre" : "captur�");
	}
	/**
	 * @return la largeur de l'image d'un palet.
	 */
	public static int getWidth() {
		return img.getWidth(null);
	}
	/**
	 * @return la hauteur de l'image d'un palet.
	 */
	public static int getHeight() {
		return img.getHeight(null);
	}
}