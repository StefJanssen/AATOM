package model.agent.humanAgent.tacticalLevel.activity.passenger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.passenger.PassengerCheckInActivity;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.util.Utilities;

/**
 * The check in activity is responsible for checking in an agent.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerCheckInActivity extends PassengerCheckInActivity {

	/**
	 * The flight of the agent.
	 */
	private Flight flight;
	/**
	 * The serving desk.
	 */
	private Desk servingDesk;

	/**
	 * Creates a check-in activity.
	 * 
	 * @param flight
	 *            The flight.
	 */
	public BasicPassengerCheckInActivity(Flight flight) {
		this.flight = flight;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isFinished())
			return false;

		// we are at the right position
		if (flight.getCheckInQueue().getLeavingPosition().distanceTo(movement.getPosition()) < 0.6) {
			// set front of queue
			activityModule.setInFrontOfQueue();

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

	@Override
	public Position getActivityPosition() {
		return flight.getCheckInQueue().getLeavingPosition();
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
				if (desk.isOpen())
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
				if (desk.isOpen())
					closestDesks.add(position, desk);
			}
		}
		return closestDesks.subList(0, Math.min(closestDesks.size(), flight.getCheckInDesks().size()));
	}

	/**
	 * Gets a temporary position to prevent getting stuck behind other
	 * passengers.
	 * 
	 * @return The temporary position.
	 */
	private Position getTemporaryPosition() {
		// vector from queue to serving postion
		Vector v = Utilities.getVector(servingDesk.getServingPosition(), flight.getCheckInQueue());
		// other way around and scaled
		v = v.scalarMultiply(-0.8);
		// new position
		return new Position(servingDesk.getServingPosition().x + v.x, servingDesk.getServingPosition().y + v.y);
	}

	@Override
	public void goToActivity() {
		if (isFinished())
			return;

		if (isGoingToActivity())
			return;
		navigationModule.setGoal(getActivityPosition());
		super.goToActivity();
		return;
	}

	@Override
	public void startActivity() {
		super.startActivity();

		// Go to the available desk
		activityModule.setQueuing(0);
		navigationModule.setGoal(servingDesk.getServingPosition());
		navigationModule.setShortTermGoal(getTemporaryPosition());
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