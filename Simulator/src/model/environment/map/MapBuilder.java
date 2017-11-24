package model.environment.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.agent.humanAgent.OperatorAgent;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicETDCheckActivity;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicLuggageCheckActivity;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicLuggageDropActivity;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicOperatorCheckInActivity;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicPhysicalCheckActivity;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicTravelDocumentCheckActivity;
import model.agent.humanAgent.tacticalLevel.activity.operator.impl.BasicXRayActivity;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.physicalObject.Chair;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.QueueSeparator;
import model.environment.objects.physicalObject.Wall;
import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.util.SimulationObject;
import simulation.simulation.util.Utilities;
import util.math.distributions.NormalDistribution;

/**
 * A class that enables the practical building of a map. It includes functions
 * to create queues, checkpoints, and so on.
 * 
 * @author S.A.M. Janssen
 *
 */
public final class MapBuilder {

	/**
	 * The map.
	 */
	private Map map;

	/**
	 * Creates a map builder.
	 * 
	 * @param map
	 *            The map.
	 */
	public MapBuilder(Map map) {
		this.map = map;
	}

	/**
	 * Creates a map builder.
	 * 
	 * @param simulator
	 *            The simulator.
	 */
	public MapBuilder(Simulator simulator) {
		this(simulator.getMap());
	}

	/**
	 * Create a check in area.
	 * 
	 * @param start
	 *            The starting position.
	 * @param numberOfDesks
	 *            The number of desks.
	 * @param degreeRotation
	 *            The degrees it is rotated.
	 * @return A list of SimulationObjects that form the check in area.
	 */
	public List<SimulationObject> checkInArea(Position start, int numberOfDesks, double degreeRotation) {
		List<SimulationObject> components = new ArrayList<>();

		// desks
		for (int i = 0; i < numberOfDesks; i++) {

			Position agentPosition = Utilities.transform(new Position(start.x + i * 2 + 0.5, start.y - 0.3), start,
					degreeRotation);
			Position deskPosition = Utilities.transform(new Position(start.x + i * 2 + 0.5, start.y + 0.35), start,
					degreeRotation);
			Desk desk = createDesk(new Position(start.x + i * 2, start.y), 1, 0.2, start, degreeRotation, deskPosition);
			components.add(desk);
			OperatorAgent agent = new OperatorAgent(map, agentPosition, 0.25, 80,
					new BasicOperatorCheckInActivity(desk, new NormalDistribution(60, 6)));
			components.add(agent);
		}

		// queue
		double xOffsetQueue = 0;
		double yOffsetQueue = 2;

		List<SimulationObject> queueComponents = queue(new Position(start.x + xOffsetQueue, start.y + yOffsetQueue), 4,
				numberOfDesks * 2, false, start, degreeRotation);
		components.addAll(queueComponents);

		// other

		return components;
	}

