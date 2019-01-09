package model.agent.humanAgent.aatom.strategicLevel;

import model.agent.humanAgent.aatom.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.aatom.strategicLevel.goal.BasicOperatorGoalModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.BasicOperatorPlanner;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;

/**
 * The strategic model for operators.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorStrategicModel extends StrategicModel {

	/**
	 * Creates the strategic model.
	 * 
	 * @param assignment
	 *            The assignment of the agent.
	 */
	public BasicOperatorStrategicModel(Activity assignment) {
		super(new BasicOperatorPlanner(assignment), new BasicOperatorGoalModule(assignment), new BeliefModule());
	}

	@Override
	public boolean wantsToBeDestroyed() {
		return false;
	}
}
