package model.environment.objects.physicalObject;

import java.util.List;

import model.environment.position.Position;

/**
 * The queue separator is an object that is used to control the formation of
 * queues. It is assumed to be rectangular.
 * 
 * @author S.A.M. Janssen
 */
public class QueueSeparator extends PhysicalObject {

	/**
	 * Creates a rectangular queue separator with a specified top left corner,
	 * width and height.
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
	public QueueSeparator(double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
	}

	/**
	 * Creates a queue separator from four corner coordinates.
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
	public QueueSeparator(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
		super(x0, x1, x2, x3, y0, y1, y2, y3);
	}

	/**
	 * Create a queue separator from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public QueueSeparator(double[] x, double[] y) {
		super(x, y);
	}

	/**
	 * Create a queue separator from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public QueueSeparator(List<Position> corners) {
		super(corners);
	}
}
