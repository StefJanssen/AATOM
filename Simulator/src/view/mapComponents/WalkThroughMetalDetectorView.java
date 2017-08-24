package view.mapComponents;

import java.awt.Color;

import model.environment.objects.physicalObject.sensor.WalkThroughMetalDetector;

/**
 * Visualizes a {@link WalkThroughMetalDetector}.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class WalkThroughMetalDetectorView extends MapComponentView {

	/**
	 * The {@link WalkThroughMetalDetector}.
	 */
	private WalkThroughMetalDetector wtmd;

	/**
	 * Creates the view.
	 * 
	 * @param wtmd
	 *            The walk through metal detector.
	 */
	public WalkThroughMetalDetectorView(WalkThroughMetalDetector wtmd) {
		this.wtmd = wtmd;
	}

	@Override
	public String getAboutString() {
		return "";
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawPolygon(Color.GRAY, wtmd.getCorners());
		setBounds(ShapeDrawer.getRectangle(wtmd.getCorners().get(0), 1, 0.4));
	}
}
