package util.analytics;

import model.agent.humanAgent.Passenger;

/**
 * A parameter tracker for the agent numbers.
 * 
 * @author S.A.M. Janssen
 */
public class AgentNumberAnalyzer extends Analyzer {

	@Override
	public String[] getLineNames() {
		return new String[] { "Passengers" };
	}

	@Override
	public String getTitle() {
		return "Agent Numbers";
	}

	@Override
	public double[] getValues() {
		double passengers = map.getMapComponents(Passenger.class).size();
		return new double[] { passengers };
	}

	@Override
	public String getYAxis() {
		return "# of agents";
	}
}
