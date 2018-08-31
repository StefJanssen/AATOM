package model.agent.humanAgent.tacticalLevel.navigation;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.agent.humanAgent.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.position.Position;
import model.map.Map;

/**
 * The Passenger goal activity determines and updates the goals for a passenger
 * in the airport.
 * 
 * @author S.A.M. Janssen
 */
public class PassengerNavigationModule extends NavigationModule {

	/**
	 * Check in goals.
	 */
	private List<Position> checkInGoals;
	/**
	 * The flight.
	 */
	private Flight flight;

	/**
	 * Create a goal activity.
	 * 
	 * @param flight
	 *            The flight.
	 * 
	 */
	public PassengerNavigationModule(Flight flight) {
		this(flight, new ArrayList<Position>());
	}

	/**
	 * Creates a goal activity.
	 * 
	 * @param flight
	 *            The flight.
	 * @param goalPositions
	 *            The goal positions.
	 */
	public PassengerNavigationModule(Flight flight, List<Position> goalPositions) {
		super(goalPositions);
		this.flight = flight;
	}

	/**
	 * Set the goal position if we are doing a check-in.
	 * 
	 * @param position
	 *            The position from where the goal is set.
	 * @return The goal positions.
	 */
	private List<Position> getCheckInGoal(Position position) {
		List<Position> positions = new ArrayList<>();
		Position current = position;

		for (int i = checkInGoals.size() - 1; i >= 0; i--) {
			List<Position> positions2 = pathFinder.getPath(current, checkInGoals.get(i), false);
			current = checkInGoals.get(i);
			positions.addAll(positions2);
		}

		if (positions.isEmpty())
			positions = pathFinder.getPath(position, flight.getCheckInQueue().getLeavingPosition());

		return positions;

	}

	@Override
	public void init(Map map, MovementModel movementModel, ActivityModule activityModule,
			ObservationModule observation) {
		checkInGoals = new ArrayList<>();
		if (!flight.equals(Flight.NO_FLIGHT)) {
			QueuingArea area = flight.getCheckInQueue();
			float xDiff = -area.getEntrancePosition().x + area.getLeavingPosition().x;
			float yDiff = -area.getEntrancePosition().y + area.getLeavingPosition().y;

			Position middle = new Position(area.getEntrancePosition().x + xDiff / 2,
					area.getEntrancePosition().y + yDiff / 2);

			checkInGoals.add(area.getLeavingPosition().clone());
			checkInGoals.add(middle);
			checkInGoals.add(area.getEntrancePosition().clone());
		}
		super.init(map, movementModel, activityModule, observation);
	}

	@Override
	protected void removeGoals(int numberOfGoals) {
		for (int i = 0; i < numberOfGoals; i++) {
			if (!goalPositions.isEmpty()) {
				Position p = goalPositions.remove(0);
				if (!checkInGoals.isEmpty()) {
					if (p.distanceTo(checkInGoals.get(checkInGoals.size() - 1)) < 0.5) {
						checkInGoals.remove(checkInGoals.size() - 1);
					}
				}
			}
		}
	}

	@Override
	public void setGoal(Position position) {
		if (position != null && !flight.equals(Flight.NO_FLIGHT)
				&& position.distanceTo(flight.getCheckInQueue().getLeavingPosition()) < 0.5)
			goalPositions = getCheckInGoal(movementModel.getPosition());
		else
			super.setGoal(position);
	}

	@Override
	public void setShortTermGoal(Position position) {
		if (flight.equals(Flight.NO_FLIGHT)) {
			super.setShortTermGoal(position);
			return;
		}

		if (!goalPositions.isEmpty() && goalPositions.get(goalPositions.size() - 1)
				.distanceTo(flight.getCheckInQueue().getLeavingPosition()) < 0.5) {
			List<Position> positions = getCheckInGoal(position);
			positions.add(0, position);
			super.setShortTermGoals(positions);
		} else
			super.setShortTermGoal(position);
	}

	@Override
	public void setShortTermGoals(List<Position> positions) {
		if (!goalPositions.isEmpty()) {
			if (flight.equals(Flight.NO_FLIGHT)) {
				super.setShortTermGoals(positions);
				return;
			}

			if (goalPositions.get(goalPositions.size() - 1)
					.distanceTo(flight.getCheckInQueue().getLeavingPosition()) < 0.5) {
				List<Position> positions2 = getCheckInGoal(positions.get(positions.size() - 1));
				positions2.addAll(0, positions);
				super.setShortTermGoals(positions2);
			} else
				super.setShortTermGoals(positions);
		} else
			super.setShortTermGoals(positions);

	}
}