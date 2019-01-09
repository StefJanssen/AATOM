package model.environment.objects.physicalObject.sensor;

import java.util.List;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * A Walk Through Metal Detector (WTMD) is used at an airport checkpoint to
 * detect metallic objects.
 * 
 * @author S.A.M. Janssen
 */
public abstract class WalkThroughMetalDetector extends Sensor {

	/**
	 * The check position.
	 */
	protected Position checkPosition;
	/**
	 * The passengers in consideration.
	 */
	protected Passenger personsInCosideration;
	/**
	 * The last observed passenger.
	 */
	protected Passenger lastObservedPassenger;

	/**
	 * Creates a WTMD from its corner {@link Position}s.
	 * 
	 * @param corners
	 *            The corners.
	 * @param checkPosition
	 *            The check position.
	 */
	public WalkThroughMetalDetector(List<Position> corners, Position checkPosition) {
		super(corners);
		position = Utilities.getAveragePosition(corners);
		this.checkPosition = checkPosition;
	}

	/**
	 * Determines if the passenger can go.
	 * 
	 * @return True if it can, false otherwise.
	 */
	public abstract boolean canGo();

	/**
	 * Gets the check position.
	 * 
	 * @return The check position.
	 */
	public Position getCheckPosition() {
		return checkPosition;
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

		if (personsInCosideration != null && personsInCosideration.isDestroyed())
			personsInCosideration = null;

		return lastObservedPassenger;
	}

	/**
	 * Sets the persons in consideration.
	 * 
	 * @param passenger
	 *            The passenger.
	 */
	public void setPersonsInConsideration(Passenger passenger) {
		personsInCosideration = passenger;
	}
}