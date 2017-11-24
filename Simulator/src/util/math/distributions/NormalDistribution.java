package util.math.distributions;

/**
 * The normal distribution.
 * 
 * @author S.A.M. Janssen
 */
public class NormalDistribution extends MathDistribution {

	/**
	 * The mean.
	 */
	private double mean;
	/**
	 * The standard deviation.
	 */
	private double standardDeviation;

	/**
	 * Creates a normal distribution with a mean and a standard deviation.
	 * 
	 * @param mean
	 *            The mean.
	 * @param standardDeviation
	 *            The standard deviation.
	 */
	public NormalDistribution(double mean, double standardDeviation) {
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	@Override
	public double getValue() {
		return random.nextGaussian() * standardDeviation + mean;
	}

}
