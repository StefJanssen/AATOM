package model.agent.humanAgent.operationalLevel.action.communication;

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
				movement.setStopOrder((Double) communication);
			if (communication instanceof Integer)
				movement.setStopOrder((Integer) communication);
		} else if (type.equals(CommunicationType.GOTO)) {
			if (communication instanceof Position)
				navigation.setGoal((Position) communication);
		}
	}
}
