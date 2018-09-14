package model.agent.humanAgent.aatom.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.aatom.tacticalLevel.activity.operator.TravelDocumentCheckActivity;
import model.environment.position.Position;

/**
 * TODO this activity is not yet implemented.
 * 
 * The travel document check activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicTravelDocumentCheckActivity extends TravelDocumentCheckActivity {

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
