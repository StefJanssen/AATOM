package util.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.tacticalLevel.activity.passenger.GateActivity;
import model.environment.position.Position;
import model.map.Map;

/**
 * Tracks the average distance covered by passengers.
 * 
 * @author S.A.M. Janssen
 */
public class DistanceAnalyzer extends Analyzer implements PassengerAnalyzer {

	/**
	 * The map.
	 */
	private List<Passenger> passengers;
	/**
	 * The distance covered.
	 */
	private HashMap<Passenger, Double> distanceCovered;
	/**
	 * The positions of the agents.
	 */
	private HashMap<Passenger, Position> currentPosition;

	@Override
	public void addPassenger(Passenger passenger) {
		passengers.add(passenger);
		distanceCovered.put(passenger, 0.0);
		currentPosition.put(passenger, passenger.getPosition());
	}

	@Override
	public String[] getLineNames() {
		return new String[] { "Average distance" };
	}

	@Override
	public String getTitle() {
		return "Average distance covered";
	}

	@Override
	public double[] getValues() {
		double[] distance = new double[1];
		for (Entry<Passenger, Double> p : distanceCovered.entrySet())
			distance[0] += p.getValue();
		distance[0] /= distanceCovered.keySet().size();
		return distance;
	}

	@Override
	public String getYAxis() {
		return "Average distance (m)";
	}

	/**
	 * Determines if a passenger is performing its gate activity.
	 * 
	 * @param p
	 *            The passenger.
	 * @return True if it is, false otherwise.
	 */
	private boolean isPerformingGateActivity(Passenger p) {
		return p.getActiveActivity() instanceof GateActivity;
	}

	@Override
	public void setMap(Map map) {
		super.setMap(map);
		passengers = new ArrayList<>();
		distanceCovered = new HashMap<>();
		currentPosition = new HashMap<>();
		for (Passenger p : map.getMapComponents(Passenger.class)) {
			passengers.add(p);
			distanceCovered.put(p, 0.0);
			currentPosition.put(p, p.getPosition());
		}
	}

	@Override
	public void update(int timeStep) {
		for (Entry<Passenger, Double> p : distanceCovered.entrySet()) {
			if (!isPerformingGateActivity(p.getKey())) {
				double curr = p.getValue();
				double distance = currentPosition.get(p.getKey()).distanceTo(p.getKey().getPosition());
				distanceCovered.put(p.getKey(), curr + distance);
				currentPosition.put(p.getKey(), p.getKey().getPosition());
			}
		}
		super.update(timeStep);
	}
}
