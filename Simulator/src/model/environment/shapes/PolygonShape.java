package model.environment.shapes;

import java.awt.geom.Path2D;
import java.util.List;

import model.environment.map.MapComponent;
import model.environment.position.Position;

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

	/**
	 * Gets the shape of the polygon.
	 * 
	 * @return The shape.
	 */
	public Path2D getShape();
}
