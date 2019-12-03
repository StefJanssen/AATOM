package util.math.distributions;

import util.math.RandomPlus;

/**
 * The exponential distribution.
 * 
 * @author S.A.M. Janssen
 */
public class PoissonDistribution extends MathDistribution {

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
	public PoissonDistribution(double lambda) {
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
	public PoissonDistribution(double lambda, RandomPlus random) {
		super(random);
		this.lambda = lambda;
	}

	@Override
	public double getValue() {
		  double L = Math.exp(-lambda);
		  double p = 1.0;
		  int k = 0;

		  do {
		    k++;
		    p *= Math.random();
		  } while (p > L);

		  return k - 1;
	}
}