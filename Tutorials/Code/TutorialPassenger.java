import java.awt.Color;
import java.util.Collection;

import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.operationalLevel.BasicPassengerOperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.strategicLevel.BasicPassengerStrategicModel;
import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.tacticalLevel.BasicPassengerTacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.TacticalModel;
import model.environment.objects.area.Facility;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;

@SuppressWarnings("javadoc")
public class TutorialPassenger extends Passenger {

	/**
	 * The builder class.
	 * 
	 * @author S.A.M. Janssen
	 */
	public static class Builder extends Passenger.Builder<Builder> {

		public TutorialPassenger build() {
			if (strategicModel == null)
				strategicModel = new BasicPassengerStrategicModel(facility, checkedIn, checkPointDropTime,
						checkPointCollectTime, flight);

			if (tacticalModel == null)
				tacticalModel = new BasicPassengerTacticalModel(flight);

			if (operationalModel == null)
				operationalModel = new BasicPassengerOperationalModel(desiredSpeed);

			return new TutorialPassenger(flight, checkedIn, facility, position, radius, mass, luggage, strategicModel,
					tacticalModel, operationalModel, color);
		}
	}

	private TutorialPassenger(Flight flight, boolean checkedIn, Class<? extends Facility> facility, Position position,
			double radius, double mass, Collection<Luggage> luggage, StrategicModel strategicModel,
			TacticalModel tacticalModel, OperationalModel operationalModel, Color color) {
		super(flight, checkedIn, facility, position, radius, mass, luggage, strategicModel, tacticalModel,
				operationalModel, color);
	}

	public void update(int timeStep) {
		super.update(timeStep);
		if (isSitting())
			setLog(new String[] { "1" });
	}
}
