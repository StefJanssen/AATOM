package view.mapComponents;

import java.awt.Color;

import model.environment.objects.area.Restaurant;

/**
 * Visualizes a restaurant.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class RestaurantView extends AreaView {

	/**
	 * Creates a {@link Restaurant} view.
	 * 
	 * @param restaurant
	 *            The restaurant.
	 */
	public RestaurantView(Restaurant restaurant) {
		super(restaurant);
		areaColor = new Color(0, 0, 139);
	}

}
