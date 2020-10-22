package devices;
/**
 * Interface qui permet de d�crire un composant comme �tant un 
 * composant du <code>Robot</code> et d'�tre mis � jour � chaque 
 * update g�n�rale.
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