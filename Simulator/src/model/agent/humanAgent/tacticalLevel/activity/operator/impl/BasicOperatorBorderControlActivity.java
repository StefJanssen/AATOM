package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.tacticalLevel.activity.operator.OperatorBorderControlActivity;
import model.environment.position.Position;

/**
 * TODO this activity is not yet implemented.
 * 
 * The operator border control activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorBorderControlActivity extends OperatorBorderControlActivity {

	@Override
	public boolean canStart(int timeStep) {
		return false;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void update(int timeStep) {
	}

}
