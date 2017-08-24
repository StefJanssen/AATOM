package model.agent.humanAgent.operationalLevel;

import model.agent.humanAgent.operationalLevel.action.communication.BasicOperatorCommunicationModule;
import model.agent.humanAgent.operationalLevel.action.movement.StaticModel;
import model.agent.humanAgent.operationalLevel.observation.BasicOperatorObservationModel;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.map.Map;

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
	 * @param map
	 *            The map.
	 * @param assignment
	 *            The assignment.
	 */
	public BasicOperatorOperationalModel(Map map, Activity assignment) {
		super(new StaticModel(), new BasicOperatorObservationModel(map),
				new BasicOperatorCommunicationModule(assignment));
	}
}
