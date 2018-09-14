package model.agent.humanAgent.aatom.operationalLevel.action.movement;

import model.environment.position.Position;
import model.environment.position.Vector;

/**
 * The basic model moves, without taking anything of its surroundings into
 * account.
 * 
 * @author S.A.M. Janssen
 *
 */
public class BasicMovementModule extends MovementModule {

	/**
	 * Creates a basic model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public BasicMovementModule(double desiredSpeed) {
		super(desiredSpeed);
	}

	@Override
	public Vector getMove(int timeStep) {
		if (agent.getGoalPosition().equals(Position.NO_POSITION)) {
			currentVelocity = new Vector(0, 0);
			return currentVelocity;
		}
		double xDiff = agent.getGoalPosition().x - getPosition().x;
		double yDiff = agent.getGoalPosition().y - getPosition().y;
		double distance = Math.sqrt(Math.abs(xDiff) * Math.abs(xDiff) + Math.abs(yDiff) * Math.abs(yDiff));
		if (distance < 0.00001) {
			currentVelocity = new Vector(0, 0);
			return currentVelocity;
		}
		Vector newSpeed = new Vector(xDiff / distance, yDiff / distance);

		currentVelocity = newSpeed.scalarMultiply(desiredSpeed);
		return currentVelocity.scalarMultiply(timeStep / 1000.0);
	}
}
