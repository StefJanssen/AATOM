package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.OperatorAgent;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.operator.XRayActivity;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.sensor.Observation;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;

/**
 * The x-ray activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicXRayActivity extends XRayActivity {

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
	 * The bagChecker.
	 */
	private OperatorAgent bagChecker;
	/**
	 * The latest observation.
	 */
	private Observation<?> observation;

	/**
	 * Creates the X-Ray activity.
	 * 
	 * @param xRaySystem
	 *            The x-ray system.
	 * @param bagChecker
	 *            The bag checker.
	 */
	public BasicXRayActivity(XRaySystem xRaySystem, OperatorAgent bagChecker) {
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
	public BasicXRayActivity(XRaySystem xRaySystem, OperatorAgent bagChecker, double threatLevelThreshold) {
		this.threatLevelThreshold = threatLevelThreshold;
		this.xRaySystem = xRaySystem;
		this.bagChecker = bagChecker;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isInProgress())
			return false;

		observation = xRaySystem.getXRaySensor().getObservation();
		if (!observation.equals(Observation.NO_OBSERVATION)) {
			return true;
		}
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

		if ((Double) observation.getObservation() > threatLevelThreshold) {
			Luggage luggage = xRaySystem.getXRaySensor().getLastObservedLuggage();
			bagChecker.communicate(CommunicationType.SEARCH, luggage);
		}

		recoverTime = 3.0;
		xRaySystem.pauseSystem(true);
		super.startActivity();
	}

	@Override
	public void update(int timeStep) {
		recoverTime -= timeStep / 1000.0;

		if (recoverTime > 0)
			return;

		xRaySystem.pauseSystem(false);
		endActivity();
	}

}
