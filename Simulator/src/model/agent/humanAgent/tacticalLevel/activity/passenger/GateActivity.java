package model.agent.humanAgent.tacticalLevel.activity.passenger;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.area.GateArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.Chair;
import simulation.simulation.util.Utilities;

/**
 * The sit activity is responsible for the sitting behavior of an agent.
 * 
 * @author S.A.M. Janssen
 */
public class GateActivity extends Activity {

	/**
	 * The chosen chair.
	 */
	private Chair chair;
	/**
	 * Timer to prevent checking every time.
	 */
	private double timer;
	/**
	 * Set the goal.
	 */
	private boolean setGoal;
	/**
	 * The gate area.
	 */
	private GateArea gateArea;

	/**
	 * Creates the gate activity.
	 * 
	 * @param flight
	 *            The flight.
	 */
	public GateActivity(Flight flight) {
		gateArea = flight.getGateArea();
	}

	@Override
	public boolean canStart(int timeStep) {
		if (Utilities.getDistance(movement.getPosition(), gateArea) < 0.5)
			return true;
		return false;
	}

	@Override
	public void goToActivity() {
		if (isGoingToActivity())
			return;

		if (isInProgress())
			return;

		if (isFinished())
			return;

		navigationModule.setGoal(gateArea.getCorners().get(0));
		super.goToActivity();
	}

	/**
	 * Sets a chair that the agent can sit on.
	 * 
	 */
	private void setChair() {
		for (Chair chair : observations.getObservation(Chair.class)) {
			if (Utilities.getDistance(chair.getPosition(), gateArea) < 1) {
				if (!chair.isOccupied()) {
					movement.setStopOrder(0);
					this.chair = chair;
					return;
				}
			}
		}
	}

	@Override
	public void startActivity() {
		super.startActivity();
		if (chair == null)
			setChair();

		if (chair != null) {
			navigationModule.setGoal(chair.getEntryPosition());
			setGoal = true;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void update(int timeStep) {

		if (movement.isSitting())
			return;

		// all chairs are full.
		if (chair == null) {

			// find a random position and move there
			if (timer >= 0)
				timer += timeStep / 1000.0;
			if (timer > 600) {
				setChair();
				timer = -1;
				navigationModule.setGoal(Utilities.generatePosition(gateArea.getShape()));
			}
			// stop after 2 minutes of moving
			if (timer < 0) {
				timer += timeStep / 1000.0;
				if (timer < 120) {
					setChair();
					if (chair == null)
						movement.setStopOrder(-1);
					else
						movement.setStopOrder(0);
					timer = -1;
				}
			}
		}
		// we found a chair.
		else {
			if (chair.isOccupied()) {
				setGoal = false;
				setChair();
				timer = 0;
			} else if (!((Passenger) agent).setSitDown(chair)) {
				if (!setGoal || navigationModule.getReachedGoal()) {
					navigationModule.setGoal(chair.getEntryPosition());
					setGoal = true;
				}
			}
		}
	}
}