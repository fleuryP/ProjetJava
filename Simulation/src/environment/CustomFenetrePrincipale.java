package environment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import agent.ControlledRobot;
import agent.Robot;
import devices.Motor;
import devices.UpdatableDevice;
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

	private ControlledRobot[]robotsControles;

	public CustomFenetrePrincipale() {
		super();
		setSize(getSize().width, getSize().height + 35);

		Robot[]allRobots = getPanel().getRobots();
		int p = 0; for (int i = 0; i < allRobots.length; i++)
			if (allRobots[i] instanceof ControlledRobot) p++;
		robotsControles = new ControlledRobot[p]; p = 0; 
		for (int i = 0; i < allRobots.length; i++) {
			if (allRobots[i] instanceof ControlledRobot) {
				robotsControles[p] = (ControlledRobot)allRobots[i]; p++;
			}
		}
		initPlayButton();
	}
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
		for (int i = 0; i < tabButs.length; i++) {
			tabButs[i] = createBouton(butIcons[i],i);
		}
		//bouton play/pause
		tabButs[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPanel().setPause(!getPanel().isInPause());
			}
		});
		//bouton direction via curseur
		tabButs[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPanel().setNeedCursor(!getPanel().isCursorDisplayed());
			}
		});
		//bouton relocalisation via drag
		tabButs[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPanel().setDrag(!getPanel().isDragNeeded());
			}
		});
		//bouton ouvrir pinces
		tabButs[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (ControlledRobot r : robotsControles) {
					Motor moteur = (Motor)r.getComposant(UpdatableDevice.DevicesIndex.INDEX_MOTOR);
					moteur.setState(Motor.POSITIVE_TURN);
				}
			}
		});
		//bouton fermer pinces
		tabButs[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (ControlledRobot r : robotsControles) {
					Motor moteur = (Motor)r.getComposant(UpdatableDevice.DevicesIndex.INDEX_MOTOR);
					moteur.setState(Motor.NEGATIVE_TURN);
				}
			}
		});
		//bouton tourner gauche
		tabButs[5].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for (ControlledRobot r : robotsControles) {
					r.setAngle(r.getAngle() - 0.1);
				}
			}
		});
		//bouton tourner droite
		tabButs[6].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for (ControlledRobot r : robotsControles) {
					r.setAngle(r.getAngle() + 0.1);
				}
			}
		});
	}
	/**
	 * Méthode outil qui permet de paramétrer nos boutons avec les mêmes paramètres.
	 * On ne se soucie que de son image et de son rang en partant du coin inférieur
	 * gauche de la fenêtre.
	 * @param icon L'icon du bouton.
	 * @param pos La position du bouton en partant de la gauche.
	 * @return un Bouton paramétré mais sans listener.
	 */
	private JButton createBouton(ImageIcon icon,int pos) {
		JButton b = new JButton(icon);
		this.add(b);
		b.setBounds(pos*35,Plateau.Y,35,35);
		b.setBorderPainted(false);
		return b;
	}
}