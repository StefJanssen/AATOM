package model.agent.humanAgent.tacticalLevel;

import model.agent.humanAgent.strategicLevel.StrategicModel;
import model.agent.humanAgent.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.tacticalLevel.navigation.PassengerNavigationModule;
import model.environment.map.Map;
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
	 * @param map
	 *            The map.
	 * @param flight
	 *            The flight.
	 */
	public BasicPassengerTacticalModel(Map map, Flight flight) {
		super(new ActivityModule(), new PassengerNavigationModule(map, flight));
	}
}