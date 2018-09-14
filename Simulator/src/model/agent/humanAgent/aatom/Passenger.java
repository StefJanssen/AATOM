package model.agent.humanAgent.aatom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.BasicPassengerOperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.strategicLevel.BasicPassengerStrategicModel;
import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.tacticalLevel.BasicPassengerTacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.TacticalModel;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.position.Position;
import util.math.distributions.MathDistribution;
import util.math.distributions.NormalDistribution;

/**
 * A Passenger is a type of {@link HumanAgent} that is a passenger in the
 * simulation.
 * 
 * @author S.A.M. Janssen
 */
public class Passenger extends AatomHumanAgent {

	/**
	 * Builder class for passenger. Inheritance based on this post:
	 * https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-
	 * class
	 * 
	 * @author S.A.M. Janssen
	 * @param <T>
	 *            The subclass of the builder.
	 */
	@SuppressWarnings("unchecked")
	public static class Builder<T extends Builder<T>> {

		protected Flight flight = Flight.NO_FLIGHT;
		protected boolean checkedIn = false;
		protected Class<? extends Facility> facility = null;
		protected Position position = Position.NO_POSITION;
		protected double radius = 0.2;
		protected double mass = 80;
		protected double desiredSpeed = 1;
		protected Collection<Luggage> luggage = new ArrayList<>();
		protected StrategicModel strategicModel;
		protected TacticalModel tacticalModel;
		protected OperationalModel operationalModel;
		protected Color color = Color.RED;
		protected MathDistribution checkPointDropTime = new NormalDistribution(54.6, 36.09);
		protected MathDistribution checkPointCollectTime = new NormalDistribution(71.5, 54.95);

		/**
		 * Creates the passenger.
		 * 
		 * @return The passenger.
		 */
		public Passenger build() {
			if (strategicModel == null)
				strategicModel = new BasicPassengerStrategicModel(facility, checkedIn, checkPointDropTime,
						checkPointCollectTime, flight);

			if (tacticalModel == null)
				tacticalModel = new BasicPassengerTacticalModel(flight);

			if (operationalModel == null)
				operationalModel = new BasicPassengerOperationalModel(desiredSpeed);

			return new Passenger(flight, checkedIn, facility, position, radius, mass, luggage, strategicModel,
					tacticalModel, operationalModel, color);
		}

		/**
		 * Set checked in.
		 * 
		 * @param checkedIn
		 *            Checked in or not.
		 * @return The builder.
		 */
		public T setCheckedIn(boolean checkedIn) {
			this.checkedIn = checkedIn;
			return (T) this;
		}

		/**
		 * Set the color.
		 * 
		 * @param color
		 *            The color.
		 * @return The builder.
		 */
		public T setColor(Color color) {
			this.color = color;
			return (T) this;
		}

		/**
		 * Set the facility to visit.
		 * 
		 * @param facility
		 *            The facility.
		 * @return The builder.
		 */
		public T setFacility(Class<? extends Facility> facility) {
			this.facility = facility;
			return (T) this;
		}

		/**
		 * Set the {@link Flight}.
		 * 
		 * @param flight
		 *            The flight.
		 * @return The builder.
		 */
		public T setFlight(Flight flight) {
			this.flight = flight;
			return (T) this;
		}

		/**
		 * Set the luggage.
		 * 
		 * @param luggage
		 *            The luggage.
		 * @return The builder.
		 */
		public T setLuggage(Collection<Luggage> luggage) {
			this.luggage = luggage;
			return (T) this;
		}

		/**
		 * Set the luggage.
		 * 
		 * @param luggage
		 *            The luggage.
		 * @return The builder.
		 */
		public T setLuggage(Luggage luggage) {
			this.luggage.add(luggage);
			return (T) this;
		}

		/**
		 * Set the mass.
		 * 
		 * @param mass
		 *            The mass.
		 * @return The builder.
		 */
		public T setMass(double mass) {
			this.mass = mass;
			return (T) this;
		}

		/**
		 * Set the operational model.
		 * 
		 * @param operationalModel
		 *            The operational model.
		 * @return The builder.
		 */
		public T setOperationalModel(OperationalModel operationalModel) {
			this.operationalModel = operationalModel;
			return (T) this;
		}

		/**
		 * Set the {@link Position}.
		 * 
		 * @param position
		 *            The position.
		 * @return The builder.
		 */
		public T setPosition(Position position) {
			this.position = position;
			return (T) this;
		}

		/**
		 * Set the radius.
		 * 
		 * @param radius
		 *            The radius.
		 * @return The builder.
		 */
		public T setRadius(double radius) {
			this.radius = radius;
			return (T) this;
		}

		/**
		 * Set the strategic model.
		 * 
		 * @param strategicModel
		 *            The strategic model.
		 * @return The builder.
		 */
		public T setStrategicModel(StrategicModel strategicModel) {
			this.strategicModel = strategicModel;
			return (T) this;
		}

		/**
		 * Set the tactical model.
		 * 
		 * @param tacticalModel
		 *            The tactical model.
		 * @return The builder.
		 */
		public T setTacticalModel(TacticalModel tacticalModel) {
			this.tacticalModel = tacticalModel;
			return (T) this;
		}

	}

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
	 * Creates a passenger with a specified {@link StrategicModel} and
	 * {@link OperationalModel} and some {@link Luggage}.
	 * 
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
	protected Passenger(Flight flight, boolean checkedIn, Class<? extends Facility> facility, Position position,
			double radius, double mass, Collection<Luggage> luggage, StrategicModel strategicModel,
			TacticalModel tacticalModel, OperationalModel operationalModel, Color color) {
		super(position, radius, mass, strategicModel, tacticalModel, operationalModel, color);
		if (position.equals(Position.NO_POSITION) || flight.equals(Flight.NO_FLIGHT))
			throw new IllegalArgumentException("No position and/or flight given.");

		this.flight = flight;
		this.checkedIn = checkedIn;
		if (checkedIn)
			flight.checkIn(this);
		this.luggage = luggage;
		if (luggage.isEmpty())
			luggage.add(Luggage.NO_LUGGAGE);
		this.facility = facility;
		for (Luggage l : this.luggage)
			l.setOwner(this);

		if (checkedIn) {
			for (Luggage l : this.luggage) {
				if (l.getLuggageType().equals(LuggageType.CHECKED))
					throw new RuntimeException("You cannot be checked in already if you have checked luggage as well.");
			}
		}
	}

	/**
	 * Checks if the passenger is checked in.
	 * 
	 * @return True if checked in, false otherwise.
	 */
	public boolean isCheckedIn() {
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

	@Override
	public void update(int timeStep) {
		Position prev = position;
		super.update(timeStep);

		// move luggage with the passenger
		for (Luggage l : luggage) {
			if (l.getPosition().equals(prev))
				l.setPosition(position);
		}
	}
}
