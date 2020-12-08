import java.util.Random;
import environment.CustomFenetrePrincipale;

public class Exe {
	public static void main(String[] args) {
		/**
		 * Execute un objet de type </code>FenetrePrincipale<code>
		 * et la rend visible.
		 */
		CustomFenetrePrincipale p = new CustomFenetrePrincipale();
		p.setVisible(true);
		
		Random rg = new Random();
		rg.nextGaussian();	
	}
}