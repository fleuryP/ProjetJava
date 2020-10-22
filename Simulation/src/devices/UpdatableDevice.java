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
	public static enum DevicesIndex { 
		INDEX_BIGMOTOR_LEFT,
		INDEX_BIGMOTOR_RIGHT,
		INDEX_MOTOR,
		INDEX_TOUCH,
		INDEX_SONIC,
		INDEX_COLOR}
	
	public void updateDevice();
}