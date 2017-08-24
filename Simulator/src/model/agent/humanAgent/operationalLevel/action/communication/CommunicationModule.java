package model.agent.humanAgent.operationalLevel.action.communication;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.tacticalLevel.navigation.NavigationModule;

/**
 * The communication module contains the communication that an agent can
 * perform.
 * 
 * @author S.A.M. Janssen
 */
public abstract class CommunicationModule {

	/**
	 * The movement model.
	 */
	protected MovementModel movement;
	/**
	 * The navigation module.
	 */
	protected NavigationModule navigation;

	/**
	 * Communicate.
	 * 
	 * @param type
	 *            The type of communication.
	 * @param communication
	 *            The communication.
	 */
	public abstract void communicate(CommunicationType type, Object communication);

	/**
	 * Sets the movement model.
	 * 
	 * @param movement
	 *            The movement model.
	 * @param navigation
	 *            The navigation module.
	 */
	public void init(MovementModel movement, NavigationModule navigation) {
		this.movement = movement;
		this.navigation = navigation;
	}
}
