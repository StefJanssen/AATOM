package model.map.shapes;

import java.util.ArrayList;
import java.util.List;

import model.environment.position.Position;
import model.environment.position.Vector;
import model.map.PhysicalMapComponent;
import simulation.simulation.util.Utilities;
import util.math.RandomPlus;

/**
 * A polygon map component.
 * 
 * @author S.A.M. Janssen
 */
public abstract class PolygonMapComponent extends PhysicalMapComponent implements PolygonShape {

	/**
	 * The {@link PathShape} shape of the physical obstacle.
	 */
	protected final PathShape shape;

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
	public PolygonMapComponent(double topX, double topY, double width, double height) {
		super(new Position(topX, topY));
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Negative width and height not allowed.");

		List<Position> corners = new ArrayList<>();
		corners.add(new Position(topX, topY));
		corners.add(new Position(topX + width, topY));
		corners.add(new Position(topX + width, topY + height));
		corners.add(new Position(topX, topY + height));
		shape = new PathShape(corners);
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
	public PolygonMapComponent(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
		super(new Position(x0, y0));
		if ((x1 < x0 && y1 < y0) || (x2 < x0 && y2 < y0) || (x3 < x0 && y3 < y0))
			throw new IllegalArgumentException("First position should be the top left position.");
		List<Position> corners = new ArrayList<>();
		corners.add(new Position(x0, y0));
		corners.add(new Position(x1, y1));
		corners.add(new Position(x2, y2));
		corners.add(new Position(x3, y3));
		shape = new PathShape(corners);
	}

	/**
	 * Create a wall from a list of (x,y) coordinates.
	 * 
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 */
	public PolygonMapComponent(double[] x, double[] y) {
		super(new Position(x[0], y[0]));
		List<Position> corners = new ArrayList<>();
		for (int i = 0; i < x.length; i++) {
			corners.add(new Position(x[i], y[i]));
			if (i > 0) {
				if (x[i] < x[0] && y[i] < y[0])
					throw new IllegalArgumentException("First position should be the top left position.");
			}
		}
		shape = new PathShape(corners);
	}

	/**
	 * Create a wall from a list of corner {@link Position}s.
	 * 
	 * @param corners
	 *            The list of corner positions.
	 */
	public PolygonMapComponent(List<Position> corners) {
		super(corners.get(0));
		for (int i = 1; i < corners.size(); i++) {
			if (corners.get(i).x < corners.get(0).x && corners.get(i).y < corners.get(0).y)
				throw new IllegalArgumentException("First position should be the top left position.");

		}
		shape = new PathShape(corners);
	}

	/**
	 * Determines if a point out of a set of points is contained in a shape.
	 * 
	 * @param positions
	 *            The points.
	 * @return True if a point is in the shape, false otherwise.
	 */
	public boolean contains(List<Position> positions) {
		return shape.contains(positions);
	}

	/**
	 * Determines if the shape contains a position.
	 * 
	 * @param position
	 *            The position.
	 * @return True if it does, false otherwise.
	 */
	public boolean contains(Position position) {
		return shape.contains(position);
	}

	/**
	 * Generates a position inside the shape.
	 * 
	 * @return The position.
	 */
	public Position generatePosition() {
		return shape.generatePosition();
	}

	/**
	 * Generates a position inside the shape.
	 * 
	 * @param randomGenerator
	 *            The random generator.
	 * @return The position.
	 */
	public Position generatePosition(RandomPlus randomGenerator) {
		return shape.generatePosition(randomGenerator);
	}

	/**
	 * Generates a set of positions inside the shape.
	 * 
	 * @param numberOfPositions
	 *            The number of positions we want to generate.
	 * @return The positions.
	 */
	public List<Position> generatePositions(int numberOfPositions) {
		return shape.generatePositions(numberOfPositions);
	}

	/**
	 * Generates a set of positions inside the shape.
	 * 
	 * @param numberOfPositions
	 *            The number of positions we want to generate.
	 * @param randomGenerator
	 *            The random generator.
	 * @return The positions.
	 */
	public List<Position> generatePositions(int numberOfPositions, RandomPlus randomGenerator) {
		return shape.generatePositions(numberOfPositions, randomGenerator);
	}

	/**
	 * Gets the list of corner {@link Position}s.
	 * 
	 * @return The list of corner positions.
	 */
	@Override
	public List<Position> getCorners() {
		return shape.getCorners();
	}

	/**
	 * Gets the {@link PathShape} shape of the physical obstacle.
	 * 
	 * @return The shape.
	 */
	private PathShape getShape() {
		return shape;
	}

	@Override
	public Vector getVector(Position position) {
		// if is contained
		if (contains(position))
			return new Vector(0, 0);

		List<Position> corners = shape.getCorners();
		double minDistance = Double.MAX_VALUE;
		Vector shortest = null;

		for (int i = 0; i < corners.size(); i++) {
			Vector v = Utilities.getPositionToLineVector(position, corners.get(i),
					corners.get((i + 1) % corners.size()));
			double currentDistance = v.length();
			if (currentDistance < minDistance) {
				minDistance = currentDistance;
				shortest = v;
			}
		}
		return shortest;
	}

	/**
	 * Determines if two {@link PolygonMapComponent}s intersect.
	 * 
	 * @param other
	 *            The second shape.
	 * @return True if they intersect, false otherwise.
	 */
	public boolean isCollision(PolygonMapComponent other) {
		return shape.isCollision(other.getShape());
	}

	@Override
	public boolean isLineCollision(Position start, Position end) {
		return shape.isLineCollision(start, end);
	}

	@Override
	public String toString() {
		return getCorners().toString();
	}
}
