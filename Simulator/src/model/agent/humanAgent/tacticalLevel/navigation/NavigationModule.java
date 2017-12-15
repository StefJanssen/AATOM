package model.agent.humanAgent.tacticalLevel.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.tacticalLevel.activity.passenger.QueueActivity;
import model.agent.humanAgent.tacticalLevel.navigation.pathfinder.JumpPointSearchPathFinder;
import model.agent.humanAgent.tacticalLevel.navigation.pathfinder.PathFinder;
import model.environment.map.Map;
import model.environment.objects.area.Area;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import simulation.simulation.util.Updatable;
import simulation.simulation.util.Utilities;

/**
 * The goal activity is responsible for the goal setting of the agent. It
 * determines if the agent is stuck, and updates itself accordingly. It also
 * allows for external parties to influence the goals, using the 'set' methods.
 * 
 * @author S.A.M. Janssen
 */
public class NavigationModule implements Updatable {

	/**
	 * The goal {@link Position}s.
	 */
	protected List<Position> goalPositions;
	/**
	 * Goal refresh counter.
	 */
	private double goalRefreshTime;
	/**
	 * The method to determine our path.
	 */
	protected PathFinder pathFinder;
	/**
	 * The stuck detector.
	 */
	protected StuckDetector stuckDetector;
	/**
	 * The movement model.
	 */
	protected MovementModel movementModel;
	/**
	 * The observation module.
	 */
	protected ObservationModule observationModule;
	/**
	 * The activity module.
	 */
	protected ActivityModule activityModule;
	/**
	 * The map.
	 */
	private Map map;

	/**
	 * Create a goal activity.
	 * 
	 * @param map
	 *            The map.
	 * 
	 */
	public NavigationModule(Map map) {
		this(map, new ArrayList<Position>());
	}

	/**
	 * Creates a goal activity.
	 * 
	 * @param map
	 *            The map.
	 * 
	 * @param goalPositions
	 *            The goal positions.
	 */
	public NavigationModule(Map map, List<Position> goalPositions) {
		this.map = map;
		this.goalPositions = goalPositions;
	}

	/**
	 * Gets the closest goal position.
	 * 
	 * @return Tue closest goal position.
	 */
	public Position getGoalPosition() {
		if (goalPositions.isEmpty())
			return Position.NO_POSITION;
		return goalPositions.get(0);
	}

	/**
	 * Gets the goal positions.
	 * 
	 * @return The goal positions.
	 */
	public List<Position> getGoalPositions() {
		return goalPositions;
	}

	/**
	 * Gets the queue goal.
	 * 
	 * @return The goal.
	 */
	private Position getQueueGoal() {
		Collection<Area> areas = observationModule.getObservation(Area.class);
		for (Area area : areas) {
			if (area instanceof QueuingArea) {
				return ((QueuingArea) area).getLeavingPosition();
			}
		}
		return Position.NO_POSITION;
	}

	/**
	 * Determines if the goal is reached.
	 * 
	 * @return True if the goal is reached, false otherwise.
	 */
	public boolean getReachedGoal() {
		return goalPositions.isEmpty();
	}

	/**
	 * Handles the behavior if the agent gets stuck.
	 * 
	 * @param timeStep
	 *            The time step.
	 */
	protected void handleStuckBehavior(int timeStep) {
		stuckDetector.update(timeStep);

		// if queuing, go to front of queue
		if (isQueuing()) {
			if (stuckDetector.isStuck(false, 60)) {
				setGoal(getQueueGoal());
				stuckDetector.reset();
			}
		}

		// if stuck, go to next activity OR current goal
		if (stuckDetector.isStuck(20)) {
			if (getGoalPosition().equals(Position.NO_POSITION) && activityModule.getActiveActivities().size() == 0) {
				setGoal(activityModule.getNextActivityPosition());
				stuckDetector.reset();
			} else if (getGoalPositions().size() > 0) {
				setGoal(getGoalPositions().get(getGoalPositions().size() - 1));
				if (!inQueuingArea()) {
					setShortTermGoal(
							new Position(movementModel.getPosition().x + Utilities.RANDOM_GENERATOR.nextDouble() - 0.5,
									movementModel.getPosition().y + Utilities.RANDOM_GENERATOR.nextDouble() - 0.5));
				}
				stuckDetector.reset();

			}
		}
	}

	/**
	 * Sets the agent.
	 * 
	 * @param movementModel
	 *            The movement model.
	 * @param activityModule
	 *            The activity module.
	 * @param observationModule
	 *            The observation module.
	 */
	public void init(MovementModel movementModel, ActivityModule activityModule, ObservationModule observationModule) {
		this.movementModel = movementModel;
		this.activityModule = activityModule;
		this.observationModule = observationModule;
		stuckDetector = new StuckDetector(movementModel);
		pathFinder = new JumpPointSearchPathFinder(map, 0.4);
		if (goalPositions.size() == 1) {
			Position p = goalPositions.get(0);
			setGoal(p);
		}
	}

