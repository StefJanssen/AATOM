package view.mapComponents;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.OperatorAgent;
import model.environment.position.Position;

/**
 * Visualizes a {@link HumanAgent}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class OperatorAgentView extends MapComponentView {

	/**
	 * The {@link HumanAgent} to visualize
	 */
	private OperatorAgent agent;

	/**
	 * Create the Agent view.
	 * 
	 * @param agent
	 *            The agent to visualize
	 */
	public OperatorAgentView(OperatorAgent agent) {
		this.agent = agent;
	}

	@Override
	public String getAboutString() {
		return agent.getAssignment().getClass().getSimpleName() + " " + agent.hashCode();
	}

	/**
	 * Getter for the {@link HumanAgent}.
	 * 
	 * @return The Agent.
	 */
	public HumanAgent getAgent() {
		return agent;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawCircle(agent.getColor(), agent.getPosition(), agent.getRadius());

		// Set the bounds for the about box.
		setBounds(ShapeDrawer.getRectangle(
				new Position(agent.getPosition().x - agent.getRadius(), agent.getPosition().y - agent.getRadius()),
				2 * agent.getRadius(), 2 * agent.getRadius()));

	}
}