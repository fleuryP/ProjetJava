package utils;

public final class Maths {

	private Maths() {}

	/**
	 * M�thode qui permet de renvoyer une valeur dans un intervalle � partir de la position
	 * d'une autre valeur dans un autre intervalle. Par exemple, 
	 * <p>
	 * givesSameScale(0,6,10,22,16) -> 3
	 * <p>
	 * car 16 se situe aux 1/2 de l'intervalle [10,22]. Si on r�cup�re la valeur aux 1/2
	 * de l'intervalle [0,6], on r�cup�re 3.
	 * @param min Le minimum de l'intervalle cible.
	 * @param max Le maximum de l'intervalle cible.
	 * @param MIN Le minimum de l'intervalle source.
	 * @param MAX Le maximum de l'intervalle source.
	 * @param value La valeur de l'intervalle source.
	 * @return La valeur de l'intervalle [min;max].
	 */
	public static double givesSameScale(double min, double max, double MIN, double MAX, double value) {
		if (value < MIN || value > MAX) throw new IllegalArgumentException("The argument 'value' is out of range");
		if (MIN > MAX) throw new IllegalArgumentException("The source range is incorrect");
		double rapport = (value-MIN)/(MAX-MIN);
		if (min > max) {
			return min - rapport * (min-max);
		}
		return min + rapport * (max-min);
	}

}
