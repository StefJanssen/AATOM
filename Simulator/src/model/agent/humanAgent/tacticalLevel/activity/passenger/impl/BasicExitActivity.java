package model.agent.humanAgent.tacticalLevel.activity.passenger.impl;

import java.util.Collection;

import model.agent.humanAgent.tacticalLevel.activity.passenger.ExitActivity;
import model.environment.objects.area.EntranceArea;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * The exit activity allows the departing passenger to leave the airport.
 * 
 * @author S.A.M. Janssen
 */
public class BasicExitActivity extends ExitActivity {

	@Override
	public boolean canStart(int timeStep) {
		return true;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void goToActivity() {
		return;
	}

	@Override
	public void startActivity() {
		Collection<EntranceArea> entrances = observations.getObservation(EntranceArea.class);
		double distance = Double.MAX_VALUE;
		EntranceArea goToArea = null;
		for (EntranceArea area : entrances) {
			double d = Utilities.getDistance(movement.getPosition(), area);
			if (d < distance) {
				distance = d;
				goToArea = area;
			}
		}
		navigationModule.setGoal(Utilities.generatePosition(goToArea.getShape()));
		super.startActivity();
	}

	@Override
	public void update(int timeStep) {
		if (navigationModule.getReachedGoal())
			endActivity();
	}

}