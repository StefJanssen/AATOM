package model.agent.humanAgent.operationalLevel.action.movement;

import model.agent.humanAgent.Passenger;
import model.environment.position.Vector;

/**
 * The basic model moves, without taking anything of its surroundings into
 * account.
 * 
 * @author S.A.M. Janssen
 *
 */
public class BasicModel extends MovementModel {

	/**
	 * Creates a basic model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public BasicModel(double desiredSpeed) {
		super(desiredSpeed);
	}

	@Override
	public Vector getMove(int timeStep) {
		double xDiff = ((Passenger) agent).getGoalPosition().x - getPosition().x;
		double yDiff = ((Passenger) agent).getGoalPosition().y - getPosition().y;
		double distance = Math.sqrt(Math.abs(xDiff) * Math.abs(xDiff) + Math.abs(yDiff) * Math.abs(yDiff));
		currentVelocity = new Vector(xDiff / distance, yDiff / distance).scalarMultiply(desiredSpeed);
		return currentVelocity;
	}
}
