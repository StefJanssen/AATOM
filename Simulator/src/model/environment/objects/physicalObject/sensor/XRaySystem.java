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
import simulation.simulation.util.DirectlyUpdatable;
import simulation.simulation.util.Utilities;

/**
 * An X-Ray system is a system used at a checkpoint of an airport. It consists
 * out of an {@link XRaySensor} and some physical objects (i.e. conveyer belts)
 * around it.
 * 
 * It is seen as an {@link Agent}, as it is able to perform actions in its
 * environment (i.e. it moves luggage that is in the system).
 * 
 * @author S.A.M. Janssen
 */
public class XRaySystem extends PhysicalObject implements DirectlyUpdatable {

	/**
	 * The collect position for bags.
	 */
	private Position luggageCollectPosition;

	/**
	 * The drop off position for bags.
	 */
	private Position luggageDropOffPosition;

	/**
	 * The ending position of the luggage.
	 */
	private Position luggageEndPosition;
	/**
	 * The luggage that is in the system.
	 */
	private Collection<Luggage> luggageInSystem;
	/**
	 * The starting position of the luggage.
	 */
	private Position luggageStartPosition;
	/**
	 * The vector point in the direction of the luggage end position, from the
	 * luggage start position.
	 */
	private Vector moveLuggageVector;
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
	 * The system is paused.
	 */
	private boolean paused;
	/**
	 * The system is open or closed.
	 */
	private boolean isOpen;

	/**
	 * Creates an x-ray system from a set of corner points.
	 * 
	 * @param systemCornerPoints
	 *            The corner points of the system.
	 * @param machineCornerPoints
	 *            The corner points of the machine.
	 * @param luggageStart
	 *            The starting position of the luggage.
	 * @param luggageEnd
	 *            The ending position of the luggage.
	 * @param map
	 *            The map.
	 */
	public XRaySystem(List<Position> systemCornerPoints, List<Position> machineCornerPoints, Position luggageStart,
			Position luggageEnd, Map map) {
		this(systemCornerPoints, machineCornerPoints, luggageStart, luggageEnd, map, false);
	}

	/**
	 * Creates an x-ray system from a set of corner points.
	 * 
	 * @param systemCornerPoints
	 *            The corner points of the system.
	 * @param machineCornerPoints
	 *            The corner points of the machine.
	 * @param luggageStart
	 *            The starting position of the luggage.
	 * @param luggageEnd
	 *            The ending position of the luggage.
	 * @param map
	 *            The map.
	 * @param otherWayAround
	 *            An mirrored x-ray or not.
	 */
	public XRaySystem(List<Position> systemCornerPoints, List<Position> machineCornerPoints, Position luggageStart,
			Position luggageEnd, Map map, boolean otherWayAround) {
		super(systemCornerPoints);
		xray = new XRaySensor(machineCornerPoints, map);

		luggageInSystem = new ArrayList<>();
		luggageStartPosition = luggageStart;
		luggageEndPosition = luggageEnd;

		Vector start = new Vector(luggageStart.x, luggageStart.y);
		Vector end = new Vector(luggageEnd.x, luggageEnd.y);

		Vector v = end.subtractVector(start);
		v = v.normalize();
		luggageDropOffPosition = Utilities.transform(new Position(luggageStart.x + v.x, luggageStart.y + v.y),
				luggageStart, 90);
		if (otherWayAround)
			luggageDropOffPosition = Utilities.transform(new Position(luggageStart.x + v.x, luggageStart.y + v.y),
					luggageStart, 270);

		v = start.subtractVector(end);
		v = v.normalize();
		luggageCollectPosition = Utilities.transform(new Position(luggageEnd.x + v.x, luggageEnd.y + v.y), luggageEnd,
				270);
		if (otherWayAround)
			luggageCollectPosition = Utilities.transform(new Position(luggageEnd.x + v.x, luggageEnd.y + v.y),
					luggageEnd, 90);

		moveLuggageVector = end.subtractVector(start);
		moveLuggageVector = moveLuggageVector.normalize().scalarMultiply(0.5);

		isOpen = true;
	}

	/**
	 * Adds luggage to the system at the {@link #luggageStartPosition}.
	 * 
	 * @param luggage
	 *            The luggage.
	 */
	public void addBaggage(Luggage luggage) {
		luggageInSystem.add(luggage);
		luggage.setPosition(luggageStartPosition);
	}

	/**
	 * Collect {@link Luggage} from the system.
	 * 
	 * @param bag
	 *            The bag.
	 * @return True if successful, false otherwise.
	 */
	public boolean collectBaggage(Luggage bag) {
		if (bag.getPosition().distanceTo(luggageEndPosition) <= 0.1) {
			bag.setPosition(bag.getOwner().getPosition());
			luggageInSystem.remove(bag);
			return true;
		}
		return false;
	}

	/**
	 * Gets the luggage in the system.
	 * 
	 * @return The luggage.
	 */
	public Collection<Luggage> getBaggageInSystem() {
		return luggageInSystem;
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
	 * Gets the luggage collect position.
	 * 
	 * @return The position
	 */
	public Position getCollectPosition() {
		return luggageCollectPosition;
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
	 * Gets the luggage drop off position.
	 * 
	 * @return The position
	 */
	public Position getDropOffPosition() {
		return luggageDropOffPosition;
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
	 * Is open or not.
	 * 
	 * @return True if open, false if closed.
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Pauses or unpaused the system.
	 * 
	 * @param paused
	 *            True if it is paused, false otherwise.
	 */
	public void pauseSystem(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Removes {@link Luggage} from the system and returns it to the
	 * {@link HumanAgent} that owns the luggage.
	 * 
	 * @param luggage
	 *            The luggage.
	 */
	public void removeBaggage(Luggage luggage) {
		luggageInSystem.remove(luggage);
		Position ownerPosition = luggage.getOwner().getPosition();
		luggage.setPosition(ownerPosition);
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

	/**
	 * Sets open.
	 * 
	 * @param isOpen
	 *            Open or not.
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public void update(int timeStep) {
		if (!paused) {
			for (Luggage bag : luggageInSystem) {
				if (bag.getPosition().distanceTo(luggageEndPosition) > 0.1) {
					Position current = bag.getPosition();
					Vector move = moveLuggageVector.scalarMultiply(timeStep / 1000.0);
					bag.setPosition(new Position(current.x + move.x, current.y + move.y));
				}
			}
		}
	}

}