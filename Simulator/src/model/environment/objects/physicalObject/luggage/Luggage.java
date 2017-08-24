package model.environment.objects.physicalObject.luggage;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.environment.map.MapComponent;
import model.environment.position.Position;

/**
 * Luggage is something that is carried and owned by a {@link HumanAgent}.
 * Luggage has a complexity and threat level.
 * 
 * @author S.A.M. Janssen
 */
public class Luggage extends MapComponent {

	/**
	 * Holder to indicate no baggage.
	 */
	public static final Luggage NO_LUGGAGE = new Luggage(LuggageType.CARRY_ON, -1, -1);
	/**
	 * The threat level of the baggage.
	 */
	private double threatLevel;
	/**
	 * The owner of the baggage.
	 */
	private Passenger owner;
	/**
	 * The type of luggage.
	 */
	private LuggageType type;
	/**
	 * The complexity of the bag.
	 */
	private double complexity;

	/**
	 * Creates the luggage.
	 * 
	 * @param type
	 *            The type.
	 * 
	 * @param threatLevel
	 *            The threat level of the luggage.
	 * @param complexity
	 *            The luggage complexity.
	 */
	public Luggage(LuggageType type, double threatLevel, double complexity) {
		super(Position.NO_POSITION);
		this.type = type;
		this.threatLevel = threatLevel;
		this.complexity = complexity;
	}

	/**
	 * Gets the complexity.
	 * 
	 * @return The complexity.
	 */
	public double getComplexity() {
		return complexity;
	}

	/**
	 * Gets the luggage type.
	 * 
	 * @return The type.
	 */
	public LuggageType getLuggageType() {
		return type;
	}

	/**
	 * Gets the owner of the baggage.
	 * 
	 * @return The owner.
	 */
	public Passenger getOwner() {
		return owner;
	}

	/**
	 * Gets the threat level.
	 * 
	 * @return The threat level.
	 */
	public double getThreatLevel() {
		return threatLevel;
	}

	/**
	 * Sets the owner of the baggage.
	 * 
	 * @param owner
	 *            The owner.
	 */
	public void setOwner(Passenger owner) {
		this.owner = owner;
		// We assume that the position of the baggage is the same as its owner.
		// At least, at initialization.
		position = owner.getPosition();
	}

	/**
	 * Sets the position of the baggage.
	 * 
	 * @param position
	 *            The position.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
}