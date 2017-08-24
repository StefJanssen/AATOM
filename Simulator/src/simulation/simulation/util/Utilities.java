package simulation.simulation.util;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.map.MapComponent;
import model.environment.objects.area.QueuingArea;
import model.environment.position.Position;
import model.environment.position.Vector;
import model.environment.shapes.CircularShape;
import model.environment.shapes.PolygonShape;
import util.math.RandomPlus;

/**
 * Utilities are a set of utility methods that can be used by all classes across
 * the project. Methods include collision detection, distance measures and
 * vectors from one object to another.
 * 
 * 
 * @author S.A.M. Janssen
 */
public class Utilities {

	/**
	 * A random generator.
	 */
	public static RandomPlus RANDOM_GENERATOR = new RandomPlus(0);

	/**
	 * Determines if a point out of a set of points is contained in a shape.
	 * 
	 * @param shape
	 *            The shape.
	 * @param positions
	 *            The points.
	 * @return True if a point is in the shape, false otherwise.
	 */
	private static boolean contains(Path2D shape, List<Position> positions) {
		for (Position position : positions) {
			if (contains(shape, position))
				return true;
		}
		return false;
	}

	/**
	 * Determines if a position is contained in a shape.
	 * 
	 * @param shape
	 *            The shape.
	 * @param position
	 *            The position.
	 * @return True if the position is contained in the shape, false otherwise.
	 */
	private static boolean contains(Path2D shape, Position position) {
		return shape.contains(position.x, position.y);
	}

	/**
	 * Generates a position inside a shape.
	 * 
	 * @param shape
	 *            The shape.
	 * @return The position.
	 */
	public static Position generatePosition(Path2D shape) {
		Rectangle r = shape.getBounds();
		float x = -1, y = -1;

		int count = 0;
		while (!shape.contains(x, y)) {
			x = (float) (r.getX() + 0.5 + (r.getWidth() - 1) * RANDOM_GENERATOR.nextFloat());
			y = (float) (r.getY() + 0.5 + (r.getHeight() - 1) * RANDOM_GENERATOR.nextFloat());
			// preventing infinite loops from happening.
			count++;
			if (count == 100)
				break;
		}
		return new Position(x, y);
	}

	/**
	 * Generates a set of positions inside a shape.
	 * 
	 * @param numberOfPositions
	 *            The number of positions we want to generate.
	 * @param shape
	 *            The shape.
	 * @return The positions.
	 */
	public static List<Position> generatePositions(int numberOfPositions, Path2D shape) {
		List<Position> list = new ArrayList<>();
		for (int i = 0; i < numberOfPositions; i++) {
			list.add(generatePosition(shape));
		}
		return list;
	}

	/**
	 * Gets the central position of a set of points.
	 * 
	 * @param corners
	 *            The corner points.
	 * @return The central position.
	 */
	public static Position getAveragePosition(List<Position> corners) {
		float x = 0;
		float y = 0;
		for (Position p : corners) {
			x += p.x;
			y += p.y;
		}
		return new Position(x / corners.size(), y / corners.size());
	}

	/**
	 * Gets the closest queuing area.
	 * 
	 * @param mapComponents
	 *            The map components.
	 * @param queues
	 *            The queues.
	 * @return The closest queuing area.
	 */
	public static QueuingArea getClosestQueuingArea(Collection<? extends MapComponent> mapComponents,
			Collection<QueuingArea> queues) {
		QueuingArea closest = null;
		double distance = Double.MAX_VALUE;

		for (MapComponent mapComponent : mapComponents) {
			for (QueuingArea area : queues) {
				double dis = getDistance(mapComponent.getPosition(), area);
				if (dis < distance) {
					distance = dis;
					closest = area;
				}
			}
			return closest;
		}
		return closest;
	}

	/**
	 * Gets the distance between a {@link HumanAgent} and {@link MapComponent}.
	 * 
	 * @param agent
	 *            The agent.
	 * @param component
	 *            The component.
	 * @return The distance.
	 */
	public static double getDistance(HumanAgent agent, MapComponent component) {
		return getDistance(agent.getPosition(), component);
	}

	/**
	 * Gets the distance between a {@link Position} and a {@link MapComponent}.
	 * 
	 * @param position
	 *            The position.
	 * @param component
	 *            The component.
	 * @return The distance.
	 */
	public static double getDistance(Position position, MapComponent component) {
		return getVector(position, component).length();
	}

	/**
	 * Gets all{@link MapComponent}s in a list within a specified distance from
	 * a {@link Position}.
	 * 
	 * @param position
	 *            The position.
	 * @param radius
	 *            The specified distance.
	 * @param mapComponents
	 *            The list of components.
	 * @param <T>
	 *            The specified subclass of {@link MapComponent}.
	 * @return The components within the radius.
	 */
	public static <T extends MapComponent> Collection<T> getMapComponentsInNeighborhood(Position position,
			double radius, Collection<T> mapComponents) {
		Collection<T> closeObjects = new ArrayList<>();

		for (T component : mapComponents) {
			if (getDistance(position, component) < radius)
				closeObjects.add(component);
		}
		return closeObjects;
	}

