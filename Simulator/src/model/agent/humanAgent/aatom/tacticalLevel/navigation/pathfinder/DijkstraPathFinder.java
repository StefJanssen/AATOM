package model.agent.humanAgent.aatom.tacticalLevel.navigation.pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.environment.position.Position;
import model.map.Map;

/**
 * The Dijkstra Path Finder uses the Dijkstra algorithm to find a path from a
 * start {@link Position} to a goal {@link Position}.
 * 
 * @author S.A.M. Janssen
 *
 */
public class DijkstraPathFinder extends PathFinder {

	/**
	 * A list of unvisited nodes.
	 */
	private List<DijkstraNode> unvisited;
	/**
	 * A list of visited nodes.
	 */
	private List<DijkstraNode> visited;

	/**
	 * Creates the Dijkstra Path Finder.
	 * 
	 * @param map
	 *            The map.
	 * @param precision
	 *            The precision.
	 */
	public DijkstraPathFinder(Map map, double precision) {
		super(map, precision);
	}

	/**
	 * Gets the neighbors of a node.
	 * 
	 * @param node
	 *            The node.
	 * @return A list of neighbors.
	 */
	private Collection<DijkstraNode> getNeighbors(DijkstraNode node) {
		if (!node.neighbors.isEmpty())
			return node.neighbors;
		Collection<DijkstraNode> neighbors = new ArrayList<>();
		for (DijkstraNode other : unvisited) {
			if (other.x >= node.x - 1 && other.x <= node.x + 1) {
				if (other.y >= node.y - 1 && other.y <= node.y + 1) {
					if (!other.equals(node)) {
						neighbors.add(other);
					}
				}
			}
		}
		node.neighbors = neighbors;
		return neighbors;
	}

	/**
	 * Gets a Dijkstra node from a {@link Position}. Returns null if the node
	 * cannot be found.
	 * 
	 * @param position
	 *            The position.
	 * @return The node.
	 */
	private DijkstraNode getNodeFromPosition(Position position) {
		int x = (int) (position.x / precision);
		int y = (int) (position.y / precision);

		for (DijkstraNode node : unvisited) {
			if (node.x == x && node.y == y)
				return node;
		}

		for (DijkstraNode node : visited) {
			if (node.x == x && node.y == y)
				return node;
		}
		return null;
	}

	/**
	 * Gets the node with the lowest distance.
	 * 
	 * @return The node with the lowest distance.
	 */
	private DijkstraNode getNodeWithLowestDistance() {
		DijkstraNode smallest = null;
		double smallestValue = Double.MAX_VALUE;

		for (DijkstraNode node : unvisited) {
			if (node.distance < smallestValue) {
				smallestValue = node.distance;
				smallest = node;
			}
		}
		return smallest;
	}

	@Override
	public List<Position> getPath(Position start, Position goal, boolean smooth) {
		unvisited = new ArrayList<>();
		visited = new ArrayList<>();

		for (int i = 0; i < discretizedMap[0].length; i++) {
			for (int j = 0; j < discretizedMap.length; j++) {
				if (!discretizedMap[j][i]) {
					DijkstraNode n = new DijkstraNode(i, j);
					n.distance = Double.MAX_VALUE;
					unvisited.add(n);
				}
			}
		}

		DijkstraNode goalNode = getNodeFromPosition(goal);
		DijkstraNode startNode = getNodeFromPosition(start);

		startNode.distance = 0;

		while (!unvisited.isEmpty()) {
			DijkstraNode u = getNodeWithLowestDistance();
			// Check to prevent the method from continuation if the current node
			// is null. The method will return a list with only the goal
			// position if it finds a null.
			if (u == null) {
				List<Position> path = new ArrayList<>();
				path.add(goal);
				return path;
			}
			unvisited.remove(u);
			visited.add(u);

			if (u.equals(goalNode))
				break;

			// for each neighbor
			Collection<DijkstraNode> neighbors = getNeighbors(u);
			for (DijkstraNode v : neighbors) {

				double distance = 0;
				if (v.x != u.x && v.y != u.y)
					distance = Math.sqrt(2);
				else
					distance = 1;

				if (v.distance > u.distance + distance) {
					v.distance = u.distance + distance;
					v.previous = u;
				}
			}

		}

		// determine path
		List<DijkstraNode> path = new ArrayList<>();
		DijkstraNode current = goalNode;
		while (current != null) {
			path.add(current);
			current = current.previous;
		}

		// translate to real world coordinates
		List<Position> positions = new ArrayList<>();
		for (DijkstraNode node : path) {
			positions.add(getPositionFromNode(node));
		}

		Collections.reverse(positions);
		if (smooth)
			return smooth(positions);
		return positions;
	}

	/**
	 * Gets a {@link Position} from a Dijkstra Node.
	 * 
	 * @param node
	 *            The node.
	 * @return The position.
	 */
	private Position getPositionFromNode(DijkstraNode node) {
		double x = node.x * precision + 0.5 * precision;
		double y = node.y * precision + 0.5 * precision;
		return new Position(x, y);
	}

}