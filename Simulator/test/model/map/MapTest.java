package model.map;

import org.junit.Assert;
import org.junit.Test;

import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.QueueSeparator;
import model.environment.objects.physicalObject.Wall;

/**
 * Tests the map main functionalities.
 * 
 * @author S.A.M. Janssen
 */
public class MapTest {

	/**
	 * Tests the adding and removing of items on the map.
	 */
	@Test
	public void testAddAndRemove() {
		Map m = new Map();
		Wall w = new Wall(10, 10, 0.1, 0.1);
		m.add(w);
		Assert.assertTrue(m.getMapComponents(Wall.class).size() == 1);
		Assert.assertTrue(m.getMapComponents(PhysicalObject.class).size() == 1);
		Assert.assertTrue(m.getMapComponents(MapComponent.class).size() == 1);

		QueueSeparator q = new QueueSeparator(1, 1, 0.1, 0.1);
		m.add(q);
		Assert.assertTrue(m.getMapComponents(Wall.class).size() == 1);
		Assert.assertTrue(m.getMapComponents(QueueSeparator.class).size() == 1);
		Assert.assertTrue(m.getMapComponents(PhysicalObject.class).size() == 2);
		Assert.assertTrue(m.getMapComponents(MapComponent.class).size() == 2);

		m.remove(w);
		Assert.assertTrue(w.isDestroyed());
		Assert.assertTrue(m.getMapComponents(Wall.class).size() == 0);
		Assert.assertTrue(m.getMapComponents(PhysicalObject.class).size() == 1);
		Assert.assertTrue(m.getMapComponents(MapComponent.class).size() == 1);
	}

	/**
	 * Tests the constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		// map size
		Map m = new Map();
		Assert.assertEquals(m.getHeight(), 1, 0.001);
		Assert.assertEquals(m.getWidth(), 1, 0.001);

		// illegal argument
		new Map(-1, -1);
	}

	/**
	 * Tests the automatic resizing of the map.
	 */
	@Test
	public void testMapResizing() {
		Map m = new Map();
		Wall w = new Wall(10, 10, 0.1, 0.1);
		m.add(w);
		Assert.assertTrue(m.getHeight() >= 10.1);
		Assert.assertTrue(m.getWidth() >= 10.1);

		m.remove(w);
		Assert.assertEquals(m.getHeight(), 1, 0.001);
		Assert.assertEquals(m.getWidth(), 1, 0.001);

	}

}
