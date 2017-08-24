package model.environment.objects.physicalObject.sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.Agent;
import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.environment.map.Map;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.util.Utilities;

/**
 * An X-Ray system is a system used at a checkpoint of an airport. It consists
 * out of an {@link XRaySensor} and some physical objects (i.e. conveyer belts)
 * around it.
 * 
 * It is seen as an {@link Agent}, as it is able to perform actions in its
 * environment (i.e. it moves baggage that is in the system).
 * 
 * @author S.A.M. Janssen
 */
public class XRaySystem extends PhysicalObject {

	/**
	 * The collect position for bags.
	 */
	private Position baggageCollectPosition;

	/**
	 * The drop off position for bags.
	 */
	private Position baggageDropOffPosition;

	/**
	 * The ending position of the baggage.
	 */
	private Position baggageEndPosition;
	/**
	 * The baggage that is in the system.
	 */
	private Collection<Luggage> baggageInSystem;
	/**
	 * The starting position of the baggage.
	 */
	private Position baggageStartPosition;
	/**
	 * The vector point in the direction of the baggage end position, from the
	 * baggage start position.
	 */
	private Vector moveBagVector;
	/**
	 * The {@link XRaySensor}.
	 */
	private XRaySensor xray;
	/**
	 * The drop off passenger.
	 */
	private Passenger dropOffPassenger;
	/**
	 * The collect passenger.
	 */
	private Passenger collectPassenger;

	/**
	 * Creates an x-ray system from a set of corner points.
	 * 
	 * @param systemCornerPoints
	 *            The corner points of the system.
	 * @param machineCornerPoints
	 *            The corner points of the machine.
	 * @param baggageStart
	 *            The starting position of the baggage.
	 * @param baggageEnd
	 *            The ending position of the baggage.
	 * @param map
	 *            The map.
	 */
	public XRaySystem(List<Position> systemCornerPoints, List<Position> machineCornerPoints, Position baggageStart,
			Position baggageEnd, Map map) {
		this(systemCornerPoints, machineCornerPoints, baggageStart, baggageEnd, map, false);
	}

	/**
	 * Creates an x-ray system from a set of corner points.
	 * 
	 * @param systemCornerPoints
	 *            The corner points of the system.
	 * @param machineCornerPoints
	 *            The corner points of the machine.
	 * @param baggageStart
	 *            The starting position of the baggage.
	 * @param baggageEnd
	 *            The ending position of the baggage.
	 * @param map
	 *            The map.
	 * @param otherWayAround
	 *            An mirrored x-ray or not.
	 */
	public XRaySystem(List<Position> systemCornerPoints, List<Position> machineCornerPoints, Position baggageStart,
			Position baggageEnd, Map map, boolean otherWayAround) {
		super(systemCornerPoints);
		xray = new XRaySensor(machineCornerPoints, map);

		baggageInSystem = new ArrayList<>();
		baggageStartPosition = baggageStart;
		baggageEndPosition = baggageEnd;

		Vector start = new Vector(baggageStart.x, baggageStart.y);
		Vector end = new Vector(baggageEnd.x, baggageEnd.y);

		Vector v = end.subtractVector(start);
		v = v.normalize();
		baggageDropOffPosition = Utilities.transform(new Position(baggageStart.x + v.x, baggageStart.y + v.y),
				baggageStart, 90);
		if (otherWayAround)
			baggageDropOffPosition = Utilities.transform(new Position(baggageStart.x + v.x, baggageStart.y + v.y),
					baggageStart, 270);

		v = start.subtractVector(end);
		v = v.normalize();
		baggageCollectPosition = Utilities.transform(new Position(baggageEnd.x + v.x, baggageEnd.y + v.y), baggageEnd,
				270);
		if (otherWayAround)
			baggageCollectPosition = Utilities.transform(new Position(baggageEnd.x + v.x, baggageEnd.y + v.y),
					baggageEnd, 90);

		moveBagVector = end.subtractVector(start);
		moveBagVector = moveBagVector.normalize().scalarMultiply(0.5);
	}

	/**
	 * Adds baggage to the system at the {@link #baggageStartPosition}.
	 * 
	 * @param baggage
	 *            The baggage.
	 */
	public void addBaggage(Luggage baggage) {
		baggageInSystem.add(baggage);
		baggage.setPosition(baggageStartPosition);
	}

	/**
	 * Collect {@link Luggage} from the system.
	 * 
	 * @param bag
	 *            The bag.
	 * @return True if successful, false otherwise.
	 */
	public boolean collectBaggage(Luggage bag) {
		if (bag.getPosition().distanceTo(baggageEndPosition) <= 0.1) {
			bag.setPosition(bag.getOwner().getPosition());
			baggageInSystem.remove(bag);
			return true;
		}
		return false;
	}

	/**
	 * Gets the baggage in the system.
	 * 
	 * @return The baggage.
	 */
	public Collection<Luggage> getBaggageInSystem() {
		return baggageInSystem;
	}

	/**
	 * Gets the collect passenger.
	 * 
	 * @return The passenger.
	 */
	public Passenger getCollectPassenger() {
		return collectPassenger;
	}

	/**
	 * Gets the baggage collect position.
	 * 
	 * @return The position
	 */
	public Position getCollectPosition() {
		return baggageCollectPosition;
	}

	/**
	 * Gets the drop off passenger.
	 * 
	 * @return The passenger.
	 */
	public Passenger getDropOffPassenger() {
		return dropOffPassenger;
	}

	/**
	 * Gets the baggage drop off position.
	 * 
	 * @return The position
	 */
	public Position getDropOffPosition() {
		return baggageDropOffPosition;
	}

	/**
	 * Gets the {@link XRaySensor}.
	 * 
	 * @return The sensor.
	 */
	public XRaySensor getXRaySensor() {
		return xray;
	}

	/**
	 * Move the baggage forward.
	 * 
	 * @param timeStep
	 *            The time step.
	 */
	public void move(int timeStep) {
		for (Luggage bag : baggageInSystem) {
			if (bag.getPosition().distanceTo(baggageEndPosition) > 0.1) {
				Position current = bag.getPosition();
				Vector move = moveBagVector.scalarMultiply(timeStep / 1000.0);
				bag.setPosition(new Position(current.x + move.x, current.y + move.y));
			}
		}
	}

	/**
	 * Removes {@link Luggage} from the system and returns it to the
	 * {@link HumanAgent} that owns the baggage.
	 * 
	 * @param baggage
	 *            The baggage.
	 */
	public void removeBaggage(Luggage baggage) {
		baggageInSystem.remove(baggage);
		Position ownerPosition = baggage.getOwner().getPosition();
		baggage.setPosition(ownerPosition);
	}

	/**
	 * Sets the collect passenger.
	 * 
	 * @param collectPassenger
	 *            The passenger.
	 */
	public void setCollectPassenger(Passenger collectPassenger) {
		this.collectPassenger = collectPassenger;
	}

	/**
	 * Sets the drop off passenger.
	 * 
	 * @param dropOffPassenger
	 *            The passenger.
	 */
	public void setDropOffPassenger(Passenger dropOffPassenger) {
		this.dropOffPassenger = dropOffPassenger;
	}
}