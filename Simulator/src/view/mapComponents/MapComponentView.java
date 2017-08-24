package view.mapComponents;

import javax.swing.JComponent;

import model.environment.map.MapComponent;

/**
 * Superclass for each of the MapComponentViews. It visualizes a
 * {@link MapComponent}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public abstract class MapComponentView extends JComponent {

	/**
	 * Returns a string containing information about the map component it
	 * visualizes. If null is returned, the specific component does not have any
	 * information it wishes to visualize. It is required to override the method
	 * {@link javax.swing.JComponent#setBounds(java.awt.Rectangle)} to represent
	 * the actual bounding box of the object it visualizes, in the method
	 * {@link #paintComponent()}.
	 * 
	 * @return The information of the map component.
	 */
	public String getAboutString() {
		return null;
	}

	/**
	 * Paints the mapComponentView. The method should use the
	 * {@link ShapeDrawer} class to draw the desired shapes.
	 * 
	 * If you wish to implement the {@link MapComponentView#getAboutString()}
	 * about method, the paint method should override the method
	 * {@link javax.swing.JComponent#setBounds(java.awt.Rectangle)} to represent
	 * the actual bounding box of the object it visualizes.
	 */
	public abstract void paintComponent();
}
