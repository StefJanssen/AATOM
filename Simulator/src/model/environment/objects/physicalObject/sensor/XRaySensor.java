package model.environment.objects.physicalObject.sensor;

import java.awt.geom.Path2D;
import java.util.List;

import model.environment.map.Map;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;
import model.environment.shapes.PolygonShape;
import simulation.simulation.util.Utilities;

/**
 * An X-Ray sensor is used at an airport checkpoint to scan baggage for illegal
 * objects. It is part of an {@link XRaySystem}.
 * 
 * @author S.A.M. Janssen
 */
public abstract class XRaySensor extends Sensor implements PolygonShape {

	/**
	 * The corner points of the X-Ray sensor.
	 */
	protected List<Position> corners;
	/**
	 * The shape.
	 */
	protected Path2D shape;
	/**
	 * The last observed luggage.
	 */
	protected Luggage lastObservedLuggage;

	/**
	 * Creates an X-Ray sensor from its corner {@link Position}s.
	 * 
	 * @param corners
	 *            The corner points.
	 * @param map
	 *            The map.
	 */
	public XRaySensor(List<Position> corners, Map map) {
		super(Utilities.getAveragePosition(corners), map);
		this.corners = corners;
		shape = Utilities.getShape(corners);
		lastObservedLuggage = Luggage.NO_LUGGAGE;
	}

	/**
	 * Gets the corner points.
	 * 
	 * @return The corner points.
	 */
	@Override
	public List<Position> getCorners() {
		return corners;
	}

	/**
	 * Gets the last observed luggage.
	 * 
	 * @return The last observed luggage.
	 */
	public Luggage getLastObservedLuggage() {
		return lastObservedLuggage;
	}

	@Override
	public Path2D getShape() {
		return shape;
	}

}
