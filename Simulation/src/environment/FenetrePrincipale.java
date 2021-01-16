package environment;
import java.awt.BorderLayout;

import javax.swing.JFrame;
/**
 * Classe qui décrit la fenetre en tant que JFrame et ses composants.
 * On ajoute à cette JFrame un <code>PlateauGraphique</code>, sous-type
 * de <code>Plateau</code> et qui est lui sous-type de <code>JPanel</code>.
 * La fenêtre doit ne pas être resizable, on lui enlève donc ses "décorations"
 * (la barre d'attribut d'une fenêtre classique Windows) et son Layout.
 * 
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 */
public class FenetrePrincipale extends JFrame {
	private static final long serialVersionUID = 1L;
	
	protected PlateauGraphique panel;
	
	public FenetrePrincipale() {
		setLayout(new BorderLayout());
		setSize(Plateau.X+16,Plateau.Y+39);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel = new PlateauGraphique();
		add(panel, BorderLayout.CENTER);
	}
}