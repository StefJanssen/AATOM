package model.environment.objects.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;

/**
 * A gate area that can only be accessed by passing border control.
 * 
 * @author S.A.M. Janssen
 */
public class BorderControlGateArea extends GateArea {

	/**
	 * The desks.
	 */
	private List<Desk> borderControlDesks;
	/**
	 * The queing area.
	 */
	private QueuingArea borderControlQueue;
	/**
	 * The desks.
	 */
	private Collection<Passenger> passedBorderControl;

	/**
	 * Creates a rectangular gate area from a specified (x,y) coordinate, width
	 * and height.
	 * 
	 * @param topX
	 *            The x coordinate.
	 * @param topY
	 *            The y coordinate.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param borderControlDesks
	 *            The border control desks.
	 * @param borderControlQueue
	 *            The border control queue.
	 * 
	 */
	public BorderControlGateArea(double topX, double topY, double width, double height, List<Desk> borderControlDesks,
			QueuingArea borderControlQueue) {
		super(topX, topY, width, height);
		this.borderControlDesks = borderControlDesks;
		this.borderControlQueue = borderControlQueue;
		passedBorderControl = new ArrayList<>();
	}

	/**
	 * Creates a rectangular gate area from a specified (x,y) coordinate, width
	 * and height.
	 * 
	 * @param gateNumber
	 *            The gate number.
	 * 
	 * @param topX
	 *            The x coordinate.
	 * @param topY
	 *            The y coordinate.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param borderControlDesks
	 *            The border control desks.
	 * @param borderControlQueue
	 *            The border control queue.
	 */
	public BorderControlGateArea(int gateNumber, double topX, double topY, double width, double height,
			List<Desk> borderControlDesks, QueuingArea borderControlQueue) {
		super(gateNumber, topX, topY, width, height);
		this.borderControlDesks = borderControlDesks;
		this.borderControlQueue = borderControlQueue;
		passedBorderControl = new ArrayList<>();
	}

	/**
	 * Creates the area with a set of specified corner points.
	 * 
	 * @param gateNumber
	 *            The gate number.
	 * 
	 * @param corners
	 *            The corner points.
	 * 
	 * @param borderControlDesks
	 *            The border control desks.
	 * @param borderControlQueue
	 *            The border control queue.
	 * 
	 */
	public BorderControlGateArea(int gateNumber, List<Position> corners, List<Desk> borderControlDesks,
			QueuingArea borderControlQueue) {
		super(gateNumber, corners);
		this.borderControlDesks = borderControlDesks;
		this.borderControlQueue = borderControlQueue;
		passedBorderControl = new ArrayList<>();
	}

	/**
	 * Creates the area with a set of specified corner points.
	 * 
	 * 
	 * @param corners
	 *            The corner points.
	 * @param borderControlDesks
	 *            The border control desks.
	 * @param borderControlQueue
	 *            The border control queue.
	 * 
	 */
	public BorderControlGateArea(List<Position> corners, List<Desk> borderControlDesks,
			QueuingArea borderControlQueue) {
		super(corners);
		this.borderControlDesks = borderControlDesks;
		this.borderControlQueue = borderControlQueue;
		passedBorderControl = new ArrayList<>();
	}

	/**
	 * Determines if a passenger already passed border control.
	 * 
	 * @param agent
	 *            The agent.
	 * @return True if it did, false otherwise.
	 */
	public boolean alreadyPassedBorderControl(Passenger agent) {
		return passedBorderControl.contains(agent);
	}

	/**
	 * Indicates that a passenger is checked when crossing border control.
	 * 
	 * @param agent
	 *            The passenger.
	 */
	public void borderControl(Passenger agent) {
		if (agent.getFlight().getGateArea().equals(this) && !passedBorderControl.contains(agent))
			passedBorderControl.add(agent);
	}

	/**
	 * Gets the border control desks.
	 * 
	 * @return The border control desks.
	 */
	public List<Desk> getBorderControlDesks() {
		return borderControlDesks;
	}

	/**
	 * Gets the border control queue.
	 * 
	 * @return The border control queue.
	 */
	public QueuingArea getBorderControlQueue() {
		return borderControlQueue;
	}
}