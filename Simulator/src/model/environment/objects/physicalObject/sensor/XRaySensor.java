package model.environment.objects.physicalObject.sensor;

import java.util.List;

import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;

/**
 * An X-Ray sensor is used at an airport checkpoint to scan baggage for illegal
 * objects. It is part of an {@link XRaySystem}.
 * 
 * @author S.A.M. Janssen
 */
public abstract class XRaySensor extends Sensor {

	/**
	 * The last observed luggage.
	 */
	protected Luggage lastObservedLuggage;

	/**
	 * Creates an X-Ray sensor from its corner {@link Position}s.
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public XRaySensor(List<Position> corners) {
		super(corners);
		lastObservedLuggage = Luggage.NO_LUGGAGE;
	}

	/**
	 * Gets the last observed luggage.
	 * 
	 * @return The last observed luggage.
	 */
	public Luggage getLastObservedLuggage() {
		return lastObservedLuggage;
	}
}
