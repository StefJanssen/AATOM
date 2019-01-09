package model.agent.humanAgent.aatom;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.tacticalLevel.TacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.environment.objects.physicalObject.Chair;
import model.environment.position.Position;
import model.environment.position.Vector;

/**
 * A AatomHumanAgent is a human that can move around on a map. It contains three
 * main elements: a {@link StrategicModel}, a {@link TacticalModel}, and a
 * {@link OperationalModel}. The {@link StrategicModel} determines the higher
 * level plans of the agent. It for instance determines its goals and the
 * preferred speed it wants to walk. The {@link TacticalModel} provides tactical
 * decisions, for instance on the execution of activities. Finally, the
 * {@link OperationalModel} determines the lower level interactions with the
 * surroundings.
 * 
 * The AatomHumanAgent acts autonomously and bases its decisions on its
 * surroundings and its internal (high level) state.
 * 
 * @author S.A.M. Janssen
 */
public abstract class AatomHumanAgent extends HumanAgent {

	/**
	 * The {@link StrategicModel}.
	 */
	protected StrategicModel strategicModel;
	/**
	 * The {@link TacticalModel}.
	 */
	protected TacticalModel tacticalModel;
	/**
	 * The {@link OperationalModel}.
	 */
	protected OperationalModel operationalModel;

	/**
	 * Creates a human agent with a specified {@link StrategicModel} and
	 * {@link OperationalModel} with default color red.
	 * 
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param strategicModel
	 *            The strategic model.
	 * @param tacticalModel
	 *            The tactical model.
	 * @param operationalModel
	 *            The operational model.
	 * 
	 */
	public AatomHumanAgent(Position position, double radius, double mass, StrategicModel strategicModel,
			TacticalModel tacticalModel, OperationalModel operationalModel) {
		this(position, radius, mass, strategicModel, tacticalModel, operationalModel, Color.RED);
	}

	/**
	 * Creates a human agent.
	 * 
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param strategicModel
	 *            The strategic model.
	 * @param tacticalModel
	 *            The tactical model.
	 * @param operationalModel
	 *            The operational model.
	 * @param color
	 *            The color.
	 */
	public AatomHumanAgent(Position position, double radius, double mass, StrategicModel strategicModel,
			TacticalModel tacticalModel, OperationalModel operationalModel, Color color) {
		super(position, radius, mass, color);
		if (strategicModel == null)
			throw new IllegalArgumentException("Strategic model cannot be null");
		if (tacticalModel == null)
			throw new IllegalArgumentException("Tactical model cannot be null");
		if (operationalModel == null)
			throw new IllegalArgumentException("Operational model cannot be null");

		this.strategicModel = strategicModel;
		this.tacticalModel = tacticalModel;
		this.operationalModel = operationalModel;
	}

	/**
	 * Communicate.
	 * 
	 * @param type
	 *            The type of communication.
	 * @param communication
	 *            The communication.
	 */
	@Override
	public void communicate(CommunicationType type, Object communication) {
		operationalModel.communicate(type, communication);
	}

	/**
	 * Gets a {@link Collection} of active {@link Activity}s.
	 * 
	 * @return The active {@link Activity}s.
	 */
	public Activity getActiveActivity() {
		return tacticalModel.getActiveActivity();
	}

	/**
	 * Gets the current velocity from the {@link OperationalModel}.
	 * 
	 * @return The current velocity.
	 */
	public Vector getCurrentVelocity() {
		return operationalModel.getCurrentVelocity();
	}

	/**
	 * Gets the desired speed.
	 * 
	 * @return The desired speed.
	 */
	public double getDesiredSpeed() {
		return operationalModel.getDesiredSpeed();
	}

	/**
	 * Gets the goal {@link Position} from the {@link StrategicModel}.
	 * 
	 * @return The goal position.
	 */
	public Position getGoalPosition() {
		return tacticalModel.getGoalPosition();
	}

	/**
	 * Gets a list of all goal {@link Position}s from the {@link StrategicModel}
	 * .
	 * 
	 * @return The goal position.
	 */
	public List<Position> getGoalPositions() {
		return tacticalModel.getGoalPositions();
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
	@Override
	protected <T> Collection<T> getObservation(Class<T> type) {
		return operationalModel.getObservation(type);
	}

	/**
	 * Check if we reached the goal {@link Position}.
	 * 
	 * @return True if we reached the goal, false otherwise.
	 */
	public boolean getReachedGoal() {
		return tacticalModel.getReachedGoal();
	}

	/**
	 * Indicates if the agent has a stop order.
	 * 
	 * @return True if he is ordered to stop, false otherwise.
	 */
	public boolean getStopOrder() {
		return operationalModel.getStopOrder();
	}

	@Override
	public void init() {
		operationalModel.init(map, this, tacticalModel.getNavigationModule(), tacticalModel.getActivityModule());
		strategicModel.init(operationalModel.getObservationModule());
		tacticalModel.init(map, this, operationalModel.getMovementModel(), operationalModel.getObservationModule(),
				strategicModel.getActivityPlanner(), strategicModel.getGoalModule().getGoalActivities());
	}

	/**
	 * Determines if the agent is queuing.
	 * 
	 * @return True if he is queuing, false otherwise.
	 */
	public boolean isQueuing() {
		return tacticalModel.isQueuing();
	}

	/**
	 * Determines if the agent is sitting.
	 * 
	 * @return True if the agent is sitting, false otherwise.
	 */
	public boolean isSitting() {
		return operationalModel.isSitting();
	}

	/**
	 * Update the agent's position and internal representations using the
	 * generated move of the {@link OperationalModel}, the
	 * {@link StrategicModel#update(int)} and {@link TacticalModel#update(int)}
	 * methods.
	 * 
	 * @param timeStep
	 *            The used time step (in milliseconds) in the simulation.
	 */
	@Override
	public void update(int timeStep) {
		super.update(timeStep);
		// think first...
		strategicModel.update(timeStep);
		tacticalModel.update(timeStep);

		// then act...
		operationalModel.update(timeStep);
		Vector move = operationalModel.getMove(timeStep);
		position = new Position(position.x + move.x, position.y + move.y);

		// sitting routine
		if (operationalModel.isSitting()) {
			tacticalModel.setGoal(Position.NO_POSITION);
			Chair chair = operationalModel.getMovementModel().getChair();
			position = new Position(chair.getPosition().x + 0.5 * chair.getWidth(),
					chair.getPosition().y + 0.5 * chair.getWidth());
		}

	}

	@Override
	public boolean wantsToBeDestoryed() {
		return strategicModel.wantsToBeDestroyed();
	}
}