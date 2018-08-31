package model.agent.humanAgent.strategicLevel.goal;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.tacticalLevel.activity.passenger.CheckpointActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.ExitActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.GateActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.impl.BasicCheckpointActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.impl.BasicExitActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.impl.BasicFacilityActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.impl.BasicGateActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.impl.BasicPassengerBorderControlActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.impl.BasicPassengerCheckInActivity;
import model.environment.objects.area.BorderControlGateArea;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import util.math.distributions.MathDistribution;

/**
 * The goal module for passengers.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerGoalModule extends GoalModule {

	/**
	 * Gets the goals for the passenger.
	 * 
	 * @param facility
	 *            Visits facility or not.
	 * @param checkedIn
	 *            Checked-in or not.
	 * @param checkPointDropTime
	 *            The distribution of checkpoint luggage drop times.
	 * @param checkPointCollectTime
	 *            The distribution of checkpoint luggage collect times.
	 * @param flight
	 *            The flight.
	 * @return The goals.
	 */
	private static Collection<Goal> getPassengerGoals(Class<? extends Facility> facility, boolean checkedIn,
			MathDistribution checkPointDropTime, MathDistribution checkPointCollectTime, Flight flight) {
		Collection<Goal> goals = new ArrayList<>();
		if (flight.equals(Flight.NO_FLIGHT))
			return goals;

		if (flight.getFlightType().equals(FlightType.ARRIVING)) {
			ExitActivity exit = new BasicExitActivity();
			goals.add(new Goal(exit, -1));
			if (facility != null)
				goals.add(new Goal(new BasicFacilityActivity(facility), exit));
		} else {
			GateActivity gate = new BasicGateActivity(flight);
			CheckpointActivity checkpoint = new BasicCheckpointActivity(flight, checkPointDropTime,
					checkPointCollectTime);
			if (!checkedIn)
				goals.add(new Goal(new BasicPassengerCheckInActivity(flight), checkpoint));
			goals.add(new Goal(checkpoint, gate));
			if (flight.getGateArea() instanceof BorderControlGateArea)
				goals.add(new Goal(new BasicPassengerBorderControlActivity(flight), gate));
			goals.add(new Goal(gate, flight.getTimeToFlight()));
			if (facility != null)
				goals.add(new Goal(new BasicFacilityActivity(facility), gate));
		}
		return goals;
	}

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
	public BasicPassengerGoalModule(Class<? extends Facility> facility, boolean checkedIn,
			MathDistribution checkPointDropTime, MathDistribution checkPointCollectTime, Flight flight) {
		super(getPassengerGoals(facility, checkedIn, checkPointDropTime, checkPointCollectTime, flight));
	}
}