	/**
	 * Creates a checkpoint.
	 * 
	 * @param start
	 *            The starting position.
	 * @param numberOfLanes
	 *            The number of lanes.
	 * @param queueWidth
	 *            The width of the queue.
	 * @param blockingWall
	 *            Add a blocking wall or not.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return A list of SimulationObjects that form the checkpoint.
	 */
	public List<SimulationObject> checkpoint(Position start, int numberOfLanes, double queueWidth, boolean blockingWall,
			double degreeRotation) {

		List<SimulationObject> components = new ArrayList<>();

		double yOffsetXray = 0;
		double systemWidth = 0.75;
		double systemHeight = 6.0;
		double wtmdWidth = 1;
		double wtmdHeight = 0.4;

		double distanceBetweenLanes = 3.5;

		for (int i = 0; i < numberOfLanes; i++) {
			// x ray
			double xOffsetXray = distanceBetweenLanes * i;
			if (i >= 2)
				xOffsetXray -= 1.5;

			List<Position> cornerPoints = getCornerPoints(new Position(start.x + xOffsetXray, start.y + yOffsetXray),
					systemWidth, systemHeight, start, degreeRotation);

			List<Position> machineCornerPoints = getCornerPoints(
					new Position(start.x + xOffsetXray, start.y + yOffsetXray + systemHeight / 2), systemWidth,
					systemWidth, start, degreeRotation);
			Position baggageStart = Utilities.transform(
					new Position(start.x + systemWidth / 2 + xOffsetXray, start.y + yOffsetXray + systemHeight - 0.5),
					start, degreeRotation);
			Position baggageEnd = Utilities.transform(
					new Position(start.x + systemWidth / 2 + xOffsetXray, start.y + yOffsetXray + 0.5), start,
					degreeRotation);

			boolean otherWayAround = i % 2 == 0;
			XRaySystem xray = new XRaySystem(cornerPoints, machineCornerPoints, baggageStart, baggageEnd, map,
					otherWayAround);
			components.add(xray);

			// luggage check
			Position agentPosition2 = Utilities.transform(
					new Position(start.x + 1.05 + xOffsetXray, start.y + yOffsetXray + 0.5), start, degreeRotation);
			if (otherWayAround)
				agentPosition2 = Utilities.transform(
						new Position(start.x - 0.3 + xOffsetXray, start.y + yOffsetXray + 0.5), start, degreeRotation);

			OperatorAgent luggageCheck = new OperatorAgent(map, agentPosition2, 0.25, 80,
					new BasicLuggageCheckActivity());
			components.add(luggageCheck);

			// luggage drop
			Position agentPosition3 = Utilities.transform(
					new Position(start.x + 1.05 + xOffsetXray, start.y + yOffsetXray + systemHeight - 0.5), start,
					degreeRotation);
			if (otherWayAround)
				agentPosition3 = Utilities.transform(
						new Position(start.x - 0.3 + xOffsetXray, start.y + yOffsetXray + systemHeight - 0.5), start,
						degreeRotation);

			OperatorAgent luggageDrop = new OperatorAgent(map, agentPosition3, 0.25, 80,
					new BasicLuggageDropActivity());
			components.add(luggageDrop);

			// x-ray
			Position agentPosition = Utilities.transform(
					new Position(start.x + 1.05 + xOffsetXray, start.y + yOffsetXray + systemHeight / 2 + 0.5), start,
					degreeRotation);

			if (otherWayAround)
				agentPosition = Utilities.transform(
						new Position(start.x - 0.3 + xOffsetXray, start.y + yOffsetXray + systemHeight / 2 + 0.5),
						start, degreeRotation);
			components.add(new OperatorAgent(map, agentPosition, 0.25, 80, new BasicXRayActivity(xray, luggageCheck)));

			if (otherWayAround) {
				// WTMD
				double xOffsetWTMD = xOffsetXray + systemWidth + (distanceBetweenLanes - systemWidth) / 2
						- wtmdWidth / 2;
				double yOffsetWTMD = yOffsetXray + systemHeight / 2;

				Position checkPosition = Utilities.transform(
						new Position(start.x + xOffsetWTMD, start.y + yOffsetWTMD - 1.25), start, degreeRotation);

				WalkThroughMetalDetector wtmd = new WalkThroughMetalDetector(
						getCornerPoints(new Position(start.x + xOffsetWTMD, start.y + yOffsetWTMD), wtmdWidth,
								wtmdHeight, start, degreeRotation),
						checkPosition, map);
				components.add(wtmd);

				QueueSeparator queue = createQueueSeparator(
						new Position(start.x + xOffsetXray + systemWidth, start.y + yOffsetWTMD),
						xOffsetWTMD - xOffsetXray - systemWidth, 0.1, start, degreeRotation);
				components.add(queue);
				queue = createQueueSeparator(new Position(start.x + xOffsetWTMD + wtmdWidth, start.y + yOffsetWTMD),
						xOffsetWTMD - xOffsetXray - systemWidth, 0.1, start, degreeRotation);
				components.add(queue);

				Position directionsPosition = Utilities.transform(
						new Position(start.x + xOffsetWTMD + wtmdWidth / 4, start.y + yOffsetWTMD - 2), start,
						degreeRotation);
				OperatorAgent physicalCheck = new OperatorAgent(map, directionsPosition, 0.25, 80,
						new BasicPhysicalCheckActivity(wtmd));
				components.add(physicalCheck);

				Position directionsPosition2 = Utilities.transform(
						new Position(start.x + xOffsetWTMD + 3 * wtmdWidth / 4, start.y + yOffsetWTMD - 2), start,
						degreeRotation);
				OperatorAgent etd = new OperatorAgent(map, directionsPosition2, 0.25, 80,
						new BasicETDCheckActivity(wtmd));
				components.add(etd);
			}
		}

		// Queue
		double yOffsetQueue = yOffsetXray + systemHeight + 2;
		double xOffsetQueue = 0.1;

		if (numberOfLanes > 0 && queueWidth > 0) {
			components
					.add(new OperatorAgent(map,
							Utilities.transform(
									new Position(start.x + xOffsetQueue - 0.35, start.y + yOffsetQueue - 0.35), start,
									degreeRotation),
							0.25, 80, new BasicTravelDocumentCheckActivity()));
		}

		int numberOfQueueLanes = 5;
		if (queueWidth > 0) {
			List<SimulationObject> queue = queue(new Position(start.x + xOffsetQueue, start.y + yOffsetQueue),
					numberOfQueueLanes, queueWidth, blockingWall, start, degreeRotation);
			components.addAll(queue);
		}
		return components;
	}

