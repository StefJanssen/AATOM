package model.agent.humanAgent.aatom.operationalLevel.action.communication.impl;

import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.environment.position.Position;

/**
 * The passenger communication module.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerCommunicationModule extends CommunicationModule {

	@Override
	public void communicate(CommunicationType type, Object communication) {
		if (type.equals(CommunicationType.WAIT)) {
			if (communication instanceof Double)
				movementModule.setStopOrder((Double) communication);
			if (communication instanceof Integer)
				movementModule.setStopOrder((Integer) communication);
		} else if (type.equals(CommunicationType.GOTO)) {
			if (communication instanceof Position)
				navigationModule.setGoal((Position) communication);
		} else if (type.equals(CommunicationType.SEARCH)) {
			if (communication instanceof Double)
				activityModule.setSearch((Double) communication);
			if (communication instanceof Integer)
				activityModule.setSearch((Integer) communication);
		}
	}
}
