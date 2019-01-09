package util.math.distributions;

import simulation.simulation.util.Utilities;
import util.math.RandomPlus;

/**
 * A mathematical distribution.
 * 
 * @author S.A.M. Janssen
 */
public abstract class MathDistribution {

	/**
	 * The random generator.
	 */
	protected RandomPlus random;

	/**
	 * Creates a math distribution.
	 * 
	 */
	public MathDistribution() {
		random = Utilities.RANDOM_GENERATOR;
	}

	/**
	 * Creates a math distribution based on a separate random generator.
	 * 
	 * @param random
	 *            The random generator.
	 */
	public MathDistribution(RandomPlus random) {
		this.random = random;
	}

	/**
	 * Draws a number from the random distribution.
	 * 
	 * @return The random number.
	 */
	public abstract double getValue();
}
