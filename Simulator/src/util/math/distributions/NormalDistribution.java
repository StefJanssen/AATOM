package util.math.distributions;

import util.math.RandomPlus;

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

	/**
	 * Creates a normal distribution with a mean and a standard deviation.
	 * 
	 * @param mean
	 *            The mean.
	 * @param standardDeviation
	 *            The standard deviation.
	 * @param random
	 *            The random generator.
	 */
	public NormalDistribution(double mean, double standardDeviation, RandomPlus random) {
		super(random);
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	@Override
	public double getValue() {
		return random.nextGaussian() * standardDeviation + mean;
	}

}
