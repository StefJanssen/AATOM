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
	private final Position entrancePosition;
	/**
	 * The leaving position.
	 */
	private final Position leavingPosition;

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
		if (!contains(new Position(entrancePosition.x - 0.01, entrancePosition.y - 0.01))
				&& !contains(new Position(entrancePosition.x + 0.01, entrancePosition.y + 0.01)))
			throw new IllegalArgumentException("The entrance position and the leaving position should be in the area.");
		if (!contains(new Position(leavingPosition.x - 0.01, leavingPosition.y - 0.01))
				&& !contains(new Position(leavingPosition.x + 0.01, leavingPosition.y + 0.01)))
			throw new IllegalArgumentException("The entrance position and the leaving position should be in the area.");

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
		if (!contains(new Position(entrancePosition.x - 0.01, entrancePosition.y - 0.01))
				&& !contains(new Position(entrancePosition.x + 0.01, entrancePosition.y + 0.01)))
			throw new IllegalArgumentException("The entrance position and the leaving position should be in the area.");
		if (!contains(new Position(leavingPosition.x - 0.01, leavingPosition.y - 0.01))
				&& !contains(new Position(leavingPosition.x + 0.01, leavingPosition.y + 0.01)))
			throw new IllegalArgumentException("The entrance position and the leaving position should be in the area.");
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