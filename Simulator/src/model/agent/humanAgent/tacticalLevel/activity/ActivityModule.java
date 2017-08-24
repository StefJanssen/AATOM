package model.agent.humanAgent.tacticalLevel.activity;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.strategicLevel.reasoning.planning.ActivityPlanner;
import model.agent.humanAgent.tacticalLevel.activity.passenger.QueueActivity;
import model.agent.humanAgent.tacticalLevel.navigation.NavigationModule;
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
	protected Collection<Activity> activities;
	/**
	 * The queue activity.
	 */
	protected QueueActivity queueActivity;
	/**
	 * The planner.
	 */
	protected ActivityPlanner planner;

	/**
	 * Gets a {@link Collection} of active {@link Activity}s.
	 * 
	 * @return The active {@link Activity}s.
	 */
	public Collection<Activity> getActiveActivities() {
		Collection<Activity> activeActivities = new ArrayList<>();
		if (queueActivity != null) {
			if (queueActivity.isInProgress())
				activeActivities.add(queueActivity);
		}

		for (Activity a : activities) {
			if (a.isInProgress())
				activeActivities.add(a);
		}
		return activeActivities;
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
	 * 
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
	public void init(HumanAgent agent, MovementModel movementModel, ObservationModule observationModule,
			Collection<Activity> activities, ActivityPlanner planner, NavigationModule navigationModule) {
		this.activities = activities;
		this.planner = planner;

		for (Activity activity : activities)
			activity.init(agent, movementModel, observationModule, navigationModule, this);

		if (agent instanceof Passenger) {
			queueActivity = new QueueActivity();
			queueActivity.init(agent, movementModel, observationModule, navigationModule, this);
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
