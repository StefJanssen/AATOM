package model.environment.objects.physicalObject.sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.physicalObject.Openable;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.util.DirectlyUpdatable;
import simulation.simulation.util.Utilities;

/**
 * An X-Ray system is a system used at a checkpoint of an airport. It consists
 * of an {@link XRaySensor} and some physical objects (i.e. conveyer belts)
 * around it.
 * 
 * @author S.A.M. Janssen
 */
public abstract class XRaySystem extends PhysicalObject implements DirectlyUpdatable, Openable {

	/**
	 * The collect position for bags.
	 */
	private Position[] luggageCollectPositions;

	/**
	 * The drop off position for bags.
	 */
	private Position[] luggageDropOffPositions;

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
	private Passenger[] dropOffPassengers;
	/**
	 * The collect passenger.
	 */
	private Passenger[] collectPassengers;
	/**
	 * The system is paused.
	 */
	private boolean paused;
	/**
	 * The system is open or closed.
	 */
	private boolean isOpen;
	/**
	 * The leave position.
	 */
	private Position leavePosition;
	/**
	 * The enter position.
	 */
	private Position enterPosition;

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
	public XRaySystem(List<Position> systemCornerPoints, XRaySensor sensor, Position luggageStart,
			Position luggageEnd) {
		this(systemCornerPoints, sensor, luggageStart, luggageEnd, false);
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
	public XRaySystem(List<Position> systemCornerPoints, XRaySensor sensor, Position luggageStart, Position luggageEnd,
			boolean otherWayAround) {
		super(systemCornerPoints);
		xray = sensor;

		luggageInSystem = new ArrayList<>();
		luggageStartPosition = luggageStart;
		luggageEndPosition = luggageEnd;

		int numberOfDropOffPositions = 3;
		luggageDropOffPositions = new Position[numberOfDropOffPositions];
		luggageCollectPositions = new Position[numberOfDropOffPositions];
		dropOffPassengers = new Passenger[numberOfDropOffPositions];
		collectPassengers = new Passenger[numberOfDropOffPositions];

		Vector start = new Vector(luggageStart.x, luggageStart.y);
		Vector end = new Vector(luggageEnd.x, luggageEnd.y);

		moveLuggageVector = end.subtractVector(start);
		moveLuggageVector = moveLuggageVector.normalize().scalarMultiply(0.5);

		Position luggageStart0 = new Position(luggageStart.x + 1.2 * moveLuggageVector.x,
				luggageStart.y + 1.2 * moveLuggageVector.y);
		Position luggageStart2 = new Position(luggageStart.x + 2.4 * moveLuggageVector.x,
				luggageStart.y + 2.4 * moveLuggageVector.y);

		Vector v = end.subtractVector(start);
		v = v.normalize();

		luggageDropOffPositions[1] = Utilities
				.transform(new Position(luggageStart0.x + 0.6 * v.x, luggageStart0.y + 0.6 * v.y), luggageStart0, 90);
		luggageDropOffPositions[2] = Utilities
				.transform(new Position(luggageStart.x + 0.6 * v.x, luggageStart.y + 0.6 * v.y), luggageStart, 90);
		luggageDropOffPositions[0] = Utilities
				.transform(new Position(luggageStart2.x + 0.6 * v.x, luggageStart2.y + 0.6 * v.y), luggageStart2, 90);

		if (otherWayAround) {
			luggageDropOffPositions[1] = Utilities.transform(
					new Position(luggageStart0.x + 0.6 * v.x, luggageStart0.y + 0.6 * v.y), luggageStart0, 270);
			luggageDropOffPositions[2] = Utilities
					.transform(new Position(luggageStart.x + 0.6 * v.x, luggageStart.y + 0.6 * v.y), luggageStart, 270);
			luggageDropOffPositions[0] = Utilities.transform(
					new Position(luggageStart2.x + 0.6 * v.x, luggageStart2.y + 0.6 * v.y), luggageStart2, 270);
		}

		v = start.subtractVector(end);
		v = v.normalize();

		Position luggageEnd0 = new Position(luggageEnd.x - 3 * moveLuggageVector.x,
				luggageEnd.y - 3 * moveLuggageVector.y);
		Position luggageEnd2 = new Position(luggageEnd.x - 4.5 * moveLuggageVector.x,
				luggageEnd.y - 4.5 * moveLuggageVector.y);

		luggageCollectPositions[1] = Utilities
				.transform(new Position(luggageEnd0.x + 0.6 * v.x, luggageEnd0.y + 0.6 * v.y), luggageEnd0, 270);
		luggageCollectPositions[0] = Utilities
				.transform(new Position(luggageEnd.x + 0.6 * v.x, luggageEnd.y + 0.6 * v.y), luggageEnd, 270);
		luggageCollectPositions[2] = Utilities
				.transform(new Position(luggageEnd2.x + 0.6 * v.x, luggageEnd2.y + 0.6 * v.y), luggageEnd2, 270);

		if (otherWayAround) {
			luggageCollectPositions[1] = Utilities
					.transform(new Position(luggageEnd0.x + 0.6 * v.x, luggageEnd0.y + 0.6 * v.y), luggageEnd0, 90);
			luggageCollectPositions[0] = Utilities
					.transform(new Position(luggageEnd.x + 0.6 * v.x, luggageEnd.y + 0.6 * v.y), luggageEnd, 90);
			luggageCollectPositions[2] = Utilities
					.transform(new Position(luggageEnd2.x + 0.6 * v.x, luggageEnd2.y + 0.6 * v.y), luggageEnd2, 90);
		}

		Position tmpLeave = new Position(luggageEnd.x + 2 * moveLuggageVector.x,
				luggageEnd.y + 2 * moveLuggageVector.y);
		leavePosition = Utilities.transform(new Position(tmpLeave.x + 0.6 * v.x, tmpLeave.y + 0.6 * v.y), tmpLeave,
				270);

		if (otherWayAround) {
			leavePosition = Utilities.transform(new Position(tmpLeave.x + 0.6 * v.x, tmpLeave.y + 0.6 * v.y), tmpLeave,
					90);
		}

		Position tmpStart = new Position(luggageStart.x + 2 * moveLuggageVector.x, luggageStart.y);
		enterPosition = Utilities.transform(new Position(tmpStart.x + 2 * v.x, tmpStart.y + 2 * v.y), tmpStart, 270);

		if (otherWayAround) {
			enterPosition = Utilities.transform(new Position(tmpStart.x + 2 * v.x, tmpStart.y + 2 * v.y), tmpStart, 90);
		}

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
	 * @param position
	 *            The position.
	 * 
	 * @return The passenger.
	 */
	public Passenger getCollectPassenger(int position) {
		for (int i = 0; i < collectPassengers.length; i++) {
			if (collectPassengers[i] != null) {
				if (collectPassengers[i].isDestroyed())
					collectPassengers[i] = null;
			}
		}
		return collectPassengers[position];
	}

	/**
	 * Gets the luggage collect position.
	 * 
	 * @param position
	 *            The index.
	 * 
	 * @return The position
	 */
	public Position getCollectPosition(int position) {
		if (position >= getNumberOfCollectIndices() || position < 0)
			return null;
		return luggageCollectPositions[position];
	}

	/**
	 * Gets the drop off passenger.
	 * 
	 * @param index
	 *            The index.
	 * 
	 * @return The passenger.
	 */
	public Passenger getDropOffPassenger(int index) {
		for (int i = 0; i < dropOffPassengers.length; i++) {
			if (dropOffPassengers[i] != null) {
				if (dropOffPassengers[i].isDestroyed())
					dropOffPassengers[i] = null;
			}
		}
		return dropOffPassengers[index];
	}

	/**
	 * Gets the luggage drop off position.
	 * 
	 * @param index
	 *            The index.
	 * 
	 * @return The position
	 */
	public Position getDropOffPosition(int index) {
		if (index >= getNumberOfDropoffIndices() || index < 0)
			return null;
		return luggageDropOffPositions[index];
	}

	/**
	 * Gets the enter position for passengers of the system.
	 * 
	 * @return The leave position.
	 */
	public Position getEnterPosition() {
		return enterPosition;
	}

	/**
	 * Gets the leave position for passengers of the system.
	 * 
	 * @return The leave position.
	 */
	public Position getLeavePosition() {
		return leavePosition;
	}

	/**
	 * Gets the next collect index. If -1 is returned, no position is available.
	 * 
	 * @return The next index.
	 */
	public int getNextCollectIndex() {
		for (int i = 0; i < getNumberOfCollectIndices(); i++) {
			if (getCollectPassenger(i) == null)
				return i;
		}
		return -1;
	}

	/**
	 * Gets the next drop off index. If -1 is returned, no position is
	 * available.
	 * 
	 * @return The next index.
	 */
	public int getNextDropOffIndex() {
		for (int i = 0; i < getNumberOfDropoffIndices(); i++) {
			if (getDropOffPassenger(i) == null)
				return i;
		}
		return -1;

	}

	/**
	 * Gets the number of collect places for passengers.
	 * 
	 * @return The number of collect places.
	 */
	public int getNumberOfCollectIndices() {
		return luggageCollectPositions.length;
	}

	/**
	 * Gets the number of drop off places for passengers.
	 * 
	 * @return The number of drop off places.
	 */
	public int getNumberOfDropoffIndices() {
		return luggageDropOffPositions.length;
	}

	/**
	 * Gets the {@link XRaySensor}.
	 * 
	 * @return The sensor.
	 */
	public XRaySensor getXRaySensor() {
		return xray;
	}

	@Override
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
	 * @param index
	 *            The index.
	 */
	public void setCollectPassenger(Passenger collectPassenger, int index) {
		if (index >= getNumberOfCollectIndices() || index < 0)
			return;
		this.collectPassengers[index] = collectPassenger;
	}

	/**
	 * Sets the drop off passenger.
	 * 
	 * @param dropOffPassenger
	 *            The passenger.
	 * @param index
	 *            The index.
	 */
	public void setDropOffPassenger(Passenger dropOffPassenger, int index) {
		if (index >= getNumberOfDropoffIndices() || index < 0)
			return;
		this.dropOffPassengers[index] = dropOffPassenger;
	}

	@Override
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