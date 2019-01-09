package model.agent.humanAgent.aatom.tacticalLevel.activity.operator.impl;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.environment.objects.area.BorderControlGateArea;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;
import util.math.distributions.MathDistribution;

/**
 * The operator border control activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorBorderControlActivity extends Activity {

	/**
	 * The {@link Passenger}s that he already instructed.
	 */
	private Collection<Passenger> alreadyInstructed;
	/**
	 * The desk it operates at.
	 */
	private Desk desk;
	/**
	 * The distribution of waiting times.
	 */
	private MathDistribution waitingTime;

	/**
	 * Creates a border control activity for the border operator.
	 * 
	 * @param desk
	 *            The desk.
	 * 
	 * @param waitingTime
	 *            The distribution of waiting times.
	 */
	public BasicOperatorBorderControlActivity(Desk desk, MathDistribution waitingTime) {
		this.desk = desk;
		this.waitingTime = waitingTime;
		alreadyInstructed = new ArrayList<Passenger>();
	}

	@Override
	public boolean canStart(int timeStep) {
		if (!isInProgress() && desk.getAgentAtDesk() != null) {
			if (desk.getAgentAtDesk().getPosition().distanceTo(desk.getServingPosition()) < 0.6)
				return true;
			else if (desk.getAgentAtDesk().getPosition().distanceTo(desk.getServingPosition()) < 1
					&& ((Passenger) desk.getAgentAtDesk()).getReachedGoal())
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
		super.startActivity();
		desk.getAgentAtDesk().communicate(CommunicationType.WAIT, waitingTime.getValue());
	}

	@Override
	public void update(int timeStep) {
		if (!alreadyInstructed.contains(desk.getAgentAtDesk())) {
			alreadyInstructed.add((Passenger) desk.getAgentAtDesk());
			BorderControlGateArea gateArea = (BorderControlGateArea) ((Passenger) desk.getAgentAtDesk()).getFlight()
					.getGateArea();
			gateArea.borderControl((Passenger) desk.getAgentAtDesk());
		} else if (desk.getAgentAtDesk().getPosition().distanceTo(desk.getServingPosition()) > 1.5) {
			desk.reserveDesk(null);
			endActivity();
		}
	}
}
