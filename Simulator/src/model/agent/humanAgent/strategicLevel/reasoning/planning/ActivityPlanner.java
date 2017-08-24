package model.agent.humanAgent.strategicLevel.reasoning.planning;

import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.strategicLevel.goal.GoalModule;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import simulation.simulation.util.Updatable;

/**
 * An activity planning
 * 
 * @author S.A.M. Janssen
 */
public abstract class ActivityPlanner implements Updatable {

	/**
	 * The planning.
	 */
	protected List<Activity> planning;
	/**
	 * The activities.
	 */
	protected Collection<Activity> activities;

	/**
	 * Gets the next activity.
	 * 
	 * @return The next activity.
	 */
	public abstract Activity getNextActivity();

	/**
	 * Gets the planning.
	 * 
	 * @return The planning.
	 */
	public List<Activity> getPlanning() {
		return planning;
	}

	/**
	 * Initializes the activity planner.
	 * 
	 * @param goalModule
	 *            The goal module.
	 */
	public void init(GoalModule goalModule) {
		this.activities = goalModule.getGoalActivities();
		getPlanning();
	}
}
