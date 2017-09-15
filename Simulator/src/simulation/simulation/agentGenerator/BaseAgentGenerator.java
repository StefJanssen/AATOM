package simulation.simulation.agentGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
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
	public HumanAgent generateAgent(long numberOfSteps, int timeStep, boolean forced) {
		// if we allow agent generation OR if agent generation is forced
		if (forced || canGenerate(timeStep)) {
			Luggage luggage = new Luggage(LuggageType.CARRY_ON, Utilities.RANDOM_GENERATOR.nextDouble(),
					Utilities.RANDOM_GENERATOR.nextDouble());
			if (areas.isEmpty())
				return null;
			EntranceArea area = areas.get(Utilities.RANDOM_GENERATOR.nextInt(areas.size()));
			Position start = Utilities.generatePosition(area.getShape());
			Flight flight = getEligibleFlight();
			if (flight != null) {
				return new Passenger(simulator.getMap(), flight, false, getFacility(), start, 0.2, 80, luggage,
						Color.RED);
			}
		}
		return null;
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