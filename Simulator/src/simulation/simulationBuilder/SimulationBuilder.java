package simulation.simulationBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.humanAgent.OperatorAgent;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicOperatorCheckInActivity;
import model.environment.map.Map;
import model.environment.map.MapBuilder;
import model.environment.objects.area.EntranceArea;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.area.Restaurant;
import model.environment.objects.area.Shop;
import model.environment.objects.area.Toilet;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.QueueSeparator;
import model.environment.objects.physicalObject.Wall;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.agentGenerator.BaseAgentGenerator;
import simulation.simulation.endingCondition.NoPassengerEndingConditions;
import simulation.simulation.util.SimulationObject;
import simulation.simulation.util.Utilities;
import util.analytics.ActivityDistributionAnalyzer;
import util.analytics.AgentNumberAnalyzer;
import util.analytics.DistanceAnalyzer;
import util.analytics.MissedFlightsAnalyzer;
import util.analytics.QueueAnalyzer;
import util.analytics.TimeInQueueAnalyzer;
import util.analytics.TimeToGateAnalyzer;
import util.analytics.TimeToGatePerFlightAnalyzer;
import util.io.logger.BaseLogger;

/**
 * Prebuilt scenarios.
 * 
 * @author S.A.M. Janssen
 */
public final class SimulationBuilder {

	/**
	 * Creates the airside arrival objects.
	 * 
	 * @return The airside arrival.
	 */
	private static List<SimulationObject> airsideArrival() {
		SimulationObject w;
		List<SimulationObject> walls = new ArrayList<>();

		w = new Wall(103.8, 49.48, 0.11, 5.33);
		walls.add(w);
		w = new Wall(103.8, 49.48, 5.32, 0.12);
		walls.add(w);
		w = new Wall(109.0, 49.48, 0.12, 5.33);
		walls.add(w);
		w = new Wall(103.8, 54.68, 5.26, 0.12);
		walls.add(w);
		w = new Wall(111.75, 54.15, 4.5, 0.1);
		walls.add(w);
		w = new Wall(119.91, 43.79, 0.11, 7.02);
		walls.add(w);
		w = new Wall(116.04, 50.59, 5.0, 0.22);
		walls.add(w);
		w = new Wall(116.08, 33.09, 5.67, 0.22);
		walls.add(w);
		w = new Wall(116.08, 33.09, 0.22, 6.66);
		walls.add(w);
		w = new Wall(118.31, 43.79, 43.41, 0.22);
		walls.add(w);
		w = new Wall(120.81, 46.49, 3.71, 0.38);
		walls.add(w);
		w = new Wall(124.43, 43.79, 0.11, 3.08);
		walls.add(w);
		w = new Wall(121.27, 32.47, 2.48, 0.1);
		walls.add(w);
		w = new Wall(128.7, 32.47, 1.26, 0.1);
		walls.add(w);
		w = new Wall(129.86, 32.47, 0.1, 7.51);
		walls.add(w);
		w = new Wall(128.7, 39.88, 1.26, 0.1);
		walls.add(w);
		w = new Wall(118.41, 39.93, 5.34, 0.1);
		walls.add(w);
		w = new Wall(121.57, 32.47, 0.18, 7.52);
		walls.add(w);
		w = new Wall(141.44, 11.96, 0.1, 19.8);
		walls.add(w);
		w = new Wall(137.86, 29.52, 2.2, 0.1);
		walls.add(w);
		w = new Wall(132.93, 29.52, 2.2, 0.1);
		walls.add(w);
		w = new Wall(112.61, 32.98, 3.61, 0.11);
		walls.add(w);
		w = new Wall(116.08, 39.65, 1.23, 0.11);
		walls.add(w);
		w = new Wall(120.81, 46.49, 0.22, 4.3);
		walls.add(w);
		w = new Wall(103.8, 49.48, 0.11, 5.33);
		walls.add(w);
		w = new Wall(103.8, 49.48, 5.32, 0.12);
		walls.add(w);
		w = new Wall(109.0, 49.48, 0.12, 5.33);
		walls.add(w);
		w = new Wall(103.8, 54.68, 5.26, 0.12);
		walls.add(w);
		w = new Wall(111.75, 54.15, 4.5, 0.1);
		walls.add(w);
		w = new Wall(119.91, 43.79, 0.11, 7.02);
		walls.add(w);
		w = new Wall(116.04, 50.59, 5.0, 0.22);
		walls.add(w);
		w = new Wall(116.08, 33.09, 5.67, 0.22);
		walls.add(w);
		w = new Wall(116.08, 33.09, 0.22, 6.66);
		walls.add(w);
		w = new Wall(118.31, 43.79, 43.41, 0.22);
		walls.add(w);
		w = new Wall(120.81, 46.49, 3.71, 0.38);
		walls.add(w);
		w = new Wall(124.43, 43.79, 0.11, 3.08);
		walls.add(w);
		w = new Wall(121.27, 32.47, 2.48, 0.1);
		walls.add(w);
		w = new Wall(128.7, 32.47, 1.26, 0.1);
		walls.add(w);
		w = new Wall(129.86, 32.47, 0.1, 7.51);
		walls.add(w);
		w = new Wall(128.7, 39.88, 1.26, 0.1);
		walls.add(w);
		w = new Wall(118.41, 39.93, 5.34, 0.1);
		walls.add(w);
		w = new Wall(121.57, 32.47, 0.18, 7.52);
		walls.add(w);
		w = new Wall(141.44, 11.96, 0.1, 19.8);
		walls.add(w);
		w = new Wall(137.86, 29.52, 2.2, 0.1);
		walls.add(w);
		w = new Wall(132.93, 29.52, 2.2, 0.1);
		walls.add(w);
		w = new Wall(112.61, 32.98, 3.61, 0.11);
		walls.add(w);
		w = new Wall(116.08, 39.65, 1.23, 0.11);
		walls.add(w);
		w = new Wall(120.81, 46.49, 0.22, 4.3);
		walls.add(w);
		return walls;
	}

