package model.agent.humanAgent.aatom.tacticalLevel.activity;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.position.Position;
import model.map.Map;
import simulation.simulation.util.Updatable;

/**
 * An activity is something that an agent can do. An activity will be updated
 * every simulation step and internally updates its state based on its
 * environment.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Activity implements Updatable {

	/**
	 * The movement model.
	 */
	protected MovementModule movement;
	/**
	 * The activity state.
	 */
	private ActivityState activityState = ActivityState.NOT_STARTED;
	/**
	 * Flag to indicate that the agent is going to the activity.
	 */
	private boolean goingToActivity;
	/**
	 * The observation module.
	 */
	protected ObservationModule observations;
	/**
	 * The agent.
	 */
	protected HumanAgent agent;
	/**
	 * The navigation module.
	 */
	protected NavigationModule navigationModule;
	/**
	 * The activity module.
	 */
	protected ActivityModule activityModule;
	/**
	 * The map.
	 */
	protected Map map;

	/**
	 * Determines if the activity can start.
	 * 
	 * @param timeStep
	 *            The time step.
	 * @return True if it wants to start, false otherwise.
	 */
	public abstract boolean canStart(int timeStep);

	/**
	 * Starts the activity.
	 */
	public void endActivity() {
		activityState = ActivityState.FINISHED;
	}

	/**
	 * Gets the activity position.
	 * 
	 * @return The position.
	 */
	public abstract Position getActivityPosition();

	/**
	 * Gets the activity state.
	 * 
	 * @return The activity state.
	 */
	public ActivityState getActivityState() {
		return activityState;
	}

	/**
	 * Sends the agent to the activity area.
	 * 
	 */
	public void goToActivity() {
		goingToActivity = true;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param map
	 *            The map.
	 * @param agent
	 *            The agent
	 * @param movement
	 *            The movement model.
	 * @param observations
	 *            The observation module.
	 * @param navigationModule
	 *            The navigation module.
	 * @param activityModule
	 *            The activity module.
	 */
	public void init(Map map, HumanAgent agent, MovementModule movement, ObservationModule observations,
			NavigationModule navigationModule, ActivityModule activityModule) {
		this.map = map;
		this.agent = agent;
		this.navigationModule = navigationModule;
		this.movement = movement;
		this.observations = observations;
		this.activityModule = activityModule;
	}

	/**
	 * Determines if the activity is done.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isFinished() {
		return activityState.equals(ActivityState.FINISHED);
	}

	/**
	 * Flag to indicate that the agent is going to an activity.
	 * 
	 * @return True if it is, false otherwise.
	 */
	protected boolean isGoingToActivity() {
		if (navigationModule.getReachedGoal())
			return false;
		return goingToActivity;
	}

	/**
	 * Indicates if the activity is in progress.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isInProgress() {
		return activityState.equals(ActivityState.IN_PROGRESS);
	}

	/**
	 * Starts the activity.
	 */
	public void startActivity() {
		activityState = ActivityState.IN_PROGRESS;
	}
}
