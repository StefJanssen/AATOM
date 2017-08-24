package model.environment.objects.physicalObject.sensor;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
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
public class XRaySensor extends Sensor implements PolygonShape {

	/**
	 * The bags that are checked already.
	 */
	private Collection<Luggage> checkedLuggage;
	/**
	 * The corner points of the X-Ray sensor.
	 */
	private List<Position> corners;
	/**
	 * The shape.
	 */
	private Path2D shape;
	/**
	 * The last observed luggage.
	 */
	private Luggage lastObservedLuggage;

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
		checkedLuggage = new ArrayList<>();
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
	public Observation<?> getObservation() {
		for (Luggage luggage : map.getMapComponents(Luggage.class)) {
			if (!checkedLuggage.contains(luggage)) {
				if (shape.contains(luggage.getPosition().x, luggage.getPosition().y)) {
					lastObservedLuggage = luggage;
					checkedLuggage.add(luggage);
					return new Observation<Double>(luggage.getThreatLevel());
				}
			}
		}
		return Observation.NO_OBSERVATION;
	}

	@Override
	public Path2D getShape() {
		return shape;
	}

}
