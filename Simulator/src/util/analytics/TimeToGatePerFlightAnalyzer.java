package util.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.flight.Flight;
import model.map.Map;

/**
 * A Time To Gate Per Flight Parameter Tracker indicates the average times to
 * gate for the different flights.
 * 
 * @author S.A.M. Janssen
 */
public class TimeToGatePerFlightAnalyzer extends Analyzer implements PassengerAnalyzer {

	/**
	 * The flight schedule.
	 */
	private List<Flight> schedule;
	/**
	 * The passengers.
	 */
	private List<Passenger> passengers;
	/**
	 * The time to gate hashmap.
	 */
	private HashMap<Flight, HashMap<Passenger, Double>> timeToGate;
	/**
	 * The arrival times.
	 */
	private HashMap<Passenger, Double> arrivalTimes;

	@Override
	public void addPassenger(Passenger passenger) {
		passengers.add(passenger);
		arrivalTimes.put(passenger, map.getTime());
	}

	@Override
	public String[] getLineNames() {
		String[] names = new String[schedule.size()];
		for (int i = 0; i < schedule.size(); i++) {
			names[i] = schedule.get(i).toString();
		}
		return names;
	}

	@Override
	public String getTitle() {
		return "Time To Gate";
	}

	@Override
	public double[] getValues() {
		double[] times = new double[schedule.size()];
		for (Passenger p : passengers) {
			if (reachedGate(p)) {
				HashMap<Passenger, Double> currentHash = timeToGate.get(p.getFlight());
				if (!currentHash.containsKey(p)) {
					currentHash.put(p, map.getTime() - arrivalTimes.get(p));
				}
			}
		}

		for (int i = 0; i < schedule.size(); i++) {
			Set<Passenger> set = timeToGate.get(schedule.get(i)).keySet();
			double time = 0;
			for (Passenger p : set)
				time += timeToGate.get(schedule.get(i)).get(p);
			times[i] = time / set.size();
		}

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
		return passenger.getFlight().getGateArea().contains(passenger.getPosition());
	}

	@Override
	public void setMap(Map map) {
		super.setMap(map);
		schedule = new ArrayList<>();
		timeToGate = new HashMap<>();
		passengers = new ArrayList<>();
		arrivalTimes = new HashMap<>();

		for (Flight f : map.getMapComponents(Flight.class)) {
			schedule.add(f);
			timeToGate.put(f, new HashMap<Passenger, Double>());
		}

		for (Passenger p : map.getMapComponents(Passenger.class)) {
			passengers.add(p);
			arrivalTimes.put(p, 0.0);
		}
	}

}
