package agent;
/**
 * Interface qui permet � un <code>Robot</code> de se d�placer.
 * @author GATTACIECCA Bastien
 */
public interface Movable {
	/**
	 * Le <code>Robot</code> s'arrete.
	 */
	public abstract void arreter();
	/**
	 * Le <code>Robot</code> s'arrete instantan�ment (due � une collision).
	 */
	public abstract void immediateStop();
	/**
	 * Le <code>Robot</code> attribue � ses <code>BigMotor</code> le
	 * param�tre de vitesse moteur v. Si v < 0, le robot recule.
	 * @param v la vitesse moteur � atteindre pour les deux roues du
	 * <code>Robot</code>.
	 */
	public abstract void avancer(double v);
	/**
	 * Le <code>Robot</code> tourne sur lui m�me jusqu'� l'appel de
	 * la m�thode <code>arreter()</code>.
	 * @param tourne positivement si dir �valu� � true, n�gativement sinon.
	 */
	public abstract void startRotating(boolean dir);
	/**
	 * Le <code>Robot</code> tourne sur lui m�me gr�ce � un param�tre
	 * d'angle en RADIANS.
	 * @param angle l'angle en radians.
	 */
	public abstract void rotation(double angle);
}