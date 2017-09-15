package model.agent.humanAgent.tacticalLevel.activity.operator;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.position.Position;

/**
 * TODO this activity is not yet implemented.
 * 
 * The operator border control activity.
 * 
 * @author S.A.M. Janssen
 */
public class OperatorBorderControlActivity extends Activity {

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
