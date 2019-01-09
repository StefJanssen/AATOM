package view.mapComponents;

import java.awt.Color;

import model.environment.objects.area.Shop;

/**
 * Visualizes a {@link Shop}.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class ShopView extends AreaView {

	/**
	 * Creates a {@link Shop} view.
	 * 
	 * @param shop
	 *            The shop.
	 */
	public ShopView(Shop shop) {
		super(shop);
		areaColor = new Color(173, 216, 230);
	}

}
