package util.math.distributions;

/**
 * The Weibull distribution.
 * 
 * @author S.A.M. Janssen
 */
public class WeibullDistribution extends MathDistribution {

	/**
	 * A parameter.
	 */
	private double a;
	/**
	 * B parameter.
	 */
	private double b;

	/**
	 * Generates a weibull random variable.
	 * 
	 * @param a
	 *            A parameter.
	 * @param b
	 *            B parameter.
	 */
	public WeibullDistribution(double a, double b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public double getValue() {
		return Math.pow(a * (-Math.log(random.nextDouble())), 1 / b);
	}

}
