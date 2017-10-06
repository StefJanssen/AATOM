package model.agent.humanAgent.tacticalLevel.activity.passenger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.passenger.CheckpointActivity;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;

/**
 * The checkpoint activity is the activity executed around the checkpoint of an
 * airport.
 * 
 * @author S.A.M. Janssen
 */
public class BasicCheckpointActivity extends CheckpointActivity {

	/**
	 * The x-ray system that the agent is assigned to.
	 */
	private XRaySystem xRaySystem;
	/**
	 * The wtmd.
	 */
	private WalkThroughMetalDetector wtmd;
	/**
	 * The activity position.
	 */
	private Position activityPosition;
	/**
	 * Waiting time left.
	 */
	private double waitingTimeLeft = 12;
	/**
	 * The phase.
	 */
	private int phase;
	/**
	 * The time in a specific checkpoint activity phase.
	 */
	private double timeInPhase = 0;

	/**
	 * Creates a checkpoint activity with a specified high level model of an
	 * agent.
	 * 
	 * @param flight
	 *            The flight.
	 * 
	 */
	public BasicCheckpointActivity(Flight flight) {
		this.activityPosition = flight.getCheckPointQueue().getLeavingPosition();

	}

	@Override
	public boolean canStart(int timeStep) {
		if (movement.getPosition().distanceTo(activityPosition) < 0.5) {
			// We should wait for agents to reach their x-ray machine if they
			// are on their way
			if (waitingTimeLeft == 12) {
				activityModule.setQueuing(12);
				activityModule.setInFrontOfQueue();
			}

			// If we're waiting, we cannot start yet.
			if (waitingTimeLeft > 0) {
				waitingTimeLeft -= timeStep / 1000.0;
				return false;
			}

			// Get the closest desks
			List<XRaySystem> systems = getClosestSystems();
			for (XRaySystem system : systems) {
				if (system.getDropOffPassenger() == null) {
					// not occupied
					xRaySystem = system;
					activityModule.setQueuing(0);
					return true;
				}
			}
			// all systems occupied
			activityModule.setQueuing(10);

		}
		return false;
	}

	/**
	 * Gets the activity position.
	 * 
	 * @return The activity position.
	 */
	@Override
	public Position getActivityPosition() {
		return activityPosition;
	}

	/**
	 * Gets the closest systems.
	 * 
	 * @return The closest systems.
	 */
	private List<XRaySystem> getClosestSystems() {
		Collection<XRaySystem> systems = observations.getObservation(XRaySystem.class);
		List<XRaySystem> closestSystems = new ArrayList<>();
		if (systems.isEmpty())
			return closestSystems;

		// loop through systems
		for (XRaySystem system : systems) {
			if (closestSystems.size() == 0) {
				if (system.isOpen())
					closestSystems.add(system);
			} else {
				int position = 0;
				for (int i = 0; i < closestSystems.size(); i++) {
					XRaySystem curr = closestSystems.get(i);
					if (system.getDropOffPosition().distanceTo(movement.getPosition()) > curr.getDropOffPosition()
							.distanceTo(movement.getPosition())) {
						position = i + 1;
					} else
						break;
				}
				if (system.isOpen())
					closestSystems.add(position, system);
			}
		}
		return closestSystems.subList(0, closestSystems.size()); 
	}

	/**
	 * Gets the closest systems.
	 * 
	 * @return The closest systems.
	 */
	private WalkThroughMetalDetector getClosestWTMD() {
		Collection<WalkThroughMetalDetector> systems = observations.getObservation(WalkThroughMetalDetector.class);
		List<WalkThroughMetalDetector> closestSystems = new ArrayList<>();
		if (systems.isEmpty())
			return null;

		for (WalkThroughMetalDetector desk : systems) {
			if (closestSystems.size() == 0) {
				closestSystems.add(desk);
			} else {
				int position = 0;
				for (int i = 0; i < closestSystems.size(); i++) {

					WalkThroughMetalDetector curr = closestSystems.get(i);
					if (desk.getPosition().distanceTo(movement.getPosition()) > curr.getPosition()
							.distanceTo(movement.getPosition())) {
						position = i + 1;
					} else
						break;
				}
				closestSystems.add(position, desk);
			}
		}
		return closestSystems.get(0);
	}

	@Override
	public void goToActivity() {
		if (isFinished())
			return;

		if (isInProgress())
			return;

		if (isGoingToActivity())
			return;

		super.goToActivity();
		navigationModule.setGoal(activityPosition);
		return;
	}

	@Override
	public void startActivity() {
		super.startActivity();
		activityModule.setQueuing(0);
		navigationModule.setGoal(xRaySystem.getDropOffPosition());
		xRaySystem.setDropOffPassenger((Passenger) agent);
		phase++;
	}

	@Override
	public void update(int timeStep) {
		timeInPhase += timeStep / 1000.0;

		// waiting
		if (!movement.getStopOrder() && phase == 1
				&& movement.getPosition().distanceTo(xRaySystem.getDropOffPosition()) < 0.4) {

			if (wtmd == null)
				wtmd = getClosestWTMD();

			if (wtmd != null && navigationModule.getReachedGoal()) {
				if (wtmd.canGo()) {
					// go to wtmd
					xRaySystem.setDropOffPassenger(null);
					for (Luggage l : ((Passenger) agent).getLuggage()) {
						xRaySystem.addBaggage(l);
					}
					wtmd.setPersonsInConsideration((Passenger) agent);
					navigationModule.setGoal(wtmd.getPosition());
					timeInPhase = 0;
					phase++;
				}
			}
		}
		if ((timeInPhase > 120 || !movement.getStopOrder()) && phase == 2) {
			// go to luggage collect area
			if (navigationModule.getReachedGoal() && xRaySystem.getCollectPassenger() == null) {
				movement.setStopOrder(0);
				navigationModule.setGoal(xRaySystem.getCollectPosition());
				xRaySystem.setCollectPassenger((Passenger) agent);
				timeInPhase = 0;
				phase++;
			}
		}
		if (phase == 3 && movement.getPosition().distanceTo(wtmd.getCheckPosition()) < 0.5
				&& !((Passenger) agent).getStopOrder()) {
			navigationModule.setGoal(xRaySystem.getCollectPosition());
			xRaySystem.setCollectPassenger((Passenger) agent);
		}
		if (!movement.getStopOrder() && phase == 3
				&& movement.getPosition().distanceTo(xRaySystem.getCollectPosition()) < 0.4) {
			xRaySystem.setCollectPassenger(null);
			for (Luggage l : ((Passenger) agent).getLuggage()) {
				xRaySystem.removeBaggage(l);
			}
			endActivity();
		}
	}
}