package model.agent.humanAgent;

import java.awt.Color;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;
import util.io.logger.Logger;

/**
 * Tests the human agent class.
 * 
 * @author S.A.M. Janssen
 */
public class HumanAgentTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		HumanAgent agent = new HumanAgent(new Position(10, 10), 0.2, 80, Color.RED) {

			@Override
			public boolean wantsToBeDestoryed() {
				return false;
			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};
		Assert.assertEquals(agent.getPosition(), new Position(10, 10));
		Assert.assertEquals(agent.getMass(), 80, 0.001);
		Assert.assertEquals(agent.getRadius(), 0.2, 0.001);
		Assert.assertEquals(agent.getColor(), Color.RED);
		Assert.assertFalse(agent.isDestroyed());
	}

	/**
	 * Tests the updating after removal of the agent.
	 */
	@Test
	public void testRemoving() {
		HumanAgent agent = new HumanAgent(new Position(10, 10), 0.2, 80, Color.RED) {

			@Override
			public boolean wantsToBeDestoryed() {
				return map.getTime() > 10;
			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		Thread t = new Thread(s);
		t.start();
		Assert.assertFalse(agent.isDestroyed());

		try {
			t.join();
		} catch (InterruptedException e) {
		}
		Assert.assertTrue(agent.isDestroyed());
	}

	/**
	 * Test the update.
	 */
	@Test
	public void testUpdateAgent() {
		HumanAgent agent = new HumanAgent(new Position(10, 10), 0.2, 80, Color.RED) {

			@Override
			public void update(int timeStep) {
				super.update(timeStep);
				setLog(new String[] { "Hello" });
			}

			@Override
			public boolean wantsToBeDestoryed() {
				return map.getTime() > 1;
			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};

		Simulator s = new Simulator.Builder<>().setLogger(Logger.NO_LOGGER)
				.setEndingConditions(new BaseEndingConditions(3)).setGui(false).build();
		s.add(agent);
		Thread t = new Thread(s);
		Assert.assertNull(agent.writeLog());
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(agent.writeLog());
	}

	/**
	 * Test actions after the agent was removed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeTimeStep() {
		HumanAgent agent = new HumanAgent(new Position(10, 10), 0.2, 80, Color.RED) {

			@Override
			public void update(int timeStep) {
				super.update(timeStep);
				setLog(new String[] { "Hello" });
			}

			@Override
			public boolean wantsToBeDestoryed() {
				return map.getTime() > 1;
			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};
		agent.update(-1);
	}

	/**
	 * Test actions after the agent was removed.
	 */
	@Test(expected = IllegalStateException.class)
	public void testPostRemoveActions() {
		HumanAgent agent = new HumanAgent(new Position(10, 10), 0.2, 80, Color.RED) {

			@Override
			public boolean wantsToBeDestoryed() {
				return map.getTime() > 10;
			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(20)).setGui(false).build();
		s.add(agent);
		Thread t = new Thread(s);
		t.start();
		Assert.assertFalse(agent.isDestroyed());

		try {
			t.join();
		} catch (InterruptedException e) {
		}
		Assert.assertTrue(agent.isDestroyed());
		agent.update(50);
	}
}