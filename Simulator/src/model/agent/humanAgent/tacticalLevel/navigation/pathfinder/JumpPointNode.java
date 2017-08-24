package model.agent.humanAgent.tacticalLevel.navigation.pathfinder;

/**
 * A jump point search node.
 * 
 * @author S.A.M. Janssen
 */
public class JumpPointNode {

	/**
	 * X coordinate.
	 */
	protected int x;
	/**
	 * Y coordinate.
	 */
	protected int y;
	/**
	 * The parent.
	 */
	protected JumpPointNode parent;

	/**
	 * The g value.
	 */
	protected float g;

	/**
	 * The f value.
	 */
	protected float f;

	/**
	 * Node is opened or not.
	 */
	protected boolean opened;
	/**
	 * Node is closed or not.
	 */
	public boolean closed;
	/**
	 * The h value.
	 */
	public float h;

	/**
	 * Creates a jump point node.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public JumpPointNode(int x, int y) {
		this.x = x;
		this.y = y;
		g = Float.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}