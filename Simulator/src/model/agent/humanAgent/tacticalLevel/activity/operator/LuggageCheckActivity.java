package model.agent.humanAgent.tacticalLevel.activity.operator;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.physicalObject.luggage.Luggage;

/**
 * The luggage check activity.
 * 
 * @author S.A.M. Janssen
 */
public abstract class LuggageCheckActivity extends Activity {

	/**
	 * The luggage to search.
	 */
	protected Luggage luggage;

	/**
	 * Set search of luggage.
	 * 
	 * @param luggage
	 *            The luggage.
	 */
	public void setSearch(Luggage luggage) {
		this.luggage = luggage;
	}

}
