package model.agent.humanAgent.operationalLevel.observation;

import java.util.Collection;

import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.map.Map;

/**
 * An observation model is used to model observations made by a human agent.
 * 
 * @author S.A.M. Janssen
 */
public abstract class ObservationModule {

	/**
	 * The movement model.
	 */
	protected MovementModel movementModel;
	/**
	 * The map.
	 */
	protected Map map;

	/**
	 * Gets an observation of a specific type. An observation always returns a
	 * collection of map components.
	 * 
	 * @param type
	 *            The map component.
	 * @param <T>
	 *            The type of observation.
	 * @return The collection that is observed.
	 */
	public abstract <T> Collection<T> getObservation(Class<T> type);

	/**
	 * Initializes the {@link MovementModel}.
	 * 
	 * @param map
	 *            The map.
	 * @param movementModel
	 *            The movement model.
	 */
	public void init(Map map, MovementModel movementModel) {
		this.map = map;
		this.movementModel = movementModel;
	}

}