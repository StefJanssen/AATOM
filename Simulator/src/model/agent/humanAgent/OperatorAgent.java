package model.agent.humanAgent;

import java.awt.Color;

import model.agent.humanAgent.operationalLevel.BasicOperatorOperationalModel;
import model.agent.humanAgent.strategicLevel.BasicOperatorStrategicModel;
import model.agent.humanAgent.tacticalLevel.BasicOperatorTacticalModel;
import model.agent.humanAgent.tacticalLevel.activity.Activity;
import model.environment.position.Position;

/**
 * An operator agent is a {@link HumanAgent} that performs operator tasks.
 * 
 * @author S.A.M. Janssen
 *
 */
public class OperatorAgent extends AatomHumanAgent {

	/**
	 * The assignment.
	 */
	private Activity assignment;

	/**
	 * Creates an operator agent.
	 * 
	 * @param position
	 *            The position.
	 * @param radius
	 *            The radius.
	 * @param mass
	 *            The mass.
	 * @param assignment
	 *            The assignment.
	 */
	public OperatorAgent(Position position, double radius, double mass, Activity assignment) {
		this(position, radius, mass, assignment, Color.RED);
	}

	/**
	 * Creates an operator agent.
	 * 
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
	public OperatorAgent(Position position, double radius, double mass, Activity assignment, Color color) {
		super(position, radius, mass, new BasicOperatorStrategicModel(assignment), new BasicOperatorTacticalModel(),
				new BasicOperatorOperationalModel(assignment), color);
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