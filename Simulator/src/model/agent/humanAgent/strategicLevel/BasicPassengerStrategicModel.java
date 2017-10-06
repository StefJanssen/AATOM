package model.agent.humanAgent.strategicLevel;

import model.agent.humanAgent.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.strategicLevel.goal.BasicPassengerGoalModule;
import model.agent.humanAgent.strategicLevel.reasoning.planning.BasicPassengerPlanner;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;

/**
 * The strategic model for passengers.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerStrategicModel extends StrategicModel {

	/**
	 * The flight.
	 */
	private Flight flight;

	/**
	 * Creates the basic passenger strategic model.
	 * 
	 * @param facility
	 *            The facility.
	 * @param flight
	 *            The flight.
	 * @param checkedIn
	 *            Checked in or not.
	 */
	public BasicPassengerStrategicModel(Class<? extends Facility> facility, Flight flight, boolean checkedIn) {
		super(new BasicPassengerPlanner(flight,checkedIn), new BasicPassengerGoalModule(facility, flight), new BeliefModule());
		this.flight = flight;
	}

	@Override
	public boolean getWantsToBeRemoved() {
		return flight.hasLeft();
	}
}
