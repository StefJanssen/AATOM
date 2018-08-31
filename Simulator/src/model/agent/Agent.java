package model.agent;

import model.environment.position.Position;
import model.map.shapes.CircularMapComponent;
import simulation.simulation.util.DirectlyUpdatable;

/**
 * An agent is an entity that is able to act. Its method {@link #update(int)}
 * will be called at every iteration in the simulation. It is assumed to be
 * circular, but can have a radius of 0 (which is the default).
 * 
 * @author S.A.M. Janssen
 */
public abstract class Agent extends CircularMapComponent implements DirectlyUpdatable {

	/**
	 * The log value.
	 */
	private String[] log;

	/**
	 * Creates an agent at a specific {@link Position} with a 0 radius.
	 * 
	 * @param position
	 *            The position.
	 */
	public Agent(Position position) {
		this(position, 0);
	}

	/**
	 * Creates an agent at a specific {@link Position}.
	 * 
	 * @param position
	 *            The position.
	 * @param radius
	 *            The radius of the agent.
	 */
	public Agent(Position position, double radius) {
		super(position, radius);
	}

	/**
	 * Determines if the agent wants to be removed from the simulation.
	 * 
	 * @return True if it does, false otherwise.
	 */
	public abstract boolean getWantsToBeRemoved();

	/**
	 * This method is used to initialize the agent.
	 */
	public void init() {

	}

	/**
	 * Sets the log value.
	 * 
	 * @param log
	 *            The log.
	 */
	public void setLog(String[] log) {
		this.log = log;
	}

	/**
	 * Gets the log of the agent and empties it. Only to be used by the logging
	 * classes.
	 * 
	 * @return The log.
	 */
	public String[] writeLog() {
		String[] tmp = log;
		log = null;
		return tmp;
	}
}