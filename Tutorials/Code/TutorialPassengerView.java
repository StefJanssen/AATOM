import java.awt.Color;

import model.environment.position.Position;
import view.mapComponents.MapComponentView;
import view.mapComponents.ShapeDrawer;

/**
 * Creates a custom view for the tutorial passenger.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class TutorialPassengerView extends MapComponentView {

	/**
	 * The passenger.
	 */
	private TutorialPassenger passenger;

	/**
	 * Creates the view.
	 * 
	 * @param passenger
	 *            The passenger.
	 */
	public TutorialPassengerView(TutorialPassenger passenger) {
		this.passenger = passenger;
	}

	@Override
	public String getAboutString() {
		return "<html><i>Hello</i> world, my hashcode is: " + passenger.hashCode() + "</html>";
	}

	@Override
	public void paintComponent() {
		ShapeDrawer.drawCircle(Color.BLUE, passenger.getPosition(), passenger.getRadius());
		// Set the bounds for the about box.
		setBounds(ShapeDrawer.getRectangle(
				new Position(passenger.getPosition().x - passenger.getRadius(),
						passenger.getPosition().y - passenger.getRadius()),
				2 * passenger.getRadius(), 2 * passenger.getRadius()));
	}
}