	/**
	 * Gets a {@link Vector} pointing from a line to a {@link Position}.
	 * 
	 * @param position
	 *            The position.
	 * @param lineStart
	 *            The starting position of a line.
	 * @param lineEnd
	 *            The ending position of a line.
	 * @return The vector.
	 */
	private static Vector getPositionToLineVector(Position position, Position lineStart, Position lineEnd) {
		// adapted method from:
		// http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment

		float a = position.x - lineStart.x;
		float b = position.y - lineStart.y;
		float c = lineEnd.x - lineStart.x;
		float d = lineEnd.y - lineStart.y;

		float dot = a * c + b * d;
		float len_sq = c * c + d * d;
		float param = -1;

		if (len_sq != 0)
			param = dot / len_sq;

		float xx, yy;

		if (param < 0) {
			xx = lineStart.x;
			yy = lineStart.y;
		} else if (param > 1) {
			xx = lineEnd.x;
			yy = lineEnd.y;
		} else {
			xx = lineStart.x + param * c;
			yy = lineStart.y + param * d;
		}

		float dx = position.x - xx;
		float dy = position.y - yy;

		return new Vector(dx, dy);
	}

	/**
	 * Gets a {@link Path2D} shape from a list of corners.
	 * 
	 * @param corners
	 *            The corners.
	 * @return The shape.
	 */
	public static Path2D getShape(List<Position> corners) {
		Path2D path = new Path2D.Double();
		path.moveTo(corners.get(0).x, corners.get(0).y);
		for (int i = 1; i < corners.size(); ++i) {
			path.lineTo(corners.get(i).x, corners.get(i).y);
		}
		path.closePath();
		return path;
	}

	/**
	 * Gets a {@link Vector} pointing from a {@link HumanAgent} to the a
	 * {@link Position}.
	 * 
	 * @param agent
	 *            The agent.
	 * @param component
	 *            The component.
	 * @return The vector.
	 */
	public static Vector getVector(HumanAgent agent, MapComponent component) {
		return getVector(agent.getPosition(), component);
	}

	/**
	 * Gets a {@link Vector} pointing from a {@link MapComponent} to the a
	 * {@link Position}.
	 * 
	 * @param position
	 *            The first position.
	 * @param component
	 *            The second position.
	 * @return The vector.
	 */
	public static Vector getVector(Position position, MapComponent component) {
		if (component instanceof CircularShape)
			return new Vector(position.x - component.getPosition().x, position.y - component.getPosition().y);
		if (component instanceof PolygonShape)
			return getVector(position, (PolygonShape) component);
		return null;
	}

	/**
	 * Gets a {@link Vector} pointing from a {@link PolygonShape} to a
	 * {@link Position} .
	 * 
	 * @param position
	 *            The position.
	 * @param polygonShape
	 *            The polygonShape.
	 * @return The vector.
	 */
	private static Vector getVector(Position position, PolygonShape polygonShape) {
		Path2D shape = polygonShape.getShape();

		// if is contained
		if (shape.contains(position.x, position.y))
			return new Vector(0, 0);

		List<Position> corners = polygonShape.getCorners();
		double minDistance = Double.MAX_VALUE;
		Vector shortest = null;

		for (int i = 0; i < corners.size(); i++) {
			Vector v = getPositionToLineVector(position, corners.get(i), corners.get((i + 1) % corners.size()));
			double currentDistance = v.length();
			if (currentDistance < minDistance) {
				minDistance = currentDistance;
				shortest = v;
			}
		}
		return shortest;
	}

	/**
	 * Determines if two {@link PolygonShape}s intersect.
	 * 
	 * @param first
	 *            The first shape.
	 * @param second
	 *            The second shape.
	 * @return True if they intersect, false otherwise.
	 */
	public static boolean isCollision(PolygonShape first, PolygonShape second) {
		List<Position> firstCorners = first.getCorners();
		List<Position> secondCorners = second.getCorners();

		// check for containment in each other.
		if (contains(first.getShape(), secondCorners) || contains(second.getShape(), firstCorners))
			return true;

		// check for line intersections
		for (int i = 0; i < firstCorners.size(); i++) {
			if (isLineCollision(firstCorners.get(i), firstCorners.get((i + 1) % firstCorners.size()), second))
				return true;
		}
		return false;
	}

