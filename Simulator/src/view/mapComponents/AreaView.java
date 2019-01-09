package view.mapComponents;

import java.awt.Color;

import model.environment.objects.area.Area;

/**
 * Visualizes a {@link Area} by changing the color of the floor.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class AreaView extends MapComponentView {

	/**
	 * The area that is visualized.
	 */
	private Area area;

	/**
	 * The color of the area.
	 */
	protected Color areaColor;

	/**
	 * Creates the area view.
	 * 
	 * @param area
	 *            The area.
	 */
	public AreaView(Area area) {
		this.area = area;
		areaColor = Color.WHITE;
	}

	@Override
	public void paintComponent() {
		if (!areaColor.equals(Color.WHITE))
			ShapeDrawer.drawPolygon(areaColor, area.getCorners());
	}
}
