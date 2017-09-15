package model.agent.humanAgent.operationalLevel.action.movement;

import java.util.Collection;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.util.Utilities;

/**
 * The Helbing Model is an implementation of the Helbing (2000) paper. It uses
 * 'social forces' to determine the next move.
 * 
 * @author S.A.M. Janssen
 *
 */
public class HelbingModel extends MovementModel {

	/**
	 * Helbing constant A.
	 */
	private static final double A = 250;
	/**
	 * Helbing constant tau.
	 */
	private static final double accelerationTime = 0.5;
	/**
	 * Helbing constant B.
	 */
	private static final double B = 0.1;
	/**
	 * An error term to prevent accumulating errors.
	 */
	private static final double errorTerm = 0.0001;
	/////// HELBING CONSTANTS ///////
	/**
	 * Helbing constant k.
	 */
	private static final double k = 1.2e5;
	/**
	 * Helbing constant kappa.
	 */
	private static final double kappa = 2.4e5;

	/**
	 * Creates a helbing model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public HelbingModel(double desiredSpeed) {
		super(desiredSpeed);
	}

	/**
	 * Gets the agent interaction force.
	 * 
	 * @return The force.
	 */
	private Vector getAgentInteractionForce() {
		Vector agentInteractionForce = new Vector(0, 0);
		Collection<HumanAgent> humanAgents = agent.getObservation(HumanAgent.class);

		for (HumanAgent other : humanAgents) {
			if (!other.equals(agent))
				agentInteractionForce = agentInteractionForce.addVector(getSingleAgentInteractionForce(other));
		}
		return agentInteractionForce;
	}

	/**
	 * Gets the internal acceleration force.
	 * 
	 * @return The force.
	 */
	private Vector getInternalAccelerationForce() {
		Position goalPosition = agent.getGoalPosition();
		Vector e0 = new Vector(goalPosition.x - getPosition().x, goalPosition.y - getPosition().y).normalize();
		Vector numerator = e0.scalarMultiply(desiredSpeed).subtractVector(currentVelocity);
		return numerator.scalarMultiply(15 / accelerationTime);
	}

	@Override
	public Vector getMove(int timeStep) {
		if (agent.getGoalPosition().equals(Position.NO_POSITION))
			return new Vector(0, 0);

		Vector vector = getInternalAccelerationForce();
		vector = vector.addVector(getAgentInteractionForce());
		vector = vector.addVector(getPhysicalObstacleForce());

		// take into account the small accumulating errors
		if (vector.length() < errorTerm) {
			vector = new Vector(0, 0);
		}

		// delta v
		vector = vector.scalarMultiply(1 / agent.getMass());

		Vector newSpeed = boundSpeed(currentVelocity.addVector(vector));

		// we have a shaking effect
		if (newSpeed.isAproximateRotation(90, currentVelocity)) {
			newSpeed = new Vector(agent.getGoalPosition().x - getPosition().x,
					agent.getGoalPosition().y - getPosition().y).normalize();
			newSpeed = newSpeed.scalarMultiply(desiredSpeed);
		}

		currentVelocity = newSpeed;
		return currentVelocity.scalarMultiply(timeStep / 1000.0);
	}

	/**
	 * Gets the physical obstacle force.
	 * 
	 * @return The force.
	 */
	private Vector getPhysicalObstacleForce() {
		Vector physicalObstacleForce = new Vector(0, 0);

		Collection<PhysicalObject> physicalObjects = agent.getObservation(PhysicalObject.class);

		for (PhysicalObject physicalObstacle : physicalObjects) {
			physicalObstacleForce = physicalObstacleForce.addVector(getSinglePhysicalObstacleForce(physicalObstacle));
		}
		return physicalObstacleForce;
	}

	/**
	 * The agent interaction force, is a force that other {@link HumanAgent}s
	 * apply to us.
	 * 
	 * @param other
	 *            The other agent.
	 * @return The force.
	 */
	private Vector getSingleAgentInteractionForce(HumanAgent other) {

		// dij (distance between centers of mass)
		double dij = new Vector(getPosition().x - other.getPosition().x, getPosition().y - other.getPosition().y)
				.length();

		// rij (differences between radi)
		double rij = agent.getRadius() + other.getRadius();

		// nij (normalized vector pointing from other agent to current)
		Vector nij = new Vector(getPosition().x - other.getPosition().x, getPosition().y - other.getPosition().y)
				.normalize();

		// tij (tangential direction)
		Vector tij = new Vector(-nij.y, nij.x);

		if (tij.multiply(currentVelocity) < 0) {
			tij = new Vector(-tij.x, -tij.y);
		}

		// delta vji(t) (tangential velocity difference)
		Vector speed = new Vector(0, 0);
		if (other instanceof Passenger)
			speed = ((Passenger) other).getCurrentSpeed();
		Vector substractedSpeed = speed.subtractVector(currentVelocity);
		double deltaTij = tij.multiply(substractedSpeed);

		// g(rij-dij)
		double gx = rij - dij;

		// [A * exp((rij-dij)/B)]*nij
		Vector force = nij.scalarMultiply(A * Math.exp((rij - dij) / B));

		if (gx > 0) {
			// [k * g(rij-dij)]*nij
			Vector secondPart = nij.scalarMultiply(k * gx);

			// [kappa * g(rij-dij) * deltavji(t)]*tij
			Vector thirdPart = tij.scalarMultiply(kappa * gx * deltaTij);

			force = force.addVector(secondPart);
			force = force.addVector(thirdPart);
		}
		return force;
	}

	/**
	 * The physicalObstacle interaction force, is a force that physicalObstacles
	 * apply to us.
	 * 
	 * @param physicalObstacle
	 *            The physicalObstacle.
	 * @return The force.
	 */
	private Vector getSinglePhysicalObstacleForce(PhysicalObject physicalObstacle) { // fiW
		// niw (direction from physicalObstacle to agent) - to normalize
		Vector niw = Utilities.getVector(agent, physicalObstacle);

		// diw (distance between center of mass and physicalObstacle)
		double diw = niw.length();

		// niw normalization
		niw = niw.normalize();

		// tiW (tangential direction)
		Vector tiw = new Vector(-niw.y, niw.x);

		// g(ri-diw)
		double gx = 0;
		if (agent.getRadius() - diw > 0) {
			gx = agent.getRadius() - diw;
		}

		// [A * exp((ri-diw)/B)]*niw
		Vector force = niw.scalarMultiply(A * Math.exp((agent.getRadius() - diw) / B));

		if (gx > 0) {
			// k * g(ri-diw)]*niw
			Vector secondPart = niw.scalarMultiply(k * gx);

			double viTiw = currentVelocity.multiply(tiw);
			Vector thirdPart = tiw.scalarMultiply(kappa * gx * viTiw);
			force = force.addVector(secondPart);
			force = force.subtractVector(thirdPart);
		}
		return force;
	}

	@Override
	public void init(HumanAgent agent) {
		super.init(agent);
		// currentVelocity = new Vector(agent.getGoalPosition().x -
		// getPosition().x,
		// agent.getGoalPosition().y - getPosition().y).normalize();
		// currentVelocity = currentVelocity.scalarMultiply(desiredSpeed);
		currentVelocity = new Vector(1, 1).scalarMultiply(desiredSpeed);
	}

}
