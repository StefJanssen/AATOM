package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.operator.LuggageCheckActivity;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;

/**
 * The luggage check activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicLuggageCheckActivity extends LuggageCheckActivity {

	/**
	 * The mean service rate.
	 */
	private double meanServiceRate;

	/**
	 * Creates a luggage check activity.
	 * 
	 */
	public BasicLuggageCheckActivity() {
		this(25);
	}

	/**
	 * Creates a luggage check activity.
	 * 
	 * @param meanServiceRate
	 *            The mean service rate.
	 */
	public BasicLuggageCheckActivity(double meanServiceRate) {
		this.meanServiceRate = meanServiceRate;
	}

	@Override
	public boolean canStart(int timeStep) {
		return true;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	/**
	 * Set search of luggage.
	 * 
	 * @param luggage
	 *            The luggage.
	 */
	@Override
	public void setSearch(Luggage luggage) {
		super.setSearch(luggage);
		luggage.getOwner().communicate(CommunicationType.SEARCH, meanServiceRate);
	}

	@Override
	public void update(int timeStep) {
	}

}
