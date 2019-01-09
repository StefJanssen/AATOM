package util.io.logger;

import java.io.File;

import model.agent.Agent;

/**
 * The agent logger logs the user defined log of an agent.
 * 
 * @author S.A.M. Janssen
 */
public class AgentLogger extends Logger {

	/**
	 * Creates an agent logger.
	 * 
	 * @param fileName
	 *            The file name.
	 */
	public AgentLogger(String fileName) {
		super(fileName + File.separator + "agentLog.txt");
	}

	/**
	 * Gets the agent information that needs to be printed.
	 * 
	 * @param agent
	 *            The agent.
	 * @return The information to be printed.
	 */
	private String getAgentInformation(Agent agent) {
		String[] toLog = agent.writeLog();
		if (toLog == null)
			return null;

		StringBuilder builder = new StringBuilder();
		for (String s : toLog)
			builder.append(s + ",");
		String myString = builder.toString();
		return myString.substring(0, myString.length() - 1);
	}

	@Override
	public void update(long time, boolean ended) {
		for (Agent a : simulator.getMap().getMapComponents(Agent.class)) {
			String info = getAgentInformation(a);
			if (info != null)
				printLine(time + "," + a.toString() + "," + info);
		}
	}
}
