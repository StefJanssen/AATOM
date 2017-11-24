package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.tacticalLevel.activity.operator.LuggageDropActivity;
import model.environment.position.Position;

/**
 * The luggage drop activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicLuggageDropActivity extends LuggageDropActivity {

	@Override
	public boolean canStart(int timeStep) {
		return true;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void update(int timeStep) {
	}
}
