package model.environment.objects.area;

import java.util.List;

import model.environment.position.Position;

/**
 * 
 * @author S.A.M. Janssen
 */
public class QueuingArea extends Area {

	/**
	 * Entrance position.
	 */
	private Position entrancePosition;
	/**
	 * The leaving position.
	 */
	private Position leavingPosition;

	/**
	 * Creates a queuing area.
	 * 
	 * @param topX
	 *            Top left x coordinate.
	 * @param topY
	 *            Top left y coordinate.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param entrancePosition
	 *            The entrance position.
	 * @param leavingPosition
	 *            The leaving position.
	 */
	public QueuingArea(double topX, double topY, double width, double height, Position entrancePosition,
			Position leavingPosition) {
		super(topX, topY, width, height);
		this.entrancePosition = entrancePosition;
		this.leavingPosition = leavingPosition;
	}

	/**
	 * Creates the area with a set of specified corner points.
	 * 
	 * @param corners
	 *            The corner points.
	 * @param entrancePosition
	 *            The entrance position.
	 * @param leavingPosition
	 *            The leaving position.
	 * 
	 */
	public QueuingArea(List<Position> corners, Position entrancePosition, Position leavingPosition) {
		super(corners);

		this.entrancePosition = entrancePosition;
		this.leavingPosition = leavingPosition;

	}

	/**
	 * Gets the entrance position.
	 * 
	 * @return The entrance position.
	 */
	public Position getEntrancePosition() {
		return entrancePosition;
	}

	/**
	 * Gets the leaving position.
	 * 
	 * @return The leaving position.
	 */
	public Position getLeavingPosition() {
		return leavingPosition;
	}
}