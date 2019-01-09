package model.agent.humanAgent.aatom.strategicLevel;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.aatom.strategicLevel.goal.GoalModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.PlanningModule;
import simulation.simulation.util.Updatable;

/**
 * A high level model handles all high level modules of a {@link HumanAgent}.
 * They include the planning, goals and blief of a {@link HumanAgent}.
 * 
 * @author S.A.M. Janssen
 */
public abstract class StrategicModel implements Updatable {

	/**
	 * The activity planner.
	 */
	private PlanningModule planner;
	/**
	 * The goal module.
	 */
	private GoalModule goalModule;
	/**
	 * The belief module.
	 */
	private BeliefModule beliefModule;

	/**
	 * Creates a strategic model.
	 * 
	 * @param planner
	 *            The planner.
	 * @param goalModule
	 *            The goal module.
	 * @param beliefModule
	 *            The belief module.
	 */
	public StrategicModel(PlanningModule planner, GoalModule goalModule, BeliefModule beliefModule) {
		this.planner = planner;
		this.goalModule = goalModule;
		this.beliefModule = beliefModule;
	}

	/**
	 * Gets the activity planner.
	 * 
	 * @return The activity planner.
	 */
	public PlanningModule getActivityPlanner() {
		return planner;
	}

	/**
	 * Gets the belief module.
	 * 
	 * @return The belief module.
	 */
	public BeliefModule getBeliefModule() {
		return beliefModule;
	}

	/**
	 * Gets the goal module.
	 * 
	 * @return The goal module.
	 */
	public GoalModule getGoalModule() {
		return goalModule;
	}

	/**
	 * Initializes the strategic model.
	 * 
	 * @param observationModule
	 *            The observation module.
	 */
	public void init(ObservationModule observationModule) {
		planner.init(goalModule);
		beliefModule.init(planner, observationModule, goalModule.getGoalActivities());
	}

	@Override
	public void update(int timeStep) {
		goalModule.update(timeStep);
		planner.update(timeStep);
		beliefModule.update(timeStep);
	}

	/**
	 * Determines if the agent wants to be removed from the simulation.
	 * 
	 * @return True if it wants to be removed, false otherwise.
	 */
	public abstract boolean wantsToBeDestroyed();
}