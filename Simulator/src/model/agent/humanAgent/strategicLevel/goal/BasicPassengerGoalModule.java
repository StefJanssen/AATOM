package model.agent.humanAgent.strategicLevel.goal;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.tacticalLevel.activity.passenger.CheckInActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.CheckpointActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.ExitActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.FacilityActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.GateActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.PassengerBorderControlActivity;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;

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
	 * @param flight
	 *            The flight.
	 * @return The goals.
	 */
	private static Collection<Goal> getPassengerGoals(Class<? extends Facility> facility, Flight flight) {
		Collection<Goal> goals = new ArrayList<>();
		if (flight.equals(Flight.NO_FLIGHT))
			return goals;

		if (!flight.getType().equals(FlightType.DEPARTING)) {
			ExitActivity exit = new ExitActivity();
			goals.add(new Goal(exit, -1));
			if (facility != null)
				goals.add(new Goal(new FacilityActivity(facility), exit));
		} else {
			GateActivity gate = new GateActivity(flight);
			CheckpointActivity checkpoint = new CheckpointActivity(flight);
			goals.add(new Goal(new CheckInActivity(flight), checkpoint));
			goals.add(new Goal(checkpoint, gate));
			goals.add(new Goal(new PassengerBorderControlActivity(), gate));
			goals.add(new Goal(gate, flight.getTimeToFlight()));
			if (facility != null)
				goals.add(new Goal(new FacilityActivity(facility), gate));
		}
		return goals;
	}

	/**
	 * Creates the goal module for passenger based on a facility visit and
	 * flight.
	 * 
	 * @param facility
	 *            The facility visit.
	 * @param flight
	 *            The flight.
	 */
	public BasicPassengerGoalModule(Class<? extends Facility> facility, Flight flight) {
		super(getPassengerGoals(facility, flight));
	}

}
