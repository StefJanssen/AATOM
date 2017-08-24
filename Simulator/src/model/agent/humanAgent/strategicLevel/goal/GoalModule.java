package model.agent.humanAgent.strategicLevel.goal;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import simulation.simulation.util.Updatable;

/**
 * The goal module.
 * 
 * @author S.A.M. Janssen
 */
public abstract class GoalModule implements Updatable {

	/**
	 * The goals.
	 */
	private Collection<Goal> goals;

	/**
	 * Creates the goal module.
	 * 
	 * @param goals
	 *            The goals.
	 */
	public GoalModule(Collection<Goal> goals) {
		this.goals = goals;
	}

	/**
	 * Gets the goal activities.
	 * 
	 * @return The goals.
	 */
	public Collection<Activity> getGoalActivities() {
		Collection<Activity> activities = new ArrayList<>();
		for (Goal g : goals) {
			activities.add(g.getGoalActivity());
		}
		return activities;
	}

	/**
	 * Gets the goals.
	 * 
	 * @return The goals.
	 */
	public Collection<Goal> getGoals() {
		return goals;
	}

	@Override
	public void update(int timeStep) {
		for (Goal g : goals)
			g.update(timeStep);
	}
}
