package model.agent.humanAgent.tacticalLevel;

import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.tacticalLevel.navigation.NavigationModule;
import model.agent.humanAgent.tacticalLevel.navigation.OperatorNavigationModule;

/**
 * The operator tactical model is a tactical model designed for operator agents.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorTacticalModel extends TacticalModel {

	/**
	 * Creates a new operator tactical model.
	 * 
	 */
	public BasicOperatorTacticalModel() {
		this(new OperatorNavigationModule());
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