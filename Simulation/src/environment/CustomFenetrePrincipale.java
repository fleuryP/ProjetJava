package environment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import agent.ControlledRobot;
/**
 * Classe qui décrit une <code>FenetrePrincipale</code> avec une barre
 * de boutons en bas, qui permettent plusieurs fonctionnalités pour
 * faire des tests avec le robot.
 *
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 *
 */
public class CustomFenetrePrincipale extends FenetrePrincipale {
	private static final long serialVersionUID = 1L;
	/**
	 * Panel du bas qui regroupe des boutons ; qui agissent sur des <code>ControlledRobot</code>
	 * lorsqu'ils sont trigger.
	 */
	private JPanel panelWithButtons;
	/**
	 * Le robot contrôlé par la "barre d'outils" du bas.
	 */
	private ControlledRobot robotsControle;
	/**
	 * Construit une <code>FenetrePrincipale</code> plus spécifique qui permet de contrôler un
	 * robot via une barre d'outils. Cette barre reste en bas, quelque soit la taille ou le redimensionnement.
	 */
	public CustomFenetrePrincipale() {
		super();
		/*
		 * On redimmensionne pour continuer de voir l'ensemble du plateau + la barre.
		 */
		setSize(Plateau.X+16,Plateau.Y+84);
		/*
		 * On construit dans la partie Sud du getContentPane() de la Frame principale
		 * un JPanel qui va regrouper les boutons d'actions.
		 */
		panelWithButtons = new JPanel();
		FlowLayout fl = (FlowLayout)panelWithButtons.getLayout();
		fl.setHgap(0); fl.setVgap(0);
		fl.setAlignment(FlowLayout.CENTER);
		super.add(panelWithButtons, BorderLayout.SOUTH);
		
		initPlayButton();
		/*
		 * On récupère tous les 'controlled' robots, sur qui s'appliquent
		 * les évènements des boutons.
		 */
		robotsControle = panel.getControlledRobot();
		
	}
	/**
	 * On construit et ajoute les fonctionnalités des boutons.
	 */
	private void initPlayButton() {
		final ImageIcon[]butIcons = new ImageIcon[] {
				new ImageIcon("play.png"),
				new ImageIcon("localisation.png"),
				new ImageIcon("drag.png"),
				new ImageIcon("ouvrir.png"),
				new ImageIcon("fermer.png"),
				new ImageIcon("turnleft.png"),
				new ImageIcon("turnright.png")
		};
		final JButton[]tabButs = new JButton[butIcons.length];
		for (int i = 0; i < butIcons.length; i++) {
			tabButs[i] = createBouton(butIcons[i]);
		}
		//bouton play/pause
		tabButs[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setPause(!panel.isInPause());
			}
		});
		//bouton direction via curseur
		tabButs[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setNeedCursor(!panel.isCursorDisplayed());
			}
		});
		//bouton relocalisation via drag
		tabButs[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setDrag(!panel.isDragNeeded());
			}
		});
		//bouton ouvrir pinces
		tabButs[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotsControle.getPinces().open();
			}
		});
		//bouton fermer pinces
		tabButs[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotsControle.getPinces().close();
			}
		});
		//bouton tourner gauche
		tabButs[5].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				robotsControle.setAngle(robotsControle.getAngle() - Math.PI/40);
			}
		});
		//bouton tourner droite
		tabButs[6].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				robotsControle.setAngle(robotsControle.getAngle() + Math.PI/40);
			}
		});
	}
	/**
	 * Méthode outil qui permet de paramétrer nos boutons avec les mêmes paramètres.
	 * On ne se soucie que de son image et de son rang en partant du coin inférieur
	 * gauche de la fenêtre.
	 * @param icon L'icon du bouton.
	 * @return un Bouton paramétré mais sans listener.
	 */
	private final JButton createBouton(ImageIcon icon) {
		JButton b = new JButton(icon);
		b.setSize(35,35);
		b.setBorderPainted(false);
		panelWithButtons.add(b);
		return b;
	}
}