	/**
	 * Creates a {@link Desk} with a given width and height and rotation angle.
	 * The rotation is around the given origin position.
	 * 
	 * @param start
	 *            The starting position.
	 * @param width
	 *            The width (in meter).
	 * @param height
	 *            The height (in meter).
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @param servingPosition
	 *            The serving position.
	 * @return The desk.
	 */
	public Desk createDesk(Position start, double width, double height, Position origin, double degreeRotation,
			Position servingPosition) {
		return new Desk(getCornerPoints(start, width, height, origin, degreeRotation), servingPosition);

	}

	/**
	 * Creates a {@link QueueSeparator}.
	 * 
	 * @param start
	 *            The starting position.
	 * @param width
	 *            The width (in meter).
	 * @param height
	 *            The height (in meter).
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return The wall.
	 */
	public QueueSeparator createQueueSeparator(Position start, double width, double height, double degreeRotation) {
		return new QueueSeparator(getCornerPoints(start, width, height, degreeRotation));
	}

	/**
	 * Creates a {@link QueueSeparator} with a specified width and height. It is
	 * rotated around the origin position with a specified angle.
	 * 
	 * @param start
	 *            The starting position.
	 * @param width
	 *            The width (in meter).
	 * @param height
	 *            The height (in meter).
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return The wall.
	 */
	public QueueSeparator createQueueSeparator(Position start, double width, double height, Position origin,
			double degreeRotation) {
		return new QueueSeparator(getCornerPoints(start, width, height, origin, degreeRotation));
	}

	/**
	 * Creates a rotated {@link QueueSeparator} from its original corner points
	 * and a given degree rotation. The rotation is around the first corner
	 * point.
	 * 
	 * @param cornerPoints
	 *            The corner points.
	 * @param degreeRotation
	 *            The degrees it is rotated (deg.).
	 * @return The queue separator.
	 */
	public QueueSeparator createQueuSeparator(List<Position> cornerPoints, double degreeRotation) {
		return new QueueSeparator(getCornerPoints(cornerPoints, degreeRotation));
	}

	/**
	 * Creates a rotated {@link QueueSeparator} from its original corner points
	 * and a given degree rotation. The rotation is around the given origin
	 * point.
	 * 
	 * @param cornerPoints
	 *            The corner points.
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (deg.).
	 * @return The queue separator.
	 */
	public QueueSeparator createQueuSeparator(List<Position> cornerPoints, Position origin, double degreeRotation) {
		return new QueueSeparator(getCornerPoints(cornerPoints, origin, degreeRotation));
	}

	/**
	 * Creates a rotated {@link Wall} from its original corner points and a
	 * given degree rotation. The rotation is around the first corner point.
	 * 
	 * @param cornerPoints
	 *            The corner points.
	 * @param degreeRotation
	 *            The degrees it is rotated (deg.).
	 * @return The wall.
	 */
	public Wall createWall(List<Position> cornerPoints, double degreeRotation) {
		return new Wall(getCornerPoints(cornerPoints, degreeRotation));
	}

	/**
	 * Creates a rotated {@link Wall} from its original corner points and a
	 * given degree rotation. The rotation is around the given origin point.
	 * 
	 * @param cornerPoints
	 *            The corner points.
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (deg.).
	 * @return The wall.
	 */
	public Wall createWall(List<Position> cornerPoints, Position origin, double degreeRotation) {
		return new Wall(getCornerPoints(cornerPoints, origin, degreeRotation));
	}

	/**
	 * Creates a {@link Wall} with a given width and height and rotation angle.
	 * The rotation is around the given starting position.
	 * 
	 * @param start
	 *            The starting position.
	 * @param width
	 *            The width (in meter).
	 * @param height
	 *            The height (in meter).
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return The wall.
	 */
	public Wall createWall(Position start, double width, double height, double degreeRotation) {
		return new Wall(getCornerPoints(start, width, height, degreeRotation));
	}

	/**
	 * Creates a {@link Wall} with a given width and height and rotation angle.
	 * The rotation is around the given origin position.
	 * 
	 * @param start
	 *            The starting position.
	 * @param width
	 *            The width (in meter).
	 * @param height
	 *            The height (in meter).
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return The wall.
	 */
	public Wall createWall(Position start, double width, double height, Position origin, double degreeRotation) {
		return new Wall(getCornerPoints(start, width, height, origin, degreeRotation));
	}

