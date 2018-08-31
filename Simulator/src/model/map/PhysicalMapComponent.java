package model.map;

import model.environment.position.Position;
import model.environment.position.Vector;

/**
 * A physical map component has a physical presence on the map.
 * 
 * @author S.A.M. Janssen
 */
public abstract class PhysicalMapComponent extends MapComponent {

	/**
	 * The position of the map component on the map.
	 */
	protected Position position;

	/**
	 * Create the map component at a specified {@link Position}.
	 * 
	 * @param position
	 *            the Position.
	 */
	public PhysicalMapComponent(Position position) {
		this.position = position;
	}

	/**
	 * Gets the distance between a {@link Position} and the component.
	 * 
	 * @param position
	 *            The position.
	 * @return The distance.
	 */
	public double getDistance(Position position) {
		return getVector(position).length();
	}

	/**
	 * Gets the {@link Position} of the map component.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets a {@link Vector} pointing from the component to a {@link Position}.
	 * 
	 * @param position
	 *            The first position.
	 * @return The vector.
	 */
	public abstract Vector getVector(Position position);

	/**
	 * Determines if a line intersects with the {@link PhysicalMapComponent}.
	 * 
	 * @param start
	 *            The start position of the line.
	 * @param end
	 *            The end position of the line.
	 * 
	 * @return True if they do, false otherwise.
	 */
	public abstract boolean isLineCollision(Position start, Position end);
}
