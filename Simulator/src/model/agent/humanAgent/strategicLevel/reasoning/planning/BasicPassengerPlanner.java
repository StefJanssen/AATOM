package model.agent.humanAgent.strategicLevel.reasoning.planning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.CheckInActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.CheckpointActivity;
import model.agent.humanAgent.tacticalLevel.activity.passenger.GateActivity;

/**
 * A basic passenger activity planner.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerPlanner extends ActivityPlanner {

	/**
	 * @param activityType
	 *            The activity type.
	 * @return The specific activity.
	 */
	private Activity getActivityFromType(Class<? extends Activity> activityType) {
		for (Activity a : activities) {
			if (activityType.isInstance(a))
				return a;
		}
		return null;
	}

	@Override
	public Activity getNextActivity() {
		if (planning.isEmpty())
			return null;
		return planning.get(0);
	}

	@Override
	public List<Activity> getPlanning() {
		if (planning != null)
			return planning;

		planning = new ArrayList<>();

		// check-in
		planning.add(getActivityFromType(CheckInActivity.class));

		// checkpoint
		planning.add(getActivityFromType(CheckpointActivity.class));

		// TODO add border control to planning

		// TODO randomize location and check feasibility
		// planning.add(getActivityFromType(FacilityActivity.class));

		// gate
		planning.add(getActivityFromType(GateActivity.class));

		// remove all nulls
		planning.removeAll(Collections.singleton(null));

		return planning;
	}

	@Override
	public void update(int timeStep) {
		while (!planning.isEmpty() && planning.get(0).isFinished()) {
			planning.remove(0);
		}
	}
}
