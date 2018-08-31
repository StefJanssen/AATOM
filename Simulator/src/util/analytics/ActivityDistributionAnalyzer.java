package util.analytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.Activity;

/**
 * A parameter tracker for the distribution of activities of passengers.
 * 
 * @author S.A.M. Janssen
 */
public class ActivityDistributionAnalyzer extends Analyzer {

	/**
	 * The relevant activities.
	 */
	private List<String> activities;

	/**
	 * Creates the analyzer.
	 */
	public ActivityDistributionAnalyzer() {
		activities = new ArrayList<>(Arrays.asList("Checkpoint", "Gate", "CheckIn"));
	}

	@Override
	public String[] getLineNames() {
		String[] s = new String[activities.size()];
		for (int i = 0; i < activities.size(); i++)
			s[i] = activities.get(i);
		return s;
	}

	@Override
	public String getTitle() {
		return "Activity Distribution";
	}

	@Override
	public double[] getValues() {
		Collection<Passenger> passengers = map.getMapComponents(Passenger.class);
		double[] values = new double[activities.size()];
		for (Passenger passenger : passengers) {
			Collection<Activity> activeActivities = passenger.getActiveActivities();
			if (activeActivities.size() > 0) {
				for (Activity activity : activeActivities) {
					for (int i = 0; i < activities.size(); i++) {
						if (activity.getClass().getSimpleName().equals(activities.get(i) + "Activity"))
							values[i]++;
					}
				}
			}
		}
		return values;
	}

	@Override
	public String getYAxis() {
		return "# of passengers";
	}

}
