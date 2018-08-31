package model.map.shapes;

import java.util.List;

import model.environment.position.Position;
import model.map.MapComponent;

/**
 * All {@link MapComponent}s with a polygon shape implement this interface.
 * 
 * @author S.A.M. Janssen
 */
public interface PolygonShape {

	/**
	 * Gets the corner points of the polygon.
	 * 
	 * @return The corner points.
	 */
	public List<Position> getCorners();
}
