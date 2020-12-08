package devices;
/**
 * Interface qui permet de décrire un composant comme étant un 
 * composant du <code>Robot</code> et d'être mis à jour à chaque 
 * update générale.
 * @author GATTACIECCA Bastien
 * @author FLEURY Pierre
 * @see Motor
 * @see Sensor
 */
public interface UpdatableDevice {
	/**
	 * Liste les composants d'un <code>Robot</code>, Moteurs & Senseurs, dans <b>l'ordre</b> !
	 * Ces constantes permettent de déterminer à quel indice du tableau <code>composants</code>
	 * se trouve le composant en question. Ainsi, on trouvera le <code>BigMotor</code> qui contrôle
	 * la roue gauche en indice 0, et le <code>ColorSensor</code> qui décrit le capteur de couleur
	 * à l'indice 5. On utilise la méthode de la classe <code>Robot</code> : 
	 * <p>
	 * <b>- getComposant(DevicesIndex cste) : UpdatableDevice</b> 
	 * <p>
	 * Ainsi, on récupère notre <code>UpdatableDevice</code> en utilisant la constante associée à celui-ci.
	 * @author GATTACIECCA Bastien
	 * @see composants
	 */
	public static enum DevicesIndex {
		INDEX_BIGMOTOR_LEFT,
		INDEX_BIGMOTOR_RIGHT,
		INDEX_MOTOR,
		INDEX_TOUCH,
		INDEX_SONIC,
		INDEX_COLOR
	}
	public void updateDevice();
	public String toString();
}