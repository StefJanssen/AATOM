package model.agent.humanAgent.aatom.operationalLevel.action.movement;

import model.environment.position.Vector;

/**
 * A static model does not move at all.
 * 
 * @author S.A.M. Janssen
 *
 */
public class StaticMovementModule extends MovementModule {

	/**
	 * Creates a static model.
	 */
	public StaticMovementModule() {
		super(0);
	}

	@Override
	public Vector getMove(int timeStep) {
		return currentVelocity;
	}
}
