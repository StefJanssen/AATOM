package model.environment.objects.area;

import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Position;

/**
 * A restaurant is a {@link Area} where {@link HumanAgent}s can eat something.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Facility extends Area {

	/**
	 * Creates a rectangular area with a specified top left corner, width and
	 * height.
	 * 
	 * @param topX
	 *            The x coordinate of the top left corner.
	 * @param topY
	 *            The y coordinate of the top left corner.
	 * @param width
	 *            The width of the area.
	 * @param height
	 *            The height of the area.
	 */
	public Facility(double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
	}

	/**
	 * Creates a area from four corner coordinates.
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
	public Facility(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
		super(x0, x1, x2, x3, y0, y1, y2, y3);
	}

	/**
	 * Create a area from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public Facility(double[] x, double[] y) {
		super(x, y);
	}

	/**
	 * Create a area from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public Facility(List<Position> corners) {
		super(corners);
	}

}