package model.agent.humanAgent.aatom.tacticalLevel.activity.passenger;

import java.util.Collection;

import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.environment.objects.area.Area;
import model.environment.objects.area.QueuingArea;
import model.environment.position.Position;
import util.math.FastMath;

/**
 * The queue activity is executed when the agent needs to stop for some reason.
 * 
 * @author S.A.M. Janssen
 */
public class QueueActivity extends Activity {

	/**
	 * The time the agent is queuing.
	 */
	private double time;
	/**
	 * Determines if the agent is in front of the queue.
	 */
	private boolean inFrontOfQueue;

	@Override
	public boolean canStart(int timeStep) {
		// if we're the first one in queue, we can certainly start.
		if (inFrontOfQueue) {
			return true;
		}

		// We determine if there is an agent in front of us that we don't want
		// to push.

		// own position
		Position position = movement.getPosition();
		// goal position
		Position newPos2 = navigationModule.getGoalPosition();
		// angle
		double angle2 = getAngle(newPos2, position);

		if (isInQueue()) {
			// Observe other agents.
			Collection<Passenger> agents = observations.getObservation(Passenger.class);
			for (Passenger other : agents) {
				if (other.isQueuing()) {
					if (!other.getPosition().equals(movement.getPosition())) {
						// within range
						if (other.getPosition().distanceTo(position) < 1) {
							if ((inRange(getAngle(other.getPosition(), position), angle2, 45 / 2))) {
								// Check if the agent is queuing
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void endActivity() {
		super.endActivity();
		inFrontOfQueue = false;
		time = 0;
		movement.setStopOrder(0);
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	/**
	 * Calculates the angle between two positions.
	 * 
	 * @param target
	 *            The target position.
	 * @param origin
	 *            The origin position.
	 * @return The angle.
	 */
	private double getAngle(Position target, Position origin) {
		double angle = FastMath.atan2Deg(target.y - origin.y, target.x - origin.x);
		if (angle < 0)
			angle += 360;
		return angle;
	}

	@Override
	public void goToActivity() {
		return;
	}

	/**
	 * Determines if an angle is within a range of angles.
	 * 
	 * @param angle
	 *            The angle.
	 * @param targetAngle
	 *            The target angle.
	 * @param range
	 *            The range.
	 * @return True if it is in range, false otherwise.
	 */
	private boolean inRange(double angle, double targetAngle, double range) {
		double diff = Math.abs(angle - targetAngle);
		if (diff > 180)
			diff = Math.abs(360 - diff);
		return diff < range;
	}

	/**
	 * Determines if the agent is in front of the queue.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isInFrontOfQueue() {
		return inFrontOfQueue;
	}

	/**
	 * Determines if the agent is queuing.
	 * 
	 * @return True if it is, false otherwise.
	 */
	private boolean isInQueue() {
		Collection<Area> areas = observations.getObservation(Area.class);
		for (Area a : areas) {
			if (a instanceof QueuingArea)
				return true;
		}
		return false;
	}

	/**
	 * Sets the agent in front of the queue.
	 */
	public void setInFrontOfQueue() {
		this.inFrontOfQueue = true;
		startActivity();
	}

	@Override
	public void startActivity() {
		startActivity(-1);
	}

	/**
	 * Starts the activity with a specific time.
	 * 
	 * @param time
	 *            The time.
	 */
	public void startActivity(double time) {
		super.startActivity();
		movement.setStopOrder(time);
		this.time = time;
	}

	@Override
	public void update(int timeStep) {
		if (time > 0) {
			time -= timeStep / 1000.0;
			return;
		}

		time = 1.0;
		if (!canStart(timeStep))
			endActivity();
	}
}