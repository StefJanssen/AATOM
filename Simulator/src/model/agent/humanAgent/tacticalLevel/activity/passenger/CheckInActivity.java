package model.agent.humanAgent.tacticalLevel.activity.passenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.Desk;

/**
 * The check in activity is responsible for checking in an agent.
 * 
 * @author S.A.M. Janssen
 */
public class CheckInActivity extends Activity {

	/**
	 * The flight of the agent.
	 */
	private Flight flight;
	/**
	 * The serving desk.
	 */
	private Desk servingDesk;
	/**
	 * Agent waited.
	 */
	private double waitingTimeLeft = 8;

	/**
	 * Creates a check-in activity.
	 * 
	 * @param flight
	 *            The flight.
	 */
	public CheckInActivity(Flight flight) {
		this.flight = flight;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isFinished())
			return false;

		// we are at the right position
		if (flight.getCheckInQueue().getLeavingPosition().distanceTo(movement.getPosition()) < 0.6) {

			// We should wait for agents to reach their desks of they are on
			// their way
			if (waitingTimeLeft == 8) {
				activityModule.setQueuing(8);
				activityModule.setInFrontOfQueue();
			}

			// If we're waiting, we cannot start yet.
			if (waitingTimeLeft > 0) {
				waitingTimeLeft -= timeStep / 1000.0;
				return false;
			}

			// Determine if the desks are occupied
			List<Desk> desks = getClosestDesks();
			for (Desk desk : desks) {
				// not occupied
				if (!desk.isOccupied()) {
					servingDesk = desk;
					activityModule.setQueuing(0);
					return true;
				}
			}

			// all desks occupied
			activityModule.setInFrontOfQueue();
		}
		return false;
	}

	/**
	 * Gets the closest desk.
	 * 
	 * @return The closest desk.
	 */
	private List<Desk> getClosestDesks() {
		Collection<Desk> desks = observations.getObservation(Desk.class);
		List<Desk> closestDesks = new ArrayList<>();
		if (desks.isEmpty())
			return closestDesks;

		for (Desk desk : desks) {
			if (closestDesks.size() == 0) {
				closestDesks.add(desk);
			} else {
				int position = 0;
				for (int i = 0; i < closestDesks.size(); i++) {
					Desk curr = closestDesks.get(i);
					if (desk.getServingPosition().distanceTo(movement.getPosition()) > curr.getServingPosition()
							.distanceTo(movement.getPosition())) {
						position = i + 1;
					} else
						break;
				}
				closestDesks.add(position, desk);
			}
		}
		return closestDesks.subList(0, flight.getCheckInDesks().size());
	}

	@Override
	public void goToActivity() {
		if (isFinished())
			return;

		if (isGoingToActivity())
			return;
		navigationModule.setGoal(flight.getCheckInQueue().getLeavingPosition());
		super.goToActivity();
		return;
	}

	@Override
	public void startActivity() {
		super.startActivity();

		// Go to the available desk
		activityModule.setQueuing(0);
		navigationModule.setGoal(servingDesk.getServingPosition());
		servingDesk.setAgentAtDesk((Passenger) agent);
	}

	@Override
	public void update(int timeStep) {
		// check if our waiting period is over & we are checked in
		if (!movement.getStopOrder() && flight.alreadyCheckedIn((Passenger) agent)) {
			endActivity();
		}
	}
}