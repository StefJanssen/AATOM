import model.agent.humanAgent.aatom.OperatorAgent;
import model.agent.humanAgent.aatom.tacticalLevel.activity.operator.OperatorCheckInActivity;
import util.analytics.Analyzer;

/**
 * The analyzer used in the tutorial.
 * 
 * @author S.A.M. Janssen
 */
public class TutorialAnalzyer extends Analyzer {

	@Override
	public String[] getLineNames() {
		return new String[] { "# of operators active" };
	}

	@Override
	public String getTitle() {
		return "# of check-in operators active";
	}

	@Override
	public double[] getValues() {
		double numberOfActiveOperators = 0;
		for (OperatorAgent operator : getMap().getMapComponents(OperatorAgent.class)) {
			if (operator.getAssignment() instanceof OperatorCheckInActivity) {
				if (operator.getActiveActivity() instanceof OperatorCheckInActivity)
					numberOfActiveOperators++;
			}
		}
		return new double[] { numberOfActiveOperators };
	}

	@Override
	public String getYAxis() {
		return "# of operators active";
	}
}
