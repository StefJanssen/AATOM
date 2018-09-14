package model.agent.humanAgent.aatom.operationalLevel.action.movement;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Vector;
import simulation.simulation.util.Utilities;

/**
 * The Random model moves a {@link HumanAgent} in a random direction without
 * taking into account its surroundings.
 * 
 * @author S.A.M. Janssen
 *
 */
public class RandomMovementModule extends MovementModule {

	/**
	 * Creates a random model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public RandomMovementModule(double desiredSpeed) {
		super(desiredSpeed);
	}

	@Override
	public Vector getMove(int timeStep) {
		double x = Utilities.RANDOM_GENERATOR.nextDouble();
		double y = Utilities.RANDOM_GENERATOR.nextDouble();
		currentVelocity = new Vector(x, y).scalarMultiply(desiredSpeed);
		return currentVelocity;
	}
}