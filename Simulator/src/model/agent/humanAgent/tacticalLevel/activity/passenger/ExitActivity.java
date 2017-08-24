package model.agent.humanAgent.tacticalLevel.activity.passenger;

import java.util.Collection;

import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.area.EntranceArea;
import simulation.simulation.util.Utilities;

/**
 * The exit activity allows the departing passenger to leave the airport.
 * 
 * @author S.A.M. Janssen
 */
public class ExitActivity extends Activity {

	@Override
	public boolean canStart(int timeStep) {
		return true;
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
