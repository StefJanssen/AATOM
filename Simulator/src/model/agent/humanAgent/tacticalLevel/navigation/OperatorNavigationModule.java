package model.agent.humanAgent.tacticalLevel.navigation;

import java.util.ArrayList;
import java.util.List;

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
	 */
	public OperatorNavigationModule() {
		this(new ArrayList<Position>());
	}

	/**
	 * Creates a navigation module.
	 * 
	 * @param goalPositions
	 *            The goal positions.
	 */
	public OperatorNavigationModule(List<Position> goalPositions) {
		super(goalPositions);
	}

	@Override
	public void update(int timeStep) {
	}

}