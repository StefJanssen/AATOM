package model.environment.objects.physicalObject.luggage;

import model.agent.humanAgent.Passenger;
import model.environment.position.Position;
import model.map.shapes.CircularMapComponent;

/**
 * Luggage is something that is carried and owned by a {@link Passenger}.
 * Luggage has a complexity and threat level.
 * 
 * @author S.A.M. Janssen
 */
public class Luggage extends CircularMapComponent {

	/**
	 * Holder to indicate no luggage.
	 */
	public static final Luggage NO_LUGGAGE = new Luggage(LuggageType.CARRY_ON, -1, -1);
	/**
	 * The threat level of the luggage.
	 */
	private final float threatLevel;
	/**
	 * The owner of the luggage.
	 */
	private Passenger owner;
	/**
	 * The type of luggage.
	 */
	private final LuggageType type;
	/**
	 * The complexity of the bag.
	 */
	private final float complexity;

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
		super(Position.NO_POSITION, 0.1);
		this.type = type;
		this.threatLevel = (float) threatLevel;
		this.complexity = (float) complexity;
	}

	/**
	 * Gets the complexity.
	 * 
	 * @return The complexity.
	 */
	public float getComplexity() {
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
	 * Gets the owner of the luggage.
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
	public float getThreatLevel() {
		return threatLevel;
	}

	/**
	 * Sets the owner of the luggage.
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
	 * Sets the position of the luggage.
	 * 
	 * @param position
	 *            The position.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
}