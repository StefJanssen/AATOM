package model.agent.humanAgent.aatom.operationalLevel.action.movement;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.environment.position.Vector;

/**
 * Tests the static movement module.
 * 
 * @author S.A.M. Janssen
 */
public class MovementModuleTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		MovementModule movement = new MovementModule(1) {

			@Override
			public Vector getMove(int timeStep) {
				return new Vector(1, 1);
			}
		};
		Assert.assertEquals(movement.getDesiredSpeed(), 1, 0.001);
		Assert.assertNull(movement.getChair());
		Assert.assertFalse(movement.getStopOrder());
		Assert.assertFalse(movement.isSitting());
	}

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testStopOrder() {
		MovementModule movement = new MovementModule(1) {

			@Override
			public Vector getMove(int timeStep) {
				return new Vector(1, 1);
			}
		};
		movement.setStopOrder(10);
		Assert.assertTrue(movement.getStopOrder());
	}

	/**
	 * Tests the constructor with an illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalConstructor() {
		new MovementModule(-1) {

			@Override
			public Vector getMove(int timeStep) {
				return new Vector(1, 1);
			}
		};
	}

	/**
	 * Tests the speed bound method.
	 */
	@Test
	public void testBoundSpeed() {
		MovementModule movement = new MovementModule(1) {

			@Override
			public Vector getMove(int timeStep) {
				return new Vector(1, 1);
			}
		};

		Vector v = movement.boundSpeed(new Vector(10, 10));
		Assert.assertEquals(v.length(), 1.5, 0.001);
	}

}
