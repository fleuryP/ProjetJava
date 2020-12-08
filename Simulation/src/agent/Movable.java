package agent;
/**
 * Interface qui permet à un <code>Robot</code> de se déplacer.
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
	 * paramètre de vitesse moteur v. Si v < 0, le robot recule.
	 * @param v la vitesse moteur à atteindre pour les deux roues du
	 * <code>Robot</code>.
	 */
	public abstract void avancer(double v);
	/**
	 * Le <code>Robot</code> tourne sur lui même jusqu'à l'appel de
	 * la méthode <code>arreter()</code>.
	 * @param tourne positivement si dir évalué à true, négativement sinon.
	 */
	public abstract void startRotating(boolean dir);
	/**
	 * Le <code>Robot</code> tourne sur lui même grâce à un paramètre
	 * d'angle en RADIANS.
	 * @param angle l'angle en radians.
	 */
	public abstract void rotation(double angle);
}