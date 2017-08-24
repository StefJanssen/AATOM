package model.agent.humanAgent.tacticalLevel.navigation;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import model.environment.map.Map;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.position.Position;
import model.environment.shapes.PolygonShape;
import simulation.simulation.util.Utilities;

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
	private int[][] discretizedMap;

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
	private int[][] discretizeMap(Map map, double precision) {
		if (discretizedMap != null)
			return discretizedMap;

		int xSize = (int) (map.getWidth() / precision);
		int ySize = (int) (map.getHeight() / precision);
		int[][] discreteMap = new int[ySize][xSize];

		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				for (PolygonShape shape : map.getMapComponents(PhysicalObject.class)) {
					if (Utilities.isCollision(getShape(new Position(i * precision, j * precision), precision), shape)) {
						discreteMap[j][i] = 1;
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
	public int[][] getMap() {
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
	private PolygonShape getShape(final Position position, final double radius) {
		return new PolygonShape() {

			List<Position> corners;
			Path2D shape;

			@Override
			public List<Position> getCorners() {
				if (corners == null) {
					corners = new ArrayList<>();
					corners.add(position);
					corners.add(new Position(position.x, position.y + radius));
					corners.add(new Position(position.x + radius, position.y + radius));
					corners.add(new Position(position.x + radius, position.y));

				}
				return corners;
			}

			@Override
			public Path2D getShape() {
				if (shape == null) {
					Path2D path = new Path2D.Double();
					path.moveTo(corners.get(0).x, corners.get(0).y);
					for (int i = 1; i < corners.size(); ++i) {
						path.lineTo(corners.get(i).x, corners.get(i).y);
					}
					shape = path;
				}
				return shape;
			}
		};
	}

	/**
	 * Prints the discrete {@link Map}.
	 */
	public void printMap() {
		for (int i = 0; i < discretizedMap.length; i++) {
			for (int j = 0; j < discretizedMap[0].length; j++) {
				System.out.print(discretizedMap[i][j]);
			}
			System.out.println();
		}
	}
}
