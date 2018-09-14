package model.agent.humanAgent.aatom.tacticalLevel.activity;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.ActivityPlanner;
import model.agent.humanAgent.aatom.tacticalLevel.activity.passenger.CheckpointActivity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.passenger.QueueActivity;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.position.Position;
import model.map.Map;
import simulation.simulation.util.Updatable;

/**
 * The activity module.
 * 
 * @author S.A.M. Janssen
 */
public class ActivityModule implements Updatable {

	/**
	 * All activities of the agent.
	 */
	protected Collection<Activity> activities = new ArrayList<>();
	/**
	 * The queue activity.
	 */
	protected QueueActivity queueActivity;
	/**
	 * The planner.
	 */
	protected ActivityPlanner planner;

	/**
	 * Gets the active activity.
	 * 
	 * @return The active {@link Activity}.
	 */
	public Activity getActiveActivity() {
		if (queueActivity != null) {
			if (queueActivity.isInProgress())
				return queueActivity;
		}

		for (Activity a : activities) {
			if (a.isInProgress())
				return a;
		}
		return null;
	}

	/**
	 * Gets a {@link Collection} of {@link Activity}s.
	 * 
	 * @return The {@link Activity}s.
	 */
	public Collection<Activity> getActivities() {
		return activities;
	}

	/**
	 * Get the next activity position.
	 * 
	 * @return The next activity position.
	 */
	public Position getNextActivityPosition() {
		if (planner.getNextActivity() == null)
			return Position.NO_POSITION;
		return planner.getNextActivity().getActivityPosition();
	}

	/**
	 * 
	 * @param map
	 *            The map.
	 * @param agent
	 *            The agent.
	 * @param movementModel
	 *            The movement model.
	 * @param observationModule
	 *            The observation module.
	 * @param activities
	 *            The activities.
	 * @param planner
	 *            The planner.
	 * @param navigationModule
	 *            The navigation module.
	 */
	public void init(Map map, HumanAgent agent, MovementModule movementModel, ObservationModule observationModule,
			Collection<Activity> activities, ActivityPlanner planner, NavigationModule navigationModule) {
		this.activities = activities;
		this.planner = planner;

		for (Activity activity : activities)
			activity.init(map, agent, movementModel, observationModule, navigationModule, this);

		if (agent instanceof Passenger) {
			queueActivity = new QueueActivity();
			queueActivity.init(map, agent, movementModel, observationModule, navigationModule, this);
		}
	}

	/**
	 * Determines if the agent is queuing.
	 * 
	 * @return True if he is queuing, false otherwise.
	 */
	public boolean isQueuing() {
		if (queueActivity == null)
			return false;
		return queueActivity.isInProgress();
	}

	/**
	 * Sets the agent in front of the queue.
	 */
	public void setInFrontOfQueue() {
		if (queueActivity == null)
			return;
		queueActivity.setInFrontOfQueue();
	}

	/**
	 * Sets the agents queuing mode.
	 * 
	 * @param time
	 *            The time. If the time equals -1, the agent queues
	 *            indefinitely.
	 */
	public void setQueuing(double time) {
		if (queueActivity == null)
			return;
		queueActivity.startActivity(time);
	}

	/**
	 * Specifies that the agent needs to be searched. Only needed in the
	 * passenger checkpoint activity.
	 * 
	 * @param searchTime
	 *            The time to search.
	 */
	public void setSearch(double searchTime) {
		for (Activity a : activities) {
			if (a instanceof CheckpointActivity) {
				((CheckpointActivity) a).setSearch(searchTime);
			}
		}
	}

	@Override
	public void update(int timeStep) {
		if (queueActivity != null) {
			// if queue activity is active, update and return.
			if (queueActivity.isInProgress()) {
				queueActivity.update(timeStep);
				if (!queueActivity.isInFrontOfQueue())
					return;
			}
			// check if the queue activity can start and return.
			else if (queueActivity.canStart(timeStep)) {
				queueActivity.startActivity();
				return;
			}
		}

		// Do the current activity
		Activity curr = planner.getNextActivity();

		if (curr != null) {
			if (curr.isInProgress()) {
				curr.update(timeStep);
				return;
			}

			if (curr.canStart(timeStep)) {
				curr.startActivity();
				if (queueActivity != null) {
					if (queueActivity.isInProgress())
						queueActivity.endActivity();
				}
				return;
			}

			curr.goToActivity();
		}
	}

}
