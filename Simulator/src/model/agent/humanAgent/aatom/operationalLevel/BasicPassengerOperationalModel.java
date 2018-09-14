package model.agent.humanAgent.aatom.operationalLevel;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.BasicPassengerCommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.HelbingModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.BasicPassengerObservationModel;

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
	 * {@link HelbingModule} and a {@link BasicPassengerObservationModel}.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public BasicPassengerOperationalModel(double desiredSpeed) {
		super(new HelbingModule(desiredSpeed), new BasicPassengerObservationModel(),
				new BasicPassengerCommunicationModule());
	}
}