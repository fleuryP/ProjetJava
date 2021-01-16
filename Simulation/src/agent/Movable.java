package agent;
/**
 * Interface qui permet à un <code>MovableChassis</code> de se déplacer.
 * Le temps s'exprime en seconde (s).
 * Les autres unités doivent être homogènes ; mais au choix de l'utilisateur.
 * @author GATTACIECCA Bastien
 */
public interface Movable {
	/**
	 * Le <code>Robot</code> s'arrete.
	 */
	public abstract void arreter();
	/**
	 * Le <code>Robot</code> s'arrete instantanément (due à une collision).
	 */
	public abstract void immediateStop();
	/**
	 * Le <code>Robot</code> attribue à ses <code>BigMotor</code> le
	 * paramètre de vitesse moteur v. Si la distance est inférieure à 0, le robot recule.
	 * @param v la vitesse moteur à atteindre pour les deux roues du chassis.
	 * @param dist la distance sur la quelle le robot va avancer.
	 * <code>Robot</code>.
	 */
	public abstract void avancer(double v, double dist);
	/**
	 * Le <code>Robot</code> tourne sur lui même jusqu'à l'appel de
	 * la méthode <code>arreter()</code>.
	 * @param v la vitesse de rotation.
	 * @param dir tourne positivement si dir évalué à true, négativement sinon.
	 */
	public abstract void startRotating(double v, boolean dir);
	/**
	 * Le <code>Robot</code> tourne sur lui même grâce à un paramètre
	 * d'angle en degrés.
	 * @param v la vitesse de rotation.
	 * @param angle l'angle en degrés.
	 */
	public abstract void rotation(double v, double angle);
}