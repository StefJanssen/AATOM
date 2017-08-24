package simulation.simulation.util;

/**
 * An updatable element in the model can update itself.
 * 
 * @author S.A.M. Janssen
 */
public interface Updatable {

	/**
	 * Updates the updatable element.
	 * 
	 * @param timeStep
	 *            The time step of the simulation (in milliseconds).
	 */
	public void update(int timeStep);
}
