package model.environment.objects.area;

import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Position;

/**
 * A shop is a {@link Facility} in which {@link HumanAgent}s can buy things.
 * 
 * @author S.A.M. Janssen
 */
public class Shop extends Facility {

	/**
	 * Creates a rectangular shop from a specified (x,y) coordinate, width and
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
	public Shop(double topX, double topY, double width, double height) {
		super(topX, topY, width, height);
	}

	/**
	 * Creates a shop with a set of specified corner points.
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public Shop(List<Position> corners) {
		super(corners);
	}
	
	@Override
	public String toString() {
		return corners.toString();
	}
}