	/**
	 * Creates the airside departure objects.
	 * 
	 * @return The airside departure objects.
	 */
	private static List<SimulationObject> airsideDeparture() {
		SimulationObject w;
		List<SimulationObject> walls = new ArrayList<>();

		w = new Wall(8.85, 25.62, 0.1, 4.07);
		walls.add(w);
		w = new Wall(5.16, 29.59, 3.78, 0.1);
		walls.add(w);
		w = new Wall(40.19, 21.87, 0.1, 2.96);
		walls.add(w);
		w = new Wall(36.44, 21.87, 0.1, 1.75);
		walls.add(w);
		w = new Wall(47.69, 21.87, 0.1, 2.9);
		walls.add(w);
		w = new Wall(47.66, 25.68, 3.81, 0.1);
		walls.add(w);
		w = new Wall(51.43, 24.57, 0.1, 6.02);
		walls.add(w);
		w = new Wall(45.01, 30.54, 6.47, 0.1);
		walls.add(w);
		w = new Wall(51.43, 21.87, 0.1, 1.75);
		walls.add(w);
		w = new Wall(66.43, 21.87, 0.1, 3.82);
		walls.add(w);
		w = new Wall(65.12, 25.63, 1.36, 0.1);
		walls.add(w);
		w = new Wall(61.42, 25.63, 1.36, 0.1);
		walls.add(w);
		w = new Wall(61.42, 21.87, 0.1, 3.82);
		walls.add(w);
		w = new Wall(74.91, 25.63, 1.02, 0.1);
		walls.add(w);
		w = new Wall(71.43, 25.63, 1.35, 0.1);
		walls.add(w);
		w = new Wall(71.43, 21.87, 0.1, 3.82);
		walls.add(w);
		w = new Wall(5.24, 31.92, 2.15, 0.1);
		walls.add(w);
		w = new Wall(8.2, 31.92, 0.28, 0.1);
		walls.add(w);
		w = new Wall(8.28, 31.92, 0.11, 3.51);
		walls.add(w);
		w = new Wall(5.24, 35.34, 6.33, 0.1);
		walls.add(w);
		w = new Wall(9.28, 31.92, 2.29, 0.1);
		walls.add(w);
		w = new Wall(11.31, 29.59, 0.26, 19.42);
		walls.add(w);
		w = new Wall(11.31, 31.78, 20.27, 0.21);
		walls.add(w);
		w = new Wall(31.31, 31.78, 0.26, 1.19);
		walls.add(w);
		w = new Wall(27.0, 48.97, 9.76, 0.22);
		walls.add(w);
		w = new Wall(31.59, 42.51, 0.22, 6.48);
		walls.add(w);
		w = new Wall(36.44, 24.63, 0.1, 2.71);
		walls.add(w);
		w = new Wall(36.44, 29.34, 0.1, 1.2);
		walls.add(w);
		w = new Wall(35.01, 30.54, 2.9, 0.1);
		walls.add(w);
		w = new Wall(31.46, 30.54, 1.55, 0.1);
		walls.add(w);
		w = new Wall(32.86, 28.82, 2.2, 0.1);
		walls.add(w);
		w = new Wall(33.93, 29.21, 0.1, 1.0);
		walls.add(w);
		w = new Wall(36.44, 25.68, 0.14, 0.1);
		walls.add(w);
		w = new Wall(37.58, 25.68, 3.89, 0.1);
		walls.add(w);
		w = new Wall(41.43, 25.68, 0.1, 1.66);
		walls.add(w);
		w = new Wall(41.43, 29.34, 0.1, 1.24);
		walls.add(w);
		w = new Wall(39.91, 30.54, 3.1, 0.1);
		walls.add(w);
		w = new Wall(38.84, 29.21, 0.1, 1.02);
		walls.add(w);
		w = new Wall(37.77, 28.82, 2.2, 0.1);
		walls.add(w);

		// facilities
		walls.add(new Shop(11.6, 32, 19.8, 16.7));
		walls.add(new Toilet(5.3, 32, 6.0, 3.3));
		walls.add(new Restaurant(48.6, 36, 27, 12.7));

		return walls;
	}

