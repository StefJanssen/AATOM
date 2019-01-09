package view.mapComponents;

import model.agent.humanAgent.HumanAgent;
import model.environment.position.Position;

/**
 * Visualizes a {@link HumanAgent}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class HumanAgentView extends MapComponentView {

	/**
	 * The {@link HumanAgent} to visualize
	 */
	private HumanAgent agent;

	/**
	 * Create the Agent view.
	 * 
	 * @param agent
	 *            The agent to visualize
	 */
	public HumanAgentView(HumanAgent agent) {
		this.agent = agent;
	}

	@Override
	public String getAboutString() {
		return "";
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