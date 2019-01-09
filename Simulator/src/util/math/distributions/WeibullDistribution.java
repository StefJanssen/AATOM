package util.math.distributions;

import util.math.RandomPlus;

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

	/**
	 * Generates a weibull random variable.
	 * 
	 * @param a
	 *            A parameter.
	 * @param b
	 *            B parameter.
	 * @param random
	 *            The random generator.
	 */
	public WeibullDistribution(double a, double b, RandomPlus random) {
		super(random);
		this.a = a;
		this.b = b;
	}

	@Override
	public double getValue() {
		return Math.pow(a * (-Math.log(random.nextDouble())), 1 / b);
	}

}
