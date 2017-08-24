package model.environment.objects.area;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.map.MapComponent;
import model.environment.position.Position;
import model.environment.shapes.PolygonShape;
import simulation.simulation.util.Utilities;

/**
 * A facility is a publicly accessible place for {@link HumanAgent}s, where they
 * can do something.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Area extends MapComponent implements PolygonShape {

	/**
	 * The coordinates of the corners of the facility.
	 */
	protected final List<Position> corners;

	/**
	 * The {@link Path2D} shape of the shop.
	 */
	protected Path2D shape;

	/**
	 * Creates a rectangular area from a specified (x,y) coordinate, width and
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
	public Area(double topX, double topY, double width, double height) {
		super(new Position(topX, topY));
		corners = new ArrayList<>();
		corners.add(new Position(topX, topY));
		corners.add(new Position(topX, topY + height));
		corners.add(new Position(topX + width, topY + height));
		corners.add(new Position(topX + width, topY));
		shape = Utilities.getShape(corners);
	}

	/**
	 * Creates an area from four corner coordinates.
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
	public Area(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
		super(new Position(x0, y0));
		corners = new ArrayList<>();
		corners.add(new Position(x0, y0));
		corners.add(new Position(x1, y1));
		corners.add(new Position(x2, y2));
		corners.add(new Position(x3, y3));
		shape = Utilities.getShape(corners);
	}

	/**
	 * Create an area from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public Area(double[] x, double[] y) {
		super(new Position(x[0], y[0]));
		corners = new ArrayList<>();
		for (int i = 0; i < x.length; i++)
			corners.add(new Position(x[i], y[i]));
		shape = Utilities.getShape(corners);
	}

	/**
	 * Create an area from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public Area(List<Position> corners) {
		super(corners.get(0));
		this.corners = corners;
		shape = Utilities.getShape(corners);
	}

	/**
	 * Gets the corner points of the facility.
	 * 
	 * @return The corner points.
	 */
	@Override
	public List<Position> getCorners() {
		return corners;
	}

	/**
	 * Gets the {@link Path2D} shape of the facility.
	 * 
	 * @return The shape.
	 */
	@Override
	public Path2D getShape() {
		return shape;
	}
}
