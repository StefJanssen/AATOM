package util.io.logger;

import java.io.File;

import model.agent.Agent;
import simulation.simulation.util.Utilities;

/**
 * The Simulation Logger logs essential information about the simulation.
 * 
 * @author S.A.M. Janssen
 */
public class SimulationLogger extends Logger {

	/**
	 * The number of skips done.
	 */
	private int skips;
	/**
	 * The skip factor.
	 */
	private int skipFactor;

	/**
	 * Creates a simulation logger.
	 * 
	 * @param fileName
	 *            The file name.
	 */
	public SimulationLogger(String fileName) {
		super(fileName + File.separator + "agentTrace.txt");
		skipFactor = 20;
		skips = 20;
	}

	/**
	 * Gets the agent information that needs to be printed.
	 * 
	 * @param agent
	 *            The agent.
	 * @return The information to be printed.
	 */
	private String getAgentInformation(Agent agent) {
		StringBuilder builder = new StringBuilder();
		String string = agent.hashCode() + "," + Utilities.round(agent.getPosition().x, 2) + ","
				+ Utilities.round(agent.getPosition().y, 2);
		builder.append(string);
		return builder.toString();
	}

	@Override
	public void update(long time, boolean ended) {
		if (skips == skipFactor) {
			skips = 0;
			for (Agent a : simulator.getMap().getMapComponents(Agent.class)) {
				printLine(time + "," + getAgentInformation(a));
			}
		}
		skips++;
	}
}
