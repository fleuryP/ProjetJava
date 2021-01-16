package utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import agent.Objects;
import agent.Palet;
import devices.SonicSensor;
import environment.Plateau;
/**
 * La classe <code>Geometry</code> est une classe qui regroupe des méthodes statiques
 * pour des calculs géométriques comme par exemple trouver les deux points d'intersection
 * d'un <code>Cercle2D</code> et d'une <code>Line2D</code> ; ou encore de conserver nos angles
 * en radian dans l'intervalle ]-pi;pi].
 * @author GATTACIECCA Bastien
 *
 */
public final class Geometry {
	/**
	 * Aucune instanciation possible.
	 * 
	 */
	private Geometry() {}
	/**
	 * Réduit un angle à sa définition la plus simple. L'angle retourné
	 * appartient à ]-pi;pi].
	 * @param angle L'angle à simplifier.
	 * @return un angle compris dans ]-pi;pi]. 
	 */
	public static double computesAngle(double angle) {
		angle %= 2*Math.PI;
		if (angle > Math.PI) {
			return angle - 2 * Math.PI;
		}else if (angle <= -Math.PI) {
			return angle = angle + 2 * Math.PI;
		}
		return angle;
	}
	/**
	 * Réduit un angle à sa définition positive. L'angle retourné appartient
	 * à [0;2pi[.
	 * @param angle L'angle à simplifier.
	 * @return un angle compris dans [0;2pi[.
	 */
	public static double computesPositiveAngle(double angle) {
		angle %= 2*Math.PI;
		if (angle < 0) {
			return angle + 2 * Math.PI;
		}
		return angle;
	}
	/**
	 * Méthode outil qui renvoie un point, milieu de deux points définis par leurs 
	 * coordonnées.
	 * @param x1 La coordonnée x de la première extrémité du segment.
	 * @param y1 La coordonnée y de la première extrémité du segment.
	 * @param x2 La coordonnée x de la deuxième extrémité du segment.
	 * @param y2 La coordonnée y de la deuxième extrémité du segment.
	 * @return le milieu du segment.
	 */
	public static Point2D getMilieu(double x1, double y1, double x2, double y2) {
		return new Point2D.Double((x1+x2)/2,(y1+y2)/2);
	}
	/**
	 * Méthode outil qui renvoie un point, milieu du segment qui relie les deux points
	 * en paramètre.
	 * @param p1 la première extrémité du segment.
	 * @param p2 la deuxième extrémité du segment
	 * @return le milieu du segment.
	 */
	public static Point2D getMilieu(Point2D p1, Point2D p2) {
		if (p1 == null || p2 == null) throw new IllegalArgumentException("One of the points refer to 'null'.");
		return getMilieu(p1.getX(),p1.getY(),p2.getX(),p2.getY());
	}
	/**
	 * Méthode outil qui renvoie un point, milieu du segment en paramètre.
	 * @param L Un segment.
	 * @return le milieu du segment.
	 */
	public static Point2D getMilieu(Ligne2D L) {
		if (L == null) throw new IllegalArgumentException("The line refers to 'null'.");
		return getMilieu(L.getP1(),L.getP2());
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x1 - x2, y1 - y2);
	}
	public static double getDistance(Point2D p1, Point2D p2) {
		if (p1 == null || p2 == null) throw new IllegalArgumentException("One of the points refer to 'null'.");
		return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	/**
	 * Détermine si un segment croise pleinement un cercle. Si tel est le cas, alors
	 * ce segment croisera deux fois le cercle, et on renvoie le point qui se situe au
	 * milieu de ces deux points d'intersection.
	 * @param L un segment.
	 * @param C un cercle.
	 * @return Le point, s'il existe, milieu des deux intersections entre le segment et le cercle.
	 */
	public static Point2D intersectsOf(Ligne2D L, Cercle2D C) {
		if (L == null || C == null) throw new IllegalArgumentException("One of the arguments refer to 'null'.");
		/**
		 * On récupère les coordonnées dans des variables explicites.
		 */
		Point2D origine = L.getP1();
		Point2D extremite = L.getP2();
		/**
		 * Calcul de la distance du segment et de son angle.
		 */
		double dx = extremite.getX() - origine.getX();
		double dy = extremite.getY() - origine.getY();
		double Ldist = Math.hypot(dx,dy);
		double LdistParcourue = 0; //incrémentation
		double Lalpha = Math.atan2(dy,dx);
		/**
		 * On parcourt la droite paramétrique.
		 */
		for (double i = 0; LdistParcourue <= Ldist; i += 0.01) {
			double x = origine.getX() + i * Math.cos(Lalpha);
			double y = origine.getY() + i * Math.sin(Lalpha);
			LdistParcourue = Math.hypot(x - origine.getX(),y - origine.getY());
			/**
			 * Si présent dans le Cercle :
			 */
			if (C.contains(x,y)) {
				for (double j = i; LdistParcourue <= Ldist; j += 0.01) {
					double x1 = x + j * Math.cos(Lalpha);
					double y1 = y + j * Math.sin(Lalpha);
					LdistParcourue = Math.hypot(x - origine.getX(),y - origine.getY());
					if (!C.contains(x1,y1)) {
						return getMilieu(x,y,x1,y1);
					}
				}
			}
		}
		return null;
	}
	/**
	 * Méthode outils qui détermine si deux segments en paramètre se croisent.
	 * @param L1 Un segment.
	 * @param L2 Un autre segment.
	 * @return true si les deux segments se croisent, faux sinon.
	 */
	public static boolean intersectsOf(Ligne2D L1, Ligne2D L2) {
		if (L1 == null || L2 == null) throw new IllegalArgumentException("One of the arguments refers to 'null'.");
		if (L1.equals(L2)) return true;
		return Line2D.linesIntersect(L1.getP1().getX(),L1.getP1().getY(),L1.getP2().getX(),L1.getP2().getY(),
				L2.getP1().getX(),L2.getP1().getY(),L2.getP2().getX(),L2.getP2().getY());
	}
	/**
	 * Retourne le premier <code>Objects</code> contenu dans le triangle, en partant de son point d'origine.
	 * Si une ligne apparaît avant, c'est la distance entre l'origine du triangle et la ligne qui est renvoyée.
	 * @param t Un triangle isocèle ; qui correspond au cône que perçoit le capteur à ultrasons.
	 * @param angleL1 L'angle de la ligne qui part de l'origine et qui suit le haut du triangle.
	 * @param angleL2 L'angle de la ligne qui part de l'origine et qui suit le bas du triangle.
	 * @param c La collection d'<code>Objects</code> que l'on va tester ; s'ils sont contenu dans le triangle ou non.
	 * @return La distance de l'<code>Objects</code> le plus proche, ou d'un mur s'il l'est davantage.
	 */
	public static int getFirstObject(Triangle2D t, double angleL1, double angleL2, Collection<Objects> c) {
		Point2D org = t.getOrigine();
		int minDist = Integer.MAX_VALUE;
		for (int i = 0; i < SonicSensor.DISTANCE_MAX; i++) {
			double x1 = org.getX() + i * Math.cos(angleL1); double x2 = org.getX() + i * Math.cos(angleL2);
			double y1 = org.getY() + i * Math.sin(angleL1); double y2 = org.getY() + i * Math.sin(angleL2);
			if (!Plateau.contains(x1,y1) || !Plateau.contains(x2,y2)) {
				minDist = i;
				break;
			}
		}
		for (Objects o : c) {
			if (t.contains(o)) {
				int dist = (int)(Math.hypot(o.getX() - org.getX(), o.getY() - org.getY()));
				/*
				 * En dessous de 32cm, le capteur à ultrasons regarde "au-dessus" des palets à cause
				 * de la différence entre la hauteur du capteur et la hauteur des palets.
				 */
				if (o instanceof Palet) if (dist < 32 * 4) continue;
				minDist = Math.min(minDist,dist);
			}
		}
		return minDist;
	}
	/**
	 * 
	 * @param L1
	 * @param L2
	 * @return
	 */
	public static double computesScalaire(Ligne2D L1, Ligne2D L2) {
		return L1.getDx() * L2.getDx() + L1.getDy() * L2.getDy();
	}
}