package model.agent;

import model.environment.map.MapComponent;
import model.environment.position.Position;
import simulation.simulation.util.DirectlyUpdatable;

/**
 * An agent is an entity that is able to act. Its method {@link #update(int)}
 * will be called at every iteration in the simulation.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Agent extends MapComponent implements DirectlyUpdatable {

	/**
	 * The log value.
	 */
	private String[] log;

	/**
	 * Creates an agent at a specific {@link Position}.
	 * 
	 * @param position
	 *            The position.
	 */
	public Agent(Position position) {
		super(position);
	}

	/**
	 * Determines if the agent wants to be removed from the simulation.
	 * 
	 * @return True if it does, false otherwise.
	 */
	public abstract boolean getWantsToBeRemoved();

	/**
	 * Sets the log value.
	 * 
	 * @param log
	 *            The log.
	 */
	protected void setLog(String[] log) {
		this.log = log;
	}

	/**
	 * Gets the log of the agent and empties it.
	 * 
	 * @return The log.
	 */
	public String[] writeLog() {
		String[] tmp = log;
		log = null;
		return tmp;
	}
}