	/**
	 * Determines if a line intersects with a set of components.
	 * 
	 * @param start
	 *            The start position of the line.
	 * @param end
	 *            The end position of the line.
	 * @param components
	 *            The components.
	 * @param <T>
	 *            The specified subclass of {@link MapComponent}.
	 * 
	 * @return True if they do, false otherwise.
	 */
	public static <T extends MapComponent> boolean isLineCollision(Position start, Position end,
			Collection<T> components) {
		for (T component : components) {
			if (isLineCollision(start, end, component))
				return true;
		}
		return false;
	}

	/**
	 * Determines if a line intersects with a {@link PolygonShape}.
	 * 
	 * @param start
	 *            The start position of the line.
	 * @param end
	 *            The end position of the line.
	 * @param shape
	 *            The shape.
	 * @return True if they intersect, false otherwise.
	 */
	private static boolean isLineCollision(Position start, Position end, PolygonShape shape) {
		List<Position> corners = shape.getCorners();
		for (int i = 0; i < corners.size(); i++) {
			if (isLineCollision(start, end, corners.get(i), corners.get((i + 1) % corners.size())))
				return true;
		}
		return false;
	}

	/**
	 * Checks if two lines intersect.
	 * 
	 * @param firstLineStart
	 *            The starting position of the first line.
	 * @param firstLineEnd
	 *            The ending position of the first line.
	 * @param secondLineStart
	 *            The starting position of the second line.
	 * @param secondLineEnd
	 *            The ending position of the second line.
	 * @return True if they intersect, false otherwise.
	 */
	private static boolean isLineCollision(Position firstLineStart, Position firstLineEnd, Position secondLineStart,
			Position secondLineEnd) {
		// Code adapted from: http://www.java-gaming.org/index.php?topic=22590.0

		double x1 = firstLineStart.x;
		double x2 = firstLineEnd.x;
		double x3 = secondLineStart.x;
		double x4 = secondLineEnd.x;
		double y1 = firstLineStart.y;
		double y2 = firstLineEnd.y;
		double y3 = secondLineStart.y;
		double y4 = secondLineEnd.y;
		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 || x3 == x4 && y3 == y4) {
			return false;
		}
		// Fastest method, based on Franklin Antonio's "Faster Line Segment
		// Intersection" topic "in Graphics Gems III" book
		// (http://www.graphicsgems.org/)
		double ax = x2 - x1;
		double ay = y2 - y1;
		double bx = x3 - x4;
		double by = y3 - y4;
		double cx = x1 - x3;
		double cy = y1 - y3;

		double alphaNumerator = by * cx - bx * cy;
		double commonDenominator = ay * bx - ax * by;
		if (commonDenominator > 0) {
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator) {
				return false;
			}
		}
		double betaNumerator = ax * cy - ay * cx;
		if (commonDenominator > 0) {
			if (betaNumerator < 0 || betaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (betaNumerator > 0 || betaNumerator < commonDenominator) {
				return false;
			}
		}
		if (commonDenominator == 0) {
			// This code wasn't in Franklin Antonio's method. It was added by
			// Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			double y3LessY1 = y3 - y1;
			double collinearityTestForP3 = x1 * (y2 - y3) + x2 * (y3LessY1) + x3 * (y1 - y2); // see
																								// http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear,
			// since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0) {
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 || x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4
						|| x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2) {
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 || y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4
							|| y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Determines if a line intersects with a {@link MapComponent}.
	 * 
	 * @param start
	 *            The start position of the line.
	 * @param end
	 *            The end position of the line.
	 * @param component
	 *            The component.
	 * @param <T>
	 *            The specified subclass of {@link MapComponent}.
	 * 
	 * @return True if they do, false otherwise.
	 */
	public static <T extends MapComponent> boolean isLineCollision(Position start, Position end, T component) {
		if (component instanceof PolygonShape)
			return isLineCollision(start, end, (PolygonShape) component);
		if (component instanceof CircularShape)
			return getPositionToLineVector(((MapComponent) component).getPosition(), start, end)
					.length() < ((CircularShape) component).getRadius();
		return true;
	}

	/**
	 * Rounds a value to a certain precision.
	 * 
	 * @param value
	 *            The value.
	 * @param precision
	 *            The precision.
	 * @return The rounded value.
	 */
	public static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	/**
	 * Transforms a {@link Position} based on an origin and a rotation.
	 * 
	 * @param position
	 *            The position.
	 * @param origin
	 *            The point it is rotated around.
	 * @param degreeRotation
	 *            The degrees it is rotated.
	 * @return The transformed position.
	 */
	public static Position transform(Position position, Position origin, double degreeRotation) {
		// offset
		double x = position.x - origin.x;
		double y = position.y - origin.y;

		// rotation
		double theta = Math.toRadians(-degreeRotation);
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);

		double rotX = x * cos - y * sin;
		double rotY = x * sin + y * cos;

		// corrected for the origin
		return new Position(rotX + origin.x, rotY + origin.y);
	}

	/**
	 * Private constructor to prevent object initialization.
	 */
	private Utilities() {
	}
}