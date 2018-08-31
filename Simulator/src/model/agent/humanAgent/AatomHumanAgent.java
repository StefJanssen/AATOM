package model.agent.humanAgent;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.operationalLevel.OperationalModel;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.strategicLevel.StrategicModel;
import model.agent.humanAgent.tacticalLevel.TacticalModel;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.position.Position;
import model.environment.position.Vector;

/**
 * A AatomHumanAgent is a human that can move around on a map. It contains two main
 * elements: a {@link StrategicModel} and a {@link OperationalModel}. The
 * {@link StrategicModel} determines the higher level plans of the agent. It for
 * instance determines its goals and the preferred speed it wants to walk. The
 * {@link OperationalModel} determines the lower level interactions with the
 * surroundings.
 * 
 * The HumanAgent acts autonomously and bases its decisions on its surroundings
 * and its internal (high level) state.
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
	public Collection<Activity> getActiveActivities() {
		return tacticalModel.getActiveActivities();
	}

	/**
	 * Gets the current speed from the {@link OperationalModel}.
	 * 
	 * @return The current speed.
	 */
	public Vector getCurrentSpeed() {
		return operationalModel.getCurrentSpeed();
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
	public <T> Collection<T> getObservation(Class<T> type) {
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

	@Override
	public boolean getWantsToBeRemoved() {
		return strategicModel.getWantsToBeRemoved();
	}

	@Override
	public void init() {
		operationalModel.init(map, this, tacticalModel.getNavigationModule(), tacticalModel.getActivityModule());
		strategicModel.init(operationalModel.getObservationModule());
		tacticalModel.init(map, this, operationalModel.getMovementModel(), operationalModel.getObservationModule(),
				strategicModel.getActivityPlanner(), strategicModel.getGoalModule().getGoalActivities());
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
		// think first...
		strategicModel.update(timeStep);
		tacticalModel.update(timeStep);

		// then act...
		operationalModel.update(timeStep);
		Vector move = operationalModel.getMove(timeStep);
		position = new Position(position.x + move.x, position.y + move.y);
	}
}