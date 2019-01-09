package model.agent.humanAgent.aatom.operationalLevel.action.movement;

import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.environment.objects.physicalObject.Chair;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.util.Updatable;

/**
 * A movement model is responsible for the movement of a passenger.
 * 
 * @author S.A.M. Janssen
 */
public abstract class MovementModule implements Updatable {

	/**
	 * The agent. TODO fix
	 */
	public AatomHumanAgent agent;
	/**
	 * The current velocity.
	 */
	protected Vector currentVelocity = new Vector(0, 0);
	/**
	 * The desired speed.
	 */
	protected double desiredSpeed;
	/**
	 * The chair the agent is sitting on. Null if not sitting.
	 */
	private Chair chair;
	/**
	 * The time that the agent still needs to stop moving. If this is -1, the
	 * time is infinite.
	 */
	private double stopMovingTime;
	/**
	 * The observation module.
	 */
	protected ObservationModule observationModule;

	/**
	 * Creates a movement model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public MovementModule(double desiredSpeed) {
		if (desiredSpeed < 0)
			throw new IllegalArgumentException("Speed should be >= 0 m/s.");
		this.desiredSpeed = desiredSpeed;
	}

	/**
	 * Bounds the speed of a movement to a maximum of 1.5 the desired speed.
	 * 
	 * @param vector
	 *            The movement.
	 * @return The movement bounded with a maximum speed.
	 */
	protected Vector boundSpeed(Vector vector) {
		if (vector.length() > 1.5 * desiredSpeed)
			return vector.normalize().scalarMultiply(1.5 * desiredSpeed);
		return vector;
	}

	/**
	 * Gets the chair the agent is sitting on. Null if not sitting.
	 * 
	 * @return The chair the agent is sitting on. Null if not sitting.
	 */
	public Chair getChair() {
		return chair;
	}

	/**
	 * Gets the current velocity.
	 * 
	 * @return The current velocity.
	 */
	public Vector getCurrentVelocity() {
		if (getStopOrder())
			return new Vector(0, 0);
		return currentVelocity;
	}

	/**
	 * Gets the desired speed.
	 * 
	 * @return The desired speed.
	 */
	public double getDesiredSpeed() {
		return desiredSpeed;
	}

	/**
	 * Gets the next move. When overriding this method, please ensure that you
	 * scalar multiply the generated vector with the timestep/1000.0, and update
	 * the field {@link MovementModule#currentVelocity} to the generated value.
	 * 
	 * @param timeStep
	 *            The time step (in milliseconds).
	 * @return The next move.
	 */
	public abstract Vector getMove(int timeStep);

	/**
	 * Gets the position of the agent.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return agent.getPosition();
	}

	/**
	 * Get the remaining stop moving time.
	 * 
	 * @return The remaining time to stop moving.
	 */
	public double getStopMovingTime() {
		return stopMovingTime;
	}

	/**
	 * Indicates if the agent has a stop order. It will also make sure that the
	 * agent stops if someone in its very close neighborhood has a stop order.
	 * 
	 * @return True if he is ordered to stop, false otherwise.
	 */
	public boolean getStopOrder() {
		return stopMovingTime != 0;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            The agent.
	 * @param observationModule
	 *            The observation module.
	 */
	public void init(AatomHumanAgent agent, ObservationModule observationModule) {
		this.agent = agent;
		currentVelocity = new Vector(0, 0);
		this.observationModule = observationModule;
	}

	/**
	 * Determines if the agent is sitting.
	 * 
	 * @return True if the agent is sitting, false otherwise.
	 */
	public boolean isSitting() {
		return chair != null;
	}

	/**
	 * Asks the agent to sit down on a specific {@link Chair}. Returns the
	 * {@link Position} of the chair if he will sit down, returns
	 * {@link Position#NO_POSITION} if he will not.
	 * 
	 * @param chair
	 *            The chair.
	 * @return The position of the chair if he will sit down, and no position if
	 *         he will not.
	 */
	public boolean setSitDown(Chair chair) {
		if (chair.isOccupied())
			return false;

		if (agent.getDistance(chair.getEntryPosition()) < 1) {
			this.chair = chair;
			chair.setOccupied(agent);
			setStopOrder(-1);
			return true;
		}
		return false;
	}

	/**
	 * Sets the time that indicates if the time to stop.
	 * 
	 * @param stopOrder
	 *            The stopping time.
	 */
	public void setStopOrder(double stopOrder) {
		this.stopMovingTime = stopOrder;
	}

	@Override
	public void update(int timeStep) {
		if (stopMovingTime > 0)
			stopMovingTime -= timeStep / 1000.0;
		else if (stopMovingTime != -1)
			stopMovingTime = 0;
	}
}
