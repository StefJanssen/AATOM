package model.environment.objects.physicalObject.sensor;

import model.environment.map.Map;
import model.environment.map.MapComponent;
import model.environment.position.Position;

/**
 * A sensor can sense something in the world.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Sensor extends MapComponent {

	/**
	 * The map.
	 */
	protected Map map;
	/**
	 * The sensor state.
	 */
	protected SensorState state;

	/**
	 * Creates a sensor.
	 * 
	 * @param position
	 *            The position.
	 * @param map
	 *            The map.
	 */
	public Sensor(Position position, Map map) {
		super(position);
		this.map = map;
		this.state = SensorState.IDLE;
	}

	/**
	 * Gets an {@link Observation} from the sensor.
	 * 
	 * @return The observation.
	 */
	public abstract Observation<?> getObservation();

	/**
	 * Move the sensor to a {@link Position}.
	 * 
	 * @param position
	 *            The position to move to.
	 */
	public void move(Position position) {
		this.position = position;
	}
}
