package model.environment.position;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the position class.
 * 
 * @author S.A.M. Janssen
 */
public class PositionTest {

	/**
	 * Tests the position constructor.
	 */
	@Test
	public void testConstructor() {
		Position p = new Position(10, 10);
		Assert.assertEquals(p.x, 10, 0.001);
		Assert.assertEquals(p.y, 10, 0.001);

	}

	/**
	 * Tests the position math.
	 */
	@Test
	public void testPositionMath() {
		Position p = new Position(10, 10);
		Assert.assertEquals(p.distanceTo(new Position(15, 10)), 5, 0.001);
		Assert.assertEquals(p.distanceTo(new Position(10, 10)), 0, 0.001);
		Assert.assertNotEquals(p.distanceTo(new Position(10, 10)), 1, 0.001);

		Assert.assertTrue(p.equals(new Position(10, 10)));
		Assert.assertFalse(p.equals(new Position(10.1, 10)));

		Assert.assertEquals(p, p.clone());

	}
}
