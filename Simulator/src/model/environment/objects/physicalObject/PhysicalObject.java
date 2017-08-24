package model.environment.objects.physicalObject;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.map.MapComponent;
import model.environment.position.Position;
import model.environment.shapes.PolygonShape;
import simulation.simulation.util.Utilities;

/**
 * A physical obstacle is an object that cannot be accessed by
 * {@link HumanAgent}s. It is represented by a list of corner points, of which
 * the first one is the top left corner of the obstacle.
 * 
 * @author S.A.M. Janssen
 *
 */
public abstract class PhysicalObject extends MapComponent implements PolygonShape {

	/**
	 * The coordinates of the corners of the physical obstacle.
	 */
	protected final List<Position> corners;

	/**
	 * The {@link Path2D} shape of the physical obstacle.
	 */
	protected Path2D shape;

	/**
	 * Creates a rectangular physical obstacle from a specified (x,y)
	 * coordinate, width and height.
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
	public PhysicalObject(double topX, double topY, double width, double height) {
		super(new Position(topX, topY));
		corners = new ArrayList<>();
		corners.add(new Position(topX, topY));
		corners.add(new Position(topX + width, topY));
		corners.add(new Position(topX + width, topY + height));
		corners.add(new Position(topX, topY + height));
		shape = Utilities.getShape(corners);
	}

	/**
	 * Creates a physical obstacle from four corner coordinates.
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
		super(new Position(x0, y0));
		corners = new ArrayList<>();
		corners.add(new Position(x0, y0));
		corners.add(new Position(x1, y1));
		corners.add(new Position(x2, y2));
		corners.add(new Position(x3, y3));
		shape = Utilities.getShape(corners);
	}

	/**
	 * Create a physical obstacle from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public PhysicalObject(double[] x, double[] y) {
		super(new Position(x[0], y[0]));
		corners = new ArrayList<>();
		for (int i = 0; i < x.length; i++)
			corners.add(new Position(x[i], y[i]));
		shape = Utilities.getShape(corners);
	}

	/**
	 * Create a physical obstacle from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public PhysicalObject(List<Position> corners) {
		super(corners.get(0));
		this.corners = corners;
		shape = Utilities.getShape(corners);
	}

	/**
	 * Gets the list of corner {@link Position}s.
	 * 
	 * @return The list of corner positions.
	 */
	@Override
	public List<Position> getCorners() {
		return corners;
	}

	/**
	 * Gets the {@link Path2D} shape of the physical obstacle.
	 * 
	 * @return The shape.
	 */
	@Override
	public Path2D getShape() {
		return shape;
	}

	@Override
	public String toString() {
		return corners.toString();
	}
}