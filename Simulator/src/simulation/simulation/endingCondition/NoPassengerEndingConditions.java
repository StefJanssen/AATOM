package simulation.simulation.endingCondition;

import model.agent.humanAgent.Passenger;

/**
 * The no passengers ending conditions for a simulation ensure that a simulation ends
 * after there are no more passengers.
 * 
 * @author S.A.M. Janssen
 */
public class NoPassengerEndingConditions extends EndingConditions {

	@Override
	public Object[] getReturnValues() {
		return null;
	}

	@Override
	public boolean isEnded(long numberOfSteps) {
		return simulator.getMap().getMapComponents(Passenger.class).size() == 0 || super.isEnded(numberOfSteps);
	}
}
