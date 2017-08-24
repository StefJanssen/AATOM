package view.mapComponents;

import java.awt.Color;

import model.environment.objects.physicalObject.Desk;

/**
 * Visualizes a {@link Desk}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class DeskView extends MapComponentView {

	/**
	 * The {@link Desk}.
	 */
	private Desk physicalObstacle;

	/**
	 * Creates the PhysicalObstacleView.
	 * 
	 * @param physicalObstacle
	 *            The physicalObstacle.
	 */
	public DeskView(Desk physicalObstacle) {
		this.physicalObstacle = physicalObstacle;
	}

	/**
	 * Gets the physical obstacle.
	 * 
	 * @return The physical obstacle.
	 */
	public Desk getDesk() {
		return physicalObstacle;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawPolygon(Color.YELLOW, physicalObstacle.getCorners());
	}
}
