package model.agent.humanAgent.strategicLevel.goal;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.tacticalLevel.activity.Activity;

/**
 * The goal module for operators.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorGoalModule extends GoalModule {

	/**
	 * Gets the goals for the operator.
	 * 
	 * @param assignment
	 *            The assignment.
	 * @return The goals.
	 */
	private static Collection<Goal> getGoals(Activity assignment) {
		Collection<Goal> goals = new ArrayList<>();
		goals.add(new Goal(assignment, -1));
		return goals;
	}

	/**
	 * Creates the operator goal module.
	 * 
	 * @param assignment
	 *            The assignment.
	 */
	public BasicOperatorGoalModule(Activity assignment) {
		super(getGoals(assignment));
	}

}
