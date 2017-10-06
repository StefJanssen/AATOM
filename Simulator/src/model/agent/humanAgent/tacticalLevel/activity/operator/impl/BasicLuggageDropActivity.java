package model.agent.humanAgent.tacticalLevel.activity.operator.impl;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.tacticalLevel.activity.operator.LuggageDropActivity;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import simulation.simulation.util.Utilities;

/**
 * The luggage drop activity.
 * 
 * @author S.A.M. Janssen
 */
public class BasicLuggageDropActivity extends LuggageDropActivity {

	/**
	 * The time mean service rate.
	 */
	private double meanServiceRate;
	/**
	 * The system.
	 */
	private XRaySystem system;
	/**
	 * Passengers that are already instructed.
	 */
	private List<Passenger> alreadyInstructed;

	/**
	 * Creates a luggage drop activity.
	 * 
	 * @param system
	 *            The system.
	 */
	public BasicLuggageDropActivity(XRaySystem system) {
		this(system, 45);
	}

	/**
	 * Creates a luggage drop activity.
	 * 
	 * @param system
	 *            The system.
	 * @param meanServiceRate
	 *            The mean service rate.
	 */
	public BasicLuggageDropActivity(XRaySystem system, double meanServiceRate) {
		this.system = system;
		this.meanServiceRate = meanServiceRate;
		alreadyInstructed = new ArrayList<>();
	}

	@Override
	public boolean canStart(int timeStep) {
		if (system.getDropOffPassenger() != null) {
			if (alreadyInstructed.contains(system.getDropOffPassenger()))
				return false;

			if (system.getDropOffPosition().distanceTo(system.getDropOffPassenger().getPosition()) < 0.5)
				return true;
		}
		return false;
	}

	@Override
	public Position getActivityPosition() {
		return Position.NO_POSITION;
	}

	@Override
	public void startActivity() {
		super.startActivity();
		alreadyInstructed.add(system.getDropOffPassenger());
		system.getDropOffPassenger().communicate(CommunicationType.WAIT,
				Utilities.RANDOM_GENERATOR.nextNormal(meanServiceRate, meanServiceRate / 10));
	}

	@Override
	public void update(int timeStep) {
		endActivity();
	}

}
