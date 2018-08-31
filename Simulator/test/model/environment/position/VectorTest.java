package model.environment.position;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the vector class.
 * 
 * @author S.A.M. Janssen
 */
public class VectorTest {

	/**
	 * Tests the position constructor.
	 */
	@Test
	public void testConstructor() {
		Vector v = new Vector(10, 10);
		Assert.assertEquals(v.x, 10, 0.001);
		Assert.assertEquals(v.y, 10, 0.001);

	}

	/**
	 * Tests the position math.
	 */
	@Test
	public void testVectorMath() {
		Vector v = new Vector(0, 10);
		Assert.assertEquals(v.length(), 10, 0.001);
		Vector v2 = v.addVector(new Vector(0, 2));
		Assert.assertEquals(v2.x, 0, 0.001);
		Assert.assertEquals(v2.y, 12, 0.001);

		Vector v3 = v.addVector(new Vector(0, -10));
		Assert.assertEquals(v3.x, 0, 0.001);
		Assert.assertEquals(v3.y, 0, 0.001);

		Vector v3b = v.subtractVector(new Vector(0, 10));
		Assert.assertEquals(v3b.x, 0, 0.001);
		Assert.assertEquals(v3b.y, 0, 0.001);

		Vector v4 = v.normalize();
		Assert.assertEquals(v4.length(), 1, 0.001);
		Assert.assertNotEquals(v4.length(), 10, 0.001);

		Vector v5 = v.reverse();
		Assert.assertEquals(v5.length(), 10, 0.001);
		Assert.assertEquals(v5.x, 0, 0.001);
		Assert.assertEquals(v5.y, -10, 0.001);

		Vector v6 = new Vector(2, 2).elementWiseMultiply(new Vector(3, 3));
		Assert.assertEquals(v6.x, 6, 0.001);
		Assert.assertEquals(v6.y, 6, 0.001);

		Vector v7 = new Vector(10, 0);
		Assert.assertTrue(v7.isAproximateRotation(90, v));
		Assert.assertFalse(v7.isAproximateRotation(91, v));
		Assert.assertTrue(v.isAproximateRotation(270, v7));
		Assert.assertTrue(v.isAproximateRotation(0, v));

		float d = v.multiply(new Vector(1, 1));
		Assert.assertEquals(d, 10, 0.001);
		float d2 = v.multiply(new Vector(1, 3));
		Assert.assertEquals(d2, 30, 0.001);

		Vector v8 = v.scalarMultiply(5);
		Assert.assertEquals(v8, new Vector(0, 50));

	}
}
