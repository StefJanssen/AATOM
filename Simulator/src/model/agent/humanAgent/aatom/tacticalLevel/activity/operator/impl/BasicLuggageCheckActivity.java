package model.agent.humanAgent.aatom.tacticalLevel.activity.operator.impl;

import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.tacticalLevel.activity.operator.LuggageCheckActivity;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;
import util.math.distributions.MathDistribution;
import util.math.distributions.NormalDistribution;

/**
 * The luggage check activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicLuggageCheckActivity extends LuggageCheckActivity {

	/**
	 * The time distribution.
	 */
	private MathDistribution timeDistribution;

	/**
	 * Creates a luggage check activity.
	 * 
	 */
	public BasicLuggageCheckActivity() {
		this(new NormalDistribution(60, 6));
	}

	/**
	 * Creates a luggage check activity.
	 * 
	 * @param timeDistribution
	 *            The distribution of search times.
	 */
	public BasicLuggageCheckActivity(MathDistribution timeDistribution) {
		this.timeDistribution = timeDistribution;
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
		luggage.getOwner().communicate(CommunicationType.SEARCH, timeDistribution.getValue());
	}

	@Override
	public void update(int timeStep) {
	}

}
