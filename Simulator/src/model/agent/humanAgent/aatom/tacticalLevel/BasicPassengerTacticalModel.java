package model.agent.humanAgent.aatom.tacticalLevel;

import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.PassengerNavigationModule;
import model.environment.objects.flight.Flight;

/**
 * A basic high level model is the most basic version of a
 * {@link StrategicModel}. It forms the most basic practical implementation of
 * the concept.
 * 
 * @author S.A.M. Janssen
 *
 */
public class BasicPassengerTacticalModel extends TacticalModel {

	/**
	 * Creates a basic high level model with a certain desired speed.
	 * 
	 * @param flight
	 *            The flight.
	 */
	public BasicPassengerTacticalModel(Flight flight) {
		super(new ActivityModule(), new PassengerNavigationModule(flight));
	}
}