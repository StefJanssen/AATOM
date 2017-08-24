package model.environment.map;

import model.environment.position.Position;
import simulation.simulation.util.SimulationObject;

/**
 * A map component is an object that can be added to a {@link Map}.
 * 
 * @author S.A.M. Janssen
 *
 */
public abstract class MapComponent implements SimulationObject {

	/**
	 * States if the map component is destroyed.
	 */
	private boolean destroyed;
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
	public MapComponent(Position position) {
		this.position = position;
	}

	/**
	 * Destroys the component.
	 */
	public void destroy() {
		destroyed = true;
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
	 * Determines if the component is destroyed.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isDestroyed() {
		return destroyed;
	}
}