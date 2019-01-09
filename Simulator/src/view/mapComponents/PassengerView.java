package view.mapComponents;

import java.awt.Color;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;
import model.environment.position.Vector;

/**
 * Visualizes a {@link HumanAgent}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class PassengerView extends MapComponentView {

	/**
	 * The {@link HumanAgent} to visualize
	 */
	private Passenger agent;

	/**
	 * Flag to indicate if we draw the intended path.
	 */
	private boolean drawPath = true;

	/**
	 * Flag to indicate if we draw the speed.
	 */
	private boolean drawSpeed = false;

	/**
	 * Create the Agent view.
	 * 
	 * @param agent
	 *            The agent to visualize
	 */
	public PassengerView(Passenger agent) {
		this.agent = agent;
	}

	@Override
	public String getAboutString() {
		String information = "<html>Agent information.<br><br>";
		String internalName = "Internal name: <i>" + agent + "</i>.<br>";
		String type = "Agent Type:  <i>" + agent.getClass().getSimpleName() + "</i>.<br>";
		String goal = "Goal Position:  <i>" + agent.getGoalPosition() + "</i>.<br>";
		String position = "Position:  <i>" + agent.getPosition() + "</i>.<br>";
		String baggage = "Baggage: <i>" + agent.getLuggage() + "</i>.<br>";
		String active = "Active: <i>" + agent.getActiveActivity() + "</i>.<br>";
		String flight = "Flight information: <i>" + agent.getFlight().getFlightTime() + "</i>.<br>";
		String other = "Other information?<br>";
		String end = "</html>";
		return information + internalName + type + position + goal + baggage + active + flight + other + end;
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
		double agentRadius = agent.getRadius();
		ShapeDrawer.drawCircle(agent.getColor(), agent.getPosition(), agent.getRadius());

		if (drawSpeed) {
			Vector speed = agent.getCurrentVelocity();
			ShapeDrawer.drawLine(ShapeDrawer.getComplementaryColor(agent.getColor()), agent.getPosition(),
					new Position(speed.x + agent.getPosition().x, speed.y + agent.getPosition().y));
		}

		if (drawPath) {
			ShapeDrawer.drawPath(ShapeDrawer.getComplementaryColor(agent.getColor()), agent.getPosition(),
					agent.getGoalPositions());
		}

		if (!agent.getLuggage().isEmpty()) {
			Color c = Color.YELLOW;
			for (Luggage luggage : agent.getLuggage()) {
				ShapeDrawer.drawRectangle(c, new Position(luggage.getPosition().x - agentRadius / 2,
						luggage.getPosition().y - agentRadius / 2), agentRadius, agentRadius);
			}
		}

		// Set the bounds for the about box.
		setBounds(ShapeDrawer.getRectangle(
				new Position(agent.getPosition().x - agent.getRadius(), agent.getPosition().y - agent.getRadius()),
				2 * agent.getRadius(), 2 * agent.getRadius()));
	}
}