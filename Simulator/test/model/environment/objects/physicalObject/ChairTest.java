package model.environment.objects.physicalObject;

import java.awt.Color;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.operationalLevel.action.communication.CommunicationType;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the chair class.
 * 
 * @author S.A.M. Janssen
 */
public class ChairTest {

	/**
	 * Tests the chair constructor.
	 */
	@Test
	public void testConstructor() {
		Chair c = new Chair(new Position(10, 10), new Position(9.75, 9.75), 0.5);
		Assert.assertEquals(c.getEntryPosition(), new Position(9.75, 9.75));
		Assert.assertEquals(c.getPosition(), new Position(10, 10));
	}

	/**
	 * Test exception for entry position in chair.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryPositionInChairException() {
		new Chair(new Position(10, 10), new Position(10.25, 10.25), 0.5);
	}

	/**
	 * Test exception for entry position in chair.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFarEntryPositionException() {
		new Chair(new Position(10, 10), new Position(12.25, 12.25), 0.5);
	}

	/**
	 * Tests the occupation.
	 */
	@Test
	public void testOccupation() {
		Chair c = new Chair(new Position(10, 10), new Position(9.75, 9.75), 0.5);
		HumanAgent a = new HumanAgent(new Position(9.75, 9.75), 0.2, 80, Color.RED) {

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public boolean getWantsToBeRemoved() {
				return false;
			}

			@Override
			public void update(int timeStep) {
			}
		};

		Assert.assertFalse(c.isOccupied());
		c.setOccupied(a);
		Assert.assertTrue(c.isOccupied());
	}

	/**
	 * Tests the chair occupation after the agent is destroyed.
	 */
	@Test
	public void testOccupationAfterDestroy() {
		Chair c = new Chair(new Position(10, 10), new Position(9.75, 9.75), 0.5);
		HumanAgent a = new HumanAgent(new Position(9.75, 9.75), 0.2, 80, Color.RED) {

			@Override
			public void communicate(CommunicationType type, Object communication) {

			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public boolean getWantsToBeRemoved() {
				if (map.getTime() > 5)
					return true;
				return false;
			}

			@Override
			public void update(int timeStep) {

			}
		};
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(10)).setGui(false)
				.createSimulator();
		s.add(c);
		s.add(a);

		Assert.assertFalse(c.isOccupied());
		c.setOccupied(a);
		Assert.assertTrue(c.isOccupied());

		Thread t = new Thread(s);
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertFalse(c.isOccupied());

	}

	/**
	 * Test the occupation.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testOccupationException() {
		Chair c = new Chair(new Position(10, 10), new Position(9.75, 9.75), 0.5);
		HumanAgent a = new HumanAgent(new Position(20, 10), 0.2, 80, Color.RED) {

			@Override
			public void communicate(CommunicationType type, Object communication) {

			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public boolean getWantsToBeRemoved() {
				return false;
			}

			@Override
			public void update(int timeStep) {

			}
		};
		c.setOccupied(a);
	}

}
