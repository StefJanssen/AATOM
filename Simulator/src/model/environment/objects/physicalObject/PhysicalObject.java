package model.environment.objects.physicalObject;

import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Position;
import model.map.shapes.PolygonMapComponent;

/**
 * A physical obstacle is an object that cannot be accessed by
 * {@link HumanAgent}s. It is represented by a list of corner points, of which
 * the first one is the top left corner of the obstacle.
 * 
 * @author S.A.M. Janssen
 *
 */
public abstract class PhysicalObject extends PolygonMapComponent {

	/**
	 * Creates a rectangular physical object with a specified top left corner,
	 * width and height.
	 * 
	 * @param topX
	 *            The x coordinate of the top left corner.
	 * @param topY
	 *            The y coordinate of the top left corner.
	 * @param width
	 *            The width of the physical object.
	 * @param height
	 *            The height of the physical object.
	 */
	public PhysicalObject(double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
	}

	/**
	 * Creates a physical object from four corner coordinates.
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
	public PhysicalObject(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
		super(x0, x1, x2, x3, y0, y1, y2, y3);
	}

	/**
	 * Create a physical object from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public PhysicalObject(double[] x, double[] y) {
		super(x, y);
	}

	/**
	 * Create a physical object from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public PhysicalObject(List<Position> corners) {
		super(corners);
	}
}