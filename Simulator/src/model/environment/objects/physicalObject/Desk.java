package model.environment.objects.physicalObject;

import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Position;

/**
 * A desk is used to serve people.
 * 
 * @author S.A.M. Janssen
 */
public class Desk extends PhysicalObject implements Openable {

	/**
	 * The serving position.
	 */
	private Position servingPosition;
	/**
	 * The agent at the desk.
	 */
	private HumanAgent agentAtDesk;
	/**
	 * The system is open or closed.
	 */
	private boolean isOpen;

	/**
	 * Creates a wall from four corner coordinates.
	 * 
	 * @param x0
	 *            The first x coordinate.
	 * @param x1
	 *            The second x coordinate.
	 * @param x2
	 *            The third x coordinate.
	 * @param x3
	 *            The fourth x coordinate.
	 * @param y0
	 *            The first y coordinate.
	 * @param y1
	 *            The second y coordinate.
	 * @param y2
	 *            The third y coordinate.
	 * @param y3
	 *            The fourth y coordinate.
	 * @param servingPosition
	 *            The serving position.
	 */
	public Desk(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3,
			Position servingPosition) {
		super(x0, x1, x2, x3, y0, y1, y2, y3);
		if (contains(servingPosition))
			throw new IllegalArgumentException("Entry position should be outside of the area of the chair.");
		if (getDistance(servingPosition) > 1)
			throw new IllegalArgumentException("Entry position should be within a distance of 1 of the chair.");
		this.servingPosition = servingPosition;
		isOpen = true;
	}

	/**
	 * Creates a rectangular wall with a specified top left corner, width and
	 * height.
	 * 
	 * @param topX
	 *            The x coordinate of the top left corner.
	 * @param topY
	 *            The y coordinate of the top left corner.
	 * @param width
	 *            The width of the wall.
	 * @param height
	 *            The height of the wall.
	 * @param servingPosition
	 *            The serving position.
	 */
	public Desk(double topX, double topY, double width, double height, Position servingPosition) {
		super(topX, topY, width, height);
		if (contains(servingPosition))
			throw new IllegalArgumentException("Entry position should be outside of the area of the chair.");
		if (getDistance(servingPosition) > 1)
			throw new IllegalArgumentException("Entry position should be within a distance of 1 of the chair.");
		this.servingPosition = servingPosition;
		isOpen = true;
	}

	/**
	 * Create a wall from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 * @param servingPosition
	 *            The serving position.
	 */
	public Desk(double[] x, double[] y, Position servingPosition) {
		super(x, y);
		if (contains(servingPosition))
			throw new IllegalArgumentException("Entry position should be outside of the area of the chair.");
		if (getDistance(servingPosition) > 1)
			throw new IllegalArgumentException("Entry position should be within a distance of 1 of the chair.");
		this.servingPosition = servingPosition;
		isOpen = true;
	}

	/**
	 * Create a wall from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 * @param servingPosition
	 *            The serving position.
	 */
	public Desk(List<Position> corners, Position servingPosition) {
		super(corners);
		if (contains(servingPosition))
			throw new IllegalArgumentException("Entry position should be outside of the area of the chair.");
		if (getDistance(servingPosition) > 1)
			throw new IllegalArgumentException("Entry position should be within a distance of 1 of the chair.");
		this.servingPosition = servingPosition;
		isOpen = true;
	}

	/**
	 * Gets the agent at the desk.
	 * 
	 * @return The agent at the desk.
	 */
	public HumanAgent getAgentAtDesk() {
		if (agentAtDesk != null) {
			if (agentAtDesk.isDestroyed())
				agentAtDesk = null;
		}
		return agentAtDesk;
	}

	/**
	 * Gets the serving position.
	 * 
	 * @return The serving position.
	 */
	public Position getServingPosition() {
		return servingPosition;
	}

	/**
	 * Determines if the desk is occupied.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isOccupied() {
		if (agentAtDesk == null)
			return false;
		if (agentAtDesk.isDestroyed()) {
			agentAtDesk = null;
			return false;
		}
		return true;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Sets the agent at the desk.
	 * 
	 * @param agentAtDesk
	 *            The agent at the desk.
	 */
	public void reserveDeskPosition(HumanAgent agentAtDesk) {
		if (!isOccupied())
			this.agentAtDesk = agentAtDesk;
	}

	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + servingPosition + ")";
	}

}
