package util.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import model.agent.humanAgent.Passenger;
import simulation.simulation.Simulator;

/**
 * Determines the average time to gate for all passengers.
 * 
 * @author S.A.M. Janssen
 */
public class TimeToGateAnalyzer extends Analyzer implements PassengerAnalyzer {

	/**
	 * The passengers.
	 */
	private List<Passenger> passengers;
	/**
	 * The time to gate hashmap.
	 */
	private HashMap<Passenger, Float> timeToGate;
	/**
	 * The arrival times.
	 */
	private HashMap<Passenger, Float> arrivalTimes;

	@Override
	public void addPassenger(Passenger passenger) {
		passengers.add(passenger);
		arrivalTimes.put(passenger, time);
	}

	@Override
	public String[] getLineNames() {
		return new String[] { "Average time to gate" };
	}

	@Override
	public String getTitle() {
		return "Time To Gate";
	}

	@Override
	public double[] getValues() {
		double[] times = new double[1];
		for (Passenger p : passengers) {
			if (reachedGate(p)) {
				if (!timeToGate.containsKey(p)) {
					timeToGate.put(p, time - arrivalTimes.get(p));
				}
			}
		}

		double time = 0;
		for (Entry<Passenger, Float> p : timeToGate.entrySet())
			time += p.getValue();
		times[0] = time / timeToGate.keySet().size();
		return times;
	}

	@Override
	public String getYAxis() {
		return "Average Time To Gate (s)";
	}

	/**
	 * Determines if the passenger reached its gate.
	 * 
	 * @param passenger
	 *            The passenger.
	 * @return True if it reached the gate, false otherwise.
	 */
	private boolean reachedGate(Passenger passenger) {
		return passenger.getFlight().getGateArea().getShape().contains(passenger.getPosition().x,
				passenger.getPosition().y);
	}

	@Override
	public void setSimulator(Simulator simulator) {
		super.setSimulator(simulator);
		timeToGate = new HashMap<>();
		passengers = new ArrayList<>();
		arrivalTimes = new HashMap<>();
		for (Passenger p : getSimulator().getMap().getMapComponents(Passenger.class)) {
			passengers.add(p);
			arrivalTimes.put(p, 0.0f);
		}
	}

}
