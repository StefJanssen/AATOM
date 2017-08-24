package view.mapComponents;

import java.awt.Color;

import model.environment.objects.physicalObject.PhysicalObject;

/**
 * Visualizes a {@link PhysicalObject}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class PhysicalObjectView extends MapComponentView {

	/**
	 * The {@link PhysicalObject}.
	 */
	private PhysicalObject physicalObstacle;

	/**
	 * Creates the PhysicalObstacleView.
	 * 
	 * @param physicalObstacle
	 *            The physicalObstacle.
	 */
	public PhysicalObjectView(PhysicalObject physicalObstacle) {
		this.physicalObstacle = physicalObstacle;
	}

	/**
	 * Gets the physical obstacle.
	 * 
	 * @return The physical obstacle.
	 */
	public PhysicalObject getPhysicalObstacle() {
		return physicalObstacle;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawPolygon(Color.BLACK, physicalObstacle.getCorners());
	}
}