	/**
	 * Create a gate.
	 * 
	 * @param start
	 *            The starting position.
	 * @param numberOfRows
	 *            The number of rows for seats.
	 * @param numberOfChairsPerRow
	 *            The number of chairs per row.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return A list of SimulationObjects that form the gate area.
	 */
	public List<SimulationObject> gate(Position start, int numberOfRows, int numberOfChairsPerRow,
			double degreeRotation) {
		List<SimulationObject> components = new ArrayList<>();

		List<SimulationObject> sittingArea = sittingArea(new Position(start.x, start.y), numberOfRows,
				numberOfChairsPerRow, degreeRotation);
		components.addAll(sittingArea);

		return components;
	}

	/**
	 * Gets a list of rotated corner points, with a given rotation angle. The
	 * rotation is around the first point of the list.
	 * 
	 * @param cornerPoints
	 *            The corner points.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return The rotated corner points.
	 */
	public List<Position> getCornerPoints(List<Position> cornerPoints, double degreeRotation) {
		return getCornerPoints(cornerPoints, cornerPoints.get(0), degreeRotation);
	}

	/**
	 * Gets a list of rotated corner points, with a given rotation angle. The
	 * rotation is around the given origin point.
	 * 
	 * @param cornerPoints
	 *            The corner points.
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return The rotated corner points.
	 */
	public List<Position> getCornerPoints(List<Position> cornerPoints, Position origin, double degreeRotation) {
		List<Position> newCornerPoints = new ArrayList<>();
		for (Position p : cornerPoints) {
			newCornerPoints.add(Utilities.transform(p, origin, degreeRotation));
		}
		return newCornerPoints;
	}

	/**
	 * Gets the corner points from a given starting point, width, height and a
	 * given rotation angle. The rotation is around the given starting position.
	 * 
	 * @param position
	 *            The starting position.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param degreeRotation
	 *            The degrees it is rotated (deg.).
	 * @return The rotated positions.
	 */
	public List<Position> getCornerPoints(Position position, double width, double height, double degreeRotation) {
		return getCornerPoints(position, width, height, position, degreeRotation);
	}

	/**
	 * Gets the corner points from a given starting point, width, height and a
	 * given rotation angle. The rotation is around the given origin position.
	 * 
	 * @param position
	 *            The starting position.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param origin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (deg.).
	 * @return The rotated positions.
	 */
	public List<Position> getCornerPoints(Position position, double width, double height, Position origin,
			double degreeRotation) {
		List<Position> positions = new ArrayList<>();
		positions.add(position);
		positions.add(new Position(position.x, position.y + height));
		positions.add(new Position(position.x + width, position.y + height));
		positions.add(new Position(position.x + width, position.y));
		return getCornerPoints(positions, origin, degreeRotation);
	}

	/**
	 * Creates a queue with a specified number of lanes and width. A blocking
	 * wall (a large wall to prevent agents from moving past the queue) can be
	 * added as well. The rotation degree indicates the angle the queue is
	 * rotated around its started position.
	 * 
	 * @param start
	 *            The starting position.
	 * @param numberOfLanes
	 *            The number of lanes.
	 * @param queueWidth
	 *            The width of the queue.
	 * @param addBlockingWall
	 *            Flag to indicate if we want to add a blocking wall.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return A list of SimulationObjects that form the queue.
	 */
	public List<SimulationObject> queue(Position start, int numberOfLanes, double queueWidth, boolean addBlockingWall,
			double degreeRotation) {
		return queue(start, numberOfLanes, queueWidth, addBlockingWall, start, degreeRotation);
	}

