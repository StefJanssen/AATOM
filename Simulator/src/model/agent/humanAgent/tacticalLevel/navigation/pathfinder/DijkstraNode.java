package model.agent.humanAgent.tacticalLevel.navigation.pathfinder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Dijkstra Node class represents a specific node in the Dijkstra graph,
 * used in the {@link DijkstraPathFinder}.
 * 
 * @author S.A.M. Janssen
 *
 */
public class DijkstraNode {

	/**
	 * The distance of the node from the start.
	 */
	protected double distance;
	/**
	 * The neighbors of the node.
	 */
	protected Collection<DijkstraNode> neighbors;
	/**
	 * The predecessor of the node.
	 */
	protected DijkstraNode previous;
	/**
	 * The x position of the node.
	 */
	protected int x;
	/**
	 * The y position of the node.
	 */
	protected int y;

	/**
	 * Creates a Dijkstra Node at position (x,y).
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public DijkstraNode(int x, int y) {
		this.x = x;
		this.y = y;
		neighbors = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}