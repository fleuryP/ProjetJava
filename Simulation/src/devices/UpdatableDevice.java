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
	public static final byte INDEX_BIGMOTOR_LEFT	= 0;
	public static final byte INDEX_BIGMOTOR_RIGHT	= 1;
	public static final byte INDEX_MOTOR			= 2;
	public static final byte INDEX_TOUCH			= 3;
	public static final byte INDEX_SONIC			= 4;
	public static final byte INDEX_COLOR			= 5;
	public void updateDevice();
}