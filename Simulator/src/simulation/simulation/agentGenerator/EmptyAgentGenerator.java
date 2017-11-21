package simulation.simulation.agentGenerator;

import java.util.List;

import model.agent.humanAgent.HumanAgent;

/**
 * The empty agent generator does not generate agents at any time.
 * 
 * @author S.A.M. Janssen
 */
public class EmptyAgentGenerator extends AgentGenerator {

	@Override
	public List<HumanAgent> generateAgent(long numberOfSteps, int timeStep, boolean forced) {
		return null;
	}

}
