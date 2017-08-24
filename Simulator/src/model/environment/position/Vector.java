package model.environment.position;

/**
 * A 2D vector.
 * 
 * @author S.A.M. Janssen
 */
public class Vector extends Position {

	/**
	 * Creates a vector.
	 * 
	 * @param x
	 *            The x direction.
	 * @param y
	 *            The y direction.
	 */
	public Vector(double x, double y) {
		this((float) x, (float) y);
	}

	/**
	 * Creates a vector.
	 * 
	 * @param x
	 *            The x direction.
	 * @param y
	 *            The y direction.
	 */
	public Vector(float x, float y) {
		super(x, y);
	}

	/**
	 * Add another vector.
	 * 
	 * @param other
	 *            The other vector.
	 * @return The sum of the two vectors.
	 */
	public Vector addVector(Vector other) {
		return new Vector(x + other.x, y + other.y);
	}

	@Override
	public Vector clone() {
		super.clone();
		return new Vector(x, y);
	}

	/**
	 * Element wise multiplication of this vector and another.
	 * 
	 * @param other
	 *            The other vector.
	 * @return The element wise multiplication of the two vectors.
	 */
	public Vector elementWiseMultiply(Vector other) {
		return new Vector(other.x * x, other.y * y);
	}

	/**
	 * Checks if this vector is an approximate rotation of another vector.
	 * 
	 * @param degrees
	 *            The degrees rotation.
	 * @param other
	 *            The other vector.
	 * @return True if it is an approximate rotation, false otherwise.
	 */
	public boolean isAproximateRotation(float degrees, Vector other) {
		float angle = degrees * ((float) Math.PI / 180);
		float xDiff = x - other.x * (float) Math.cos(angle) - other.y * (float) Math.sin(angle);
		float yDiff = y - other.y * (float) Math.cos(angle) + other.x * (float) Math.sin(angle);
		return new Vector(xDiff, yDiff).length() < 0.1;
	}

	/**
	 * The length of the vector.
	 * 
	 * @return The length.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Multiplies this vector with another.
	 * 
	 * @param other
	 *            The other vector.
	 * @return The multiplication.
	 */
	public double multiply(Vector other) {
		return x * other.x + y * other.y;
	}

	/**
	 * Normalizes this vector.
	 * 
	 * @return The normalized vector.
	 */
	public Vector normalize() {
		if (length() == 0)
			return new Vector(0, 0);

		return new Vector(x / length(), y / length());
	}

	/**
	 * Reverse the vector.
	 * 
	 * @return The reverse.
	 */
	public Vector reverse() {
		return new Vector(-x, -y);
	}

	/**
	 * Scalar multiplies this vector.
	 * 
	 * @param scalar
	 *            The scalar.
	 * @return The scalar multiplied vector.
	 */
	public Vector scalarMultiply(double scalar) {
		return scalarMultiply((float) scalar);
	}

	/**
	 * Scalar multiplies this vector.
	 * 
	 * @param scalar
	 *            The scalar.
	 * @return The scalar multiplied vector.
	 */
	public Vector scalarMultiply(float scalar) {
		return new Vector(scalar * x, scalar * y);
	}

	/**
	 * Subtracts another vector from this vector.
	 * 
	 * @param other
	 *            The other vector.
	 * @return The subtracted vector.
	 */
	public Vector subtractVector(Vector other) {
		return new Vector(x - other.x, y - other.y);
	}

	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}
