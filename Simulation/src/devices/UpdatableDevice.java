package devices;
/**
 * Interface qui permet de d�crire un composant comme �tant un 
 * composant du <code>Robot</code> et d'�tre mis � jour � chaque 
 * update g�n�rale. Un composant est un <code>Device</code>, et
 * peut �tre soit un capteur, soit un moteur.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 * @see Motor
 * @see Sensor
 */
public interface UpdatableDevice {
	/**
	 * Liste les composants d'un <code>Robot</code>, Moteurs et Senseurs, dans <b>l'ordre</b> !
	 * Ces constantes permettent de d�terminer � quel indice du tableau <code>composants</code>
	 * se trouve le composant en question. Ainsi, on trouvera le <code>BigMotor</code> qui contr�le
	 * la roue gauche en indice 0, et le <code>ColorSensor</code> qui d�crit le capteur de couleur
	 * � l'indice 5. On utilise la m�thode de la classe <code>Robot</code> : 
	 * <p>
	 * <b>- getComposant(DevicesIndex cste) : UpdatableDevice</b> 
	 * <p>
	 * Ainsi, on r�cup�re notre <code>UpdatableDevice</code> en utilisant la constante associ�e � celui-ci.
	 * @author GATTACIECCA Bastien
	 */
	public static enum DevicesIndex {
		INDEX_BIGMOTOR_LEFT,
		INDEX_BIGMOTOR_RIGHT,
		INDEX_MOTOR,
		INDEX_TOUCH,
		INDEX_SONIC,
		INDEX_COLOR
	}
	/**
	 * Met � jour le <code>Device</code>.
	 */
	public void updateDevice();
	/**
	 * @return Une repr�sentation textuelle du <code>Device</code>.
	 */
	public String toString();
}