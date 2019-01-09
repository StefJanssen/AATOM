package model.agent.humanAgent.aatom.strategicLevel.reasoning.planning;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.aatom.strategicLevel.goal.GoalModule;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import simulation.simulation.util.Updatable;

/**
 * An activity planning
 * 
 * @author S.A.M. Janssen
 */
public abstract class PlanningModule implements Updatable {

	/**
	 * The planning.
	 */
	protected List<Activity> planning;
	/**
	 * The goal module.
	 */
	protected GoalModule goalModule;

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
		if (planning == null)
			planning = new ArrayList<>();
		return planning;
	}

	/**
	 * Initializes the activity planner.
	 * 
	 * @param goalModule
	 *            The goal module.
	 */
	public void init(GoalModule goalModule) {
		this.goalModule = goalModule;
		getPlanning();
	}
}