	/**
	 * Check if there is a collision with {@link PhysicalObject}s or
	 * {@link WalkThroughMetalDetector}s.
	 * 
	 * @param goalIndex
	 *            The index of the goal position.
	 * @return True if there is, false otherwise.
	 */
	private boolean isCollision(int goalIndex) {
		if (goalPositions.size() < goalIndex + 1)
			return true;

		for (XRaySystem system : map.getMapComponents(XRaySystem.class))
			if (Utilities.getDistance(goalPositions.get(goalIndex), system) < 2)
				return true;

		return Utilities.isLineCollision(movementModel.getPosition(), goalPositions.get(goalIndex),
				map.getMapComponents(PhysicalObject.class));
	}

	/**
	 * Determines if the agent is queuing.
	 * 
	 * @return True if it is, false otherwise.
	 */
	private boolean isQueuing() {
		for (Activity a : activityModule.getActiveActivities()) {
			if (a instanceof QueueActivity) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the agent is in a queuing area.
	 * 
	 * @return True if it is, false otherwise.
	 */
	private boolean inQueuingArea() {
		for (Area a : observationModule.getObservation(Area.class)) {
			if (a instanceof QueuingArea)
				return true;
		}
		return false;
	}

	/**
	 * Determines if the agent is stuck for a given number of seconds. Stuck is
	 * defined as moving at most 0.2m in <i>secondsStuck</i> seconds.
	 * 
	 * @param secondsStuck
	 *            The number of seconds it takes before an agent is considered
	 *            stuck.
	 * @return True if the agent is stuck, false otherwise.
	 */
	public boolean isStuck(double secondsStuck) {
		return stuckDetector.isStuck(secondsStuck);
	}

	/**
	 * Checks if the goal at a specified index is reached.
	 * 
	 * @param goalIndex
	 *            The index.
	 * @return True if reached, false otherwise
	 */
	private boolean reachedGoal(int goalIndex) {
		if (goalPositions.size() < goalIndex + 1)
			return false;
		return movementModel.getPosition().distanceTo(goalPositions.get(goalIndex)) < 0.3;
	}

	/**
	 * Remove a number of goals from the list.
	 * 
	 * @param numberOfGoals
	 *            The number of goals.
	 */
	protected void removeGoals(int numberOfGoals) {
		for (int i = 0; i < numberOfGoals; i++) {
			if (!goalPositions.isEmpty()) {
				goalPositions.remove(0);
			}
		}
	}

	/**
	 * Sets the goal position.
	 * 
	 * @param position
	 *            The position.
	 */
	public void setGoal(Position position) {
		stuckDetector.reset();
		goalPositions.clear();
		if (!position.equals(Position.NO_POSITION))
			goalPositions = pathFinder.getPath(movementModel.getPosition(), position);
	}

	/**
	 * Set a short term goal position.
	 * 
	 * @param position
	 *            The position.
	 */
	public void setShortTermGoal(Position position) {
		setShortTermGoals(Arrays.asList(position));
	}

	/**
	 * Sets a set of short term positions.
	 * 
	 * @param positions
	 *            The positions.
	 */
	public void setShortTermGoals(List<Position> positions) {
		// while (!goalPositions.isEmpty()) {
		// if (goalPositions.get(0).distanceTo(movementModel.getPosition()) <
		// 0.5)
		// goalPositions.remove(0);
		// else
		// break;
		// }

		List<Position> path = pathFinder.getPath(movementModel.getPosition(), positions.get(0));
		path.addAll(positions);
		if (goalPositions.size() > 0)
			path.addAll(pathFinder.getPath(path.get(path.size() - 1), goalPositions.get(goalPositions.size() - 1)));
		goalPositions = path;
	}

	@Override
	public void update(int timeStep) {
		if (!movementModel.isSitting()) {
			// update the goals that we reached.
			updateReachedGoals(timeStep);

			// handles the behavior if the agent gets stuck somewhere.
			handleStuckBehavior(timeStep);
		}
	}

	/**
	 * Update the goals based on the place that the agent is currently located.
	 * 
	 * @param timeStep
	 *            The time step.
	 */
	protected void updateReachedGoals(int timeStep) {
		// update goal refresh time.
		goalRefreshTime += timeStep / 1000.0;

		// check if we reached original goal.
		if (reachedGoal(0))
			removeGoals(1);

		// check if we reached next goal.
		else if (reachedGoal(1)) {
			if (activityModule.getActiveActivities().size() < 1)
				removeGoals(2);
		}

		// check if we are already on our way to the next goals
		else if (activityModule.getActiveActivities().size() == 0) {
			if (goalRefreshTime > 5) {
				goalRefreshTime = 0;
				if (!isCollision(3))
					removeGoals(3);
				else if (!isCollision(2))
					removeGoals(2);
			}
		}
	}
}