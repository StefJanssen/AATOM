package model.environment.objects.physicalObject;

import java.util.List;

import model.environment.position.Position;

/**
 * A wall is a physical obstacle that defines the border between different
 * areas.
 * 
 * @author S.A.M. Janssen
 */
public class Wall extends PhysicalObject {

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
	 */
	public Wall(double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
	}

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
	 */
	public Wall(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
		super(x0, x1, x2, x3, y0, y1, y2, y3);
	}

	/**
	 * Create a wall from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public Wall(double[] x, double[] y) {
		super(x, y);
	}

	/**
	 * Create a wall from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public Wall(List<Position> corners) {
		super(corners);
	}

	@Override
	public String toString() {
		return corners.toString();
	}
}