package view.mapComponents;

import java.awt.Color;

import model.environment.objects.physicalObject.sensor.XRaySystem;

/**
 * The XRaySystemView visualizes an {@link XRaySystem}.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class XRaySystemView extends MapComponentView {

	/**
	 * The {@link XRaySystem}.
	 */
	private XRaySystem xray;

	/**
	 * Create the view.
	 * 
	 * @param xray
	 *            The X-ray system.
	 */
	public XRaySystemView(XRaySystem xray) {
		this.xray = xray;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawPolygon(Color.ORANGE, xray.getCorners());
		ShapeDrawer.drawPolygon(Color.DARK_GRAY, xray.getXRaySensor().getCorners());

		for (int i = 0; i < xray.getNumberOfCollectIndices(); i++) {
			ShapeDrawer.drawCircle(Color.BLACK, xray.getCollectPosition(i), 0.1);
			ShapeDrawer.drawCircle(Color.BLACK, xray.getDropOffPosition(i), 0.1);
		}
	}

}
