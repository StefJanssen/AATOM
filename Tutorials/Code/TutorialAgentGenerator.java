import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.HumanAgent;
import model.environment.objects.area.EntranceArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.position.Position;
import simulation.simulation.agentGenerator.BaseAgentGenerator;
import simulation.simulation.util.Utilities;

/**
 * THe agent generator in the tutorial.
 * 
 * @author S.A.M. Janssen
 */
public class TutorialAgentGenerator extends BaseAgentGenerator {

	/**
	 * Creates the tutorial agent generator.
	 * 
	 * @param interArrivalTime
	 *            The interarrival time.
	 */
	public TutorialAgentGenerator(double interArrivalTime) {
		super(interArrivalTime);
	}

	@Override
	public List<HumanAgent> generateAgent(long numberOfSteps, int timeStep, boolean forced) {
		List<HumanAgent> agents = new ArrayList<>();

		if (forced || canGenerate(timeStep)) {
			Luggage luggage = new Luggage(LuggageType.CARRY_ON, Utilities.RANDOM_GENERATOR.nextDouble(),
					Utilities.RANDOM_GENERATOR.nextDouble());
			if (areas.isEmpty())
				return null;
			EntranceArea area = areas.get(Utilities.RANDOM_GENERATOR.nextInt(areas.size()));
			Position start = area.generatePosition();
			Flight flight = getEligibleFlight();
			if (flight != null) {
				agents.add(new TutorialPassenger.Builder().setFlight(flight).setPosition(start).setLuggage(luggage)
						.build());
			}
		}
		return agents;
	}
}
