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
	private PhysicalObject physicalObject;

	/**
	 * Creates the PhysicalObjectView.
	 * 
	 * @param physicalObject
	 *            The physicalObject.
	 */
	public PhysicalObjectView(PhysicalObject physicalObject) {
		this.physicalObject = physicalObject;
	}

	/**
	 * Gets the physical object.
	 * 
	 * @return The physical object.
	 */
	public PhysicalObject getPhysicalObject() {
		return physicalObject;
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawPolygon(Color.BLACK, physicalObject.getCorners());
	}
}
