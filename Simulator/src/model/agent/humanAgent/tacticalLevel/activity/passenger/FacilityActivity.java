package model.agent.humanAgent.tacticalLevel.activity.passenger;

import java.util.List;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.area.Facility;
import model.environment.objects.area.Restaurant;
import model.environment.objects.area.Shop;
import model.environment.objects.area.Toilet;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * The shop activity handles all shop related activity for an agent.
 * 
 * @author S.A.M. Janssen
 */
public class FacilityActivity extends Activity {

	/**
	 * The current facility.
	 */
	private Facility currentFacility;
	/**
	 * The type of facility.
	 */
	private Class<? extends Facility> facilityType;
	/**
	 * Waited or not.
	 */
	private boolean waited;
	/**
	 * The restaurant time.
	 */
	private float restaurantTime;
	/**
	 * The toilet time.
	 */
	private float toiletTime;

	/**
	 * Creates a shop activity.
	 * 
	 * @param facilityType
	 *            The facility type the agent will visit.
	 */
	public FacilityActivity(Class<? extends Facility> facilityType) {
		this(facilityType, 1200, 120);
	}

	/**
	 * Creates a shop activity.
	 * 
	 * @param facilityType
	 *            The facility type the agent will visit.
	 * @param restaurantTime
	 *            The restaurant time.
	 * @param toiletTime
	 *            The toilet time.
	 * 
	 */
	public FacilityActivity(Class<? extends Facility> facilityType, float restaurantTime, float toiletTime) {
		this.facilityType = facilityType;
		this.restaurantTime = restaurantTime;
		this.toiletTime = toiletTime;
	}

	@Override
	public boolean canStart(int timeStep) {
		if (currentFacility == null)
			return false;

		if (isInProgress())
			return false;

		if (Utilities.getDistance(movement.getPosition(), currentFacility) < 5)
			return true;

		return false;
	}

	@Override
	public Position getActivityPosition() {
		if (currentFacility == null)
			return Position.NO_POSITION;
		return currentFacility.getPosition();
	}

	/**
	 * Determine if there is a goal position in the current shop.
	 * 
	 * @return True if there is, false otherwise.
	 */
	private boolean getGoalPositionInShop() {
		if (currentFacility == null)
			return false;

		for (Position p : navigationModule.getGoalPositions()) {
			if (currentFacility.getShape().contains(p.x, p.y)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void goToActivity() {
		if (isGoingToActivity())
			return;

		if (isInProgress())
			return;

		setFacility();
		if (currentFacility == null) {
			endActivity();
			return;
		}
		navigationModule.setGoal(currentFacility.getPosition());
		super.goToActivity();

	}

	/**
	 * Sets the facility.
	 */
	private void setFacility() {
		if (currentFacility != null)
			return;

		double distance = Float.MAX_VALUE;
		for (Facility f : observations.getObservation(facilityType)) {
			double tempDistance = Utilities.getDistance(movement.getPosition(), f);
			if (tempDistance < distance) {
				currentFacility = f;
				distance = tempDistance;
			}
		}
	}

	@Override
	public void startActivity() {
		super.startActivity();

		if (currentFacility instanceof Shop) {
			List<Position> inAreaPoints = Utilities.generatePositions(10, currentFacility.getShape());
			navigationModule.setGoal(Position.NO_POSITION);
			navigationModule.setShortTermGoals(inAreaPoints);
		} else {
			navigationModule.setGoal(Utilities.generatePosition(currentFacility.getShape()));
		}
	}

	@Override
	public void update(int timeStep) {
		if (currentFacility instanceof Shop) {
			if (!getGoalPositionInShop())
				endActivity();
		} else if (currentFacility instanceof Restaurant) {
			if (navigationModule.getReachedGoal()) {
				if (!waited) {
					movement.setStopOrder(restaurantTime);
					waited = true;
				} else if (!movement.getStopOrder())
					endActivity();
			} else if (!waited && navigationModule.isStuck(10))
				navigationModule.setGoal(Utilities.generatePosition(currentFacility.getShape()));
		} else if (currentFacility instanceof Toilet) {
			if (navigationModule.getReachedGoal()) {
				if (!waited) {
					movement.setStopOrder(toiletTime);
					waited = true;
				} else if (!movement.getStopOrder())
					endActivity();
			} else if (!waited && navigationModule.isStuck(10))
				navigationModule.setGoal(Utilities.generatePosition(currentFacility.getShape()));
		}
	}
}
