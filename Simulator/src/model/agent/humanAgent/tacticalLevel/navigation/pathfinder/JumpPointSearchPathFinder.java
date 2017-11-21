package model.agent.humanAgent.tacticalLevel.navigation.pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.environment.map.Map;
import model.environment.position.Position;

/**
 * The jump point search path finder uses jump point search to determine paths.
 * Adapted from: https://github.com/qiao/PathFinding.js/ Original paper: Online
 * Graph Pruning for Pathfinding on Grid Maps, by Daniel Harabor and Alban
 * Grastien.
 * 
 * @author S.A.M. Janssen
 */
public class JumpPointSearchPathFinder extends PathFinder {

	/**
	 * The graph in array format.
	 */
	private JumpPointNode[][] graph;
	/**
	 * The goal node.
	 */
	private JumpPointNode goalNode;

	/**
	 * Creates the path finder.
	 * 
	 * @param map
	 *            The map.
	 * @param precision
	 *            The precision.
	 */
	public JumpPointSearchPathFinder(Map map, double precision) {
		super(map, precision);
		graph = new JumpPointNode[discretizedMap.length][discretizedMap[0].length];
	}

	/**
	 * Performs the successor step in the algorithm.
	 * 
	 * @param node
	 *            The node.
	 * @param openList
	 *            The open list.
	 */
	private void doSuccessor(JumpPointNode node, ArrayList<JumpPointNode> openList) {
		Collection<JumpPointNode> neighbors = getNeighbors(node);
		for (JumpPointNode neighbor : neighbors) {
			JumpPointNode jumpNode = jump(neighbor, node);
			if (jumpNode != null) {
				if (jumpNode.closed)
					continue;

				float distance = getDistance(jumpNode, node);
				float nextG = node.g + distance;
				if (!jumpNode.opened || nextG < jumpNode.g) {
					jumpNode.g = nextG;
					jumpNode.h = getDistance(jumpNode, goalNode);
					jumpNode.f = jumpNode.g + jumpNode.h;
					jumpNode.parent = node;

					if (!jumpNode.opened) {
						openList.add(jumpNode);
						jumpNode.opened = true;
					}
				}
			}
		}
	}

