package model.environment.objects.physicalObject;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Position;

/**
 * A chair.
 * 
 * @author S.A.M. Janssen
 *
 */
public class Chair extends PhysicalObject {

	/**
	 * The agent on the chair.
	 */
	private HumanAgent agent;
	/**
	 * The position in front of the chair.
	 */
	private Position entryPosition;
	/**
	 * The width of the chair.
	 */
	private final double width;

	/**
	 * Creates a chair at a certain {@link Position} with a certain width.
	 * 
	 * @param position
	 *            The position.
	 * @param entryPosition
	 *            The entry position.
	 * @param width
	 *            The width.
	 */
	public Chair(Position position, Position entryPosition, double width) {
		super(position.x, position.y, width, width);
		if (contains(entryPosition))
			throw new IllegalArgumentException("Entry position should be outside of the area of the chair.");
		if (getDistance(entryPosition) > 1)
			throw new IllegalArgumentException("Entry position should be within a distance of 1 of the chair.");

		this.width = width;
		this.entryPosition = entryPosition;
	}

	/**
	 * Gets the entry {@link Position}.
	 * 
	 * @return The entry position.
	 */
	public Position getEntryPosition() {
		return entryPosition;
	}

	/**
	 * Gets the width of the chair.
	 * 
	 * @return The width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Indicates if the chair is occupied.
	 * 
	 * @return True if it is occupied, false otherwise.
	 */
	public boolean isOccupied() {
		if (agent == null)
			return false;
		if (agent.isDestroyed()) {
			agent = null;
			return false;
		}
		return true;
	}

	/**
	 * Occupies the chair with an agent.
	 * 
	 * @param agent
	 *            The agent.
	 */
	public void setOccupied(HumanAgent agent) {
		if (agent.getDistance(entryPosition) > 2)
			throw new IllegalArgumentException("The agent is over 2 meters away from the chair. It cannot access it.");
		if (!isOccupied())
			this.agent = agent;
	}
}