package model.environment.objects.physicalObject;

import java.awt.Color;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * Tests the chair class.
 * 
 * @author S.A.M. Janssen
 */
public class DeskTest {

	/**
	 * Tests the desk constructor.
	 */
	@Test
	public void testConstructor() {
		Desk d = new Desk(10, 10, 1, 0.1, new Position(10.5, 10.3));
		Assert.assertEquals(d.getServingPosition(), new Position(10.5, 10.3));
		Assert.assertEquals(d.getPosition(), new Position(10, 10));
	}

	/**
	 * Test exception for entry position in chair.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryPositionInChairException() {
		new Desk(10, 10, 1, 0.1, new Position(10.25, 10.05));
	}

	/**
	 * Test exception for entry position in chair.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFarEntryPositionException() {
		new Desk(10, 10, 1, 0.1, new Position(13, 10));
	}

	/**
	 * Tests the occupation.
	 */
	@Test
	public void testOccupation() {
		Desk d = new Desk(10, 10, 1, 0.1, new Position(10.5, 10.3));
		HumanAgent a = new HumanAgent(new Position(10.5, 10.3), 0.2, 80, Color.RED) {

			@Override
			public void communicate(CommunicationType type, Object communication) {

			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public boolean wantsToBeDestoryed() {
				if (map.getTime() > 5)
					return true;
				return false;
			}

			@Override
			public void update(int timeStep) {

			}
		};

		Assert.assertFalse(d.isOccupied());
		d.reserveDesk(a);
		Assert.assertTrue(d.isOccupied());
	}

	/**
	 * Tests the chair occupation after the agent is destroyed.
	 */
	@Test
	public void testOccupationAfterDestroy() {
		Desk d = new Desk(10, 10, 1, 0.1, new Position(10.5, 10.3));
		HumanAgent a = new HumanAgent(new Position(10.5, 10.3), 0.2, 80, Color.RED) {

			@Override
			public void communicate(CommunicationType type, Object communication) {

			}

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}

			@Override
			public boolean wantsToBeDestoryed() {
				if (map.getTime() > 5)
					return true;
				return false;
			}

			@Override
			public void update(int timeStep) {

			}
		};
		Simulator s = new Simulator.Builder<>().setEndingConditions(new BaseEndingConditions(10)).setGui(false).build();
		s.add(d);
		s.add(a);

		Assert.assertFalse(d.isOccupied());
		d.reserveDesk(a);
		Assert.assertTrue(d.isOccupied());

		Thread t = new Thread(s);
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertFalse(d.isOccupied());

	}

}
