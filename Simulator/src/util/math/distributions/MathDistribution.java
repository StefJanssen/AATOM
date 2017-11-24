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
	protected RandomPlus random = Utilities.RANDOM_GENERATOR;

	/**
	 * Draws a number from the random distribution.
	 * 
	 * @return The random number.
	 */
	public abstract double getValue();
}
