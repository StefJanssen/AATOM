package model.agent.humanAgent.tacticalLevel.navigation.pathfinder;

/**
 * An A* node is a node used in the {@link AStarPathFinder}.
 * 
 * @author S.A.M. Janssen
 */
public class AStarNode implements Comparable<AStarNode> {

	/**
	 * The distance of the node from the start.
	 */
	protected double distance;
	/**
	 * The x position of the node.
	 */
	protected int x;
	/**
	 * The y position of the node.
	 */
	protected int y;
	/**
	 * The heuristic distance of the node to the goal node.
	 */
	protected double heuristicDistance;

	/**
	 * The predecessor of the node.
	 */
	protected AStarNode previous;

	/**
	 * Creates an A* node with a specified x- and y coordinate.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public AStarNode(int x, int y) {
		this.x = x;
		this.y = y;
		heuristicDistance = Double.MAX_VALUE;
	}

	@Override
	public int compareTo(AStarNode o) {
		double first = heuristicDistance + distance;
		double second = o.heuristicDistance + o.distance;
		if (first < second)
			return -1;
		if (Math.abs(first - second) < .0000001)
			return 0;
		return 1;
	}
}
