package model.agent.humanAgent.tacticalLevel.navigation;

import java.util.List;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.environment.map.Map;
import model.environment.position.Position;

/**
 * The Passenger goal activity determines and updates the goals for a passenger
 * in the airport.
 * 
 * @author S.A.M. Janssen
 */
public class OperatorNavigationModule extends NavigationModule {

	/**
	 * Creates a navigation module.
	 * 
	 * @param map
	 *            The map.
	 */
	public OperatorNavigationModule(Map map) {
		super(map);
	}

	/**
	 * Creates a navigation module.
	 * 
	 * @param map
	 *            The map.
	 * @param goalPositions
	 *            The goal positions.
	 */
	public OperatorNavigationModule(Map map, List<Position> goalPositions) {
		super(map, goalPositions);
	}

	/**
	 * Sets the agent.
	 * 
	 * @param movementModel
	 *            The movement model.
	 * @param activityModule
	 *            The activity module.
	 * @param observation
	 *            The observation module.
	 */
	@Override
	public void init(MovementModel movementModel, ActivityModule activityModule, ObservationModule observation) {
		this.movementModel = movementModel;
		this.activityModule = activityModule;
		this.observationModule = observation;
	}

	@Override
	public void update(int timeStep) {
	}

}