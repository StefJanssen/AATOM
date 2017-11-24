package util.math.distributions;

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

	@Override
	public double getValue() {
		return Math.log(1 - random.nextDouble()) / (-lambda);
	}

}
