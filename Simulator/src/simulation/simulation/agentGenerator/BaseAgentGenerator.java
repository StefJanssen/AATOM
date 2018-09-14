package simulation.simulation.agentGenerator;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.area.EntranceArea;
import model.environment.objects.area.Facility;
import model.environment.objects.area.Shop;
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
public class BaseAgentGenerator extends AgentGenerator {

	/**
	 * The area in which the agent is generated.
	 */
	protected List<EntranceArea> areas;
	/**
	 * The inter arrival time in seconds.
	 */
	private double interArrivalTime;

	/**
	 * Creates a base agent generator.
	 * 
	 * @param interArrivalTime
	 *            The inter arrival time in seconds.
	 */
	public BaseAgentGenerator(double interArrivalTime) {
		this.interArrivalTime = interArrivalTime;
	}

	/**
	 * Determines if the agent is generated.
	 * 
	 * @param timeStep
	 *            The time step.
	 * 
	 * @return True if it is, false otherwise.
	 */
	protected boolean canGenerate(int timeStep) {
		return Utilities.RANDOM_GENERATOR.nextDouble() < 1.0 / interArrivalTime / (1000.0 / timeStep);
	}

	@Override
	public List<HumanAgent> generateAgent(long numberOfSteps, int timeStep, boolean forced) {
		List<HumanAgent> agents = new ArrayList<>();
		// if we allow agent generation OR if agent generation is forced
		if (forced || canGenerate(timeStep)) {
			Luggage luggage = new Luggage(LuggageType.CARRY_ON, Utilities.RANDOM_GENERATOR.nextDouble(),
					Utilities.RANDOM_GENERATOR.nextDouble());
			if (areas.isEmpty())
				return null;
			EntranceArea area = areas.get(Utilities.RANDOM_GENERATOR.nextInt(areas.size()));
			Position start = area.generatePosition();
			Flight flight = getEligibleFlight();
			if (flight != null) {
				Passenger p = new Passenger.Builder<>().setFlight(flight)
						.setCheckedIn(Utilities.RANDOM_GENERATOR.nextBoolean()).setFacility(getFacility())
						.setPosition(start).setLuggage(luggage).build();
				agents.add(p);
			}
		}
		return agents;
	}

	/**
	 * Returns an eligible flight.
	 * 
	 * @return The flight.
	 */
	protected Flight getEligibleFlight() {
		List<Flight> flights = new ArrayList<>();
		for (Flight f : simulator.getMap().getMapComponents(Flight.class)) {
			if (f.getTimeToFlight() > 1800 && f.getTimeToFlight() < 9000)
				flights.add(f);
		}

		if (flights.size() == 0)
			return null;
		return flights.get(Utilities.RANDOM_GENERATOR.nextInt(flights.size()));
	}

	/**
	 * Generates a random facility type.
	 * 
	 * @return The facility.
	 */
	private Class<? extends Facility> getFacility() {
		int rand = Utilities.RANDOM_GENERATOR.nextInt(2);
		if (rand == 0)
			return null;
		return Shop.class;
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