	/**
	 * Cretaes the belts of RTHA.
	 * 
	 * @return The belts.
	 */
	private static List<SimulationObject> belts() {
		SimulationObject w;
		List<SimulationObject> walls = new ArrayList<>();

		w = new Wall(151.91, 12.46, 18.31, 1.02);
		walls.add(w);
		w = new Wall(169.19, 12.46, 1.02, 15.27);
		walls.add(w);
		w = new Wall(151.92, 26.71, 18.29, 1.02);
		walls.add(w);
		w = new Wall(151.91, 24.46, 1.02, 3.28);
		walls.add(w);
		w = new Wall(151.95, 24.46, 16.86, 1.02);
		walls.add(w);
		w = new Wall(167.79, 14.71, 1.02, 10.77);
		walls.add(w);
		w = new Wall(151.97, 14.71, 16.85, 1.02);
		walls.add(w);
		w = new Wall(151.91, 12.46, 1.02, 3.28);
		walls.add(w);
		w = new Wall(151.29, 35.14, 16.67, 1.02);
		walls.add(w);
		w = new Wall(166.94, 28.2, 1.02, 7.96);
		walls.add(w);
		w = new Wall(166.94, 28.2, 3.27, 1.01);
		walls.add(w);
		w = new Wall(169.19, 28.19, 1.02, 14.81);
		walls.add(w);
		w = new Wall(166.94, 42.0, 3.27, 1.01);
		walls.add(w);
		w = new Wall(166.94, 37.39, 1.02, 5.61);
		walls.add(w);
		w = new Wall(151.29, 37.39, 16.67, 1.02);
		walls.add(w);
		w = new Wall(151.29, 35.14, 1.02, 3.28);
		walls.add(w);
		w = new Wall(110.31, 24.17, 1.0, 10.25);
		walls.add(w);
		w = new Wall(111.61, 24.17, 1.0, 10.25);
		walls.add(w);
		w = new Wall(108.84, 34.42, 3.77, 1.7);
		walls.add(w);
		w = new Wall(106.96, 36.13, 3.77, 1.7);
		walls.add(w);
		w = new Wall(105.08, 37.83, 3.77, 1.7);
		walls.add(w);
		w = new Wall(103.19, 39.53, 3.77, 1.7);
		walls.add(w);
		w = new Wall(103.19, 41.24, 3.28, 2.36);
		walls.add(w);
		return walls;
	}

	/**
	 * Creates the check-in desks.
	 * 
	 * @param mapBuilder
	 *            The map builder.
	 * 
	 * @return The check-in desks.
	 */
	private static List<SimulationObject> checkInDesks(MapBuilder mapBuilder) {
		List<SimulationObject> desks = new ArrayList<>();
		Desk w;

		w = new Desk(46.67, 56.2, 1.17, 0.1, new Position(47.25, 56.7));
		desks.add(w);
		w = new Desk(48.46, 56.2, 1.17, 0.1, new Position(49.04, 56.7));
		desks.add(w);
		w = new Desk(50.26, 56.2, 1.17, 0.1, new Position(50.84, 56.7));
		desks.add(w);
		w = new Desk(52.05, 56.2, 1.17, 0.1, new Position(52.63, 56.7));
		desks.add(w);
		w = new Desk(53.85, 56.2, 1.17, 0.1, new Position(54.43, 56.7));
		desks.add(w);
		w = new Desk(55.64, 56.2, 1.17, 0.1, new Position(56.22, 56.7));
		desks.add(w);
		w = new Desk(57.44, 56.2, 1.17, 0.1, new Position(58.02, 56.7));
		desks.add(w);
		w = new Desk(59.23, 56.2, 1.17, 0.1, new Position(59.81, 56.7));
		desks.add(w);
		w = new Desk(61.03, 56.2, 1.17, 0.1, new Position(61.61, 56.7));
		desks.add(w);
		w = new Desk(62.82, 56.2, 1.17, 0.1, new Position(63.4, 56.7));
		desks.add(w);
		w = new Desk(64.62, 56.2, 1.17, 0.1, new Position(65.2, 56.7));
		desks.add(w);
		w = new Desk(66.41, 56.2, 1.17, 0.1, new Position(67.0, 56.7));
		desks.add(w);
		w = new Desk(68.21, 56.2, 1.17, 0.1, new Position(68.79, 56.7));
		desks.add(w);
		w = new Desk(70.0, 56.2, 1.17, 0.1, new Position(70.59, 56.7));
		desks.add(w);
		w = new Desk(71.8, 56.2, 1.17, 0.1, new Position(72.38, 56.7));
		desks.add(w);
		w = new Desk(73.59, 56.2, 1.17, 0.1, new Position(74.18, 56.7));
		desks.add(w);

		desks.addAll(mapBuilder.queue(new Position(46.67, 58.7), 4, 6, false, 0));
		desks.addAll(mapBuilder.queue(new Position(54.17, 58.7), 4, 6, false, 0));
		desks.addAll(mapBuilder.queue(new Position(61.67, 58.7), 4, 6, false, 0));
		desks.addAll(mapBuilder.queue(new Position(69.17, 58.7), 4, 5.5, false, 0));
		desks.add(new Wall(76, 54.7, 0.1, 8));
		return desks;
	}

