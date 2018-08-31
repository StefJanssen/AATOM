package model.agent.humanAgent.operationalLevel;

import model.agent.humanAgent.operationalLevel.action.communication.BasicOperatorCommunicationModule;
import model.agent.humanAgent.operationalLevel.action.movement.StaticModel;
import model.agent.humanAgent.operationalLevel.observation.BasicOperatorObservationModel;
import model.agent.humanAgent.tacticalLevel.activity.Activity;

/**
 * The basic operator operational model contains an observation model,
 * communication model and a static movement model.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorOperationalModel extends OperationalModel {

	/**
	 * Creates a basic operator low level model.
	 * 
	 * @param assignment
	 *            The assignment.
	 */
	public BasicOperatorOperationalModel(Activity assignment) {
		super(new StaticModel(), new BasicOperatorObservationModel(), new BasicOperatorCommunicationModule(assignment));
	}
}
