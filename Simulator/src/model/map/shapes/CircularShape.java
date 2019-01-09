package model.map.shapes;

import model.map.MapComponent;

/**
 * All {@link MapComponent}s with a circular shape implement this interface.
 * 
 * @author S.A.M. Janssen
 */
public interface CircularShape {

	/**
	 * Gets the radius.
	 * 
	 * @return The radius.
	 */
	public float getRadius();
}
