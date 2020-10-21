package agent;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import environment.Plateau;
import environment.PlateauGraphique;

public class ControlledRobot extends Robot {
	private PlateauGraphique pg;
	public ControlledRobot(PlateauGraphique pg, String name, double x, double y) {
		super(pg, name, x, y);
		this.pg = pg;
		deplacerRobot();
	}
	@Override
	public void update() {
		super.update();
		if (pg.isCursorDisplayed()) {
			Point p = Plateau.curseur;
			double distance = Math.hypot(p.y - getY(), p.x - getX());
			if (distance > 10) {
				setAngle(Math.atan2(p.y - getY(), p.x - getX()));
				setX(getX() + Math.cos(getAngle()));
				setY(getY() + Math.sin(getAngle()));
			}
		}
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