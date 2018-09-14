package model.agent.humanAgent.aatom.operationalLevel.action.movement;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
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
import model.environment.objects.physicalObject.Wall;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the static movement module.
 * 
 * @author S.A.M. Janssen
 */
public class BasicMovementModuleTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		BasicMovementModule basicMovement = new BasicMovementModule(1);
		Assert.assertEquals(basicMovement.getDesiredSpeed(), 1, 0.001);
	}

	/**
	 * Generates a base agent with a static movement model.
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
		ArrayList<Position> goals = new ArrayList<>();
		goals.add(new Position(1, 1));
		NavigationModule navigation = new NavigationModule(goals);

		TacticalModel tactical = new TacticalModel(activity, navigation) {
		};

		MovementModule movement = new BasicMovementModule(1);

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

		return new AatomHumanAgent(new Position(10, 10), 0.2, 80, strategy, tactical, operational) {
		};
	}

	/**
	 * A basic movement test. In this case, the agent should not move at all.
	 */
	@Test
	public void basicMovementTest() {
		AatomHumanAgent a = generateBaseAgent();

		Simulator s = new Simulator.Builder<>().setGui(false).setEndingConditions(new BaseEndingConditions(20))
				.createSimulator();
		s.add(a);
		Thread t = new Thread(s);
		t.start();

		Assert.assertEquals(a.getPosition(), new Position(10, 10));
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(a.getPosition().distanceTo(new Position(1, 1)) < 0.5);
	}

	/**
	 * The basic movement model moves around the walls.
	 */
	@Test
	public void basicWallMovementTest() {
		Simulator s2 = new Simulator.Builder<>().setGui(false).setEndingConditions(new BaseEndingConditions(20))
				.createSimulator();
		AatomHumanAgent a2 = generateBaseAgent();
		Wall w = new Wall(7, 2, 0.1, 8);
		s2.add(w);
		s2.add(a2);
		Thread t2 = new Thread(s2);
		t2.start();

		Assert.assertEquals(a2.getPosition(), new Position(10, 10));
		try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(a2.getPosition().distanceTo(new Position(1, 1)) < 0.5);
	}

	/**
	 * The basic movement model will remove its goal if it is stuck inside a
	 * room.
	 */
	@Test
	public void basicWallStuckMovementTest() {
		Simulator s3 = new Simulator.Builder<>().setGui(false).setEndingConditions(new BaseEndingConditions(20))
				.createSimulator();
		AatomHumanAgent a3 = generateBaseAgent();
		Wall w = new Wall(7, 2, 0.1, 9);
		Wall w2 = new Wall(7, 11, 4, 0.1);
		Wall w3 = new Wall(7, 2, 4, 0.1);
		Wall w4 = new Wall(11, 2, 0.1, 9);

		s3.add(w);
		s3.add(w2);
		s3.add(w3);
		s3.add(w4);

		s3.add(a3);
		Thread t3 = new Thread(s3);
		t3.start();

		Assert.assertEquals(a3.getPosition(), new Position(10, 10));
		Assert.assertEquals(a3.getGoalPosition(), new Position(1, 1));

		try {
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(a3.getPosition(), new Position(10, 10));
		Assert.assertEquals(a3.getGoalPosition(), Position.NO_POSITION);

	}

}
