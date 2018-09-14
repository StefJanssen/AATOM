package model.agent.humanAgent.aatom.strategicLevel;

import model.agent.humanAgent.aatom.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.aatom.strategicLevel.goal.BasicPassengerGoalModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.BasicPassengerPlanner;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;
import util.math.distributions.MathDistribution;

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
	 * Creates the goal module for passenger based on a facility visit,
	 * checked-in and flight.
	 * 
	 * @param facility
	 *            The facility visit.
	 * @param checkedIn
	 *            Checked-in or not.
	 * @param checkPointDropTime
	 *            The distribution of checkpoint luggage drop times.
	 * @param checkPointCollectTime
	 *            The distribution of checkpoint luggage collect times.
	 * @param flight
	 *            The flight.
	 */
	public BasicPassengerStrategicModel(Class<? extends Facility> facility, boolean checkedIn,
			MathDistribution checkPointDropTime, MathDistribution checkPointCollectTime, Flight flight) {
		super(new BasicPassengerPlanner(flight, checkedIn),
				new BasicPassengerGoalModule(facility, checkedIn, checkPointDropTime, checkPointCollectTime, flight),
				new BeliefModule());
		this.flight = flight;
	}

	@Override
	public boolean getWantsToBeRemoved() {
		return flight.hasLeft();
	}
}