	/**
	 * Creates a queue with a specified number of lanes and width. A blocking
	 * wall (a large wall to prevent agents from moving past the queue) can be
	 * added as well. The rotation degree indicates the angle the queue is
	 * rotated around the origin position.
	 * 
	 * @param start
	 *            The starting position.
	 * @param numberOfLanes
	 *            The number of lanes.
	 * @param queueWidth
	 *            The width of the queue.
	 * @param addBlockingWall
	 *            Flag to indicate if we want to add a blocking wall.
	 * @param rotationOrigin
	 *            The rotation origin.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return A list of SimulationObjects that form the queue.
	 */
	public List<SimulationObject> queue(Position start, int numberOfLanes, double queueWidth, boolean addBlockingWall,
			Position rotationOrigin, double degreeRotation) {
		// Queue
		double xOffsetQueue = start.x;
		double yOffsetQueue = start.y;
		double walkWidth = 1;
		double queueHeight = 0.1;

		List<SimulationObject> components = new ArrayList<>();

		for (int i = 0; i <= numberOfLanes; i++) {
			QueueSeparator queueSeparationUnit = null;
			if (i % 2 == 0)
				queueSeparationUnit = createQueueSeparator(
						new Position(xOffsetQueue + 1, yOffsetQueue + (i * walkWidth)), queueWidth - 1, queueHeight,
						rotationOrigin, degreeRotation);
			else
				queueSeparationUnit = createQueueSeparator(new Position(xOffsetQueue, yOffsetQueue + (i * walkWidth)),
						queueWidth - 1, queueHeight, rotationOrigin, degreeRotation);
			components.add(queueSeparationUnit);
		}

		// left queue separation unit
		QueueSeparator queueSeparationUnit = createQueueSeparator(new Position(xOffsetQueue - 0.1, yOffsetQueue), 0.1,
				numberOfLanes * walkWidth + queueHeight, rotationOrigin, degreeRotation);
		components.add(queueSeparationUnit);

		// right queue separation unit
		queueSeparationUnit = createQueueSeparator(new Position(xOffsetQueue + queueWidth, yOffsetQueue), 0.1,
				numberOfLanes * walkWidth + queueHeight, rotationOrigin, degreeRotation);
		components.add(queueSeparationUnit);

		if (addBlockingWall) {
			// huge wall
			Wall blockingWall = createWall(new Position(0, yOffsetQueue), xOffsetQueue, queueHeight, rotationOrigin,
					degreeRotation);
			components.add(blockingWall);
			blockingWall = createWall(new Position(xOffsetQueue + queueWidth, yOffsetQueue), 35, queueHeight,
					rotationOrigin, degreeRotation);
			components.add(blockingWall);
		}

		// auto create queuing areas
		List<Position> corners = new ArrayList<>();
		corners.add(Utilities.transform(start, rotationOrigin, degreeRotation));
		corners.add(Utilities.transform(new Position(start.x + queueWidth, start.y), rotationOrigin, degreeRotation));
		corners.add(Utilities.transform(
				new Position(start.x + queueWidth,
						start.y + numberOfLanes * (walkWidth + queueHeight / 2) - queueHeight),
				rotationOrigin, degreeRotation));
		corners.add(Utilities.transform(
				new Position(start.x, start.y + numberOfLanes * (walkWidth + queueHeight / 2) - queueHeight),
				rotationOrigin, degreeRotation));

		Position entrance = Utilities.transform(new Position(start.x + 0.5, start.y), rotationOrigin, degreeRotation);
		Position leaving = Position.NO_POSITION;
		if (numberOfLanes % 2 != 0) {
			leaving = Utilities.transform(
					new Position(start.x + queueWidth - 0.5,
							start.y + numberOfLanes * (walkWidth + queueHeight / 2) - queueHeight),
					rotationOrigin, degreeRotation);
		} else {
			leaving = Utilities.transform(
					new Position(start.x + 0.5, start.y + numberOfLanes * (walkWidth + queueHeight / 2) - queueHeight),
					rotationOrigin, degreeRotation);
		}
		components.add(new QueuingArea(corners, leaving, entrance));

		return components;
	}

	/**
	 * Create a sitting area.
	 * 
	 * @param start
	 *            The starting position.
	 * @param numberOfRows
	 *            The number of rows of chairs.
	 * @param numberOfChairsPerRow
	 *            The number of chairs per row.
	 * @param degreeRotation
	 *            The degrees it is rotated (in deg.).
	 * @return A list of SimulationObjects that form the sitting area.
	 */
	public List<SimulationObject> sittingArea(Position start, int numberOfRows, int numberOfChairsPerRow,
			double degreeRotation) {
		List<SimulationObject> components = new ArrayList<>();
		double chairWidth = 0.6;
		double chairSpacing = 0.1;
		for (int i = 0; i < numberOfChairsPerRow; i++) {
			for (int j = 0; j < numberOfRows; j++) {
				Position chairPos = new Position(start.x + i * (chairWidth + chairSpacing),
						start.y + j * 3 * chairWidth);
				Position chairEntry = new Position(start.x + 0.5 * chairWidth + i * (chairWidth + chairSpacing),
						start.y - 0.5 + j * 3 * chairWidth);
				Position transformedEntry = Utilities.transform(chairEntry, start, degreeRotation);

				Position transformedPos = Utilities.transform(chairPos, start, degreeRotation);
				Chair c = new Chair(transformedPos, transformedEntry, chairWidth);
				components.add(c);
			}
		}
		Collections.shuffle(components, Utilities.RANDOM_GENERATOR);
		return components;
	}
}