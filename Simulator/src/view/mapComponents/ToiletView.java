package view.mapComponents;

import java.awt.Color;

import model.environment.objects.area.Toilet;

/**
 * Visualizes a {@link Toilet}.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class ToiletView extends AreaView {

	/**
	 * Creates a toilet view.
	 * 
	 * @param toilet
	 *            The toilet.
	 */
	public ToiletView(Toilet toilet) {
		super(toilet);
		areaColor = new Color(255, 182, 193);
	}

}
