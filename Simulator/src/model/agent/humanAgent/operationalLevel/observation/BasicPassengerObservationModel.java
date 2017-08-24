package model.agent.humanAgent.operationalLevel.observation;

import java.util.Collection;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.agent.humanAgent.operationalLevel.action.movement.MovementModel;
import model.environment.map.Map;
import model.environment.objects.area.Area;
import model.environment.objects.area.Restaurant;
import model.environment.objects.area.Shop;
import model.environment.objects.area.Toilet;
import model.environment.objects.physicalObject.Chair;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.PhysicalObject;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import simulation.simulation.util.Utilities;

/**
 * A basic observation model for a passenger.
 * 
 * @author S.A.M. Janssen
 */
public class BasicPassengerObservationModel extends ObservationModule {

	/**
	 * Creates an observation model.
	 * 
	 * @param map
	 *            The map.
	 */
	public BasicPassengerObservationModel(Map map) {
		super(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getObservation(Class<T> type) {
		if (type.equals(HumanAgent.class)) {
			// TODO PARAMETERS
			Collection<HumanAgent> agentsInNeighborhood = Utilities.getMapComponentsInNeighborhood(
					movementModel.getPosition(), 1, map.getMapComponents(HumanAgent.class));
			return (Collection<T>) agentsInNeighborhood;
		} else if (type.equals(PhysicalObject.class)) {
			Collection<PhysicalObject> physicalObjects = Utilities.getMapComponentsInNeighborhood(
					movementModel.getPosition(), 1, map.getMapComponents(PhysicalObject.class));
			return (Collection<T>) physicalObjects;
		} else if (type.equals(Passenger.class)) {
			Collection<Passenger> passenger = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 4,
					map.getMapComponents(Passenger.class));
			return (Collection<T>) passenger;
		} else if (type.equals(Desk.class)) {
			Collection<Desk> desks = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 15,
					map.getMapComponents(Desk.class));
			return (Collection<T>) desks;
		} else if (type.equals(Area.class)) {
			Collection<Area> areas = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 0.1,
					map.getMapComponents(Area.class));
			return (Collection<T>) areas;
		} else if (type.equals(XRaySystem.class)) {
			Collection<XRaySystem> xray = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 10,
					map.getMapComponents(XRaySystem.class));
			return (Collection<T>) xray;
		} else if (type.equals(WalkThroughMetalDetector.class)) {
			Collection<WalkThroughMetalDetector> wtmds = Utilities.getMapComponentsInNeighborhood(
					movementModel.getPosition(), 5, map.getMapComponents(WalkThroughMetalDetector.class));
			return (Collection<T>) wtmds;
		} else if (type.equals(Chair.class)) {
			Collection<Chair> chairs = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 20,
					map.getMapComponents(Chair.class));
			return (Collection<T>) chairs;
		} else if (type.equals(Shop.class)) {
			Collection<Shop> chairs = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 30,
					map.getMapComponents(Shop.class));
			return (Collection<T>) chairs;
		} else if (type.equals(Restaurant.class)) {
			Collection<Restaurant> chairs = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 30,
					map.getMapComponents(Restaurant.class));
			return (Collection<T>) chairs;
		} else if (type.equals(Toilet.class)) {
			Collection<Toilet> chairs = Utilities.getMapComponentsInNeighborhood(movementModel.getPosition(), 30,
					map.getMapComponents(Toilet.class));
			return (Collection<T>) chairs;
		}
		return null;
	}

	@Override
	public void init(MovementModel movementModel) {
		this.movementModel = movementModel;
	}
}
