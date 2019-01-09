package model.map.shapes;

import model.environment.position.Position;
import model.environment.position.Vector;
import model.map.MapComponent;
import model.map.PhysicalMapComponent;
import simulation.simulation.util.Utilities;

/**
 * A circular {@link MapComponent}.
 * 
 * @author S.A.M. Janssen
 */
public abstract class CircularMapComponent extends PhysicalMapComponent implements CircularShape {

	/**
	 * The radius (meter) of the agent.
	 */
	float radius;

	/**
	 * Creates a circular {@link MapComponent}.
	 * 
	 * @param position
	 *            The position of the center.
	 * @param radius
	 *            The radius.
	 */
	public CircularMapComponent(Position position, double radius) {
		super(position);
		if (radius < 0)
			throw new IllegalArgumentException("Radius cannot be negative.");
		this.radius = (float) radius;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	@Override
	public Vector getVectorToPosition(Position position) {
		return new Vector(position.x - getPosition().x, position.y - getPosition().y);
	}

	@Override
	public boolean isLineCollision(Position start, Position end) {
		return Utilities.getPositionToLineVector(position, start, end).length() < radius;
	}

}
