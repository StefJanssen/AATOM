package model.agent.humanAgent.tacticalLevel;

import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.tacticalLevel.navigation.NavigationModule;
import model.agent.humanAgent.tacticalLevel.navigation.OperatorNavigationModule;
import model.environment.map.Map;

/**
 * The operator tactical model is a tactical model designed for operator agents.
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

	/**
	 * Creates a new operator tactical model.
	 * 
	 * @param navigationModule
	 *            The navigation module.
	 */
	public BasicOperatorTacticalModel(NavigationModule navigationModule) {
		super(new ActivityModule(), navigationModule);
	}

}