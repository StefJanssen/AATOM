package model.agent.humanAgent.aatom.strategicLevel.belief;

import java.util.List;

import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.environment.objects.area.Area;

/**
 * The belief of an agent.
 * 
 * @author S.A.M. Janssen
 */
public class Belief {

	/**
	 * The location.
	 */
	private Area location;
	/**
	 * The activity states.
	 */
	private List<ActivityStatePair> activities;
	/**
	 * The planning.
	 */
	private List<Activity> planning;

	/**
	 * Creates a belief.
	 * 
	 * @param location
	 *            The location.
	 * @param activities
	 *            The activities.
	 * @param planning
	 *            The planning.
	 */
	public Belief(Area location, List<ActivityStatePair> activities, List<Activity> planning) {
		this.location = location;
		this.activities = activities;
		this.planning = planning;
	}

	/**
	 * Gets the activity states.
	 * 
	 * @return The activity states.
	 */
	public List<ActivityStatePair> getActivities() {
		return activities;
	}

	/**
	 * Gets the location.
	 * 
	 * @return The location.
	 */
	public Area getLocation() {
		return location;
	}

	/**
	 * Gets the planning.
	 * 
	 * @return The planning.
	 */
	public List<Activity> getPlanning() {
		return planning;
	}
}
