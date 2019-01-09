package model.agent.humanAgent.aatom.strategicLevel.reasoning.planning;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;

/**
 * A basic activity planner for operators.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorPlanner extends PlanningModule {

	/**
	 * The assignment.
	 */
	private final Activity assignment;

	/**
	 * Creates the planner.
	 * 
	 * @param assignment
	 *            The assignment.
	 */
	public BasicOperatorPlanner(Activity assignment) {
		this.assignment = assignment;
	}

	@Override
	public Activity getNextActivity() {
		return assignment;
	}

	@Override
	public List<Activity> getPlanning() {
		ArrayList<Activity> planning = new ArrayList<>();
		planning.add(assignment);
		return planning;
	}

	@Override
	public void update(int timeStep) {
	}
}
