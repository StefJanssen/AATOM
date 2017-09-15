package model.agent.humanAgent.tacticalLevel.activity.operator;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * The luggage check activity.
 * 
 * @author S.A.M. Janssen
 */
public class LuggageCheckActivity extends Activity {

	/**
	 * The luggage to search.
	 */
	private Luggage luggage;
	/**
	 * The system.
	 */
	private XRaySystem system;
	/**
	 * Passengers that are already instructed.
	 */
	private List<Passenger> alreadyInstructed;
	/**
	 * The mean service rate.
	 */
	private double meanServiceRate;

	/**
	 * Creates a luggage check activity.
	 * 
	 * @param system
	 *            The system.
	 */
	public LuggageCheckActivity(XRaySystem system) {
		this(system, 30);
	}

	/**
	 * Creates a luggage check activity.
	 * 
	 * @param system
	 *            The system.
	 * @param meanServiceRate
	 *            The mean service rate.
	 */
	public LuggageCheckActivity(XRaySystem system, double meanServiceRate) {
		this.system = system;
		this.meanServiceRate = meanServiceRate;
		alreadyInstructed = new ArrayList<>();
	}

	@Override
	public boolean canStart(int timeStep) {
		if (system.getCollectPassenger() != null) {
			if (alreadyInstructed.contains(system.getCollectPassenger()))
				return false;

			if (system.getCollectPosition().distanceTo(system.getCollectPassenger().getPosition()) < 0.5)
				return true;
		}
		return false;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	/**
	 * Sets the luggage to search.
	 * 
	 * @param luggage
	 *            The luggage.
	 */
	public void setSearch(Luggage luggage) {
		this.luggage = luggage;
	}

	@Override
	public void startActivity() {
		alreadyInstructed.add(system.getCollectPassenger());
		double myServiceRate = meanServiceRate;

		if (luggage != null) {
			if (luggage.getOwner().equals(system.getCollectPassenger())) {
				myServiceRate *= 2;
				luggage = null;
			}
		}

		system.getCollectPassenger().communicate(CommunicationType.WAIT,
				Utilities.RANDOM_GENERATOR.nextNormal(myServiceRate, myServiceRate / 10));
		super.startActivity();
	}

	@Override
	public void update(int timeStep) {
		if (system.getCollectPassenger().getStopOrder())
			return;

		endActivity();
	}

}
