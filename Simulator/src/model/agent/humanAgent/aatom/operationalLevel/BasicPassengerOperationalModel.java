package model.agent.humanAgent.aatom.operationalLevel;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.impl.BasicPassengerCommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.impl.HelbingMovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.impl.BasicPassengerObservationModule;

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
	 * {@link HelbingMovementModule} and a
	 * {@link BasicPassengerObservationModule}.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public BasicPassengerOperationalModel(double desiredSpeed) {
		super(new HelbingMovementModule(desiredSpeed), new BasicPassengerObservationModule(),
				new BasicPassengerCommunicationModule());
	}
}