	/**
	 * Empties the map and sets all values to null. This method is used to
	 * prevent memory from large amounts of memory being stored in the agents
	 * for no reason.
	 */
	private void emptyMap() {
		for (int i = 0; i < graph[0].length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (!discretizedMap[j][i]) {
					graph[j][i] = null;
				}
			}
		}
	}

	/**
	 * Gets the distance between two nodes.
	 * 
	 * @param first
	 *            The first node.
	 * @param second
	 *            The second node.
	 * @return The distance.
	 */
	private float getDistance(JumpPointNode first, JumpPointNode second) {
		if (first == null || second == null)
			return Float.MAX_VALUE;

		float dx = first.x - second.x;
		float dy = first.y - second.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Gets the neighbors of a {@link JumpPointNode}.
	 * 
	 * @param node
	 *            The node.
	 * @return The neighbors.
	 */
	private Collection<JumpPointNode> getNeighbors(JumpPointNode node) {
		Collection<JumpPointNode> neighbors = new ArrayList<>();
		int x = node.x;
		int y = node.y;

		JumpPointNode parent = node.parent;

		if (parent != null) {
			// get the normalized direction of travel
			int dx = node.x - parent.x;
			int dy = node.y - parent.y;
			if (dx != 0)
				dx /= Math.abs(dx);
			if (dy != 0)
				dy /= Math.abs(dy);

			if (dx != 0 && dy != 0) {
				if (isTraversable(x, y + dy)) {
					neighbors.add(getNode(x, y + dy));
				}
				if (isTraversable(x + dx, y)) {
					neighbors.add(getNode(x + dx, y));
				}
				if (isTraversable(x + dx, y + dy)) {
					neighbors.add(getNode(x + dx, y + dy));
				}
				if (!isTraversable(x - dx, y)) {
					neighbors.add(getNode(x - dx, y + dy));
				}
				if (!isTraversable(x, y - dy)) {
					neighbors.add(getNode(x + dx, y - dy));
				}
			}
			// search horizontally/vertically
			else {
				if (dx == 0) {
					if (isTraversable(x, y + dy)) {
						neighbors.add(getNode(x, y + dy));
					}
					if (!isTraversable(x + 1, y)) {
						neighbors.add(getNode(x + 1, y + dy));
					}
					if (!isTraversable(x - 1, y)) {
						neighbors.add(getNode(x - 1, y + dy));
					}
				} else {
					if (isTraversable(x + dx, y)) {
						neighbors.add(getNode(x + dx, y));
					}
					if (!isTraversable(x, y + 1)) {
						neighbors.add(getNode(x + dx, y + 1));
					}
					if (!isTraversable(x, y - 1)) {
						neighbors.add(getNode(x + dx, y - 1));
					}
				}
			}
		} else {
			neighbors.add(getNode(x + 1, y));
			neighbors.add(getNode(x + 1, y + 1));
			neighbors.add(getNode(x + 1, y - 1));
			neighbors.add(getNode(x - 1, y));
			neighbors.add(getNode(x - 1, y + 1));
			neighbors.add(getNode(x - 1, y - 1));
			neighbors.add(getNode(x, y + 1));
			neighbors.add(getNode(x, y - 1));
		}
		neighbors.removeAll(Collections.singleton(null));
		return neighbors;
	}

	/**
	 * Gets the next node from the open list.
	 * 
	 * @param openList
	 *            The open list.
	 * @return The next node.
	 */
	private JumpPointNode getNextNode(ArrayList<JumpPointNode> openList) {
		if (openList.isEmpty())
			return null;
		JumpPointNode smallest = openList.get(0);
		for (JumpPointNode node : openList) {
			if (node.f < smallest.f)
				smallest = node;
		}
		return smallest;
	}

	/**
	 * Gets a node from its coordinates.
	 * 
	 * @param x
	 *            X coordinate.
	 * @param y
	 *            Y coordinate.
	 * @return The node.
	 */
	private JumpPointNode getNode(int x, int y) {
		if (!isTraversable(x, y))
			return null;
		return graph[y][x];
	}

	/**
	 * Gets a JPS node from a {@link Position}.
	 * 
	 * @param position
	 *            The position.
	 * @return The node.
	 */
	private JumpPointNode getNodeFromPosition(Position position) {
		int x = (int) (position.x / precision);
		int y = (int) (position.y / precision);

		// fix input
		if (y >= graph.length)
			y = graph.length - 1;
		if (x >= graph[0].length)
			x = graph[0].length - 1;

		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		// We take a neighboring node if we cannot find the node....
		if (graph[y][x] == null) {
			Collection<JumpPointNode> neighbors = getNeighbors(new JumpPointNode(x, y));
			if (!neighbors.isEmpty())
				return neighbors.iterator().next();
		}

		return graph[y][x];
	}

	@Override
	public List<Position> getPath(Position start, Position goal, boolean smooth) {
		if (smooth && (directPathPossible(start, goal) || start.equals(Position.NO_POSITION)
				|| goal.equals(Position.NO_POSITION))) {
			List<Position> goalPositions = new ArrayList<>();
			goalPositions.add(goal);
			return goalPositions;
		}

		resetMap();
		ArrayList<JumpPointNode> openList = new ArrayList<>();
		List<JumpPointNode> path = new ArrayList<>();
		goalNode = getNodeFromPosition(goal);
		JumpPointNode startNode = getNodeFromPosition(start);
		if (startNode == null || goalNode == null) {
			List<Position> goalPositions = new ArrayList<>();
			goalPositions.add(goal);
			return goalPositions;
		}

		openList.add(startNode);
		startNode.opened = true;
		startNode.g = 0;
		startNode.f = 0;

		while (!openList.isEmpty()) {
			JumpPointNode current = getNextNode(openList);
			openList.remove(current);
			current.opened = true;
			current.closed = true;
			// if we're done create the final list
			if (current.equals(goalNode)) {
				path.add(current);
				JumpPointNode parent = current.parent;
				while (parent != null) {
					path.add(parent);
					parent = parent.parent;
				}
				break;
			}

			// we're not done, so find succesors for the curernt node.
			doSuccessor(current, openList);
		}

		// translate to real world coordinates
		List<Position> positions = new ArrayList<>();
		for (JumpPointNode pathNode : path) {
			if (pathNode.equals(goalNode))
				positions.add(goal);
			else
				positions.add(getPositionFromNode(pathNode));
		}

		Collections.reverse(positions);
		if (!positions.isEmpty())
			positions.remove(0);

		// empty map to prevent memory 'leaks'
		emptyMap();
		// if (smooth)
		// return smooth(positions);
		return positions;
	}

	/**
	 * Gets a {@link Position} from a A Star Node.
	 * 
	 * @param node
	 *            The node.
	 * @return The position.
	 */
	private Position getPositionFromNode(JumpPointNode node) {
		double x = node.x * precision + 0.5 * precision;
		double y = node.y * precision + 0.5 * precision;
		return new Position(x, y);
	}

	/**
	 * Determines if the graph is traversable at (x,y)
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @return True if it is traversable, false otherwise.
	 */
	private boolean isTraversable(int x, int y) {
		if (x >= graph[0].length || y >= graph.length || x < 0 || y < 0)
			return false;
		return graph[y][x] != null;
	}

	/**
	 * Perform the jump.
	 * 
	 * @param child
	 *            The child.
	 * @param parent
	 *            The parent.
	 * @return The jump node.
	 */
	private JumpPointNode jump(JumpPointNode child, JumpPointNode parent) {
		if (child == null)
			return null;

		if (child.equals(goalNode))
			return child;

		int dx = child.x - parent.x;
		int dy = child.y - parent.y;

		int x = child.x;
		int y = child.y;

		// diagonal
		if (dx != 0 && dy != 0) {
			if ((isTraversable(x - dx, y + dy) && !isTraversable(x - dx, y))
					|| (isTraversable(x + dx, y - dy) && !isTraversable(x, y - dy))) {
				return child;
			}

			// when moving diagonally, must check for vertical/horizontal jump
			// points
			JumpPointNode first = jump(getNode(x + dx, y), child);
			JumpPointNode second = jump(getNode(x, y + dy), child);

			if (first != null || second != null) {
				return child;
			}
		}
		// horizontally/vertically
		else {
			if (dx != 0) { // moving along x
				if ((isTraversable(x + dx, y + 1) && !isTraversable(x, y + 1))
						|| (isTraversable(x + dx, y - 1) && !isTraversable(x, y - 1))) {
					return child;
				}
			} else {
				if ((isTraversable(x + 1, y + dy) && !isTraversable(x + 1, y))
						|| (isTraversable(x - 1, y + dy) && !isTraversable(x - 1, y))) {
					return child;
				}
			}
		}

		return jump(getNode(x + dx, y + dy), child);
	}

	/**
	 * Resets the map.
	 */
	private void resetMap() {
		for (int i = 0; i < graph[0].length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (!discretizedMap[j][i]) {
					graph[j][i] = new JumpPointNode(i, j);
				}
			}
		}
	}

}
