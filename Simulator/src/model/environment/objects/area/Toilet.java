package model.environment.objects.area;

import java.util.List;

import model.environment.position.Position;

/**
 * A toilet is a {@link Facility}.
 * 
 * @author S.A.M. Janssen
 */
public class Toilet extends Facility {

	/**
	 * Creates a rectangular toilet from a specified (x,y) coordinate, width and
	 * height.
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
	public Toilet(double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
	}

	/**
	 * Creates a restaurant with a set of specified corner points.
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public Toilet(List<Position> corners) {
		super(corners);
	}
}
