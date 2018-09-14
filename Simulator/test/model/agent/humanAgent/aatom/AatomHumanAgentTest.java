package model.agent.humanAgent.aatom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.agent.humanAgent.aatom.strategicLevel.StrategicModel;
import model.agent.humanAgent.aatom.strategicLevel.belief.BeliefModule;
import model.agent.humanAgent.aatom.strategicLevel.goal.Goal;
import model.agent.humanAgent.aatom.strategicLevel.goal.GoalModule;
import model.agent.humanAgent.aatom.strategicLevel.reasoning.planning.ActivityPlanner;
import model.agent.humanAgent.aatom.tacticalLevel.TacticalModel;
import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.ActivityModule;
import model.agent.humanAgent.aatom.tacticalLevel.navigation.NavigationModule;
import model.environment.objects.physicalObject.Chair;
import model.environment.position.Position;
import model.environment.position.Vector;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the aatom human agent class.
 * 
 * @author S.A.M. Janssen
 */
public class AatomHumanAgentTest {

	/**
	 * Generates a base agent.
	 * 
	 * @return The agent.
	 */
	public AatomHumanAgent generateBaseAgent() {
		ActivityPlanner planner = new ActivityPlanner() {

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
			public boolean getWantsToBeRemoved() {
				return false;
			}
		};

		ActivityModule activity = new ActivityModule();
		NavigationModule navigation = new NavigationModule();

		TacticalModel tactical = new TacticalModel(activity, navigation) {
		};

		MovementModule movement = new MovementModule(1) {

			@Override
			public Vector getMove(int timeStep) {
				return new Vector(0, 0);
			}
		};

		ObservationModule observation = new ObservationModule() {

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}
		};

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
	 * Tests the sitting of the agent.
	 */
	@Test
	public void testSitDown() {
		AatomHumanAgent agent = generateBaseAgent();

		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false)
				.createSimulator();

		s.add(agent);

		Chair chair = new Chair(new Position(10, 10), new Position(10.25, 10.75), 0.5);
		s.add(chair);
		Thread t = new Thread(s);
		t.start();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Assert.assertEquals(agent.getPosition(), new Position(10.25, 10.75));
		Assert.assertFalse(agent.isSitting());
		Assert.assertTrue(agent.operationalModel.setSitDown(chair));
		Assert.assertTrue(agent.isSitting());
		try {
			Thread.sleep(1);
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(agent.getPosition(), new Position(10.25, 10.25));

	}

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {

		AatomHumanAgent agent = generateBaseAgent();

		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false)
				.createSimulator();

		s.add(agent);
		Assert.assertNull(agent.getActiveActivity());
		Assert.assertEquals(agent.getCurrentVelocity(), new Vector(0, 0));
		Assert.assertEquals(agent.getGoalPosition(), Position.NO_POSITION);
		Assert.assertTrue(agent.getGoalPositions().isEmpty());
		Assert.assertEquals(agent.getColor(), Color.RED);
		Assert.assertEquals(agent.getDesiredSpeed(), 1, 0.001);
		Assert.assertFalse(agent.getWantsToBeRemoved());
		Assert.assertFalse(agent.getStopOrder());
		Assert.assertFalse(agent.isSitting());
		Assert.assertFalse(agent.isDestroyed());
		Assert.assertFalse(agent.isQueuing());
	}

	/**
	 * Test an illegal constructor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalConstructor() {
		new AatomHumanAgent(new Position(10, 10), 0.2, 80, null, null, null) {
		};
	}

	/**
	 * Test an illegal constructor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalConstructor2() {
		new AatomHumanAgent(null, 0.2, 80, null, null, null) {
		};
	}

	/**
	 * Test an illegal constructor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalConstructor3() {
		new AatomHumanAgent(new Position(10, 10), -0.2, 80, null, null, null) {
		};
	}

	/**
	 * Test an illegal constructor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalConstructor4() {
		new AatomHumanAgent(new Position(10, 10), 0.2, -80, null, null, null) {
		};
	}

}
