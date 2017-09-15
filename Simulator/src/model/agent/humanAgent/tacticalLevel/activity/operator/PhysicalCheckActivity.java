package model.agent.humanAgent.tacticalLevel.activity.operator;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.position.Position;

/**
 * TODO this activity is not yet implemented.
 * 
 * The physical check activity.
 * 
 * @author S.A.M. Janssen
 */
public class PhysicalCheckActivity extends Activity {

	/**
	 * The wtmd.
	 */
	protected WalkThroughMetalDetector wtmd;
	/**
	 * The passenger under consideration.
	 */
	protected Passenger passenger;

	/**
	 * Creates a physical check activity.
	 * 
	 * @param wtmd
	 *            The WTMD.
	 */
	public PhysicalCheckActivity(WalkThroughMetalDetector wtmd) {
		this.wtmd = wtmd;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isInProgress())
			return false;
		return false;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void startActivity() {
		if (isInProgress())
			return;

		if (isFinished())
			return;

		super.startActivity();
	}

	@Override
	public void update(int timeStep) {
	}

}
