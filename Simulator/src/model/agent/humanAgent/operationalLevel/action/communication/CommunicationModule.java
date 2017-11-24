package model.agent.humanAgent.operationalLevel.action.communication;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
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
	protected MovementModel movementModule;
	/**
	 * The navigation module.
	 */
	protected NavigationModule navigationModule;
	/**
	 * The navigation module.
	 */
	protected ActivityModule activityModule;

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
	 * @param activity
	 *            The activity module.
	 */
	public void init(MovementModel movement, NavigationModule navigation, ActivityModule activity) {
		this.movementModule = movement;
		this.navigationModule = navigation;
		this.activityModule = activity;
	}
}
