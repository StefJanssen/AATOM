package model.agent.humanAgent.tacticalLevel.navigation;

import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.position.Position;
import model.map.Map;
import model.map.shapes.PolygonMapComponent;

/**
 * The discretized version of the map used for navigation. It follows the
 * singleton design pattern.
 * 
 * @author S.A.M. Janssen
 */
public final class DiscretizedMap {

	/**
	 * Singleton object.
	 */
	private static DiscretizedMap instance;

	/**
	 * Gets the singleton instance of the map.
	 * 
	 * @param map
	 *            The map.
	 * @param precision
	 *            The precision.
	 * @return The discretized map object.
	 */
	public static DiscretizedMap getInstance(Map map, double precision) {
		if (instance == null)
			instance = new DiscretizedMap(map, precision);
		return instance;
	}

	/**
	 * A discrete version of the {@link Map}.
	 */
	private boolean[][] discretizedMap;

	/**
	 * This constructor can be used if you have a boolean representation of the
	 * map already. This saves computational time for larger maps. To generate a
	 * boolean representation, use the {@link DiscretizedMap#printMap()}
	 * function after generating the map using the standard
	 * {@link DiscretizedMap#getInstance(Map, double)} method.
	 * 
	 * @param discretizedMap
	 *            The boolean representation of the map.
	 */
	public DiscretizedMap(boolean[][] discretizedMap) {
		this.discretizedMap = discretizedMap;
		instance = this;
	}

	/**
	 * Private method to prevent object creation.
	 * 
	 * @param precision
	 *            The precision.
	 * @param map
	 *            The map.
	 */
	private DiscretizedMap(Map map, double precision) {
		discretizedMap = discretizeMap(map, precision);
	}

	/**
	 * Gets a discrete version of the {@link Map} in a specified precision.
	 * 
	 * @param map
	 *            The map.
	 * 
	 * @param precision
	 *            The precision.
	 * @return The discrete map.
	 */
	private boolean[][] discretizeMap(Map map, double precision) {
		if (discretizedMap != null)
			return discretizedMap;

		int xSize = (int) (map.getWidth() / precision);
		int ySize = (int) (map.getHeight() / precision);
		boolean[][] discreteMap = new boolean[ySize][xSize];

		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				for (PolygonMapComponent shape : map.getMapComponents(PhysicalObject.class)) {
					if (getShape(new Position(i * precision, j * precision), precision).isCollision(shape)) {
						discreteMap[j][i] = true;
					}
				}
			}
		}
		return discreteMap;
	}

	/**
	 * Gets the map.
	 * 
	 * @return The map.
	 */
	public boolean[][] getMap() {
		return discretizedMap;
	}

	/**
	 * Gets the shape of a tile based on the position and radius.
	 * 
	 * @param position
	 *            The position.
	 * @param radius
	 *            The radius.
	 * @return The shape.
	 */
	private PolygonMapComponent getShape(final Position position, final double radius) {
		return new PolygonMapComponent(position.x, position.y, radius, radius) {
		};
	}

	/**
	 * Prints the discrete {@link Map}.
	 */
	public void printMap() {
		System.out.println("private static boolean[][] getMap() {");
		System.out.println("boolean[][] map = new boolean[][] {");
		for (int i = 0; i < discretizedMap.length; i++) {
			System.out.print("{");
			for (int j = 0; j < discretizedMap[0].length; j++) {
				System.out.print(discretizedMap[i][j]);
				if (j != discretizedMap[0].length - 1)
					System.out.print(",");
			}
			System.out.print("}");
			if (i != discretizedMap.length - 1)
				System.out.println(",");
		}
		System.out.println("};");
		System.out.println("return map;");
		System.out.println("}");
	}
}
