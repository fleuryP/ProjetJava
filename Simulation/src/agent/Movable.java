package agent;
/**
 * Interface qui permet � un <code>MovableChassis</code> de se d�placer.
 * Le temps s'exprime en seconde (s).
 * Les autres unit�s doivent �tre homog�nes ; mais au choix de l'utilisateur.
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
	 * param�tre de vitesse moteur v. Si la distance est inf�rieure � 0, le robot recule.
	 * @param v la vitesse moteur � atteindre pour les deux roues du chassis.
	 * @param dist la distance sur la quelle le robot va avancer.
	 * <code>Robot</code>.
	 */
	public abstract void avancer(double v, double dist);
	/**
	 * Le <code>Robot</code> tourne sur lui m�me jusqu'� l'appel de
	 * la m�thode <code>arreter()</code>.
	 * @param v la vitesse de rotation.
	 * @param dir tourne positivement si dir �valu� � true, n�gativement sinon.
	 */
	public abstract void startRotating(double v, boolean dir);
	/**
	 * Le <code>Robot</code> tourne sur lui m�me gr�ce � un param�tre
	 * d'angle en degr�s.
	 * @param v la vitesse de rotation.
	 * @param angle l'angle en degr�s.
	 */
	public abstract void rotation(double v, double angle);
}