package model.agent.humanAgent.aatom;

import java.awt.Color;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.BasicOperatorOperationalModel;
import model.agent.humanAgent.aatom.strategicLevel.BasicOperatorStrategicModel;
import model.agent.humanAgent.aatom.tacticalLevel.BasicOperatorTacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
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
		if (assignment == null)
			throw new IllegalArgumentException("Assignment cannot be null.");
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