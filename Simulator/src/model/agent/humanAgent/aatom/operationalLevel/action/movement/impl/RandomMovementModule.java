package model.agent.humanAgent.aatom.operationalLevel.action.movement.impl;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.environment.position.Vector;
import simulation.simulation.util.Utilities;
import util.math.RandomPlus;

/**
 * The Random model moves a {@link HumanAgent} in a random direction without
 * taking into account its surroundings.
 * 
 * @author S.A.M. Janssen
 *
 */
public class RandomMovementModule extends MovementModule {

	/**
	 * The random generator.
	 */
	private RandomPlus random;

	/**
	 * Creates a random model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public RandomMovementModule(double desiredSpeed) {
		this(desiredSpeed, Utilities.RANDOM_GENERATOR);
	}

	/**
	 * Creates a random movement model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 * @param random
	 *            The random generator.
	 */
	public RandomMovementModule(double desiredSpeed, RandomPlus random) {
		super(desiredSpeed);
		this.random = random;
	}

	@Override
	public Vector getMove(int timeStep) {
		double x = random.nextDouble() * 2 - 1;
		double y = random.nextDouble() * 2 - 1;
		currentVelocity = new Vector(x, y).scalarMultiply(desiredSpeed);
		return currentVelocity;
	}
}