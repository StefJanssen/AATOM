package model.agent.humanAgent.aatom.tacticalLevel.activity.passenger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.tacticalLevel.activity.passenger.CheckpointActivity;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import util.math.distributions.MathDistribution;

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
	 * The phase.
	 */
	private int phase;
	/**
	 * The time in a specific checkpoint activity phase.
	 */
	private double timeInPhase = 0;
	/**
	 * The drop off position.
	 */
	private int dropOffPosition = -1;
	/**
	 * The collect position.
	 */
	private int collectPosition = -1;
	/**
	 * Luggage has been dropped or not.
	 */
	private boolean dropped;
	/**
	 * Luggage has been collected or not.
	 */
	private boolean collected;
	/**
	 * The luggage drop time distribution.
	 */
	private MathDistribution dropDistribution;
	/**
	 * The luggage collect time distribution.
	 */
	private MathDistribution collectDistribution;

	/**
	 * Creates a checkpoint activity with a specified high level model of an
	 * agent.
	 * 
	 * @param flight
	 *            The flight.
	 * @param dropDistribution
	 *            The random distribution for drop time.
	 * @param collectDistribution
	 *            The random distribution for collect time.
	 * 
	 */
	public BasicCheckpointActivity(Flight flight, MathDistribution dropDistribution,
			MathDistribution collectDistribution) {
		this.activityPosition = flight.getCheckPointQueue().getLeavingPosition();
		this.dropDistribution = dropDistribution;
		this.collectDistribution = collectDistribution;
	}

	@Override
	public boolean canStart(int timeStep) {
		// we are at the right position
		if (movement.getPosition().distanceTo(activityPosition) < 0.5) {
			// set front of queue
			activityModule.setInFrontOfQueue();

			// Get the closest desks
			List<XRaySystem> systems = getClosestSystems();

			// get the position numbers that are available for each system
			List<Integer> indices = new ArrayList<>();
			for (int i = 0; i < systems.size(); i++) {
				int pos = systems.get(i).getNextDropOffIndex();
				if (pos == -1)
					pos = 10;
				indices.add(pos);
			}

			// find next system
			for (int i = 0; i < systems.get(0).getNumberOfDropoffIndices(); i++) {
				int number = indices.indexOf(i);
				if (number != -1) {
					xRaySystem = systems.get(number);
					dropOffPosition = xRaySystem.getNextDropOffIndex();
					activityModule.setQueuing(0);
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Determines if a collect position is available. Assigns the position if it
	 * is.
	 * 
	 * @return True if there is a position, false otherwise.
	 */
	private boolean collectPositionAvailable() {
		collectPosition = xRaySystem.getNextCollectIndex();
		return collectPosition != -1;
	}

	/**
	 * Drop the luggage at the point and go to the wtmd.
	 */
	private void dropLuggageAndgoToWTMD() {
		// when not waiting anymore
		if (!movement.getStopOrder() && phase == 1
				&& movement.getPosition().distanceTo(xRaySystem.getDropOffPosition(dropOffPosition)) < 0.3) {
			if (wtmd == null)
				wtmd = getClosestWTMD();

			if (wtmd != null && navigationModule.getReachedGoal()) {
				if (wtmd.canGo() && collectPositionAvailable()) {
					// go to wtmd
					xRaySystem.setDropOffPassenger(null, dropOffPosition);
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
	}

	/**
	 * Finish the collect action.
	 */
	private void finishCollect() {
		if (!movement.getStopOrder() && phase == 3
				&& movement.getPosition().distanceTo(xRaySystem.getCollectPosition(collectPosition)) < 0.3) {
			xRaySystem.setCollectPassenger(null, collectPosition);
			for (Luggage l : ((Passenger) agent).getLuggage()) {
				xRaySystem.removeBaggage(l);
			}
			navigationModule.setGoal(xRaySystem.getLeavePosition());
			phase++;
		}

		if (phase == 4 && navigationModule.getReachedGoal()) {
			endActivity();
		}
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
					if (system.getDropOffPosition(0).distanceTo(movement.getPosition()) > curr.getDropOffPosition(0)
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

	/**
	 * Go to the collect position.
	 */
	private void goToCollect() {
		if ((timeInPhase > 120 || !movement.getStopOrder()) && phase == 2) {
			// go to luggage collect area
			if (navigationModule.getReachedGoal() && collectPositionAvailable()) {
				movement.setStopOrder(0);
				navigationModule.setGoal(xRaySystem.getCollectPosition(collectPosition));
				xRaySystem.setCollectPassenger((Passenger) agent, collectPosition);
				timeInPhase = 0;
				phase++;
			}
		}

		// fail safe
		if (phase == 3 && movement.getPosition().distanceTo(wtmd.getCheckPosition()) < 0.3
				&& !((Passenger) agent).getStopOrder()) {
			navigationModule.setGoal(xRaySystem.getCollectPosition(collectPosition));
			xRaySystem.setCollectPassenger((Passenger) agent, collectPosition);
		}
	}

	/**
	 * Go to the system and stop there.
	 */
	private void goToSystemAndStop() {
		// when reached drop off position
		if (phase == 1 && !dropped
				&& movement.getPosition().distanceTo(xRaySystem.getDropOffPosition(dropOffPosition)) < 0.3) {
			dropped = true;
			movement.setStopOrder(dropDistribution.getValue());
		}
	}

	@Override
	public void startActivity() {
		super.startActivity();
		activityModule.setQueuing(0);
		navigationModule.setGoal(xRaySystem.getDropOffPosition(dropOffPosition));
		navigationModule.setShortTermGoal(xRaySystem.getEnterPosition());
		xRaySystem.setDropOffPassenger((Passenger) agent, dropOffPosition);
		phase++;
	}

	@Override
	public void update(int timeStep) {
		timeInPhase += timeStep / 1000.0;

		goToSystemAndStop();
		dropLuggageAndgoToWTMD();
		goToCollect();
		waitAtCollect();
		finishCollect();
	}

	/**
	 * Wait at the collect position.
	 */
	private void waitAtCollect() {
		if (phase == 3 && !collected
				&& movement.getPosition().distanceTo(xRaySystem.getCollectPosition(collectPosition)) < 0.3) {
			collected = true;
			movement.setStopOrder(collectDistribution.getValue() + searchTime);
		}
	}

}