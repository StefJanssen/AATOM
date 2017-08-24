package model.agent.humanAgent.operationalLevel;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.operationalLevel.action.communication.BasicPassengerCommunicationModule;
import model.agent.humanAgent.operationalLevel.action.movement.HelbingModel;
import model.agent.humanAgent.operationalLevel.observation.BasicPassengerObservationModel;
import model.environment.map.Map;

/**
 * The Low Level Model handles all low level interactions of a
 * {@link HumanAgent} with its surroundings.
 * 
 * @author S.A.M. Janssen
 *
 */
public class BasicPassengerOperationalModel extends OperationalModel {

	/**
	 * Creates a basic passenger low level model, containing a
	 * {@link HelbingModel} and a {@link BasicPassengerObservationModel}.
	 * 
	 * @param map
	 *            The map.
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public BasicPassengerOperationalModel(Map map, double desiredSpeed) {
		super(new HelbingModel(desiredSpeed), new BasicPassengerObservationModel(map),
				new BasicPassengerCommunicationModule());
	}
}