package simulation.simulation.endingCondition;

/**
 * The base ending conditions for a simulation ensure that a simulation ends
 * after a fixed number of steps.
 * 
 * @author S.A.M. Janssen
 */
public class BaseEndingConditions extends EndingConditions {

	/**
	 * The maximum number of steps.
	 */
	private double maximumSeconds;

	/**
	 * Creates the base ending conditions with a specified maximum number of
	 * steps.
	 * 
	 * @param maximumSeconds
	 *            The maximum number of seconds.
	 */
	public BaseEndingConditions(double maximumSeconds) {
		this.maximumSeconds = maximumSeconds;
	}

	@Override
	public Object[] getReturnValues() {
		return null;
	}

	@Override
	public boolean isEnded(long numberOfSteps) {
		return numberOfSteps * (simulator.getTimeStep() / 1000.0) >= maximumSeconds || super.isEnded(numberOfSteps);
	}
}
