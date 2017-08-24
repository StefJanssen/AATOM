package view.mapComponents;

import java.awt.Color;

import model.environment.objects.physicalObject.Chair;

/**
 * Visualizes a {@link Chair}.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class ChairView extends MapComponentView {
	/**
	 * The chair.
	 */
	private Chair chair;

	/**
	 * Creates the chair view.
	 * 
	 * @param chair
	 *            The chair.
	 */
	public ChairView(Chair chair) {
		this.chair = chair;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawRectangle(Color.GREEN, chair.getPosition(), chair.getWidth(), chair.getWidth());
	}

}
