package util.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.flight.Flight;
import model.map.Map;

/**
 * A parameter tracker for missed flights.
 * 
 * @author S.A.M. Janssen
 */
public class MissedFlightsAnalyzer extends Analyzer {

	/**
	 * The flight schedule.
	 */
	private List<Flight> flightSchedule;
	/**
	 * The missed flights.
	 */
	private HashMap<Flight, Integer> missedFlights;

	@Override
	public String[] getLineNames() {
		String[] names = new String[flightSchedule.size()];
		for (int i = 0; i < names.length; i++)
			names[i] = flightSchedule.get(i).toString();
		return names;
	}

	@Override
	public String getTitle() {
		return "Missed flights numbers";
	}

	@Override
	public double[] getValues() {
		double[] values = new double[flightSchedule.size()];
		for (int i = 0; i < flightSchedule.size(); i++) {
			if (missedFlights.containsKey(flightSchedule.get(i)))
				values[i] = missedFlights.get(flightSchedule.get(i));
		}
		return values;
	}

	@Override
	public String getYAxis() {
		return "# of passengers that missed flight";
	}

	/**
	 * Determines if the passenger reached its gate.
	 * 
	 * @param passenger
	 *            The passenger.
	 * @return True if it reached the gate, false otherwise.
	 */
	private boolean reachedGate(Passenger passenger) {
		return passenger.getFlight().getGateArea().getDistance(passenger.getPosition()) < 10;
	}

	@Override
	public void setMap(Map map) {
		super.setMap(map);
		flightSchedule = new ArrayList<>();
		missedFlights = new HashMap<>();
		for (Flight f : map.getMapComponents(Flight.class))
			flightSchedule.add(f);
	}

	@Override
	public void update(int timeStep) {
		for (Flight f : flightSchedule) {
			if (f.hasLeft() && !missedFlights.containsKey(f)) {
				Collection<Passenger> passengers = map.getMapComponents(Passenger.class);
				int misses = 0;
				for (Passenger passenger : passengers) {
					if (passenger.getFlight().equals(f)) {
						if (!reachedGate(passenger)) {
							passenger.setLog(new String[] { "missed Flight", "Flight name " + f.toString(),
									"Passenger position " + passenger.getPosition().toString(),
									"Active activities " + passenger.getActiveActivity(),
									"Gate area corners "
											+ passenger.getFlight().getGateArea().getCorners().toString() });
							misses++;
						}
					}
				}
				missedFlights.put(f, misses);
			}
		}
		super.update(timeStep);
	}
}
