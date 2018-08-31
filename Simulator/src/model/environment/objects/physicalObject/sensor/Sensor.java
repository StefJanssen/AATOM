package model.environment.objects.physicalObject.sensor;

import java.util.List;

import model.environment.position.Position;
import model.map.shapes.PolygonMapComponent;

/**
 * A sensor can sense something in the world.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Sensor extends PolygonMapComponent {

	/**
	 * The sensor state.
	 */
	protected SensorState state;

	/**
	 * Creates a sensor.
	 * 
	 * @param corners
	 *            The positions.
	 */
	public Sensor(List<Position> corners) {
		super(corners);
		this.state = SensorState.IDLE;
	}

	/**
	 * Gets an {@link Observation} from the sensor.
	 * 
	 * @return The observation.
	 */
	public abstract Observation<?> getObservation();
}
