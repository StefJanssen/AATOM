package model.agent.humanAgent.aatom.operationalLevel.observation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runners.MethodSorters;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.impl.BasicMovementModule;
import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.aatom.strategicLevel.goal.Goal;
import model.agent.humanAgent.aatom.strategicLevel.goal.GoalModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.PlanningModule;
import model.agent.humanAgent.aatom.tacticalLevel.TacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.Wall;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the operational model.
 * 
 * @author S.A.M. Janssen
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicPassengerObservationModuleTest {

	
	
	private BasicPassengerObservationModule observation;

	/**
	 * Generates a base agent.
	 * 
	 * @return The agent.
	 */
	public AatomHumanAgent generateBaseAgent() {
		PlanningModule planner = new PlanningModule() {

			@Override
			public void update(int timeStep) {
			}

			@Override
			public Activity getNextActivity() {
				return null;
			}
		};

		GoalModule goal = new GoalModule(new ArrayList<Goal>()) {
		};

		BeliefModule belief = new BeliefModule();

		StrategicModel strategy = new StrategicModel(planner, goal, belief) {

			@Override
			public boolean wantsToBeDestroyed() {
				return false;
			}
		};

		ActivityModule activity = new ActivityModule();
		NavigationModule navigation = new NavigationModule();

		TacticalModel tactical = new TacticalModel(activity, navigation) {
		};

		MovementModule movement = new BasicMovementModule(1);

		observation = new BasicPassengerObservationModule();

		CommunicationModule communication = new CommunicationModule() {

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};

		OperationalModel operational = new OperationalModel(movement, observation, communication) {
		};

		return new AatomHumanAgent(new Position(10.25, 10.75), 0.2, 80, strategy, tactical, operational) {
		};
	}

	/**
	 * Tests the observation of a human agent.
	 */
	@Test
	public void testAgentObservation() {
		AatomHumanAgent agent = generateBaseAgent();
		
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);

		Thread t = new Thread(s);
		t.start();
		while(!s.isRunning()) {
		}
		s.setRunning(false);
		Collection<HumanAgent> a = observation.getObservation(HumanAgent.class);
		s.step();
		Assert.assertEquals(a.size(), 1);
	}

	/**
	 * Tests the observation of a human agent.
	 */
	@Test
	public void testAgentObservation2() {
		// generate passenger to observe
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);

		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);

		Passenger p = new Passenger.Builder<>().setPosition(new Position(10.25, 10)).setFlight(f)
				.setLuggage(new Luggage(LuggageType.CARRY_ON, 1, 1)).build();

		AatomHumanAgent agent = generateBaseAgent();
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		s.add(p);

		Thread t = new Thread(s);
		t.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Collection<HumanAgent> a = observation.getObservation(HumanAgent.class);
		Assert.assertEquals(a.size(), 2);
	}

	/**
	 * Tests the observation of a human agent.
	 */
	@Test
	public void testPassengerObservation() {
		// generate passenger to observe
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);

		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);

		Passenger p = new Passenger.Builder<>().setPosition(new Position(10.25, 10)).setFlight(f)
				.setLuggage(new Luggage(LuggageType.CARRY_ON, 1, 1)).build();

		AatomHumanAgent agent = generateBaseAgent();
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		s.add(p);

		Thread t = new Thread(s);
		t.start();
		while(!s.isRunning()) {
		}
		s.setRunning(false);
		Collection<Passenger> a = observation.getObservation(Passenger.class);
		s.setRunning(true);
		Assert.assertEquals(a.size(), 1);
	}

	/**
	 * Tests the observation of a human agent.
	 */
	@Test
	public void testNoPassengerObservation() {
		// generate passenger to observe
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);

		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);

		Passenger p = new Passenger.Builder<>().setPosition(new Position(1.25, 10)).setFlight(f)
				.setLuggage(new Luggage(LuggageType.CARRY_ON, 1, 1)).build();

		AatomHumanAgent agent = generateBaseAgent();
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		s.add(p);

		Thread t = new Thread(s);
		t.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Collection<Passenger> a = observation.getObservation(Passenger.class);
		Assert.assertEquals(a.size(), 0);
	}

	/**
	 * Tests the observation of a human agent.
	 */
	@Test
	public void testWallObservation() {

		Wall w = new Wall(10, 10, 5, 0.1);
		AatomHumanAgent agent = generateBaseAgent();
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		s.add(w);

		Thread t = new Thread(s);
		t.start();
		while(!s.isRunning()) {
		}
		s.setRunning(false);
		Collection<PhysicalObject> a = observation.getObservation(PhysicalObject.class);
		Assert.assertEquals(a.size(), 1);
	}

	/**
	 * Tests the observation of a human agent.
	 */
	@Test
	public void testDeskObservation() {
		Desk d = new Desk(10, 10, 1, 0.1, new Position(10.5, 10.5));
		AatomHumanAgent agent = generateBaseAgent();
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		s.add(d);

		Thread t = new Thread(s);
		t.start();
		while(!s.isRunning()) {
		}
		s.setRunning(false);
		Collection<Desk> a = observation.getObservation(Desk.class);
		s.setRunning(true);
		Assert.assertEquals(a.size(), 1);
	}

}