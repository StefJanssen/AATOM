package model.agent.humanAgent;

import java.awt.Color;

import model.agent.Agent;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.environment.position.Position;

/**
 * A HumanAgent is a human that has a color and a mass. It can communicate and
 * do observations. The HumanAgent acts autonomously and bases its decisions on
 * its surroundings and its internal (high level) state.
 * 
 * @author S.A.M. Janssen
 */
public abstract class HumanAgent extends Agent {

	/**
	 * The {@link Color} of the agent.
	 */
	protected Color color;
	/**
	 * The mass (kilograms) of the agent.
	 */
	protected double mass;

	/**
	 * Creates a human agent.
	 * 
	 * @param position
	 *            The position on the map.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param color
	 *            The color.
	 */
	public HumanAgent(Position position, double radius, double mass, Color color) {
		super(position, radius);
		if (mass < 0)
			throw new IllegalArgumentException("Mass cannot be negative.");
		if (color == null)
			throw new IllegalArgumentException("Color cannot be null");
		this.mass = mass;
		this.color = color;
	}

	/**
	 * Communicate.
	 * 
	 * @param type
	 *            The type of communication.
	 * @param communication
	 *            The communication.
	 */
	public abstract void communicate(CommunicationType type, Object communication);

	/**
	 * Gets the {@link Color}.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the mass.
	 * 
	 * @return The mass.
	 */
	public double getMass() {
		return mass;
	}

	@Override
	public String toString() {
		return Integer.toString(hashCode());
	}
}