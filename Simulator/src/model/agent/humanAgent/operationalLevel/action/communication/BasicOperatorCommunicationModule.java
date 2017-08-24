package model.agent.humanAgent.operationalLevel.action.communication;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.agent.humanAgent.tacticalLevel.activity.operator.LuggageCheckActivity;
import model.environment.objects.physicalObject.luggage.Luggage;

/**
 * The operator agent communication module.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorCommunicationModule extends CommunicationModule {

	/**
	 * The assignment.
	 */
	private Activity assignment;

	/**
	 * Creates a communication model.
	 * 
	 * @param assignment
	 *            The assignment of the agent.
	 */
	public BasicOperatorCommunicationModule(Activity assignment) {
		this.assignment = assignment;
	}

	@Override
	public void communicate(CommunicationType type, Object communication) {
		if (type.equals(CommunicationType.SEARCH)) {
			if (communication instanceof Luggage) {
				if (assignment instanceof LuggageCheckActivity) {
					((LuggageCheckActivity) assignment).setSearch((Luggage) communication);
				}
			}
		}
	}

}
