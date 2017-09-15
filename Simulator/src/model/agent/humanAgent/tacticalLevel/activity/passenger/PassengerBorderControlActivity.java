package model.agent.humanAgent.tacticalLevel.activity.passenger;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.position.Position;

/**
 * TODO this activity is not yet implemented.
 * 
 * The border control activity.
 * 
 * @author S.A.M. Janssen
 */
public class PassengerBorderControlActivity extends Activity {

	@Override
	public boolean canStart(int timeStep) {
		return false;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void goToActivity() {
		return;
	}

	@Override
	public void update(int timeStep) {
	}

}
