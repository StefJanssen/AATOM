package model.environment.position;

/**
 * Position in the 2D plain.
 * 
 * @author S.A.M. Janssen
 */
public class Position implements Cloneable {

	/**
	 * Holder to indicate no position.
	 */
	public static final Position NO_POSITION = new Position(-1, -1);
	/**
	 * An error tolerance.
	 */
	private static final float epsilon = 0.00001f;
	/**
	 * The x position.
	 */
	public final float x;
	/**
	 * The y position.
	 */
	public final float y;

	/**
	 * Creates a new position.
	 * 
	 * @param x
	 *            x position.
	 * @param y
	 *            y position.
	 */
	public Position(double x, double y) {
		this((float) x, (float) y);
	}

	/**
	 * Creates a new position.
	 * 
	 * @param x
	 *            x position.
	 * @param y
	 *            y position.
	 */
	public Position(float x, float y) {
		if (Math.abs(x) < epsilon)
			x = 0;
		if (Math.abs(y) < epsilon)
			y = 0;
		this.x = x;
		this.y = y;
	}

	@Override
	public Position clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new Position(x, y);
	}

	/**
	 * The distance to another position.
	 * 
	 * @param other
	 *            The other position.
	 * @return The distance.
	 */
	public float distanceTo(Position other) {
		return (float) Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
	}

	/**
	 * Checks if this position is the same as an other position.
	 * 
	 * @param other
	 *            The other position.
	 * @return True if they are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Position)
			return x == ((Position) other).x && y == ((Position) other).y;
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
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
	private float round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (float) Math.round(value * scale) / scale;
	}

	@Override
	public String toString() {
		return "(" + round(x, 3) + ", " + round(y, 3) + ")";
	}
}
