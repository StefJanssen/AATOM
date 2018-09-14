package model.agent.humanAgent.aatom.strategicLevel.goal;

import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import simulation.simulation.util.Updatable;

/**
 * A goal represents something that an agent wants. It
 * 
 * @author S.A.M. Janssen
 */
public class Goal implements Updatable {

	/**
	 * The goal activity.
	 */
	private Activity goalActivity;
	/**
	 * The time activity.
	 */
	private Activity timeActivity;
	/**
	 * The time the activity has to be done.
	 */
	private double activityTime;
	/**
	 * The time.
	 */
	private double time;
	/**
	 * The goal state.
	 */
	private GoalState goalState;

	/**
	 * Creates a goal based on another activity.
	 * 
	 * @param activity
	 *            The activity.
	 * @param timeActivity
	 *            The activity it needs to finish before.
	 */
	public Goal(Activity activity, Activity timeActivity) {
		this.goalActivity = activity;
		this.timeActivity = timeActivity;
		time = -1;
	}

	/**
	 * Creates a time based goal.
	 * 
	 * @param activity
	 *            The activity.
	 * @param time
	 *            The time.
	 */
	public Goal(Activity activity, double time) {
		this.goalActivity = activity;
		this.activityTime = time;
	}

	/**
	 * Gets the goal activity.
	 * 
	 * @return The goal activity.
	 */
	public Activity getGoalActivity() {
		return goalActivity;
	}

	/**
	 * Gets the goal state.
	 * 
	 * @return The goal state.
	 */
	public GoalState getGoalState() {
		return goalState;
	}

	@Override
	public void update(int timeStep) {
		if (goalState != GoalState.IN_PROGRESS)
			return;

		if (goalActivity.isFinished()) {
			goalState = GoalState.ACHIEVED;
			return;
		}

		time += timeStep / 1000.0;
		// a time-based goal
		if (time != -1) {
			if (time > activityTime)
				goalState = GoalState.FAILED;
		}
		// an activity-based goal
		else {
			if (timeActivity.isFinished())
				goalState = GoalState.FAILED;
		}
	}
}
