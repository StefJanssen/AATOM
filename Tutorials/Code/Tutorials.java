import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.area.EntranceArea;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.Wall;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import model.map.Map;
import model.map.ModelComponentBuilder;
import simulation.main.Experimenter;
import simulation.modelBuilder.ModelBuilder;
import simulation.simulation.Simulator;
import simulation.simulation.agentGenerator.BaseAgentGenerator;
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
 * The tutorials.
 * 
 * @author S.A.M. Janssen
 */
public class Tutorials {
	/**
	 * Main method.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {
		// Comment all tutorial methods, except the one you want to execute.
		tutorial1();
		// tutorial2a();
		// tutorial2b();
		// tutorial3();
		// tutorial4();
		// tutorial5a();
		// tutorial5b();
		// tutorial6();
		// tutorial7();
		// tutorial8();
		// tutorial9();
	}

	/**
	 * Tutorial 1.
	 */
	private static void tutorial1() {
		Simulator sim = ModelBuilder.eindhovenAirport(true, 100);
		// Simulator sim = ModelBuilder.rotterdamTheHagueAirport(true, 100);
		new Thread(sim).start();
	}

	/**
	 * Tutorial 2a.
	 */
	private static void tutorial2a() {
		Simulator sim = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(true)
				.build();
		sim.add(new Wall(10, 10, 20, 2));
		new Thread(sim).start();
	}

	/**
	 * Tutorial 2b.
	 */
	private static void tutorial2b() {
		Simulator sim = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(true)
				.build();
		sim.add(new Wall(10, 10, 20, 2));
		sim.addAll(ModelComponentBuilder.queue(new Position(10, 15), 5, 20, true, 0));
		new Thread(sim).start();
	}

	/**
	 * Tutorial 3.
	 */
	private static void tutorial3() {
		Simulator sim = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(3600)).setGui(true)
				.build();
		Map map = sim.getMap();
		GateArea gate = new GateArea(0.5, 1, 9, 9);
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
		sim.add(new Passenger.Builder<>().setFlight(flight).setCheckedIn(false).setPosition(new Position(20, 40))
				.setLuggage(new Luggage(LuggageType.CARRY_ON, 0.1, 0.1)).build());
		new Thread(sim).start();
	}

	/**
	 * Tutorial 4.
	 */
	private static void tutorial4() {
		Simulator sim = new Simulator.Builder<>().setAgentGenerator(new BaseAgentGenerator(30))
				.setEndingConditions(new BaseEndingConditions(3600)).setGui(true).build();
		Map map = sim.getMap();
		sim.add(new EntranceArea(10, 30, 10, 10));
		GateArea gate = new GateArea(0.5, 1, 9, 9);
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
		new Thread(sim).start();
	}

	/**
	 * Tutorial 5a.
	 */
	private static void tutorial5a() {
		Simulator sim = new Simulator.Builder<>().setAgentGenerator(new BaseAgentGenerator(30))
				.setEndingConditions(new BaseEndingConditions(3700)).setGui(true).build();
		sim.add(new EntranceArea(10, 30, 10, 10));
		Map map = sim.getMap();
		GateArea gate = new GateArea(0.5, 1, 9, 9);
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

	/**
	 * Tutorial 5b.
	 */
	private static void tutorial5b() {
		Simulator sim = new Simulator.Builder<>().setAgentGenerator(new BaseAgentGenerator(30))
				.setEndingConditions(new BaseEndingConditions(3700)).setGui(true).build();
		sim.add(new EntranceArea(10, 30, 10, 10));
		Map map = sim.getMap();
		GateArea gate = new GateArea(0.5, 1, 9, 9);
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
		sim.add(new TutorialAnalzyer());
		new Thread(sim).start();
	}

	/**
	 * Tutorial 6.
	 */
	private static void tutorial6() {
		Simulator sim = new Simulator.Builder<>().setAgentGenerator(new TutorialAgentGenerator(30))
				.setEndingConditions(new BaseEndingConditions(3700)).setGui(true).setLogger(new BaseLogger()).build();
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

	/**
	 * Tutorial 7.
	 */
	private static void tutorial7() {
		Simulator sim = new Simulator.Builder<>().setAgentGenerator(new TutorialAgentGenerator(30))
				.setEndingConditions(new BaseEndingConditions(3700)).setGui(true).setLogger(new BaseLogger(true))
				.build();
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

	/**
	 * Tutorial 8.
	 */
	private static void tutorial8() {
		Simulator sim = new Simulator.Builder<>().setAgentGenerator(new TutorialAgentGenerator(30))
				.setEndingConditions(new BaseEndingConditions(3700)).setGui(true).setLogger(new BaseLogger(true))
				.build();
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

	/**
	 * Tutorial 9.
	 */
	private static void tutorial9() {
		List<String[]> inputs = new ArrayList<>();
		inputs.add(new String[] { "30", "111111" });
		inputs.add(new String[] { "30", "222222" });
		inputs.add(new String[] { "40", "111111" });
		inputs.add(new String[] { "40", "222222" });
		Experimenter experimenter = new Experimenter(inputs, ExperimenterMain.class);
		new Thread(experimenter).start();
	}
}