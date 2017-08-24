package model.environment.map;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.position.Position;
import model.environment.shapes.CircularShape;
import model.environment.shapes.PolygonShape;
import simulation.simulation.util.SimulationObject;

/**
 * The map forms the basis for our model. It gathers all physical elements of
 * the model and contains useful methods for interaction.
 * 
 * @author S.A.M. Janssen
 *
 */
public class Map {

	/**
	 * The {@link SimulationObject}s on the map. The {@link Multimap} is used as
	 * an internal representation to quickly find collections of
	 * {@link SimulationObject}s of a specified type. This results in some extra
	 * memory usage, but enables fast access times.
	 */
	private Multimap<Class<?>, SimulationObject> mapComponents;
	/**
	 * The height of the map.
	 */
	private double height;
	/**
	 * The width of the map.
	 */
	private double width;

	/**
	 * Creates a map that automatically scales to the items that are added.
	 */
	public Map() {
		this(0, 0);
	}

	/**
	 * Creates the map with a minimum size. The size increases if items are
	 * added that fall outside of the dimensions.
	 * 
	 * @param width
	 *            The width of the map (in meter).
	 * @param height
	 *            The height of the map (in meter).
	 */
	public Map(double width, double height) {
		this.width = width;
		this.height = height;
		mapComponents = ArrayListMultimap.create();
	}

	/**
	 * Adds a map component to the map.
	 * 
	 * @param object
	 *            The map component.
	 */
	public void add(SimulationObject object) {
		// throw error messages in case of problems
		checkForAddIssues(object);

		// add a key for each superclass, up until the MapComponent class.
		Class<?> mapComponentClass = object.getClass();

		while (!mapComponentClass.getSimpleName().equals("Object")) {
			mapComponents.put(mapComponentClass, object);

			// check for interfaces
			for (Class<?> c : mapComponentClass.getInterfaces()) {
				mapComponents.put(c, object);
			}
			mapComponentClass = mapComponentClass.getSuperclass();
		}
		mapComponents.put(mapComponentClass, object);
		updateDimensions();
	}

	/**
	 * Checks if there is an issue by adding a {@link SimulationObject} to the
	 * map. Throw an exception if there is.
	 * 
	 * @param simulationObject
	 *            The simulation object.
	 */
	private void checkForAddIssues(SimulationObject simulationObject) {
		if (simulationObject instanceof PhysicalObject) {
			Collection<Passenger> agents = getMapComponents(Passenger.class);

			if (!agents.isEmpty()) {
				throw new RuntimeException("You added a physical object to the map, after you added a passenger. "
						+ "Please add all physical objects before adding passengers to the map.");
			}
		} else if (simulationObject instanceof QueuingArea) {
			Collection<Flight> flights = getMapComponents(Flight.class);

			if (!flights.isEmpty()) {
				throw new RuntimeException("You added a queueing area to the map, after you added a flight. "
						+ "Please add all queueing area before adding flights to the map.");
			}
		}

	}

	/**
	 * Gets the height of the map.
	 * 
	 * @return The height.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Get all {@link MapComponent}s that are an instance of a specific class.
	 * 
	 * @param <T>
	 *            The class instance.
	 * @param className
	 *            The class name.
	 * @return A {@link Collection} of the map components.
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getMapComponents(Class<T> className) {
		return (Collection<T>) mapComponents.get(className);
	}

	/**
	 * Gets the width of the map.
	 * 
	 * @return The width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Check if a {@link Position} is out of map.
	 * 
	 * @param position
	 *            The position.
	 * @return True if it is out of the map, false otherwise.
	 */
	public boolean isOutOfBounds(Position position) {
		if (position.x > width || position.x < 0 || position.y > height || position.y < 0)
			return true;
		return false;
	}

	/**
	 * Remove a {@link MapComponent} from the map.
	 * 
	 * @param mapComponent
	 *            The map component.
	 */
	public void remove(MapComponent mapComponent) {
		if (mapComponent instanceof HumanAgent)
			mapComponents.values().removeAll(Collections.singleton(mapComponent));
	}

	/**
	 * Updates the dimensions of the map.
	 */
	private void updateDimensions() {
		double tempWidth = 0;
		double tempHeight = 0;
		for (MapComponent mapComponent : getMapComponents(MapComponent.class)) {
			if (mapComponent instanceof CircularShape) {
				double tempX = mapComponent.position.x + ((CircularShape) mapComponent).getRadius();
				double tempY = mapComponent.position.y + ((CircularShape) mapComponent).getRadius();
				if (tempX > tempWidth)
					tempWidth = tempX;
				if (tempY > tempHeight)
					tempHeight = tempY;
			} else if (mapComponent instanceof PolygonShape) {
				List<Position> corners = ((PolygonShape) mapComponent).getCorners();
				double tempX = 0;
				double tempY = 0;
				for (Position corner : corners) {
					if (corner.x > tempX)
						tempX = corner.x;
					if (corner.y > tempY)
						tempY = corner.y;
				}
				if (tempX > tempWidth)
					tempWidth = tempX;
				if (tempY > tempHeight)
					tempHeight = tempY;
			}
		}

		if (tempWidth + 1 > width)
			width = Math.round(tempWidth + 1);
		if (tempHeight + 1 > height)
			height = Math.round(tempHeight + 1);
	}
}