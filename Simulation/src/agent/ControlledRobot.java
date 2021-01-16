package agent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import environment.Plateau;
import environment.PlateauGraphique;

public class ControlledRobot extends Robot {
	private PlateauGraphique pg;
	private double distance;
	public ControlledRobot(PlateauGraphique pg, String name, double x, double y) {
		super(pg, name, x, y);
		this.pg = pg;
		distance = 0;
		deplacerRobot();
	}
	@Override
	public void update() {
		super.update();
		if (pg.isCursorDisplayed()) {
			distance = Math.hypot(
					Plateau.curseur.y - getY(), 
					Plateau.curseur.x - getX());
			if (distance > 10) {
				setAngle(Math.atan2(
						Plateau.curseur.y - getY(), 
						Plateau.curseur.x - getX()));
				setX(getX() + Math.cos(getAngle()));
				setY(getY() + Math.sin(getAngle()));
			}
		}
	}
	/**
	 * @return La vitesse du <code>Robot</code>.
	 */
	public double getSpeed() {
		return isMoving() ? 1.0d : 0.0d;  
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMoving() {
		return distance > 10;
	}
	/**
	 * Représentation textuelle de l'état du robot.
	 */
	public String toString() {
		String str = super.toString();
		return str + "  -  Type : Controlled";
	}
	private void deplacerRobot() {
		pg.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (!pg.isDragNeeded()) return;
				setX(e.getX());
				setY(e.getY());
			}
		});
	}
}