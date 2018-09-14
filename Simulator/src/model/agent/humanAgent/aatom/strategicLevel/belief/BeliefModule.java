package model.agent.humanAgent.aatom.strategicLevel.belief;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.ActivityPlanner;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.environment.objects.area.Area;
import simulation.simulation.util.Updatable;

/**
 * The belief module.
 * 
 * @author S.A.M. Janssen
 */
public class BeliefModule implements Updatable {

	/**
	 * The historic beliefs.
	 */
	private List<Belief> beliefs;
	/**
	 * The planner.
	 */
	private ActivityPlanner planner;
	/**
	 * The observation module.
	 */
	private ObservationModule observationModule;
	/**
	 * The activities.
	 */
	private Collection<Activity> activities;

	/**
	 * Creates the belief module.
	 * 
	 */
	public BeliefModule() {
		beliefs = new ArrayList<>();
	}

	/**
	 * Generates a new belief.
	 * 
	 * @return The belief.
	 */
	private Belief generateBelief() {
		return new Belief(getLocation(), getActivityStatePairs(), getPlan());
	}

	/**
	 * Gets the activity state pairs.
	 * 
	 * @return The activity state pairs.
	 */
	private List<ActivityStatePair> getActivityStatePairs() {
		List<ActivityStatePair> activityStatePairs = new ArrayList<>();
		for (Activity activity : activities) {
			activityStatePairs.add(new ActivityStatePair(activity.getClass(), activity.getActivityState()));
		}
		return activityStatePairs;
	}

	/**
	 * Gets the current belief.
	 * 
	 * @return The current belief.
	 */
	public Belief getCurrentBelief() {
		return beliefs.get(beliefs.size() - 1);
	}

	/**
	 * Gets the location.
	 * 
	 * @return The location.
	 */
	private Area getLocation() {
		Area mostSpecific = null;
		if (observationModule.getObservation(Area.class) != null) {
			for (Area a : observationModule.getObservation(Area.class)) {
				if (mostSpecific == null) {
					mostSpecific = a;
				} else {
					// TODO how to determine which is most specific
				}
			}
		}
		return mostSpecific;
	}

	/**
	 * Get a plan from the planning.
	 * 
	 * @return A copy of the plan.
	 */
	private List<Activity> getPlan() {
		List<Activity> plan = new ArrayList<>();
		for (Activity act : planner.getPlanning())
			plan.add(act);
		return plan;
	}

	/**
	 * Initializes the belief module.
	 * 
	 * @param planner
	 *            The planner.
	 * @param observationModule
	 *            The observation module.
	 * @param activities
	 *            The activities.
	 */
	public void init(ActivityPlanner planner, ObservationModule observationModule, Collection<Activity> activities) {
		this.planner = planner;
		this.observationModule = observationModule;
		this.activities = activities;
		beliefs.add(generateBelief());
	}

	/**
	 * Determines if two beliefs have the same activity state pair vector.
	 * 
	 * @param first
	 *            First belief.
	 * @param second
	 *            Second belief.
	 * @return True if they do, false otherwise.
	 */
	private boolean isSameActivityStatePairVector(Belief first, Belief second) {
		for (ActivityStatePair asp : first.getActivities()) {
			boolean same = false;
			for (ActivityStatePair asp2 : second.getActivities()) {
				if (asp.getActivity().equals(asp2.getActivity())
						&& asp.getActivityState().equals(asp2.getActivityState())) {
					same = true;
				}
			}
			if (!same)
				return false;
		}
		return true;
	}

	@Override
	public void update(int timeStep) {
		// Belief newBelief = generateBelief();
		// updateBelief(newBelief);
	}

	/**
	 * Updates the belief if it's different from the current belief.
	 * 
	 * @param newBelief
	 *            The new belief.
	 */
	@SuppressWarnings("unused")
	private void updateBelief(Belief newBelief) {
		Belief current = getCurrentBelief();

		// location check
		if (current.getLocation() == null)
			return;
		boolean location = current.getLocation().equals(newBelief.getLocation());

		// activity state pairs
		boolean asp = isSameActivityStatePairVector(current, newBelief);

		// planning check
		boolean planning = true;
		int index = 0;
		for (Activity act : current.getPlanning()) {
			if (!newBelief.getPlanning().get(index).equals(act)) {
				planning = false;
				break;
			}
			index++;
		}
		if (!(planning && asp && location))
			beliefs.add(newBelief);
	}
}
