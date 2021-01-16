package utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import agent.Objects;
import agent.Palet;
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
	 * Retourne la distance entre deux points, définis par leurs coordonnées.
	 * @param x1 la coordonnée x du premier point.
	 * @param y1 la coordonnée y du premier point.
	 * @param x2 la coordonnée x du deuxième point.
	 * @param y2 la coordonnée y du deuxième point.
	 * @return la distance qui relie les deux points.
	 */
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x1 - x2, y1 - y2);
	}
	/**
	 * Retourne la distance entre deux points en paramètre.
	 * @param p1 Le premier point.
	 * @param p2 Le deuxième point.
	 * @return la distance qui relie les deux points.
	 */
	public static double getDistance(Point2D p1, Point2D p2) {
		if (p1 == null || p2 == null) throw new IllegalArgumentException("One of the points refer to 'null'.");
		return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	/**
	 * Détermine si un segment croise pleinement un cercle. Si tel est le cas, alors
	 * ce segment croisera deux fois le cercle, et on renvoie le point qui se situe au
	 * milieu de ces deux points d'intersection. Par question d'optimisation, un premier
	 * appel à <code>intersects(Ligne2D,Cercle2D)</code> est fait car dans la très grande
	 * majorité des appels à cette fonction, le retour sera faux (en effet, la méthode est
	 * appelée de très nombreuse fois par itération). Et cette méthode est plus rapide que
	 * de parcourir toute la droite pour déterminer un point d'intersection.
	 * @param L un segment.
	 * @param C un cercle.
	 * @return Le point, s'il existe, milieu des deux intersections entre le segment et le cercle.
	 */
	public static Point2D intersectsOf(Ligne2D L, Cercle2D C) {
		if (L == null || C == null) throw new IllegalArgumentException("One of the arguments refers to 'null'.");
		if (!intersects(L,C)) return null;
		double cos = Math.cos(L.getAngle()), sin = Math.sin(L.getAngle());
		for (double i = 0; i < L.getDistance(); i += 0.1) {
			double x = L.getX1() + i * cos;
			double y = L.getY1() + i * sin;
			if (C.contains(x, y))
				return new Point2D.Double(x,y);
		}
		return null;
	}
	/**
	 * Teste si un segment et un cercle en paramètre se croisent. Si l'une des extrémités du segment
	 * est contenu dans le cercle, alors on renvoie vrai immédiatement. Pour tester le croisement,
	 * on utilise un <code>Triangle2D</code> qui relie d'une part les deux extrémité du segment et qui a pour
	 * sommet le centre du cercle. Si la médiane de ce triangle est plus petite que le rayon du cercle, alors
	 * le segment croise nécessairement le cercle ; mais la réciproque n'est pas nécessairement vraie. En effet,
	 * si une des extrémités est proche du cercle et que l'autre est très éloigné, la médiane deviendra plus grande
	 * et indiquera que le segment ne croise plus le cercle (alors que si). Pour compenser cela on utilise la hauteur
	 * et on moyenne hauteur et médiane.
	 * @param L Une ligne.
	 * @param C Un cercle.
	 * @return true si le segment et le cercle se croisent, false sinon.
	 */
	public static boolean intersects(Ligne2D L, Cercle2D C) {
		if (L == null || C == null) throw new IllegalArgumentException("One of the arguments refers to 'null'.");
		if (C.contains(L.getP1()) || C.contains(L.getP2())) return true;
		/*
		 * On regarde le triangle formé par les deux extrémité du segment
		 * et le centre du cercle.
		 */
		ArrayList<Point2D> array = new ArrayList<Point2D>();
		array.add(L.getP1());
		array.add(L.getP2());
		array.add(C.getCenter());
		Triangle2D tgl = new Triangle2D(array);
		/*
		 * Calcul de la médiane du sommet du triangle.
		 * Condition de la médiane suffisante.
		 */
		return (tgl.getMediane() + tgl.getMediane())/2 < C.getRayon();
	}
	/**
	 * Méthode outils qui détermine si deux segments en paramètre se croisent.
	 * @param L1 Un segment.
	 * @param L2 Un autre segment.
	 * @return true si les deux segments se croisent, faux sinon.
	 */
	public static boolean intersects(Ligne2D L1, Ligne2D L2) {
		if (L1 == null || L2 == null) throw new IllegalArgumentException("One of the arguments refers to 'null'.");
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
		int minDist = Integer.MAX_VALUE;
		Objects nearestObj = null;
		Point2D org = t.getOrigine();
		/*
		 * 1er test : est-ce qu'il y a dans le cône un objet qui est encore plus proche ? Si oui,
		 * on récupère cet objet.
		 */
		for (Objects o : c) {
			if (t.contains(o.getShape().getForm().getCenter())) {
				int dist = (int) getDistance(org.getX(), org.getY(), o.getX(), o.getY());
				/*
				 * En dessous de 32cm, le capteur à ultrasons regarde "au-dessus" des palets à cause
				 * de la différence entre la hauteur du capteur et la hauteur des palets.
				 */
				if (o instanceof Palet && dist < 32 * 4) continue;
				if (dist < minDist) {
					minDist = dist;
					nearestObj = o;
				}				
			}
		}
		/*
		 * 2eme test : est-ce que l'un des bords du cône touche un mur.
		 */
		for (int i = 0; i < minDist; i++) {
			double x1 = org.getX() + i * Math.cos(angleL1); double y1 = org.getY() + i * Math.sin(angleL1);
			double x2 = org.getX() + i * Math.cos(angleL2); double y2 = org.getY() + i * Math.sin(angleL2);
			if (!Plateau.contains(x1,y1) || !Plateau.contains(x2,y2)) {
				minDist = i; 
				break;
			}			
		}
		if (nearestObj == null) return minDist;
		/*
		 * Si l'objet existe, on calcule avec précision la distance qui sépare 
		 */
		double angle = (angleL1 + angleL2)/2;
		for (int i = 0; i < minDist; i ++) {
			double x = org.getX() + i * Math.cos(angle);
			double y = org.getY() + i * Math.sin(angle);
			if (nearestObj.contains(x,y))
				minDist = (int)Math.round(i);
		}
		return minDist;
	}
	/**
	 * Calcul le scalaire des deux vecteurs directeurs des deux lignes.
	 * @param L1 Une ligne.
	 * @param L2 Une autre ligne.
	 * @return Le scalaire des deux lignes.
	 */
	public static double computesScalaire(Ligne2D L1, Ligne2D L2) {
		return L1.getDx() * L2.getDx() + L1.getDy() * L2.getDy();
	}
	/**
	 * Méthode qui permet de renvoyer une valeur dans un intervalle à partir de la position
	 * d'une autre valeur dans un autre intervalle. 
	 * On paramètre la méthode comme ceci givesSameScale({0,6},{10,22},16) ce qui donne : 
	 * <p>
	 * givesSameScale(0,6,10,22,16) donne 3
	 * <p>
	 * car 16 se situe aux 1/2 de l'intervalle [10,22]. Si on récupère la valeur aux 1/2
	 * de l'intervalle [0,6], on récupère 3.
	 * @param min Le minimum de l'intervalle cible.
	 * @param max Le maximum de l'intervalle cible.
	 * @param MIN Le minimum de l'intervalle source.
	 * @param MAX Le maximum de l'intervalle source.
	 * @param value La valeur de l'intervalle source.
	 * @return La valeur de l'intervalle [min;max].
	 */
	public static double givesSameScale(double min, double max, double MIN, double MAX, double value) {
		if (value < MIN || value > MAX) throw new IllegalArgumentException("The argument 'value' is out of range : ("+value+")");
		if (MIN > MAX) throw new IllegalArgumentException("The source range is incorrect");
		double rapport = (value-MIN)/(MAX-MIN);
		if (min > max)
			return min - rapport * (min-max);
		return min + rapport * (max-min);
	}
}