package model.agent.humanAgent.tacticalLevel.activity.operator;

import model.agent.humanAgent.OperatorAgent;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.sensor.Observation;
import model.environment.objects.physicalObject.sensor.XRaySystem;

/**
 * The x-ray activity.
 * 
 * @author S.A.M. Janssen
 */
public class XRayActivity extends Activity {

	/**
	 * The x-ray system.
	 */
	private XRaySystem xRaySystem;
	/**
	 * The recovery time of an x-ray agent.
	 */
	private double recoverTime;
	/**
	 * The threat level threshold.
	 */
	private double threatLevelThreshold;
	/**
	 * The luggage to communicate.
	 */
	private Luggage luggage;
	/**
	 * The bagChecker.
	 */
	private OperatorAgent bagChecker;

	/**
	 * Creates the X-Ray activity.
	 * 
	 * @param xRaySystem
	 *            The x-ray system.
	 * @param bagChecker
	 *            The bag checker.
	 */
	public XRayActivity(XRaySystem xRaySystem, OperatorAgent bagChecker) {
		this(xRaySystem, bagChecker, 0.75);
	}

	/**
	 * Creates the X-Ray activity.
	 * 
	 * @param xRaySystem
	 *            The x-ray system.
	 * @param bagChecker
	 *            The bag checker.
	 * @param threatLevelThreshold
	 *            The threat level threshold.
	 */
	public XRayActivity(XRaySystem xRaySystem, OperatorAgent bagChecker, double threatLevelThreshold) {
		this.threatLevelThreshold = threatLevelThreshold;
		this.xRaySystem = xRaySystem;
		this.bagChecker = bagChecker;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isInProgress())
			return false;

		recoverTime -= timeStep / 1000.0;

		if (recoverTime > 0)
			return false;

		Observation<?> observation = xRaySystem.getXRaySensor().getObservation();
		if (!observation.equals(Observation.NO_OBSERVATION)) {
			if ((Double) observation.getObservation() > threatLevelThreshold) {
				luggage = xRaySystem.getXRaySensor().getLastObservedLuggage();
				return true;
			}
		}
		return false;
	}

	@Override
	public void startActivity() {
		if (isInProgress())
			return;

		bagChecker.communicate(CommunicationType.SEARCH, luggage);
		super.startActivity();
	}

	@Override
	public void update(int timeStep) {
		recoverTime = 3.0;
		endActivity();
	}
}
