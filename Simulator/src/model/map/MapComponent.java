package model.map;

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
	 * Set map.
	 */
	protected Map map;

	/**
	 * Destroys the component.
	 */
	void destroy() {
		destroyed = true;
	}

	/**
	 * Determines if the component is destroyed.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * Sets the map of the {@link MapComponent}.
	 * 
	 * @param map
	 *            The map.
	 */
	void setMap(Map map) {
		this.map = map;
	}
}