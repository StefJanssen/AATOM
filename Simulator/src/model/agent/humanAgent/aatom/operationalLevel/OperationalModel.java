package model.agent.humanAgent.aatom.operationalLevel;

import java.util.Collection;

import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.objects.physicalObject.Chair;
import model.environment.position.Vector;
import model.map.Map;
import simulation.simulation.util.Updatable;

/**
 * A low level model handles observations and movement of an agent.
 * 
 * @author S.A.M. Janssen
 */
public abstract class OperationalModel implements Updatable {

	/**
	 * The movement model.
	 */
	private MovementModule movementModel;
	/**
	 * The observation module.
	 */
	private ObservationModule observationModule;
	/**
	 * The communication module.
	 */
	private CommunicationModule communicationModule;

	/**
	 * @param movementModel
	 *            The movement model.
	 * @param observationModule
	 *            The observation module.
	 * @param communicationModule
	 *            The communication module.
	 */
	public OperationalModel(MovementModule movementModel, ObservationModule observationModule,
			CommunicationModule communicationModule) {
		this.movementModel = movementModel;
		this.observationModule = observationModule;
		this.communicationModule = communicationModule;
	}

	/**
	 * Communicate.
	 * 
	 * @param type
	 *            The type of communication.
	 * @param communication
	 *            The communication.
	 */
	public void communicate(CommunicationType type, Object communication) {
		communicationModule.communicate(type, communication);
	}

	/**
	 * Gets the current velocity.
	 * 
	 * @return The current velocity.
	 */
	public Vector getCurrentVelocity() {
		return movementModel.getCurrentVelocity();
	}

	/**
	 * Gets the desired speed.
	 * 
	 * @return The desired speed.
	 */
	public double getDesiredSpeed() {
		return movementModel.getDesiredSpeed();
	}

	/**
	 * Gets the next move given a certain time step.
	 * 
	 * @param timeStep
	 *            The time step.
	 * 
	 * @return The move.
	 */
	public Vector getMove(int timeStep) {
		if (getStopOrder())
			return new Vector(0, 0);
		return movementModel.getMove(timeStep);
	}

	/**
	 * Gets the movement model.
	 * 
	 * @return The movement model.
	 */
	public MovementModule getMovementModel() {
		return movementModel;
	}

	/**
	 * Gets an observation of a specific type. An observation always returns a
	 * collection of map components.
	 * 
	 * @param type
	 *            The map component.
	 * @param <T>
	 *            The type of observation.
	 * @return The collection that is observed.
	 */
	public <T> Collection<T> getObservation(Class<T> type) {
		return observationModule.getObservation(type);
	}

	/**
	 * Gets the observation module.
	 * 
	 * @return The observation module.
	 */
	public ObservationModule getObservationModule() {
		return observationModule;
	}

	/**
	 * Indicates if the agent has a stop order. It will also make sure that the
	 * agent stops if someone in its very close neighborhood has a stop order.
	 * 
	 * @return True if he is ordered to stop, false otherwise.
	 */
	public boolean getStopOrder() {
		return movementModel.getStopOrder();
	}

	/**
	 * Initializes the {@link AatomHumanAgent}.
	 * 
	 * @param map
	 *            The map.
	 * @param agent
	 *            The agent.
	 * @param navigationModule
	 *            The navigation module.
	 * @param activityModule
	 *            The activity module.
	 */
	public void init(Map map, AatomHumanAgent agent, NavigationModule navigationModule, ActivityModule activityModule) {
		movementModel.init(agent, observationModule);
		observationModule.init(map, movementModel);
		communicationModule.init(movementModel, navigationModule, activityModule);
	}

	/**
	 * Determines if the agent is sitting.
	 * 
	 * @return True if the agent is sitting, false otherwise.
	 */
	public boolean isSitting() {
		return movementModel.getChair() != null;
	}

	/**
	 * Asks the agent to sit down on a specific {@link Chair}. Returns true if
	 * successful, false otherwise.
	 * 
	 * @param chair
	 *            The chair.
	 * @return true if successful, false otherwise.
	 * 
	 */
	public boolean setSitDown(Chair chair) {
		return movementModel.setSitDown(chair);
	}

	/**
	 * Sets a flag that indicates if the agent is ordered to stop.
	 * 
	 * @param stopOrder
	 *            The time of the stop order.
	 */
	public void setStopOrder(double stopOrder) {
		movementModel.setStopOrder(stopOrder);
	}

	@Override
	public void update(int timeStep) {
		movementModel.update(timeStep);
	}

}
