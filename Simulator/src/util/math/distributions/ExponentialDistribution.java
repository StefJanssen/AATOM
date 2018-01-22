package util.math.distributions;

import util.math.RandomPlus;

/**
 * The normal distribution.
 * 
 * @author S.A.M. Janssen
 */
public class ExponentialDistribution extends MathDistribution {

	/**
	 * The lambda.
	 */
	private double lambda;

	/**
	 * Creates an exponential distribution with a specified lambda.
	 * 
	 * @param lambda
	 *            The lambda parameter.
	 */
	public ExponentialDistribution(double lambda) {
		this.lambda = lambda;
	}

	/**
	 * Creates an exponential distribution with a specified lambda.
	 * 
	 * @param lambda
	 *            The lambda parameter.
	 * @param random
	 *            The random generator.
	 */
	public ExponentialDistribution(double lambda, RandomPlus random) {
		super(random);
		this.lambda = lambda;
	}

	@Override
	public double getValue() {
		return Math.log(1 - random.nextDouble()) / (-lambda);
	}

}
