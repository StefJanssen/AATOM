package model.agent.humanAgent.operationalLevel.action.movement;

import model.environment.position.Vector;

/**
 * A static model does not move at all.
 * 
 * @author S.A.M. Janssen
 *
 */
public class StaticModel extends MovementModel {

	/**
	 * Creates a static model.
	 */
	public StaticModel() {
		super(0);
	}

	@Override
	public Vector getMove(int timeStep) {
		return currentVelocity;
	}
}
