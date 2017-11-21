package model.environment.objects.physicalObject.sensor;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.environment.map.Map;
import model.environment.position.Position;
import model.environment.shapes.PolygonShape;
import simulation.simulation.util.Utilities;

/**
 * A Walk Through Metal Detector (WTMD) is used at an airport checkpoint to
 * detect metallic objects.
 * 
 * @author S.A.M. Janssen
 */
public class WalkThroughMetalDetector extends Sensor implements PolygonShape {

	/**
	 * The corner points of the WTMD.
	 */
	private List<Position> corners;

	/**
	 * The persons under consideration.
	 */
	private List<Passenger> personsInCosideration;

	/**
	 * The {@link Path2D} shape of the WTMD.
	 */
	private Path2D shape;
	/**
	 * The check position.
	 */
	private Position checkPosition;
	/**
	 * The last observed passenger.
	 */
	private Passenger lastObservedPassenger;

	/**
	 * Creates a WTMD from its corner {@link Position}s.
	 * 
	 * @param corners
	 *            The corners.
	 * @param checkPosition
	 *            The check position.
	 * @param map
	 *            The map.
	 */
	public WalkThroughMetalDetector(List<Position> corners, Position checkPosition, Map map) {
		super(Utilities.getAveragePosition(corners), map);
		this.corners = corners;
		this.checkPosition = checkPosition;
		shape = Utilities.getShape(corners);
		personsInCosideration = new ArrayList<>();
	}

	/**
	 * Determines if the passenger can go.
	 * 
	 * @return True if it can, false otherwise.
	 */
	public boolean canGo() {
		boolean lastPassengerGone = true;
		if (getLastObservedPassenger() != null) {
			if (shape.contains(lastObservedPassenger.getPosition().x, lastObservedPassenger.getPosition().y))
				lastPassengerGone = false;
		}
		return personsInCosideration.size() <= 0 && lastPassengerGone;
	}

	/**
	 * Gets the check position.
	 * 
	 * @return The check position.
	 */
	public Position getCheckPosition() {
		return checkPosition;
	}

	/**
	 * Gets the corner points of the WTMD.
	 * 
	 * @return The corner points.
	 */
	@Override
	public List<Position> getCorners() {
		return corners;
	}

	/**
	 * Gets the last observed passenger.
	 * 
	 * @return The last observed passenger.
	 */
	public Passenger getLastObservedPassenger() {
		if (lastObservedPassenger != null) {
			if (lastObservedPassenger.isDestroyed())
				lastObservedPassenger = null;
		}

		List<Passenger> toRemove = new ArrayList<>();
		for (Passenger p : personsInCosideration) {
			if (p.isDestroyed())
				toRemove.add(p);
		}
		personsInCosideration.removeAll(toRemove);

		return lastObservedPassenger;
	}

	@Override
	public Observation<?> getObservation() {
		for (Passenger p : personsInCosideration) {
			if (shape.contains(p.getPosition().x, p.getPosition().y)) {
				personsInCosideration.remove(p);
				lastObservedPassenger = p;
				// Detected metal.
				if (Utilities.RANDOM_GENERATOR.nextDouble() < 0.1) {
					return new Observation<Integer>(1);
				}
				// Detected ETD.
				if (Utilities.RANDOM_GENERATOR.nextDouble() < 0.1) {
					return new Observation<Integer>(2);
				}
				// Detected nothing.
				return new Observation<Integer>(0);
			}
		}
		return Observation.NO_OBSERVATION;
	}

	/**
	 * Gets the {@link Path2D} shape of the shop.
	 * 
	 * @return The shape.
	 */
	@Override
	public Path2D getShape() {
		return shape;
	}

	/**
	 * Sets the persons in consideration.
	 * 
	 * @param passenger
	 *            The passenger.
	 */
	public void setPersonsInConsideration(Passenger passenger) {
		personsInCosideration.add(passenger);
	}
}