package model.agent.humanAgent.aatom.operationalLevel.action.communication.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.Passenger;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.impl.BasicMovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.aatom.strategicLevel.goal.Goal;
import model.agent.humanAgent.aatom.strategicLevel.goal.GoalModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.PlanningModule;
import model.agent.humanAgent.aatom.tacticalLevel.TacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.aatom.tacticalLevel.activity.operator.LuggageCheckActivity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.operator.impl.BasicLuggageCheckActivity;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.luggage.LuggageType;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the passenger communication.
 * 
 * @author S.A.M. Janssen
 */
public class BasicOperatorCommunicationModuleTest {

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
		ObservationModule observation = new ObservationModule() {

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}
		};
		LuggageCheckActivity c = new BasicLuggageCheckActivity();

		CommunicationModule communication = new BasicOperatorCommunicationModule(c);

		OperationalModel operational = new OperationalModel(movement, observation, communication) {
		};

		return new AatomHumanAgent(new Position(10.25, 10.75), 0.2, 80, strategy, tactical, operational) {
		};
	}

	/**
	 * Generates a base agent.
	 * 
	 * @return The agent.
	 */
	public Passenger generatePassengerAgent() {
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);

		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);

		return new Passenger.Builder<>().setPosition(new Position(10, 10)).setFlight(f)
				.setLuggage(new Luggage(LuggageType.CARRY_ON, 1, 1)).build();
	}

	/**
	 * Tests if the waiting order is received properly.
	 */
	@Test
	public void testSearchCommunication() {
		AatomHumanAgent a = generateBaseAgent();
		Passenger p = generatePassengerAgent();
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(5)).setGui(false).build();
		s.add(p);
		s.add(a);

		Thread t = new Thread(s);
		t.start();
		while(!s.isRunning()) {
		}
		s.setRunning(false);

		Collection<Luggage> luggage = p.getLuggage();
		for (Luggage l : luggage) {
			a.communicate(CommunicationType.SEARCH, l);
		}
		s.setRunning(true);
		try {
			Thread.sleep(1);
			t.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Assert.assertTrue(p.getStopOrder());

	}
}
