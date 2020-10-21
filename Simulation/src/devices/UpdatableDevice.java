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
	public static final byte INDEX_BIGMOTOR_LEFT	= 0;
	public static final byte INDEX_BIGMOTOR_RIGHT	= 1;
	public static final byte INDEX_MOTOR			= 2;
	public static final byte INDEX_TOUCH			= 3;
	public static final byte INDEX_SONIC			= 4;
	public static final byte INDEX_COLOR			= 5;
	public void updateDevice();
}