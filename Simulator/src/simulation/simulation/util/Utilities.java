package simulation.simulation.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.environment.objects.area.QueuingArea;
import model.environment.position.Position;
import model.environment.position.Vector;
import model.map.MapComponent;
import model.map.PhysicalMapComponent;
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
	public static QueuingArea getClosestQueuingArea(Collection<? extends PhysicalMapComponent> mapComponents,
			Collection<QueuingArea> queues) {
		QueuingArea closest = null;
		double distance = Double.MAX_VALUE;

		for (PhysicalMapComponent mapComponent : mapComponents) {
			for (QueuingArea area : queues) {
				double dis = area.getDistance(mapComponent.getPosition());
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
	public static <T extends PhysicalMapComponent> Collection<T> getMapComponentsInNeighborhood(Position position,
			double radius, Collection<T> mapComponents) {
		Collection<T> closeObjects = new ArrayList<>();

		for (T component : mapComponents) {
			if (component.getDistance(position) < radius)
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
	public static Vector getPositionToLineVector(Position position, Position lineStart, Position lineEnd) {
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

	// /**
	// * Gets a {@link Vector} pointing from a {@link HumanAgent} to the a
	// * {@link Position}.
	// *
	// * @param agent
	// * The agent.
	// * @param component
	// * The component.
	// * @return The vector.
	// */
	// public static Vector getVector(HumanAgent agent, MapComponent component)
	// {
	// return getVector(agent.getPosition(), component);
	// }

	// /**
	// * Gets a {@link Vector} pointing from a {@link PolygonMapComponent} to a
	// * {@link Position} .
	// *
	// * @param position
	// * The position.
	// * @param polygonShape
	// * The polygonShape.
	// * @return The vector.
	// */
	// private static Vector getVector(Position position, PolygonMapComponent
	// polygonShape) {
	// // if is contained
	// if (polygonShape.contains(position))
	// return new Vector(0, 0);
	//
	// List<Position> corners = polygonShape.getCorners();
	// double minDistance = Double.MAX_VALUE;
	// Vector shortest = null;
	//
	// for (int i = 0; i < corners.size(); i++) {
	// Vector v = getPositionToLineVector(position, corners.get(i),
	// corners.get((i + 1) % corners.size()));
	// double currentDistance = v.length();
	// if (currentDistance < minDistance) {
	// minDistance = currentDistance;
	// shortest = v;
	// }
	// }
	// return shortest;
	// }

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
	public static <T extends PhysicalMapComponent> boolean isLineCollision(Position start, Position end,
			Collection<T> components) {
		for (T component : components) {
			if (component.isLineCollision(start, end))
				return true;
		}
		return false;
	}

	/**
	 * Determines if a line intersects with a {@link PolygonMapComponent}.
	 * 
	 * @param start
	 *            The start position of the line.
	 * @param end
	 *            The end position of the line.
	 * @param shape
	 *            The shape.
	 * @return True if they intersect, false otherwise.
	 */
	// public static boolean isLineCollision(Position start, Position end,
	// PathShape shape) {
	// List<Position> corners = shape.getCorners();
	// for (int i = 0; i < corners.size(); i++) {
	// if (isLineCollision(start, end, corners.get(i), corners.get((i + 1) %
	// corners.size())))
	// return true;
	// }
	// return false;
	// }

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
	public static boolean isLineCollision(Position firstLineStart, Position firstLineEnd, Position secondLineStart,
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
	// public static <T extends MapComponent> boolean isLineCollision(Position
	// start, Position end, T component) {
	// if (component instanceof PolygonMapComponent)
	// return isLineCollision(start, end, (PolygonMapComponent) component);
	// if (component instanceof CircularMapComponent)
	// return getPositionToLineVector(((MapComponent) component).getPosition(),
	// start, end)
	// .length() < ((CircularMapComponent) component).getRadius();
	// return true;
	// }

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