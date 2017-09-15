package model.agent.humanAgent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.operationalLevel.BasicPassengerOperationalModel;
import model.agent.humanAgent.operationalLevel.OperationalModel;
import model.agent.humanAgent.strategicLevel.BasicPassengerStrategicModel;
import model.agent.humanAgent.strategicLevel.StrategicModel;
import model.agent.humanAgent.tacticalLevel.BasicPassengerTacticalModel;
import model.agent.humanAgent.tacticalLevel.TacticalModel;
import model.environment.map.Map;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.Chair;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;

/**
 * A Passenger is a type of {@link HumanAgent} that is a passenger in the
 * simulation.
 * 
 * @author S.A.M. Janssen
 */
public class Passenger extends HumanAgent {

	/**
	 * The {@link Flight} of the agent.
	 */
	private Flight flight;
	/**
	 * The {@link Luggage} of the agent.
	 */
	private Collection<Luggage> luggage;
	/**
	 * Facility type.
	 */
	private Class<? extends Facility> facility;
	/**
	 * Facility visit or not.
	 */
	private boolean checkedIn;

	/**
	 * Creates a passenger with a specified {@link BasicPassengerStrategicModel}
	 * , {@link BasicPassengerTacticalModel}, and
	 * {@link BasicPassengerOperationalModel} and some {@link Luggage}.
	 * 
	 * @param map
	 *            The map it is on.
	 * @param flight
	 *            The flight the agent is on.
	 * @param checkedIn
	 *            Checked in or not.
	 * @param facility
	 *            Facility visit or not.
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param luggage
	 *            The Baggage.
	 * @param color
	 *            The color.
	 */
	public Passenger(Map map, Flight flight, boolean checkedIn, Class<? extends Facility> facility, Position position,
			double radius, double mass, Luggage luggage, Color color) {
		this(map, flight, checkedIn, facility, position, radius, mass, luggage,
				new BasicPassengerStrategicModel(facility, flight), new BasicPassengerTacticalModel(map, flight),
				new BasicPassengerOperationalModel(map, 1), color);

	}

	/**
	 * Creates a passenger with a specified {@link StrategicModel} and
	 * {@link OperationalModel} and some {@link Luggage}.
	 * 
	 * @param map
	 *            The map it is on.
	 * @param flight
	 *            The flight the agent is on.
	 * @param checkedIn
	 *            Checked in or not.
	 * @param facility
	 *            Facility visit or not.
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param luggage
	 *            The Baggage.
	 * @param strategicModel
	 *            The strategic model.
	 * @param tacticalModel
	 *            The tactical model.
	 * @param operationalModel
	 *            The operational model.
	 * @param color
	 *            The color.
	 */
	public Passenger(Map map, Flight flight, boolean checkedIn, Class<? extends Facility> facility, Position position,
			double radius, double mass, Luggage luggage, StrategicModel strategicModel, TacticalModel tacticalModel,
			OperationalModel operationalModel, Color color) {
		super(position, radius, mass, strategicModel, tacticalModel, operationalModel, color);
		this.flight = flight;
		this.checkedIn = checkedIn;
		this.luggage = new ArrayList<>();
		this.luggage.add(luggage);
		this.facility = facility;
		for (Luggage l : this.luggage)
			l.setOwner(this);
	}

	/**
	 * Creates a passenger with a specified {@link StrategicModel} and
	 * {@link OperationalModel} with default color red.
	 * 
	 * @param map
	 *            The map it is on.
	 * @param flight
	 *            The flight the agent is on.
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 */
	public Passenger(Map map, Flight flight, Position position, double radius, double mass) {
		this(map, flight, false, null, position, radius, mass, Luggage.NO_LUGGAGE, Color.RED);
	}

	/**
	 * Creates a passenger.
	 * 
	 * @param map
	 *            The map it is on.
	 * @param flight
	 *            The flight the agent is on.
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param color
	 *            The color.
	 */
	public Passenger(Map map, Flight flight, Position position, double radius, double mass, Color color) {
		this(map, flight, false, null, position, radius, mass, Luggage.NO_LUGGAGE, color);
	}

	/**
	 * Gets checked in flag.
	 * 
	 * @return True if checked in, false otherwise.
	 */
	public boolean getCheckedIn() {
		return checkedIn;
	}

	/**
	 * Gets facility type.
	 * 
	 * @return The facility type.
	 */
	public Class<? extends Facility> getFacilityVisit() {
		return facility;
	}

	/**
	 * Gets the {@link Flight} the passenger is on.
	 * 
	 * @return The flight.
	 */
	public Flight getFlight() {
		return flight;
	}

	/**
	 * Gets the {@link Luggage}.
	 * 
	 * @return The luggage.
	 */
	public Collection<Luggage> getLuggage() {
		return luggage;
	}

	/**
	 * Indicates if the agent has a stop order.
	 * 
	 * @return True if he is ordered to stop, false otherwise.
	 */
	public boolean getStopOrder() {
		return operationalModel.getStopOrder();
	}

	/**
	 * Determines if the agent is queuing.
	 * 
	 * @return True if he is queuing, false otherwise.
	 */
	public boolean isQueuing() {
		return tacticalModel.isQueuing();
	}

	/**
	 * Determines if the agent is sitting.
	 * 
	 * @return True if the agent is sitting, false otherwise.
	 */
	public boolean isSitting() {
		return operationalModel.isSitting();
	}

	/**
	 * TODO this method should be transferred to the operational level.
	 * 
	 * Makes the agent sit on a chair. It will only sit down if it is close
	 * enough.
	 * 
	 * @param chair
	 *            The chair.
	 * @return True if it was successful, false otherwise.
	 */
	@Deprecated
	public boolean setSitDown(Chair chair) {
		Position temp = operationalModel.setSitDown(chair);
		if (!temp.equals(Position.NO_POSITION)) {
			Position oldPos = position;
			for (Luggage l : luggage) {
				if (!l.equals(Luggage.NO_LUGGAGE) && l.getPosition().equals(oldPos))
					l.setPosition(temp);
			}
			position = temp;
			operationalModel.setStopOrder(-1);
			chair.setOccupied(this);
			tacticalModel.setGoal(Position.NO_POSITION);
			return true;
		}
		return false;
	}

	@Override
	public void update(int timeStep) {
		Position prev = position;
		super.update(timeStep);
		for (Luggage l : luggage) {
			if (l.getPosition().equals(prev))
				l.setPosition(position);
		}
	}
}
