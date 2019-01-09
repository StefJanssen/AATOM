package model.agent.humanAgent.aatom.tacticalLevel.activity.passenger;

import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;

/**
 * The checkpoint activity is the activity executed around the checkpoint of an
 * airport.
 * 
 * @author S.A.M. Janssen
 */
public abstract class CheckpointActivity extends Activity {

	/**
	 * The search time.
	 */
	protected double searchTime;

	/**
	 * The time that will be spent on search on top of the standard time.
	 * 
	 * @param searchTime
	 *            The search time.
	 */
	public void setSearch(double searchTime) {
		this.searchTime = searchTime;
	}

}