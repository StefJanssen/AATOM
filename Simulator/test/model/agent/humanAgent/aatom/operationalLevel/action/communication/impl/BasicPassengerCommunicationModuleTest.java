package model.agent.humanAgent.aatom.operationalLevel.action.communication.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.AatomHumanAgent;
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
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the passenger communication.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerCommunicationModuleTest {

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

		CommunicationModule communication = new BasicPassengerCommunicationModule();

		OperationalModel operational = new OperationalModel(movement, observation, communication) {
		};

		return new AatomHumanAgent(new Position(10.25, 10.75), 0.2, 80, strategy, tactical, operational) {
		};
	}

	/**
	 * Tests if the waiting order is received properly.
	 */
	@Test
	public void testSearchCommunication() {
		AatomHumanAgent a = generateBaseAgent();

		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(120)).setGui(false)
				.build();

		s.add(a);

		Thread t = new Thread(s);
		t.start();
		while(!s.isRunning()) {
		}
		s.setRunning(false);
		a.communicate(CommunicationType.SEARCH, 4.0);
		s.setRunning(true);
		try {
			Thread.sleep(10);
			t.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Assert.assertFalse(a.getStopOrder());

	}

	/**
	 * Tests if the waiting order is received properly.
	 */
	@Test
	public void testWaitingCommunication2() {
		AatomHumanAgent a = generateBaseAgent();

		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(120)).setGui(false)
				.build();

		s.add(a);

		Thread t = new Thread(s);
		t.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		a.communicate(CommunicationType.WAIT, 150.0);
		try {
			Thread.sleep(10);
			t.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Assert.assertTrue(a.getStopOrder());
	}

}
