package model.agent.humanAgent.tacticalLevel.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.tacticalLevel.navigation.pathfinder.JumpPointSearchPathFinder;
import model.agent.humanAgent.tacticalLevel.navigation.pathfinder.PathFinder;
import model.environment.map.Map;
import model.environment.objects.area.Area;
import model.environment.objects.area.Facility;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.QueueSeparator;
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
public abstract class NavigationModule implements Updatable {

	/**
	 * The total time stuck
	 */
	private double completeStuckTime;
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
	 * The previous position is the previous position of the agent.
	 */
	private Position previousPosition;
	/**
	 * Timer to indicate out how long the agent is stuck in a position.
	 */
	protected double timeStuck;
	/**
	 * The total time stuck of an agent. This timer resets if there is a new
	 * goal set.
	 */
	private double totalTimeStuck;
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
	 * Generates a position close to the agent.
	 * 
	 * @return The position.
	 */
	private Position generateClosePosition() {
		double xOffset = Utilities.RANDOM_GENERATOR.nextDouble() - .5;
		double yOffset = Utilities.RANDOM_GENERATOR.nextDouble() - .5;
		Position newPos = new Position(movementModel.getPosition().x + xOffset,
				movementModel.getPosition().y + yOffset);
		List<Position> positions = pathFinder.getPath(movementModel.getPosition(), newPos);
		double distance = 0.0;
		for (int i = 0; i < positions.size() - 1; i++) {
			distance += positions.get(i).distanceTo(positions.get(i + 1));
		}
		if (distance > 3) {
			return generateClosePosition();
		}
		return newPos;
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
		int multiplier = 1;
		if (isInQueue())
			multiplier = 5;

		updateStuckPosition(timeStep);

		if (activityModule.getActiveActivities().size() != 0 && multiplier == 1)
			return;

		for (Facility f : map.getMapComponents(Facility.class)) {
			if (positionsInShape(getGoalPositions(), f))
				return;
		}

		if (multiplier == 5) {
			if (!getReachedGoal()) {
				if (Utilities.isLineCollision(movementModel.getPosition(), getGoalPositions().get(0),
						map.getMapComponents(QueueSeparator.class)))
					setGoal(getGoalPositions().get(getGoalPositions().size() - 1));
			}
		}

		if (isStuck(4 * multiplier)) {
			if (!getReachedGoal()) {
				timeStuck = 0.0;
				setShortTermGoal(getGoalPosition());
			}
		}

		// generates a random position close by to get out of a locked position.
		if (totalTimeStuck > 7 * multiplier) {
			boolean veryBusy = isVeryBusy();
			if ((veryBusy && totalTimeStuck > 10) || !veryBusy) {
				if (!getReachedGoal()) {
					Position p = generateClosePosition();
					setShortTermGoal(p);
					timeStuck = 0;
					totalTimeStuck = 0;
				}
			}
		}

		if (completeStuckTime > 45 * multiplier) {
			if (!getReachedGoal()) {
				// agent.updateStuckPosition();
				timeStuck = 0;
				totalTimeStuck = 0;
				completeStuckTime = 0;
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
		previousPosition = movementModel.getPosition();
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
	private boolean isInQueue() {
		Collection<Area> areas = observationModule.getObservation(Area.class);
		for (Area a : areas) {
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
		if (movementModel.getStopOrder()) {
			return false;
		}

		if (timeStuck > secondsStuck) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if it is very busy around the agent.
	 * 
	 * @return True if it is busy, false otherwise.
	 */
	private boolean isVeryBusy() {
		return observationModule.getObservation(HumanAgent.class).size() > 20;
	}

	/**
	 * Determines if a position is located in a facility.
	 * 
	 * @param p
	 *            The position.
	 * @param facility
	 *            The facility.
	 * @return True if it is, false otherwise.
	 */
	private boolean positionInShape(Position p, Area facility) {
		return facility.getShape().contains(p.x, p.y);
	}

	/**
	 * Determines if any of the positions in a list is located in a facility.
	 * 
	 * @param positions
	 *            The positions.
	 * @param facility
	 *            The facility.
	 * @return True if any of the positions is in the facility, false otherwise.
	 */
	private boolean positionsInShape(List<Position> positions, Area facility) {
		for (Position p : positions) {
			if (positionInShape(p, facility))
				return true;
		}
		return false;
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
		timeStuck = 0.0;
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
		while (!goalPositions.isEmpty()) {
			if (goalPositions.get(0).distanceTo(movementModel.getPosition()) < 0.5)
				goalPositions.remove(0);
			else
				break;
		}
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

	/**
	 * Updates the stuck parameters.
	 * 
	 * @param timeStep
	 *            The time step in the simulation.
	 */
	private void updateStuckPosition(int timeStep) {
		if (movementModel.getStopOrder() || movementModel.isSitting())
			return;

		if (movementModel.getPosition().distanceTo(previousPosition) < 0.2) {
			timeStuck += (timeStep / 1000.0);
			totalTimeStuck += (timeStep / 1000.0);
			completeStuckTime += (timeStep / 1000.0);
		} else {
			timeStuck = 0;
			totalTimeStuck = 0;
			completeStuckTime = 0;
			previousPosition = movementModel.getPosition();
		}
	}
}