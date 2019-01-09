package simulation.simulation.endingCondition;

import simulation.simulation.Simulator;

/**
 * The ending conditions of a simulation.
 * 
 * @author S.A.M. Janssen
 */
public abstract class EndingConditions {

	/**
	 * The {@link Simulator}.
	 */
	protected Simulator simulator;

	/**
	 * The simulation was ended.
	 */
	private boolean hasEnded;

	/**
	 * Forces the simulation to end.
	 */
	public void forceEnd() {
		hasEnded = true;
	}

	/**
	 * Returns the return values of a simulation.
	 * 
	 * @return The return values.
	 */
	public abstract Object[] getReturnValues();

	/**
	 * Determines if a simulation has ended. When extending this method, always
	 * return true if this super method returns true.
	 * 
	 * @param numberOfSteps
	 *            The number of steps in the simulation.
	 * @return True if the simulation is ended, false otherwise.
	 */
	public boolean hasEnded(long numberOfSteps) {
		return hasEnded;
	}

	/**
	 * Sets the simulator.
	 * 
	 * @param simulator
	 *            The simulator.
	 */
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
}
