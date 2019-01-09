package model.agent.humanAgent.aatom.tacticalLevel.navigation.pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.environment.position.Position;
import model.map.Map;

/**
 * The A* Path Finder uses the A* algorithm to find a path from a start
 * {@link Position} to a goal {@link Position}. Returns a path with only the
 * goal position if no path could be found.
 * 
 * @author S.A.M. Janssen
 *
 */
public class AStarPathFinder extends PathFinder {

	/**
	 * The set of nodes that have been searched through
	 */
	private ArrayList<AStarNode> closed;
	/**
	 * The graph in array format.
	 */
	private AStarNode[][] graph;
	/**
	 * The set of nodes that we do not yet consider fully searched
	 */
	private ArrayList<AStarNode> open;

	/**
	 * Creates an A* path finder.
	 * 
	 * @param map
	 *            The map.
	 * @param precision
	 *            The precision.
	 */
	public AStarPathFinder(Map map, double precision) {
		super(map, precision);
		closed = new ArrayList<>();
		open = new ArrayList<>();
		graph = new AStarNode[discretizedMap.length][discretizedMap[0].length];
		for (int i = 0; i < graph[0].length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (!discretizedMap[j][i]) {
					graph[j][i] = new AStarNode(i, j);
					graph[j][i].distance = Double.MAX_VALUE;
				}
			}
		}
	}

	/**
	 * Gets the heuristic distance between two {@link AStarNode}s. It is the
	 * euclidean distance between the two nodes (while assuming no obstacles),
	 * but corrects for the maximum angle allowed in the graph.
	 * 
	 * @param node
	 *            The starting node.
	 * @param goal
	 *            The goal node.
	 * @return The heuristic distance between the nodes.
	 */
	private double getHeuristicDistance(AStarNode node, AStarNode goal) {
		int xDif = Math.abs(node.x - goal.x);
		int yDif = Math.abs(node.y - goal.y);

		double diagDistance = Math.sqrt(2 * Math.pow(Math.min(xDif, yDif), 2));
		double straightDistance = Math.abs(xDif - yDif);
		return diagDistance + straightDistance;
	}

	/**
	 * Gets the neighbors of a {@link AStarNode}.
	 * 
	 * @param node
	 *            The node.
	 * @return The neighbors.
	 */
	private Collection<AStarNode> getNeighbors(AStarNode node) {
		Collection<AStarNode> neighbors = new ArrayList<>();
		int x = node.x;
		int y = node.y;

		if (y > 0 && x > 0)
			neighbors.add(graph[y - 1][x - 1]);
		if (y > 0)
			neighbors.add(graph[y - 1][x]);
		if (y > 0 && x < graph[0].length - 1)
			neighbors.add(graph[y - 1][x + 1]);

		if (x > 0)
			neighbors.add(graph[y][x - 1]);
		if (x < graph[0].length - 1)
			neighbors.add(graph[y][x + 1]);

		if (y < graph.length - 1 && x > 0)
			neighbors.add(graph[y + 1][x - 1]);
		if (y < graph.length - 1)
			neighbors.add(graph[y + 1][x]);
		if (y < graph.length - 1 && x < graph[0].length - 1)
			neighbors.add(graph[y + 1][x + 1]);

		neighbors.removeAll(Collections.singleton(null));
		return neighbors;
	}

	/**
	 * Gets a AStar node from a {@link Position}.
	 * 
	 * @param position
	 *            The position.
	 * @return The node.
	 */
	private AStarNode getNodeFromPosition(Position position) {
		int x = (int) (position.x / precision);
		int y = (int) (position.y / precision);

		// We take a neighboring node if we cannot find the node....
		if (graph[y][x] == null) {
			Collection<AStarNode> neighbors = getNeighbors(new AStarNode(x, y));
			if (!neighbors.isEmpty())
				return neighbors.iterator().next();
		}
		return graph[y][x];
	}

	@Override
	public List<Position> getPath(Position start, Position goal, boolean smooth) {
		// check if we can go directly, there is no start or no goal.
		if (directPathPossible(start, goal) || start.equals(Position.NO_POSITION)
				|| goal.equals(Position.NO_POSITION)) {
			List<Position> goalPositions = new ArrayList<>();
			goalPositions.add(goal);
			return goalPositions;
		}

		AStarNode startNode = getNodeFromPosition(start);
		AStarNode goalNode = getNodeFromPosition(goal);

		open.clear();
		closed.clear();
		open.add(startNode);
		if (startNode == null || goalNode == null) {
			List<Position> goals = new ArrayList<Position>();
			goals.add(goal);
			return goals;
		}

		startNode.previous = null;
		startNode.heuristicDistance = getHeuristicDistance(startNode, goalNode);
		startNode.distance = 0;
		int maxSearchDistance = 10000;
		int maxDepth = 0;

		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			AStarNode current = open.get(0);

			if (current.equals(goalNode)) {
				break;
			}

			open.remove(current);
			closed.add(current);

			Collection<AStarNode> neighbors = getNeighbors(current);
			for (AStarNode neighbor : neighbors) {

				// ignore walls
				if (neighbor == null)
					continue;

				// ignore neighbors in the closed set.
				if (closed.contains(neighbor))
					continue;

				double distance = 0;
				if (current.x != neighbor.x && current.y != neighbor.y)
					distance = Math.sqrt(2);
				else
					distance = 1;
				double newDistance = current.distance + distance;

				if (!open.contains(neighbor)) {
					open.add(neighbor);
				} else if (newDistance >= neighbor.distance)
					continue; // This is not a better path.

				// This path is the best until now. Record it!
				neighbor.previous = current;
				neighbor.distance = newDistance;
				neighbor.heuristicDistance = neighbor.distance + getHeuristicDistance(neighbor, goalNode);
				Collections.sort(open);
			}
		}

		AStarNode node = goalNode;
		List<AStarNode> path = new ArrayList<>();
		while (node != null) {
			path.add(node);
			node = node.previous;
		}

		// translate to real world coordinates
		List<Position> positions = new ArrayList<>();
		for (AStarNode pathNode : path) {
			positions.add(getPositionFromNode(pathNode));
		}
		Collections.reverse(positions);
		if (smooth)
			return smooth(positions);
		return positions;
	}

	/**
	 * Gets a {@link Position} from a A Star Node.
	 * 
	 * @param node
	 *            The node.
	 * @return The position.
	 */
	private Position getPositionFromNode(AStarNode node) {
		double x = node.x * precision + 0.5 * precision;
		double y = node.y * precision + 0.5 * precision;
		return new Position(x, y);
	}
}
