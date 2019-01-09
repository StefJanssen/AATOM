package model.environment.objects.area;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.environment.position.Position;

/**
 * Tests the area classes.
 * 
 * @author S.A.M. Janssen
 */
public class AreaTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		Shop s = new Shop(10, 10, 3, 3);
		Assert.assertTrue(s.getCorners().contains(new Position(10, 10)));
		Assert.assertTrue(s.getCorners().contains(new Position(10, 13)));
		Assert.assertTrue(s.getCorners().contains(new Position(13, 13)));
		Assert.assertTrue(s.getCorners().contains(new Position(13, 10)));
		Assert.assertFalse(s.getCorners().contains(new Position(13, 10.1)));

		Shop s3 = new Shop(10, 13, 13, 10, 10, 10, 13, 13);
		Assert.assertTrue(s3.getCorners().contains(new Position(10, 10)));
		Assert.assertTrue(s3.getCorners().contains(new Position(10, 13)));
		Assert.assertTrue(s3.getCorners().contains(new Position(13, 13)));
		Assert.assertTrue(s3.getCorners().contains(new Position(13, 10)));
		Assert.assertFalse(s3.getCorners().contains(new Position(13, 10.1)));
	}

	/**
	 * Test exception for illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeWidthException() {
		new Shop(10, 13, 13, 9, 10, 10, 8, 8);
	}

	/**
	 * Test exception for illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeWidthException2() {
		new Shop(10, 10, 3, -3);
	}

	/**
	 * test the queuing area.
	 */
	@Test
	public void testQueuingArea() {
		QueuingArea q = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		Assert.assertTrue(q.contains(new Position(11, 11)));
		Assert.assertTrue(q.contains(new Position(10, 10)));
		Assert.assertFalse(q.contains(new Position(9, 10)));
	}

	/**
	 * Test the shape functionality.
	 */
	@Test
	public void testShape() {
		// rectangular shape
		Shop s = new Shop(10, 10, 3, 3);
		Assert.assertTrue(s.contains(new Position(11, 11)));
		Assert.assertTrue(s.contains(new Position(10, 10)));
		Assert.assertFalse(s.contains(new Position(9, 10)));

		// u shape
		List<Position> corners = new ArrayList<>();
		corners.add(new Position(10, 10));
		corners.add(new Position(11, 10));
		corners.add(new Position(11, 11));
		corners.add(new Position(13, 11));
		corners.add(new Position(13, 10));
		corners.add(new Position(14, 10));
		corners.add(new Position(14, 14));
		corners.add(new Position(10, 14));
		Shop s2 = new Shop(corners);

		Assert.assertTrue(s2.contains(new Position(10, 10)));
		Assert.assertTrue(s2.contains(new Position(10.5, 10)));
		Assert.assertFalse(s2.contains(new Position(12, 10)));
	}

	/**
	 * Test exception for illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongQueueEntrancePosition() {
		new QueuingArea(10, 10, 4, 4, new Position(20, 10), new Position(10, 10));
	}

	/**
	 * Test exception for illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongQueueEntrancePosition2() {
		List<Position> corners = new ArrayList<>();
		corners.add(new Position(10, 10));
		corners.add(new Position(11, 10));
		corners.add(new Position(11, 11));
		corners.add(new Position(13, 11));
		corners.add(new Position(13, 10));
		corners.add(new Position(14, 10));
		corners.add(new Position(14, 14));
		corners.add(new Position(10, 14));
		new QueuingArea(corners, new Position(12, 10), new Position(10, 10));

	}

	/**
	 * Test exception for illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongQueueLeavingPosition() {
		new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 16));
	}

	/**
	 * Test exception for illegal argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongQueueLeavingPosition2() {
		List<Position> corners = new ArrayList<>();
		corners.add(new Position(10, 10));
		corners.add(new Position(11, 10));
		corners.add(new Position(11, 11));
		corners.add(new Position(13, 11));
		corners.add(new Position(13, 10));
		corners.add(new Position(14, 10));
		corners.add(new Position(14, 14));
		corners.add(new Position(10, 14));
		new QueuingArea(corners, new Position(10, 10), new Position(14, 16));

	}

}
