package model.environment.objects.area;

import java.util.List;

import model.environment.position.Position;

/**
 * 
 * @author S.A.M. Janssen
 */
public class GateArea extends Area {

	/**
	 * The gate number.
	 */
	private int gateNumber;

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
	 */
	public GateArea(double topX, double topY, double width, double height) {
		this(-1, topX, topY, width, height);
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
	 */
	public GateArea(int gateNumber, double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
		this.gateNumber = gateNumber;
	}

	/**
	 * Creates the area with a set of specified corner points.
	 * 
	 * @param gateNumber
	 *            The gate number.
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public GateArea(int gateNumber, List<Position> corners) {
		super(corners);
		this.gateNumber = gateNumber;
	}

	/**
	 * Creates the area with a set of specified corner points.
	 * 
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public GateArea(List<Position> corners) {
		this(-1, corners);
	}

	/**
	 * Gets the gate number. A gate number of -1 means that it is not
	 * initialized.
	 * 
	 * @return The gate number.
	 */
	public int getGateNumber() {
		return gateNumber;
	}
}