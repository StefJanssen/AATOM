package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.operator.ETDCheckActivity;
import model.environment.objects.physicalObject.sensor.Observation;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.position.Position;
import util.math.distributions.MathDistribution;
import util.math.distributions.NormalDistribution;

/**
 * The ETD check activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicETDCheckActivity extends ETDCheckActivity {

	/**
	 * The WTMD.
	 */
	private WalkThroughMetalDetector wtmd;
	/**
	 * The passenger to check.
	 */
	private Passenger passengerToCheck;
	/**
	 * The passenger has waited.
	 */
	private boolean waited;
	/**
	 * The goto order has been given.
	 */
	private boolean goTo;
	/**
	 * The next passenger in line.
	 */
	private Passenger nextPassenger;
	/**
	 * The passenger waiting time.
	 */
	private MathDistribution waitingDistribution;

	/**
	 * Creates an ETD check activity.
	 * 
	 * @param wtmd
	 *            The WTMD.
	 */
	public BasicETDCheckActivity(WalkThroughMetalDetector wtmd) {
		this(wtmd, new NormalDistribution(34.8, 15.17));
	}

	/**
	 * Creates an ETD check activity.
	 * 
	 * @param wtmd
	 *            The WTMD.
	 * @param waitingDistribution
	 *            The distribution of waiting times.
	 */
	public BasicETDCheckActivity(WalkThroughMetalDetector wtmd, MathDistribution waitingDistribution) {
		this.wtmd = wtmd;
		this.waitingDistribution = waitingDistribution;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (isInProgress())
			return false;

		passengerToCheck = checkObservation();
		return passengerToCheck != null;
	}

	/**
	 * Checks the observation.
	 * 
	 * @return The passenger to check.
	 */
	public Passenger checkObservation() {
		Observation<?> observation = wtmd.getObservation();
		if (!observation.equals(Observation.NO_OBSERVATION)) {
			if ((int) observation.getObservation() == 2) {
				return wtmd.getLastObservedPassenger();
			} else if ((int) observation.getObservation() == 1) {
			}
		}
		return null;

	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void update(int timeStep) {
		
		if (nextPassenger == null) {
			nextPassenger = checkObservation();
			if (nextPassenger != null)
				nextPassenger.communicate(CommunicationType.WAIT, 30);
		} else {
			checkObservation();
		}

		// passenger is at checking position
		if (passengerToCheck.getPosition().distanceTo(wtmd.getCheckPosition()) < 0.5) {
			// set waiting order
			if (!waited) {
				passengerToCheck.communicate(CommunicationType.WAIT, waitingDistribution.getValue());
				waited = true;
			}
			// check when done
			else if (!passengerToCheck.getStopOrder()) {
				// wtmd.setPassengerToCheck(null);
				passengerToCheck = nextPassenger;
				nextPassenger = null;
				waited = false;
				goTo = false;
				if (passengerToCheck == null) {
					endActivity();
				}
				return;
			}
		}
		// passenger is not at checking position
		else if (!goTo) {
			passengerToCheck.communicate(CommunicationType.WAIT, 0);
			passengerToCheck.communicate(CommunicationType.GOTO, wtmd.getCheckPosition());
			goTo = true;
		}
	}

}
