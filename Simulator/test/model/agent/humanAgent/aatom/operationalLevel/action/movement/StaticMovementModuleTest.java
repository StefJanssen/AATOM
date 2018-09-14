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
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the static movement module.
 * 
 * @author S.A.M. Janssen
 */
public class StaticMovementModuleTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		StaticMovementModule staticMovement = new StaticMovementModule();
		Assert.assertEquals(staticMovement.getDesiredSpeed(), 0, 0.001);
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
		new Position(1, 1);
		NavigationModule navigation = new NavigationModule(goals);

		TacticalModel tactical = new TacticalModel(activity, navigation) {
		};

		MovementModule movement = new StaticMovementModule();

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
		Assert.assertEquals(a.getPosition(), new Position(10, 10));
	}
}
