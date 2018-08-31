package model.agent.humanAgent.tacticalLevel.activity.passenger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.area.BorderControlGateArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;
import model.environment.position.Vector;

/**
 * The border control activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerBorderControlActivity extends Activity {

	/**
	 * The gate.
	 */
	private BorderControlGateArea gate;
	/**
	 * The serving desk.
	 */
	private Desk servingDesk;
	/**
	 * Agent waited.
	 */
	private double waitingTimeLeft = 0.1;

	/**
	 * Creates a check-in activity.
	 * 
	 * @param flight
	 *            The flight.
	 */
	public BasicPassengerBorderControlActivity(Flight flight) {
		gate = (BorderControlGateArea) flight.getGateArea();
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isFinished())
			return false;

		// we are at the right position
		if (gate.getBorderControlQueue().getLeavingPosition().distanceTo(movement.getPosition()) < 0.6) {

			// We should wait for agents to reach their desks of they are on
			// their way
			if (waitingTimeLeft == 0.1) {
				activityModule.setQueuing(0.1);
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

	@Override
	public Position getActivityPosition() {
		return gate.getBorderControlQueue().getLeavingPosition();
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
		return closestDesks.subList(0, Math.min(closestDesks.size(), gate.getBorderControlDesks().size()));
	}

	/**
	 * Gets a temporary position to prevent getting stuck behind other
	 * passengers.
	 * 
	 * @return The temporary position.
	 */
	private Position getTemporaryPosition() {
		// vector from queue to serving postion
		Vector v = gate.getBorderControlQueue().getVector(servingDesk.getServingPosition());
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
		servingDesk.reserveDeskPosition(agent);
	}

	@Override
	public void update(int timeStep) {
		if (!movement.getStopOrder() && gate.alreadyPassedBorderControl((Passenger) agent)) {
			endActivity();
		}
	}

}
