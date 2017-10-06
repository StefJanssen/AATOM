package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.operator.CheckInActivity;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * @author S.A.M. Janssen
 */
public class BasicCheckInActivity extends CheckInActivity {

	/**
	 * The {@link Passenger}s that he already instructed.
	 */
	private Collection<Passenger> alreadyInstructed;
	/**
	 * The time mean service rate.
	 */
	private double meanServiceRate;
	/**
	 * The desk it operates at.
	 */
	private Desk desk;

	/**
	 * Creates a check-in activity for the check-in operator.
	 * 
	 * @param desk
	 *            The desk.
	 * 
	 * @param meanServiceRate
	 *            The mean service rate.
	 */
	public BasicCheckInActivity(Desk desk, double meanServiceRate) {
		this.desk = desk;
		this.meanServiceRate = meanServiceRate;
		alreadyInstructed = new ArrayList<Passenger>();
	}

	@Override
	public boolean canStart(int timeStep) {
		if (!isInProgress() && desk.getAgentAtDesk() != null) {
			if (desk.getAgentAtDesk().getPosition().distanceTo(desk.getServingPosition()) < 0.6)
				return true;
			else if (desk.getAgentAtDesk().getPosition().distanceTo(desk.getServingPosition()) < 1
					&& desk.getAgentAtDesk().getReachedGoal())
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
		desk.getAgentAtDesk().communicate(CommunicationType.WAIT,
				Utilities.RANDOM_GENERATOR.nextNormal(meanServiceRate, meanServiceRate / 10));
	}

	@Override
	public void update(int timeStep) {
		if (!alreadyInstructed.contains(desk.getAgentAtDesk())) {
			alreadyInstructed.add(desk.getAgentAtDesk());
			desk.getAgentAtDesk().getFlight().checkIn(desk.getAgentAtDesk());
		} else if (desk.getAgentAtDesk().getPosition().distanceTo(desk.getServingPosition()) > 1.5) {
			desk.setAgentAtDesk(null);
			endActivity();
		}
	}
}