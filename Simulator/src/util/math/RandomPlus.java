package util.math;

import java.util.Random;

/**
 * The Random Plus class extends the general {@link Random} class, so that more
 * complex random numbers can easily be drawn as well.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class RandomPlus extends Random {

	/**
	 * The seed.
	 */
	private long seed;

	/**
	 * Creates a Random Plus object.
	 */
	public RandomPlus() {
		this(System.currentTimeMillis());
	}

	/**
	 * Creates a Random Plus object with a given seed.
	 * 
	 * @param seed
	 *            The seed.
	 */
	public RandomPlus(long seed) {
		super(seed);
		this.seed = seed;
	}

	/**
	 * Gets the seed.
	 * 
	 * @return The seed.
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * Generates a random number drawn from an exponential distribution.
	 * 
	 * @param lambda
	 *            The parameter lambda.
	 * @return The random number.
	 */
	public double nextExponential(double lambda) {
		return Math.log(1 - nextDouble()) / (-lambda);
	}

	/**
	 * Generates a random number drawn from a normal distribution.
	 * 
	 * @param mean
	 *            The mean.
	 * @param standardDeviation
	 *            The standard deviation.
	 * @return The random number.
	 */
	public double nextNormal(double mean, double standardDeviation) {
		return nextGaussian() * standardDeviation + mean;
	}
}
