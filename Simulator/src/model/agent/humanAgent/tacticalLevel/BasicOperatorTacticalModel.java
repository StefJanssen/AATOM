package model.agent.humanAgent.tacticalLevel;

import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.tacticalLevel.navigation.OperatorNavigationModule;
import model.environment.map.Map;

/**
 * The static high level model is a high level model designed for static agents.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorTacticalModel extends TacticalModel {

	/**
	 * Creates a new operator tactical model.
	 * 
	 * @param map
	 *            The map.
	 */
	public BasicOperatorTacticalModel(Map map) {
		super(new ActivityModule(), new OperatorNavigationModule(map));
	}

}