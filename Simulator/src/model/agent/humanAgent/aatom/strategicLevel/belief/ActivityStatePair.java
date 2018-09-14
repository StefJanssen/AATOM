package model.agent.humanAgent.aatom.strategicLevel.belief;

import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.ActivityState;

/**
 * An activity state pair represents the current status of a specific activity.
 * 
 * @author S.A.M. Janssen
 */
public class ActivityStatePair {

	/**
	 * The activity type.
	 */
	private Class<? extends Activity> activity;
	/**
	 * The state.
	 */
	private ActivityState activityState;

	/**
	 * Creates an activity state pair.
	 * 
	 * @param activity
	 *            The activity.
	 * @param activityState
	 *            The state.
	 */
	public ActivityStatePair(Class<? extends Activity> activity, ActivityState activityState) {
		this.activity = activity;
		this.activityState = activityState;
	}

	/**
	 * Gets the activity.
	 * 
	 * @return The activity.
	 */
	public Class<? extends Activity> getActivity() {
		return activity;
	}

	/**
	 * Gets the state.
	 * 
	 * @return The state.
	 */
	public ActivityState getActivityState() {
		return activityState;
	}
}
