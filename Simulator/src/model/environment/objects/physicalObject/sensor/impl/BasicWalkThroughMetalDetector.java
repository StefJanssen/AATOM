package model.environment.objects.physicalObject.sensor.impl;

import java.util.List;

import model.environment.map.Map;
import model.environment.objects.physicalObject.sensor.Observation;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * A Basic Walk Through Metal Detector (WTMD).
 * 
 * @author S.A.M. Janssen
 */
public class BasicWalkThroughMetalDetector extends WalkThroughMetalDetector {

	/**
	 * The etd threshold.
	 */
	private double etdThreshold;
	/**
	 * The random check threshold.
	 */
	private double randomCheckThreshold;

	/**
	 * Creates a WTMD from its corner {@link Position}s.
	 * 
	 * @param corners
	 *            The corners.
	 * @param checkPosition
	 *            The check position.
	 * @param map
	 *            The map.
	 * @param etdThreshold
	 *            The etd threshold.
	 * @param randomCheckThreshold
	 *            The random check threshold.
	 */
	public BasicWalkThroughMetalDetector(List<Position> corners, Position checkPosition, Map map, double etdThreshold,
			double randomCheckThreshold) {
		super(corners, checkPosition, map);
		this.etdThreshold = etdThreshold;
		this.randomCheckThreshold = randomCheckThreshold;
	}

	/**
	 * Determines if the passenger can go.
	 * 
	 * @return True if it can, false otherwise.
	 */
	@Override
	public boolean canGo() {
		boolean lastPassengerGone = true;
		if (getLastObservedPassenger() != null) {
			if (shape.contains(getLastObservedPassenger().getPosition().x, getLastObservedPassenger().getPosition().y))
				lastPassengerGone = false;
		}
		return personsInCosideration == null && lastPassengerGone;
	}

	@Override
	public Observation<?> getObservation() {
		if (personsInCosideration != null) {
			if (shape.contains(personsInCosideration.getPosition().x, personsInCosideration.getPosition().y)) {
				lastObservedPassenger = personsInCosideration;
				personsInCosideration = null;

				// etd check.
				if (Utilities.RANDOM_GENERATOR.nextDouble() < etdThreshold) {
					return new Observation<Integer>(1);
				}
				// random check.
				if (Utilities.RANDOM_GENERATOR.nextDouble() < randomCheckThreshold) {
					return new Observation<Integer>(2);
				}
				// Detected nothing.
				return new Observation<Integer>(0);
			}
		}
		return Observation.NO_OBSERVATION;
	}
}