	/**
	 * Creates a representation of Eindhoven Airport (EIN) and runs a
	 * simulation.
	 * 
	 * @param gui
	 *            With or without gui.
	 * @param timeStep
	 *            The time step in ms.
	 * @return The simulator.
	 */
	public static Simulator eindhovenAirport(boolean gui, int timeStep) {
		BaseAgentGenerator agentGenerator = new BaseAgentGenerator(10);
		Simulator sim = new Simulator(new Map(), gui, timeStep, new NoPassengerEndingConditions(), agentGenerator,
				new BaseLogger());
		MapBuilder mapBuilder = new MapBuilder(sim);
		Map map = sim.getMap();
		// agent generation
		List<Position> agentCorners = new ArrayList<>();
		agentCorners.add(new Position(20, 35.5));
		agentCorners.add(new Position(20, 45));
		agentCorners.add(new Position(40, 45));
		agentCorners.add(new Position(40, 35.5));
		EntranceArea entrance = new EntranceArea(agentCorners);
		sim.add(entrance);

		// gate positions
		List<Position> firstGate = new ArrayList<>();
		firstGate.add(new Position(2, 2));
		firstGate.add(new Position(2, 15));
		firstGate.add(new Position(11, 15));
		firstGate.add(new Position(11, 2));
		GateArea firstGateArea = new GateArea(firstGate);

		List<Position> secondGate = new ArrayList<>();
		secondGate.add(new Position(17, 2));
		secondGate.add(new Position(17, 15));
		secondGate.add(new Position(26, 15));
		secondGate.add(new Position(26, 2));
		GateArea secondGateArea = new GateArea(secondGate);

		List<Position> thirdGate = new ArrayList<>();
		thirdGate.add(new Position(32, 2));
		thirdGate.add(new Position(32, 15));
		thirdGate.add(new Position(41, 15));
		thirdGate.add(new Position(41, 2));
		GateArea thirdGateArea = new GateArea(thirdGate);

		List<Position> fourthGate = new ArrayList<>();
		fourthGate.add(new Position(47, 2));
		fourthGate.add(new Position(47, 15));
		fourthGate.add(new Position(56, 15));
		fourthGate.add(new Position(56, 2));
		GateArea fourthGateArea = new GateArea(fourthGate);

		sim.add(firstGateArea);
		sim.add(secondGateArea);
		sim.add(thirdGateArea);
		sim.add(fourthGateArea);

		// gate areas
		List<SimulationObject> sittingArea = mapBuilder.sittingArea(new Position(3, 3), 6, 10, 0);
		sim.addAll(sittingArea);
		sittingArea = mapBuilder.sittingArea(new Position(18, 3), 6, 10, 0);
		sim.addAll(sittingArea);
		sittingArea = mapBuilder.sittingArea(new Position(33, 3), 6, 10, 0);
		sim.addAll(sittingArea);
		sittingArea = mapBuilder.sittingArea(new Position(48, 3), 6, 10, 0);
		sim.addAll(sittingArea);
		sim.add(new Wall(19.9, 24.5, 0.1, 2.5));

		// check in desks
		List<SimulationObject> checkinDesks = mapBuilder.checkInArea(new Position(12, 25), 3, 0);
		sim.addAll(checkinDesks);
		Collection<Desk> desks1 = new ArrayList<>();
		for (SimulationObject o : checkinDesks) {
			if (o instanceof Desk)
				desks1.add((Desk) o);
		}

		checkinDesks = mapBuilder.checkInArea(new Position(20, 25), 3, 0);
		sim.addAll(checkinDesks);
		Collection<Desk> desks2 = new ArrayList<>();
		for (SimulationObject o : checkinDesks) {
			if (o instanceof Desk)
				desks2.add((Desk) o);
		}

		checkinDesks = mapBuilder.checkInArea(new Position(28, 25), 3, 0);
		sim.addAll(checkinDesks);
		Collection<Desk> desks3 = new ArrayList<>();
		for (SimulationObject o : checkinDesks) {
			if (o instanceof Desk)
				desks3.add((Desk) o);
		}

		// checkpoint
		List<SimulationObject> checkpoint = mapBuilder.checkpoint(new Position(61, 40), 2, 10, false, 90);
		sim.addAll(checkpoint);

		// flights
		List<Flight> flights = new ArrayList<>();
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1, firstGateArea, desks1,
				Utilities.getClosestQueuingArea(desks1, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.25, secondGateArea, desks1,
				Utilities.getClosestQueuingArea(desks1, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.42, thirdGateArea, desks2,
				Utilities.getClosestQueuingArea(desks2, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.5, firstGateArea, desks1,
				Utilities.getClosestQueuingArea(desks1, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.83, thirdGateArea, desks2,
				Utilities.getClosestQueuingArea(desks2, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.92, fourthGateArea, desks2,
				Utilities.getClosestQueuingArea(desks2, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 2.08, firstGateArea, desks1,
				Utilities.getClosestQueuingArea(desks1, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 2.33, thirdGateArea, desks2,
				Utilities.getClosestQueuingArea(desks2, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 3.08, secondGateArea, desks3,
				Utilities.getClosestQueuingArea(desks3, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 3.08, secondGateArea, desks3,
				Utilities.getClosestQueuingArea(desks3, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));

		sim.addAll(flights);

		// walls
		Wall wall = new Wall(80, 28, 0.1, 17);
		sim.add(wall);
		wall = new Wall(60, 28, 20, 0.1);
		sim.add(wall);
		wall = new Wall(60, 0, 0.1, 28);
		sim.add(wall);
		wall = new Wall(40, 45, 40.1, 0.1);
		sim.add(wall);
		wall = new Wall(45, 42, 29, 0.1);
		sim.add(wall);
		wall = new Wall(74, 40, 0.1, 2.1);
		sim.add(wall);
		wall = new Wall(55, 24, 0.1, 18);
		sim.add(wall);
		wall = new Wall(45, 32, 0.1, 10);
		sim.add(wall);
		wall = new Wall(0, 55, 40, 0.1);
		sim.add(wall);
		wall = new Wall(40, 45, 0.1, 10.1);
		sim.add(wall);
		wall = new Wall(11, 24, 44, 0.1);
		sim.add(wall);
		wall = new Wall(36, 24, 0.1, 8);
		sim.add(wall);
		wall = new Wall(36, 32, 9, 0.1);
		sim.add(wall);
		wall = new Wall(11, 24, 0.1, 8);
		sim.add(wall);
		wall = new Wall(0, 32, 11.1, 0.1);
		sim.add(wall);
		wall = new Wall(74, 28, 0.1, 1.8);
		sim.add(wall);
		wall = new Wall(11, 30, 0.9, 1.1);
		sim.add(wall);

		// Facilities
		List<Position> corners = new ArrayList<>();
		corners.add(new Position(0, 24));
		corners.add(new Position(0, 32));
		corners.add(new Position(5, 32));
		corners.add(new Position(5, 24));
		Shop shop = new Shop(corners);
		sim.add(shop);
		List<Position> corners2 = new ArrayList<>();
		corners2.add(new Position(5.1, 24));
		corners2.add(new Position(5.1, 32));
		corners2.add(new Position(8.5, 32));
		corners2.add(new Position(8.5, 24));
		Shop shop2 = new Shop(corners2);
		sim.add(shop2);
		List<Position> corners3 = new ArrayList<>();
		corners3.add(new Position(8.6, 24));
		corners3.add(new Position(8.6, 32));
		corners3.add(new Position(11, 32));
		corners3.add(new Position(11, 24));
		Shop shop3 = new Shop(corners3);
		sim.add(shop3);
		List<Position> corners4 = new ArrayList<>();
		corners4.add(new Position(19, 45));
		corners4.add(new Position(19, 55));
		corners4.add(new Position(30, 55));
		corners4.add(new Position(30, 45));
		Shop shop4 = new Shop(corners4);
		sim.add(shop4);
		List<Position> corners5 = new ArrayList<>();
		corners5.add(new Position(30.1, 45));
		corners5.add(new Position(30.1, 55));
		corners5.add(new Position(37, 55));
		corners5.add(new Position(37, 45));
		Shop shop5 = new Shop(corners5);
		sim.add(shop5);
		List<Position> corners6 = new ArrayList<>();
		corners6.add(new Position(25, 38));
		corners6.add(new Position(25, 41));
		corners6.add(new Position(28, 41));
		corners6.add(new Position(28, 38));
		Shop shop6 = new Shop(corners6);
		sim.add(shop6);
		List<Position> corners7 = new ArrayList<>();
		corners7.add(new Position(10, 38));
		corners7.add(new Position(10, 41));
		corners7.add(new Position(13, 41));
		corners7.add(new Position(13, 38));
		Shop shop7 = new Shop(corners7);
		sim.add(shop7);
		List<Position> corners8 = new ArrayList<>();
		corners8.add(new Position(20, 21));
		corners8.add(new Position(20, 24));
		corners8.add(new Position(55.1, 24));
		corners8.add(new Position(55.1, 21));
		Shop shop8 = new Shop(corners8);
		sim.add(shop8);
		List<Position> corners9 = new ArrayList<>();
		corners9.add(new Position(16, 17));
		corners9.add(new Position(16, 19));
		corners9.add(new Position(23, 19));
		corners9.add(new Position(23, 17));
		Shop shop9 = new Shop(corners9);
		sim.add(shop9);
		List<Position> corners10 = new ArrayList<>();
		corners10.add(new Position(58, 14));
		corners10.add(new Position(58, 18));
		corners10.add(new Position(60, 18));
		corners10.add(new Position(60, 14));
		Toilet toilet = new Toilet(corners10);
		sim.add(toilet);
		List<Position> corners11 = new ArrayList<>();
		corners11.add(new Position(0, 14));
		corners11.add(new Position(0, 18));
		corners11.add(new Position(2, 18));
		corners11.add(new Position(2, 14));
		Toilet toilet2 = new Toilet(corners11);
		sim.add(toilet2);
		List<Position> corners12 = new ArrayList<>();
		corners12.add(new Position(0, 45));
		corners12.add(new Position(0, 55));
		corners12.add(new Position(6, 55));
		corners12.add(new Position(6, 45));
		Restaurant restaurant = new Restaurant(corners12);
		sim.add(restaurant);
		List<Position> corners13 = new ArrayList<>();
		corners13.add(new Position(6.1, 45));
		corners13.add(new Position(6.1, 55));
		corners13.add(new Position(12, 55));
		corners13.add(new Position(12, 45));
		Restaurant restaurant2 = new Restaurant(corners13);
		sim.add(restaurant2);
		List<Position> corners14 = new ArrayList<>();
		corners14.add(new Position(40, 34));
		corners14.add(new Position(40, 41));
		corners14.add(new Position(43, 41));
		corners14.add(new Position(43, 34));
		Restaurant restaurant3 = new Restaurant(corners14);
		sim.add(restaurant3);

		sim.add(new QueueAnalyzer());
		sim.add(new TimeInQueueAnalyzer());
		sim.add(new ActivityDistributionAnalyzer());
		sim.add(new TimeToGateAnalyzer());
		sim.add(new TimeToGatePerFlightAnalyzer());
		sim.add(new AgentNumberAnalyzer());
		sim.add(new MissedFlightsAnalyzer());
		sim.add(new DistanceAnalyzer());
		return sim;
	}

	/**
	 * Generates walls for RTHA.
	 * 
	 * @param mapBuilder
	 *            The map builder.
	 * 
	 * @return The walls.
	 */
	private static Collection<SimulationObject> generateRTHAWalls(MapBuilder mapBuilder) {

		Collection<SimulationObject> objects = new ArrayList<>();
		objects.addAll(outerWalls());
		objects.addAll(landside());
		objects.addAll(checkInDesks(mapBuilder));
		objects.addAll(airsideArrival());
		objects.addAll(belts());
		objects.addAll(airsideDeparture());
		return objects;
	}

	/**
	 * The land side area.
	 * 
	 * @return The land side area objects.
	 */
	private static List<SimulationObject> landside() {
		SimulationObject w;
		List<SimulationObject> walls = new ArrayList<>();

		w = new QueueSeparator(37.5, 52.65, 6.95, 0.1);
		walls.add(w);

		w = new Wall(43.54, 55.97, 0.98, 0.1);
		walls.add(w);
		w = new Wall(44.43, 48.94, 0.1, 7.12);
		walls.add(w);
		w = new Wall(44.43, 52.63, 31.66, 0.11);
		walls.add(w);
		w = new Wall(76.0, 21.76, 0.1, 34.25);
		walls.add(w);
		w = new Wall(76.0, 56.01, 5.41, 0.1);
		walls.add(w);
		w = new Wall(81.33, 51.95, 0.1, 4.14);
		walls.add(w);
		w = new Wall(81.33, 51.95, 2.15, 0.1);
		walls.add(w);
		w = new Wall(83.38, 48.97, 0.1, 3.08);
		walls.add(w);
		w = new Wall(83.36, 48.97, 8.56, 0.19);
		walls.add(w);
		w = new Wall(91.92, 48.97, 0.1, 2.22);
		walls.add(w);
		w = new Wall(98.67, 21.76, 0.1, 29.42);
		walls.add(w);
		w = new Wall(91.92, 51.19, 11.99, 0.1);
		walls.add(w);
		w = new Wall(116.04, 50.59, 0.22, 7.21);
		walls.add(w);
		w = new Wall(116.04, 57.57, 4.86, 0.14);
		walls.add(w);
		w = new Wall(27.02, 63.1, 10.74, 0.1);
		walls.add(w);
		w = new Wall(37.65, 63.1, 0.13, 3.34);
		walls.add(w);
		w = new Wall(39.04, 68.51, 2.38, 0.1);
		walls.add(w);
		w = new Wall(41.32, 68.4, 0.1, 8.23);
		walls.add(w);
		w = new Wall(46.28, 52.64, 0.35, 6);
		walls.add(w);
		w = new Wall(63.9, 72.28, 10.0, 0.2);
		walls.add(w);
		w = new Wall(73.9, 69.08, 0.1, 3.45);
		walls.add(w);
		w = new Wall(73.9, 69.08, 12.64, 0.1);
		walls.add(w);
		w = new Wall(63.9, 72.28, 0.1, 4.44);
		walls.add(w);
		w = new Wall(86.44, 69.08, 0.11, 7.55);
		walls.add(w);
		w = new Wall(91.24, 63.1, 0.1, 13.73);
		walls.add(w);
		w = new Wall(91.24, 63.1, 6.9, 0.1);
		walls.add(w);
		w = new Wall(98.09, 63.1, 0.11, 13.73);
		walls.add(w);
		w = new Wall(98.23, 68.54, 2.24, 0.3);
		walls.add(w);
		w = new Wall(99.58, 68.54, 0.9, 4.06);
		walls.add(w);
		w = new Wall(112.83, 59.16, 5.22, 0.4);
		walls.add(w);
		w = new Wall(118.04, 59.29, 0.14, 5.33);
		walls.add(w);
		w = new Wall(112.83, 64.37, 5.35, 0.25);
		walls.add(w);
		w = new Wall(112.83, 59.16, 0.25, 5.46);
		walls.add(w);
		w = new Wall(113.54, 66.22, 5.37, 0.1);
		walls.add(w);
		w = new Wall(118.96, 75.66, 0.1, 1.07);
		walls.add(w);
		w = new Wall(113.54, 75.63, 5.46, 0.1);
		walls.add(w);
		w = new Wall(113.54, 66.22, 0.19, 9.5);
		walls.add(w);
		return walls;
	}

	/**
	 * Creates the outer walls.
	 * 
	 * @return The outer walls.
	 */
	private static Collection<SimulationObject> outerWalls() {
		SimulationObject w;
		List<SimulationObject> walls = new ArrayList<>();

		w = new Wall(8.82, 21.76, 97.57, 0.11);
		walls.add(w);
		w = new Wall(106.38, 21.76, 0.11, 2.46);
		walls.add(w);
		w = new Wall(106.44, 24.12, 10.03, 0.11);
		walls.add(w);
		w = new Wall(116.34, 24.12, 0.12, 6.52);
		walls.add(w);
		w = new Wall(116.44, 30.53, 14.83, 0.11);
		walls.add(w);
		w = new Wall(131.26, 11.58, 0.2, 20.49);
		walls.add(w);
		w = new Wall(131.46, 11.58, 40.0, 0.39);
		walls.add(w);
		w = new Wall(171.46, 11.58, 0.13, 32.34);
		walls.add(w);
		w = new Wall(161.57, 43.79, 10.02, 0.2);
		walls.add(w);
		w = new Wall(161.57, 43.79, 0.2, 2.7);
		walls.add(w);
		w = new Wall(161.51, 46.49, 1.69, 0.38);
		walls.add(w);
		w = new Wall(163.11, 46.49, 0.1, 11.3);
		walls.add(w);
		w = new Wall(120.75, 57.41, 42.45, 0.38);
		walls.add(w);
		w = new Wall(120.9, 57.74, 0.1, 8.76);
		walls.add(w);
		w = new Wall(118.87, 66.44, 2.07, 0.1);
		walls.add(w);
		w = new Wall(118.87, 66.44, 0.1, 5.51);
		walls.add(w);
		w = new Wall(118.9, 71.92, 11.76, 0.12);
		walls.add(w);
		w = new Wall(130.55, 71.92, 0.11, 4.83);
		walls.add(w);
		w = new Wall(36.55, 76.63, 94.11, 0.13);
		walls.add(w);
		w = new Wall(36.55, 71.92, 0.24, 4.84);
		walls.add(w);
		w = new Wall(36.73, 71.87, 2.31, 0.1);
		walls.add(w);
		w = new Wall(39.02, 66.35, 0.13, 5.68);
		walls.add(w);
		w = new Wall(26.97, 66.44, 12.05, 0.1);
		walls.add(w);
		w = new Wall(26.97, 49.17, 0.1, 17.33);
		walls.add(w);
		w = new Wall(5.07, 48.79, 22.02, 0.38);
		walls.add(w);
		w = new Wall(5.16, 25.62, 0.11, 23.17);
		walls.add(w);
		w = new Wall(5.16, 25.62, 3.77, 0.11);
		walls.add(w);
		w = new Wall(8.82, 21.76, 0.11, 3.85);
		walls.add(w);

		return walls;
	}

	/**
	 * Creates RTHA.
	 * 
	 * @param gui
	 *            With or without GUI.
	 * @param timeStep
	 *            The time step.
	 * @return The simulator.
	 */
	public static Simulator rotterdamTheHagueAirport(boolean gui, int timeStep) {

		BaseAgentGenerator agentGenerator = new BaseAgentGenerator(10);
		Simulator sim = new Simulator(gui, timeStep, new NoPassengerEndingConditions(), agentGenerator,
				new BaseLogger());

		MapBuilder mapBuilder = new MapBuilder(sim);
		Map map = sim.getMap();
		sim.addAll(generateRTHAWalls(mapBuilder));

		Collection<Desk> desks1 = map.getMapComponents(Desk.class);
		List<Desk> firstFour = new ArrayList<>();
		for (Desk d : desks1) {
			if (firstFour.size() < 4)
				firstFour.add(d);
			else
				break;
		}

		GateArea gateaArea = new GateArea(12, 22, 21, 9.5);
		sim.addAll(mapBuilder.gate(new Position(13, 23), 5, 10, 0));
		sim.add(gateaArea);
		sim.addAll(mapBuilder.gate(new Position(23, 23), 5, 10, 0));

		sim.add(new EntranceArea(50, 74, 10, 2.5));
		sim.add(new EntranceArea(102, 74, 10, 2.5));

		sim.addAll(mapBuilder.checkpoint(new Position(29, 62), 2, 0, false, 90));
		sim.addAll(mapBuilder.checkpoint(new Position(31.5, 57), 2, 0, false, 90));

		sim.add(new QueueSeparator(36, 61, 10.5, 0.1));
		sim.add(new QueueSeparator(36, 62, 9.5, 0.1));
		sim.add(new QueueSeparator(37.65, 62, 0.1, 1.1));
		sim.add(new QueueSeparator(39, 63, 7.5, 0.1));
		sim.add(new QueueSeparator(37.8, 64, 7.5, 0.1));
		sim.add(new QueueSeparator(39, 65, 7.5, 0.1));
		sim.add(new QueueSeparator(37.8, 66, 8.8, 0.1));
		sim.add(new QueueSeparator(46.57, 62.7, 0.1, 2.4));

		List<Position> corners = new ArrayList<>();
		corners.add(new Position(36, 61));
		corners.add(new Position(46.5, 61));
		corners.add(new Position(46.5, 66));
		corners.add(new Position(37.8, 66));
		corners.add(new Position(37.8, 62));
		corners.add(new Position(36, 62));
		sim.add(new QueuingArea(corners, new Position(46.5, 65.6), new Position(36, 61.4)));

		// flights
		List<Flight> flights = new ArrayList<>();
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1, gateaArea, firstFour,
				Utilities.getClosestQueuingArea(firstFour, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.25, gateaArea, firstFour,
				Utilities.getClosestQueuingArea(firstFour, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.42, gateaArea, firstFour,
				Utilities.getClosestQueuingArea(firstFour, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.5, gateaArea, firstFour,
				Utilities.getClosestQueuingArea(firstFour, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.83, gateaArea, firstFour,
				Utilities.getClosestQueuingArea(firstFour, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		flights.add(new Flight(FlightType.DEPARTING, 3600 * 1.92, gateaArea, firstFour,
				Utilities.getClosestQueuingArea(firstFour, map.getMapComponents(QueuingArea.class)),
				Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
						map.getMapComponents(QueuingArea.class))));
		// flights.add(new Flight(FlightType.DEPARTING, 3600 * 2.08, gateaArea,
		// firstFour,
		// Utilities.getClosestQueuingArea(firstFour,
		// map.getMapComponents(QueuingArea.class)),
		// Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
		// map.getMapComponents(QueuingArea.class))));
		// flights.add(new Flight(FlightType.DEPARTING, 3600 * 2.33, gateaArea,
		// firstFour,
		// Utilities.getClosestQueuingArea(firstFour,
		// map.getMapComponents(QueuingArea.class)),
		// Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
		// map.getMapComponents(QueuingArea.class))));
		// flights.add(new Flight(FlightType.DEPARTING, 3600 * 3.08, gateaArea,
		// firstFour,
		// Utilities.getClosestQueuingArea(firstFour,
		// map.getMapComponents(QueuingArea.class)),
		// Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
		// map.getMapComponents(QueuingArea.class))));
		// flights.add(new Flight(FlightType.DEPARTING, 3600 * 3.08, gateaArea,
		// firstFour,
		// Utilities.getClosestQueuingArea(firstFour,
		// map.getMapComponents(QueuingArea.class)),
		// Utilities.getClosestQueuingArea(map.getMapComponents(XRaySystem.class),
		// map.getMapComponents(QueuingArea.class))));
		sim.addAll(flights);

		for (Desk d : map.getMapComponents(Desk.class)) {
			sim.add(new OperatorAgent(map, new Position(d.getServingPosition().x, d.getServingPosition().y - 1), 0.25,
					80, new BasicOperatorCheckInActivity(d, 30)));
		}

		sim.add(new QueueAnalyzer());
		sim.add(new TimeInQueueAnalyzer());
		sim.add(new ActivityDistributionAnalyzer());
		sim.add(new TimeToGateAnalyzer());
		sim.add(new TimeToGatePerFlightAnalyzer());
		sim.add(new AgentNumberAnalyzer());
		sim.add(new MissedFlightsAnalyzer());
		sim.add(new DistanceAnalyzer());

		return sim;
	}

	/**
	 * Private constructor to prevent object creation.
	 */
	private SimulationBuilder() {
	}
}