package model.environment.objects.flight;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.Passenger;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.physicalObject.Desk;
import simulation.simulation.util.DirectlyUpdatable;
import simulation.simulation.util.SimulationObject;

/**
 * A flight.
 * 
 * @author S.A.M. Janssen
 */
public class Flight implements DirectlyUpdatable, SimulationObject {

	/**
	 * Holder for no flight.
	 */
	public static final Flight NO_FLIGHT = new Flight(FlightType.DEPARTING, 0, null, null, null, null);
	/**
	 * People that checked in already.
	 */
	private Collection<Passenger> checkedIn;
	/**
	 * The time of flight (in seconds from the start of the simulation).
	 */
	private double flightTime;
	/**
	 * The time to flight.
	 */
	private double timeToFlight;
	/**
	 * The gate area.
	 */
	private GateArea gateArea;
	/**
	 * The desks.
	 */
	private Collection<Desk> checkInDesks;
	/**
	 * The check in queuing area.
	 */
	private QueuingArea checkInQueue;
	/**
	 * The checkpoint queuing area.
	 */
	private QueuingArea checkPointQueue;
	/**
	 * The flight type.
	 */
	private FlightType flightType;

	/**
	 * Creates a new flight.
	 * 
	 * @param flightType
	 *            The flight type.
	 * @param flightTime
	 *            The flight time.
	 * @param gateArea
	 *            The gate area.
	 * @param checkInDesks
	 *            The check in desks.
	 * @param checkInQueue
	 *            The check in queue.
	 * @param checkpointQueue
	 *            The checkpoint queue.
	 */
	public Flight(FlightType flightType, double flightTime, GateArea gateArea, Collection<Desk> checkInDesks,
			QueuingArea checkInQueue, QueuingArea checkpointQueue) {
		this.flightType = flightType;
		this.flightTime = flightTime;
		timeToFlight = flightTime;
		this.checkInQueue = checkInQueue;
		this.checkPointQueue = checkpointQueue;
		this.gateArea = gateArea;
		this.checkInDesks = checkInDesks;
		checkedIn = new ArrayList<>();
	}

	/**
	 * Check if an agent is checked in already.
	 * 
	 * @param agent
	 *            The agent.
	 * @return True if the agent is already checked in, false otherwise.
	 */
	public boolean alreadyCheckedIn(Passenger agent) {
		return checkedIn.contains(agent);
	}

	/**
	 * Check an agent in into the flight.
	 * 
	 * @param agent
	 *            The agent to check in.
	 */
	public void checkIn(Passenger agent) {
		if (agent.getFlight().equals(this) && !checkedIn.contains(agent))
			checkedIn.add(agent);
	}

	/**
	 * Gets the check in desks.
	 * 
	 * @return The check in desks.
	 */
	public Collection<Desk> getCheckInDesks() {
		return checkInDesks;
	}

	/**
	 * Gets the check in queue.
	 * 
	 * @return The check in queue.
	 */
	public QueuingArea getCheckInQueue() {
		return checkInQueue;
	}

	/**
	 * Gets the checkpoint queue.
	 * 
	 * @return The checkpoint queue.
	 */
	public QueuingArea getCheckPointQueue() {
		return checkPointQueue;
	}

	/**
	 * Get the time of flight (in seconds from the start of the simulation).
	 * 
	 * @return The flight time.
	 */
	public double getFlightTime() {
		return flightTime;
	}

	/**
	 * Gets the gate area.
	 * 
	 * @return The gate area.
	 */
	public GateArea getGateArea() {
		return gateArea;
	}

	/**
	 * Gets the time (s) to flight.
	 * 
	 * @return The time to flight.
	 */
	public double getTimeToFlight() {
		return timeToFlight;
	}

	/**
	 * Gets the flight type.
	 * 
	 * @return The type.
	 */
	public FlightType getType() {
		return flightType;
	}

	/**
	 * Checks if the flight is gone.
	 * 
	 * @return True if it is, false if not.
	 */
	public boolean hasLeft() {
		return timeToFlight <= 0;
	}

	@Override
	public String toString() {
		return flightType.toString() + " (" + flightTime + ")";
	}

	@Override
	public void update(int timeStep) {
		timeToFlight -= timeStep / 1000.0;
	}
}