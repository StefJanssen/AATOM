package model.agent.humanAgent.operationalLevel.action.movement;

import model.agent.humanAgent.AatomHumanAgent;
import model.environment.objects.physicalObject.Chair;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.util.Updatable;

/**
 * A movement model is responsible for the movement of a passenger.
 * 
 * @author S.A.M. Janssen
 */
public abstract class MovementModel implements Updatable {

	/**
	 * The agent.
	 */
	protected AatomHumanAgent agent;
	/**
	 * The current velocity.
	 */
	protected Vector currentVelocity;
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
	 * Creates a movement model.
	 * 
	 * @param desiredSpeed
	 *            The desired speed.
	 */
	public MovementModel(double desiredSpeed) {
		this.desiredSpeed = desiredSpeed;
	}

	/**
	 * Bounds the speed of a movement to a maximum.
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
	 * Gets the current speed.
	 * 
	 * @return The current speed.
	 */
	public Vector getCurrentSpeed() {
		if (getStopOrder())
			return new Vector(0, 0);
		return currentVelocity;
	}

	/**
	 * Gets the next move. When overriding this method, please ensure that you
	 * scalar multiply the generated vector with the timestep/1000.0, and update
	 * the field {@link MovementModel#currentVelocity} to the generated value.
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
	 */
	public void init(AatomHumanAgent agent) {
		this.agent = agent;
		currentVelocity = new Vector(0, 0);
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
