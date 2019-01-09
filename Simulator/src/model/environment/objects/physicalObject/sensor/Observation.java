package model.environment.objects.physicalObject.sensor;

/**
 * Creates an observation.
 * 
 * @author S.A.M. Janssen
 * @param <T>
 *            The observation type.
 */
public class Observation<T> {

	/**
	 * No observation.
	 */
	public static Observation<Integer> NO_OBSERVATION = new Observation<Integer>(-1);

	/**
	 * The observation.
	 */
	private T observation;

	/**
	 * Creates an observation.
	 * 
	 * @param observation
	 *            The observation.
	 */
	public Observation(T observation) {
		this.observation = observation;
	}

	/**
	 * Gets the observation.
	 * 
	 * @return The observation.
	 */
	public T getObservation() {
		return observation;
	}
}
