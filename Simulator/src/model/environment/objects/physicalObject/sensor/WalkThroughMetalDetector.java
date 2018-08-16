package model.environment.objects.physicalObject.sensor;

import java.awt.geom.Path2D;
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
public abstract class WalkThroughMetalDetector extends Sensor implements PolygonShape {

	/**
	 * The corner points of the WTMD.
	 */
	protected List<Position> corners;
	/**
	 * The {@link Path2D} shape of the WTMD.
	 */
	protected Path2D shape;
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
	 * @param map
	 *            The map.
	 */
	public WalkThroughMetalDetector(List<Position> corners, Position checkPosition, Map map) {
		super(Utilities.getAveragePosition(corners), map);
		this.corners = corners;
		this.checkPosition = checkPosition;
		shape = Utilities.getShape(corners);
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

		if (personsInCosideration != null && personsInCosideration.isDestroyed())
			personsInCosideration = null;

		return lastObservedPassenger;
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
		personsInCosideration = passenger;
	}
}