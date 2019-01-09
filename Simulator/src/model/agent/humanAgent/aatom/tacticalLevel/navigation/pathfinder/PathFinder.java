package model.agent.humanAgent.aatom.tacticalLevel.navigation.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.agent.humanAgent.aatom.tacticalLevel.navigation.DiscretizedMap;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.position.Position;
import model.environment.position.Vector;
import model.map.Map;
import simulation.simulation.util.Utilities;

/**
 * A path finder finds a feasible path between a specified starting
 * {@link Position} and goal {@link Position} on a specified {@link Map}. A path
 * is represented as a {@link List} of {@link Position}s, where each
 * {@link Position} is a corner point in the path. If no feasible path can be
 * found, a list containing only the goal position {@link Position} is returned.
 * 
 * @author S.A.M. Janssen
 *
 */
public abstract class PathFinder {

	/**
	 * The discretized map.
	 */
	protected boolean[][] discretizedMap;
	/**
	 * The {@link Map}.
	 */
	protected Map map;
	/**
	 * The width of a tile in the discrete version of the {@link Map}.
	 */
	protected double precision;

	/**
	 * Creates a path finder.
	 * 
	 * @param map
	 *            The map.
	 * @param precision
	 *            The precision.
	 */
	public PathFinder(Map map, double precision) {
		this.map = map;
		this.precision = precision;
		discretizedMap = DiscretizedMap.getInstance(map, precision).getMap();
	}

	/**
	 * Determines if there is a direct path possible between two
	 * {@link Position}s. (i.e. there is no obstacle between the two points).
	 * 
	 * @param start
	 *            The start position.
	 * @param goal
	 *            The goal position.
	 * @return True if there is a direct path possible, false otherwise.
	 */
	protected boolean directPathPossible(Position start, Position goal) {
		return !Utilities.isLineCollision(start, goal, map.getMapComponents((PhysicalObject.class)));
	}

	/**
	 * Gets the corner points from a path, represented as a list of
	 * {@link Position}s.
	 * 
	 * @param path
	 *            The path.
	 * @return The corner points.
	 */
	protected List<Position> getCornerPoints2(List<Position> path) {
		if (path.size() < 4)
			return path;

		// find the list of corners
		List<Position> corners = new ArrayList<>();
		for (int i = 0; i < path.size() - 2; i++) {
			// get the next three points
			Position A = path.get(i);
			Position B = path.get(i + 1);
			Position C = path.get(i + 2);

			// determine the distance between each of the points
			double distanceAC = new Vector(A.x, A.y).distanceTo(C);
			double distanceBC = new Vector(B.x, B.y).distanceTo(C);
			double distanceAB = new Vector(A.x, A.y).distanceTo(B);

			// if the distance between A & B + B & C - A & C is not
			// approximately zero, this means that there is a corner in our
			// path. (I.e. the points are not on a line.)
			if (Math.abs(distanceAB + distanceBC - distanceAC) > 0.000001) {
				corners.add(0, B);
			}
		}
		corners.add(path.get(0));
		return corners;
	}

	/**
	 * Gets a feasible path from a specified start {@link Position} to a goal
	 * {@link Position}. The method returns a list of points that are the corner
	 * points of the specified path.
	 * 
	 * @param start
	 *            The start position.
	 * @param goal
	 *            The goal position.
	 * @return The path.
	 */
	public List<Position> getPath(Position start, Position goal) {
		return getPath(start, goal, true);
	}

	/**
	 * Gets a feasible path from a specified start {@link Position} to a goal
	 * {@link Position}. The method returns a list of points that are the corner
	 * points of the specified path.
	 * 
	 * @param start
	 *            The start position.
	 * @param goal
	 *            The goal position.
	 * @param smooth
	 *            Smooth or not.
	 * @return The path.
	 */
	public abstract List<Position> getPath(Position start, Position goal, boolean smooth);

	/**
	 * Gets the corner points from a path, represented as a list of
	 * {@link Position}s.
	 * 
	 * @param path
	 *            The path.
	 * @return The corner points.
	 */
	public List<Position> smooth(List<Position> path) {
		if (path.size() < 4)
			return path;

		Collections.reverse(path);
		// find the list of corners
		List<Position> corners = new ArrayList<>();
		for (int i = 0; i < path.size() - 2; i++) {
			// get the next three points
			Position A = path.get(i);
			Position B = path.get(i + 1);
			Position C = path.get(i + 2);

			// determine the distance between each of the points
			double distanceAC = new Vector(A.x, A.y).distanceTo(C);
			double distanceBC = new Vector(B.x, B.y).distanceTo(C);
			double distanceAB = new Vector(A.x, A.y).distanceTo(B);

			// if the distance between A & B + B & C - A & C is not
			// approximately zero, this means that there is a corner in our
			// path. (I.e. the points are not on a line.)
			if (Math.abs(distanceAB + distanceBC - distanceAC) > 0.0001) {
				corners.add(0, B);
			}
		}
		corners.add(0, path.get(path.size() - 1));
		corners.add(path.get(0));
		return corners;
	}
}
