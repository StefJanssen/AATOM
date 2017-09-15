package model.agent.humanAgent;

import java.awt.Color;

import model.agent.humanAgent.operationalLevel.BasicOperatorOperationalModel;
import model.agent.humanAgent.strategicLevel.BasicOperatorStrategicModel;
import model.agent.humanAgent.tacticalLevel.BasicOperatorTacticalModel;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.map.Map;
import model.environment.position.Position;

/**
 * An operator agent is a {@link HumanAgent} that performs operator tasks.
 * 
 * @author S.A.M. Janssen
 *
 */
public class OperatorAgent extends HumanAgent {

	/**
	 * The assignment.
	 */
	private Activity assignment;

	/**
	 * Creates an operator agent.
	 * 
	 * @param map
	 *            The map.
	 * @param position
	 *            The position.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param assignment
	 *            The assignment.
	 */
	public OperatorAgent(Map map, Position position, double radius, double mass, Activity assignment) {
		this(map, position, radius, mass, assignment, Color.RED);
	}

	/**
	 * Creates an operator agent.
	 * 
	 * @param map
	 *            The map.
	 * @param position
	 *            The position.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param assignment
	 *            The assignment.
	 * @param color
	 *            The color.
	 */
	public OperatorAgent(Map map, Position position, double radius, double mass, Activity assignment, Color color) {
		super(position, radius, mass, new BasicOperatorStrategicModel(assignment), new BasicOperatorTacticalModel(map),
				new BasicOperatorOperationalModel(map, assignment), color);
		this.assignment = assignment;
	}

	/**
	 * Gets the assignment of the agent.
	 * 
	 * @return The assignment.
	 */
	public Activity getAssignment() {
		return assignment;
	}
}