package view.mapComponents;

import java.awt.Color;

import model.environment.objects.physicalObject.QueueSeparator;

/**
 * Visualizes a {@link QueueSeparator}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class QueueSeparatorView extends MapComponentView {

	/**
	 * The {@link QueueSeparator}.
	 */
	private QueueSeparator queueSeparator;

	/**
	 * Creates the QueueSeparator.
	 * 
	 * @param queueSeparator
	 *            The queueSeparator.
	 */
	public QueueSeparatorView(QueueSeparator queueSeparator) {
		this.queueSeparator = queueSeparator;
	}

	/**
	 * Gets the physical obstacle.
	 * 
	 * @return The physical obstacle.
	 */
	public QueueSeparator getPhysicalObstacle() {
		return queueSeparator;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawPolygon(Color.BLUE, queueSeparator.getCorners());
	}
}
