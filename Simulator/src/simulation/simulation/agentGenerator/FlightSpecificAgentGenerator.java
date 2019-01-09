package simulation.simulation.agentGenerator;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.area.EntranceArea;
import model.environment.objects.area.Facility;
import model.environment.objects.area.Restaurant;
import model.environment.objects.area.Shop;
import model.environment.objects.area.Toilet;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.util.Utilities;

/**
 * The base agent generator generates passengers in random intervals.
 * 
 * @author S.A.M. Janssen
 */
public class FlightSpecificAgentGenerator extends AgentGenerator {

	/**
	 * The area in which the agent is generated.
	 */
	private List<EntranceArea> areas;
	/**
	 * The percentage of passengers arriving.
	 */
	private double[] percentageOfPassengers;
	/**
	 * The earliest time passengers arrive.
	 */
	private double earliestTime;
	/**
	 * The last time passengers arrive.
	 */
	private double latestTime;

	/**
	 * Creates a flight specific agent generator.
	 * 
	 * @param percentageOfPassengers
	 *            The array specifying the percentage of passengers arrive at
	 *            what time. Sum should be equal to 1. If the array contains the
	 *            values {0.3,0.7}, this means that 30% of passengers arrive in
	 *            the first half of the window, while 70% of passengers arrive
	 *            in the second half of the window- where the window is defined
	 *            as the time between {@link #earliestTime} and
	 *            {@link #latestTime}.
	 * @param earliestTime
	 *            The earliest time passengers arrive (counted from the
	 *            departure of the flight). If the time is 7200, this means that
	 *            passengers start to arrive at 7200 seconds before the flight.
	 * @param latestTime
	 *            The latest time passengers arrive (counted from the departure
	 *            of the flight). If the time is 1800, this means that
	 *            passengers arrive until 1800 seconds before the flight.
	 */
	public FlightSpecificAgentGenerator(double[] percentageOfPassengers, double earliestTime, double latestTime) {
		double sum = 0;
		for (double d : percentageOfPassengers)
			sum += d;

		if (Math.abs(sum - 1) > 0.01)
			throw new RuntimeException("The sum of the percentages of people should equal 1.");

		if (earliestTime < latestTime)
			throw new RuntimeException("The earliest time should be before the latest time. "
					+ "This means that the value should be HIGHER than the latest time value.");

		this.percentageOfPassengers = percentageOfPassengers;
		this.earliestTime = earliestTime;
		this.latestTime = latestTime;
	}

	/**
	 * Determines if the agent is generated.
	 * 
	 * @param timeStep
	 *            The time step.
	 * @param flight
	 *            The flight.
	 * 
	 * @return True if it is, false otherwise.
	 */
	protected boolean canGenerate(double timeStep, Flight flight) {

		if (flight.getTimeToFlight() > earliestTime || flight.getTimeToFlight() < latestTime)
			return false;

		// the number of buckets in which passengers are generated
		int numberOfBuckets = percentageOfPassengers.length;
		// the size of the window in which passengers arrive (in seconds)
		double window = earliestTime - latestTime;
		// the size of the buckets (in seconds)
		double bucketSize = window / numberOfBuckets;

		// define border values
		double[] borderValues = new double[numberOfBuckets - 1];
		for (int i = 0; i < borderValues.length; i++) {
			borderValues[i] = earliestTime - (i + 1) * bucketSize;

		}

		// determine bucket
		int bucket = 0;
		for (int i = 0; i < borderValues.length; i++) {
			if (flight.getTimeToFlight() > borderValues[i]) {
				break;
			} else
				bucket++;
		}

		// the number of passengers
		int numberOfPassengers = flight.getFlightSize();

		// the interarrival time
		double interArrivalTime = bucketSize / (percentageOfPassengers[bucket] * numberOfPassengers);

		// poission distributed variable
		return Utilities.RANDOM_GENERATOR.nextDouble() < 1.0 / interArrivalTime / (1000.0 / timeStep);
	}

	@Override
	public List<HumanAgent> generateAgent(long numberOfSteps, int timeStep, boolean forced) {

		List<HumanAgent> list = new ArrayList<>();

		for (Flight f : getEligibleFlights()) {
			if (forced || canGenerate(timeStep, f)) {
				Luggage luggage = new Luggage(LuggageType.CARRY_ON, Utilities.RANDOM_GENERATOR.nextDouble(),
						Utilities.RANDOM_GENERATOR.nextDouble());
				if (areas.isEmpty())
					return null;
				EntranceArea area = areas.get(Utilities.RANDOM_GENERATOR.nextInt(areas.size()));
				Position start = area.generatePosition();
				Passenger p = new Passenger.Builder<>().setFlight(f)
						.setCheckedIn(Utilities.RANDOM_GENERATOR.nextBoolean()).setFacility(getFacility())
						.setPosition(start).setLuggage(luggage).build();
				list.add(p);
			}
		}
		return list;
	}

	/**
	 * Returns the eligible flights.
	 * 
	 * @return The flight.
	 */
	protected List<Flight> getEligibleFlights() {
		List<Flight> flights = new ArrayList<>();
		for (Flight f : simulator.getMap().getMapComponents(Flight.class)) {
			if (f.getTimeToFlight() >= latestTime && f.getTimeToFlight() <= earliestTime)
				flights.add(f);
		}
		return flights;
	}

	/**
	 * Generates a random facility type.
	 * 
	 * @return The facility.
	 */
	private Class<? extends Facility> getFacility() {
		int rand = Utilities.RANDOM_GENERATOR.nextInt(4);
		if (rand == 0)
			return null;
		if (rand == 1)
			return Shop.class;
		if (rand == 2)
			return Toilet.class;
		return Restaurant.class;
	}

	/**
	 * Sets the simulator.
	 * 
	 * @param simulator
	 *            The simulator.
	 */
	@Override
	public void setSimulator(Simulator simulator) {
		super.setSimulator(simulator);
		areas = new ArrayList<>();
		for (EntranceArea a : simulator.getMap().getMapComponents(EntranceArea.class))
			areas.add(a);
	}
}