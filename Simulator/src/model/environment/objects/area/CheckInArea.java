package model.environment.objects.area;

import java.util.List;

import model.environment.position.Position;

/**
 * 
 * @author S.A.M. Janssen
 */
public class CheckInArea extends Area {

	/**
	 * Creates the area with a set of specified corner points.
	 * 
	 * @param corners
	 *            The corner points.
	 */
	public CheckInArea(List<Position> corners) {
		super(corners);
	}
}