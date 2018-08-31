package model.environment.objects.physicalObject.sensor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.objects.physicalObject.sensor.Observation;
import model.environment.objects.physicalObject.sensor.XRaySensor;
import model.environment.position.Position;

/**
 * A basic X-Ray sensor.
 * 
 * @author S.A.M. Janssen
 */
public class BasicXRaySensor extends XRaySensor {

	/**
	 * The bags that are checked already.
	 */
	private Collection<Luggage> checkedLuggage;

	/**
	 * Creates an X-Ray sensor from its corner {@link Position}s.
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public BasicXRaySensor(List<Position> corners) {
		super(corners);
		checkedLuggage = new ArrayList<>();
	}

	@Override
	public Observation<?> getObservation() {
		for (Luggage luggage : map.getMapComponents(Luggage.class)) {
			if (luggage.getLuggageType().equals(LuggageType.CARRY_ON)) {
				if (!checkedLuggage.contains(luggage)) {
					if (shape.contains(luggage.getPosition().x, luggage.getPosition().y)) {
						lastObservedLuggage = luggage;
						checkedLuggage.add(luggage);
						return new Observation<Float>(luggage.getThreatLevel());
					}
				}
			}
		}
		return Observation.NO_OBSERVATION;
	}
}
