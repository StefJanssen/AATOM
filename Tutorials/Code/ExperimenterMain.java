import model.environment.objects.area.EntranceArea;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.Wall;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import model.map.Map;
import model.map.ModelComponentBuilder;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;
import simulation.simulation.util.Utilities;
import util.analytics.ActivityDistributionAnalyzer;
import util.analytics.AgentNumberAnalyzer;
import util.analytics.DistanceAnalyzer;
import util.analytics.MissedFlightsAnalyzer;
import util.analytics.QueueAnalyzer;
import util.analytics.TimeInQueueAnalyzer;
import util.analytics.TimeToGateAnalyzer;
import util.io.logger.BaseLogger;

/**
 * Main class for the experimenter.
 * 
 * @author S.A.M. Janssen
 */
public class ExperimenterMain {

	/**
	 * Main class.
	 * 
	 * @param args
	 *            [0] = interarrivalTime. [1] = seed.
	 */
	public static void main(String[] args) {
		int interarrivalTime = Integer.parseInt(args[0]);
		long seed = Long.parseLong(args[1]);
		Simulator sim = new Simulator.Builder<>().setSimulationName(args[0] + "_" + args[1])
				.setAgentGenerator(new TutorialAgentGenerator(interarrivalTime))
				.setEndingConditions(new BaseEndingConditions(3700)).setGui(false).setLogger(new BaseLogger(true))
				.setRandomSeed(seed).build();
		sim.add(TutorialPassengerView.class);
		sim.add(new EntranceArea(10, 30, 10, 10));
		Map map = sim.getMap();
		GateArea gate = new GateArea(1, 1, 9, 9);
		sim.add(gate);
		sim.addAll(ModelComponentBuilder.sittingArea(new Position(1, 1.5), 5, 12, 0));
		sim.addAll(ModelComponentBuilder.checkInArea(new Position(2, 30), 3, 90));
		sim.addAll(ModelComponentBuilder.checkpoint(new Position(10, 5), 2, 20, true, 0));
		sim.add(new Wall(65, 0, 0.1, 48));
		sim.add(new Wall(0, 48, 65, 0.1));

		QueuingArea checkInQueue = Utilities.getClosestQueuingArea(map.getMapComponents(Desk.class),
				map.getMapComponents(QueuingArea.class));
		QueuingArea checkpointQueue = Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
				map.getMapComponents(QueuingArea.class));
		Flight flight = new Flight(FlightType.DEPARTING, 3600, gate, map.getMapComponents(Desk.class), checkInQueue,
				checkpointQueue);

		sim.add(flight);
		sim.add(new QueueAnalyzer());
		sim.add(new TimeInQueueAnalyzer());
		sim.add(new ActivityDistributionAnalyzer());
		sim.add(new TimeToGateAnalyzer());
		sim.add(new AgentNumberAnalyzer());
		sim.add(new MissedFlightsAnalyzer());
		sim.add(new DistanceAnalyzer());
		new Thread(sim).start();
	}
}
