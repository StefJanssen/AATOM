package model.environment.objects.physicalObject.sensor.impl;

import java.util.List;

import model.environment.objects.physicalObject.sensor.XRaySensor;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;

/**
 * A basic X-Ray system.
 * 
 * @author S.A.M. Janssen
 */
public class BasicXRaySystem extends XRaySystem {

	/**
	 * Creates an x-ray system from a set of corner points.
	 * 
	 * @param systemCornerPoints
	 *            The corner points of the system.
	 * @param sensor
	 *            The sensor.
	 * @param luggageStart
	 *            The starting position of the luggage.
	 * @param luggageEnd
	 *            The ending position of the luggage.
	 */
	public BasicXRaySystem(List<Position> systemCornerPoints, XRaySensor sensor, Position luggageStart,
			Position luggageEnd) {
		super(systemCornerPoints, sensor, luggageStart, luggageEnd);
	}

	/**
	 * Creates an x-ray system from a set of corner points.
	 * 
	 * @param systemCornerPoints
	 *            The corner points of the system.
	 * @param sensor
	 *            The sensor.
	 * @param luggageStart
	 *            The starting position of the luggage.
	 * @param luggageEnd
	 *            The ending position of the luggage.
	 * @param otherWayAround
	 *            An mirrored x-ray or not.
	 */
	public BasicXRaySystem(List<Position> systemCornerPoints, XRaySensor sensor, Position luggageStart,
			Position luggageEnd, boolean otherWayAround) {
		super(systemCornerPoints, sensor, luggageStart, luggageEnd, otherWayAround);
	}
}