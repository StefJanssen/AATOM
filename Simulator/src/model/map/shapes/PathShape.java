package model.map.shapes;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import model.environment.position.Position;
import simulation.simulation.util.Utilities;
import util.math.RandomPlus;

/**
 * The polygon shape class.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class PathShape extends Path2D.Float {

	private final List<Position> corners;

	/**
	 * Create the path shape.
	 * 
	 * @param corners
	 *            The corners.
	 */
	public PathShape(List<Position> corners) {
		this.corners = corners;
		moveTo(corners.get(0).x, corners.get(0).y);
		for (int i = 1; i < corners.size(); ++i) {
			lineTo(corners.get(i).x, corners.get(i).y);
		}
		closePath();
	}

	/**
	 * Determines if a point out of a set of points is contained in a shape.
	 * 
	 * @param positions
	 *            The points.
	 * @return True if a point is in the shape, false otherwise.
	 */
	public boolean contains(List<Position> positions) {
		for (Position position : positions) {
			if (contains(position))
				return true;
		}
		return false;
	}

	/**
	 * Determines if the shape contains a position.
	 * 
	 * @param position
	 *            The position.
	 * @return True if it does, false otherwise.
	 */
	public boolean contains(Position position) {
		return super.contains(position.x, position.y);
	}

	/**
	 * Generates a position inside the shape.
	 * 
	 * @return The position.
	 */
	public Position generatePosition() {
		return generatePosition(Utilities.RANDOM_GENERATOR);
	}

	/**
	 * Generates a position inside the shape.
	 * 
	 * @param randomGenerator
	 *            The random generator.
	 * @return The position.
	 */
	public Position generatePosition(RandomPlus randomGenerator) {
		Rectangle r = getBounds();
		float x = -1, y = -1;

		int count = 0;
		while (!contains(x, y)) {
			x = (float) (r.getX() + 1 + (r.getWidth() - 2) * randomGenerator.nextFloat());
			y = (float) (r.getY() + 1 + (r.getHeight() - 2) * randomGenerator.nextFloat());
			// preventing infinite loops from happening.
			count++;
			if (count == 100)
				break;
		}
		return new Position(x, y);
	}

	/**
	 * Generates a set of positions inside the shape.
	 * 
	 * @param numberOfPositions
	 *            The number of positions we want to generate.
	 * @return The positions.
	 */
	public List<Position> generatePositions(int numberOfPositions) {
		return generatePositions(numberOfPositions, Utilities.RANDOM_GENERATOR);
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
		List<Position> list = new ArrayList<>();
		for (int i = 0; i < numberOfPositions; i++) {
			list.add(generatePosition(randomGenerator));
		}
		return list;
	}

	/**
	 * Gets the list of corner {@link Position}s.
	 * 
	 * @return The list of corner positions.
	 */
	public List<Position> getCorners() {
		return corners;
	}

	/**
	 * Determines if this shape intersect with another {@link PathShape}.
	 * 
	 * @param other
	 *            The other shape.
	 * @return True if they intersect, false otherwise.
	 */
	public boolean isCollision(PathShape other) {
		List<Position> firstCorners = getCorners();
		List<Position> secondCorners = other.getCorners();

		// check for containment in each other.
		if (contains(secondCorners) || other.contains(firstCorners))
			return true;

		// check for line intersections
		for (int i = 0; i < firstCorners.size(); i++) {
			if (other.isLineCollision(firstCorners.get(i), firstCorners.get((i + 1) % firstCorners.size())))
				return true;
		}
		return false;
	}

	/**
	 * Determines if a line intersects with the shape.
	 * 
	 * @param start
	 *            The start position of the line.
	 * @param end
	 *            The end position of the line.
	 * 
	 * @return True if they do, false otherwise.
	 */
	public boolean isLineCollision(Position start, Position end) {
		for (int i = 0; i < corners.size(); i++) {
			if (Utilities.isLineCollision(start, end, corners.get(i), corners.get((i + 1) % corners.size())))
				return true;
		}
		return false;
	}

}
