package model.agent.humanAgent.aatom.operationalLevel.action.movement.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.AatomHumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.OperationalModel;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.impl.RandomMovementModule;
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
import util.math.RandomPlus;

/**
 * Tests the static movement module.
 * 
 * @author S.A.M. Janssen
 */
public class RandomMovementModuleTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		RandomPlus random = new RandomPlus(1);
		RandomMovementModule staticMovement = new RandomMovementModule(1, random);
		Assert.assertEquals(staticMovement.getDesiredSpeed(), 1, 0.001);
	}

	/**
	 * Generates a base agent with a static movement model.
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
		ArrayList<Position> goals = new ArrayList<>();
		new Position(1, 1);
		NavigationModule navigation = new NavigationModule(goals);

		TacticalModel tactical = new TacticalModel(activity, navigation) {
		};

		MovementModule movement = new RandomMovementModule(1, new RandomPlus(1));

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

		return new AatomHumanAgent(new Position(5, 5), 0.2, 80, strategy, tactical, operational) {
		};
	}

	/**
	 * A basic movement test. In this case, the agent should move randomly. The
	 * expected ending position is based on the specified random seed.
	 */
	@Test
	public void basicMovementTest() {
		AatomHumanAgent a = generateBaseAgent();
		Simulator s = new Simulator.Builder<>().setGui(false).setEndingConditions(new BaseEndingConditions(20)).build();
		s.add(a);
		Thread t = new Thread(s);
		t.start();
		Assert.assertEquals(a.getPosition(), new Position(5, 5));
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(a.getPosition().x, 4.099, 0.01);
		Assert.assertEquals(a.getPosition().y, 8.283, 0.01);

	}
}
