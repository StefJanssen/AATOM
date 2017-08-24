package simulation.simulation.agentGenerator;

import model.agent.Agent;
import simulation.simulation.Simulator;

/**
 * The agent generator is responsible for generating agents in a simulation.
 * 
 * @author S.A.M. Janssen
 */
public abstract class AgentGenerator {

	/**
	 * The {@link Simulator}.
	 */
	protected Simulator simulator;

	/**
	 * Generates an agent for a simulation. It returns null if no agent is
	 * generated.
	 * 
	 * @param numberOfSteps
	 *            The number of steps in the simulation.
	 * @param timeStep
	 *            The time step.
	 * @param forced
	 *            This parameter is used to ensure that the method adds an
	 *            agent. If it is true, the method always returns an agent. If
	 *            it is false, the specific implementation determines when an
	 *            agent is generated.
	 * 
	 * @return The generated {@link Agent}.
	 */
	public abstract Agent generateAgent(long numberOfSteps, int timeStep, boolean forced);

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
