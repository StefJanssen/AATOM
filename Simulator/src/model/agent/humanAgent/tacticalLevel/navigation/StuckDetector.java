package model.agent.humanAgent.tacticalLevel.navigation;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.environment.position.Position;
import simulation.simulation.util.Updatable;

/**
 * Detects if an agent is stuck. It is part of the navigation module.
 * 
 * @author S.A.M. Janssen
 */
public class StuckDetector implements Updatable {
	/**
	 * The movement model.
	 */
	private MovementModel movementModel;
	/**
	 * Stuck parameter.
	 */
	private double timeSamePos = 0;
	/**
	 * Previous position for stuck detection.
	 */
	private Position prevPos;

	/**
	 * Creates a stuck detector.
	 * 
	 * @param movementModel
	 *            The movement model.
	 */
	public StuckDetector(MovementModel movementModel) {
		this.movementModel = movementModel;
		prevPos = Position.NO_POSITION;
	}

	/**
	 * Determines if the agent is stuck for a given number of seconds. Stuck is
	 * defined as moving at most 0.2m in <i>secondsStuck</i> seconds.
	 * 
	 * @param includeStopMoving
	 *            Determines if the agent should be considered stuck if he has a
	 *            stop order.
	 * @param secondsStuck
	 *            The number of seconds it takes before an agent is considered
	 *            stuck.
	 * @return True if the agent is stuck, false otherwise.
	 */
	public boolean isStuck(boolean includeStopMoving, double secondsStuck) {
		if (includeStopMoving && movementModel.getStopOrder())
			return false;
		return timeSamePos > secondsStuck;
	}

	/**
	 * Determines if the agent is stuck for a given number of seconds. Stuck is
	 * defined as moving at most 0.2m in <i>secondsStuck</i> seconds.
	 * 
	 * @param secondsStuck
	 *            The number of seconds it takes before an agent is considered
	 *            stuck.
	 * @return True if the agent is stuck, false otherwise.
	 */
	public boolean isStuck(double secondsStuck) {
		return isStuck(true, secondsStuck);
	}

	/**
	 * Resets the stuck detector.
	 */
	public void reset() {
		timeSamePos = 0;
	}

	/**
	 * Updates the stuck parameters.
	 * 
	 * @param timeStep
	 *            The time step.
	 */
	@Override
	public void update(int timeStep) {
		if (movementModel.isSitting())
			return;

		if (prevPos.distanceTo(movementModel.getPosition()) < 0.2) {
			timeSamePos += timeStep / 1000.0;
		} else {
			prevPos = movementModel.getPosition();
			timeSamePos = 0.0;
		}
	}

}
