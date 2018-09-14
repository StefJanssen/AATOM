package model.agent;

import org.junit.Assert;
import org.junit.Test;

import model.environment.position.Position;

/**
 * Tests the agent class.
 * 
 * @author S.A.M. Janssen
 */
public class AgentTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		Agent a = new Agent(new Position(10, 10)) {

			@Override
			public void update(int timeStep) {
			}

			@Override
			public boolean getWantsToBeRemoved() {
				return false;
			}
		};
		Assert.assertEquals(new Position(10, 10), a.getPosition());
	}

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testLogging() {
		Agent a = new Agent(new Position(10, 10)) {

			@Override
			public void update(int timeStep) {
			}

			@Override
			public boolean getWantsToBeRemoved() {
				return false;
			}
		};
		a.setLog(new String[] { "Hello", "World" });
		String[] tmp = a.writeLog();
		Assert.assertEquals("Hello", tmp[0]);
		Assert.assertEquals("World", tmp[1]);
		Assert.assertNull(a.writeLog());
	}

}
