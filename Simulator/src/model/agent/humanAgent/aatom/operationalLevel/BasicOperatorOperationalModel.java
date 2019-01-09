package model.agent.humanAgent.aatom.operationalLevel;

import model.agent.humanAgent.aatom.operationalLevel.action.communication.impl.BasicOperatorCommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.impl.StaticMovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.impl.BasicOperatorObservationModule;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;

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
		super(new StaticMovementModule(), new BasicOperatorObservationModule(),
				new BasicOperatorCommunicationModule(assignment));
	}
}
