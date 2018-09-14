package util.analytics;

import model.agent.humanAgent.aatom.Passenger;

/**
 * A passenger analyzer analyzes the behaviour of passengers.
 * 
 * @author S.A.M. Janssen
 */
public interface PassengerAnalyzer {

	/**
	 * Adds a passenger to the analyzer.
	 * 
	 * @param passenger
	 *            The passenger.
	 */
	public void addPassenger(Passenger passenger);
}
