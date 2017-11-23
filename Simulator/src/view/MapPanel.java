package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.agent.humanAgent.HumanAgent;
import model.agent.humanAgent.Passenger;
import model.environment.map.MapComponent;
import model.environment.objects.area.Area;
import view.mapComponents.HumanAgentView;
import view.mapComponents.MapComponentView;
import view.mapComponents.ShapeDrawer;

/**
 * Visualizes the {@link Map}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class MapPanel extends JPanel {

	/**
	 * The order in which the components are drawn.
	 */
	private List<MapComponent> drawingOrder;
	/**
	 * FPS
	 */
	private int fps;
	/**
	 * The {@link MapComponent}s that are visible on the {@link Map}.
	 */
	private ConcurrentHashMap<MapComponent, MapComponentView> mapComponents;
	/**
	 * The maximum width. This is used for zooming in.
	 */
	private int maxX;
	/**
	 * The maximum height. This is used for zooming in.
	 */
	private int maxY;
	/**
	 * FPS notification
	 */
	private int numberOfCalls;
	/**
	 * The offset in the x direction. This is used for zooming in.
	 */
	private int offsetX;
	/**
	 * The offset in the y direction. This is used for zooming in.
	 */
	private int offsetY;
	/**
	 * The pixel ratio.
	 */
	private double pixelRatio;
	/**
	 * Previous time
	 */
	private long previousTime;
	/**
	 * The starting time of the simulation.
	 */
	private double time;
	/**
	 * Zoom coordinates indicate the coordinates needed to draw the zoom-in
	 * rectangle.
	 */
	private int[] zoomCoordinates;
	/**
	 * The image used to visualize a zoomed in map.
	 */
	private Image zoomImage;
	/**
	 * The user created views.
	 */
	private List<Class<? extends MapComponentView>> views;

	/**
	 * Creates the map panel.
	 * 
	 * @param pixelRatio
	 *            The pixel ratio.
	 */
	public MapPanel(double pixelRatio) {
		setBackground(Color.WHITE);

		mapComponents = new ConcurrentHashMap<>();
		drawingOrder = new ArrayList<>();
		drawingOrder = Collections.synchronizedList(drawingOrder);

		this.pixelRatio = pixelRatio;
		zoomCoordinates = new int[] { -1, -1, -1, -1 };
		offsetX = 0;
		offsetY = 0;
		maxX = 0;
		maxY = 0;
		zoomImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/zoom.png"));
		views = new ArrayList<>();
	}

	/**
	 * Adds a {@link MapComponent} to the map. It is assumed that a
	 * {@link MapComponent} has a {@link MapComponentView} that visualizes the
	 * {@link MapComponent}. The naming convention for a
	 * {@link MapComponentView} is: "MapComponentName"+"View". For instance, the
	 * {@link MapComponent} {@link HumanAgent} has a {@link MapComponentView}
	 * called {@link HumanAgentView}.
	 * 
	 * @param component
	 *            The component.
	 */
	public void addMapComponent(MapComponent component) {
		addMapComponent(component, 0);
	}

	/**
	 * Adds a {@link MapComponent} to the map. It is a recursive method that at
	 * takes a component and a value as an input. The value represents the
	 * number of super classes above the component we want to add a
	 * visualization of. This is used, because some map components do not have a
	 * specific visualization. For instance, the {@link Passenger} does not have
	 * a view corresponding to it, but the {@link HumanAgent} (the superclass of
	 * {@link Passenger}) does have a corresponding view.
	 * 
	 * @param component
	 *            The component.
	 * @param superClasses
	 *            The number of super classes above.
	 */
	private void addMapComponent(MapComponent component, int superClasses) {
		try {
			String className = component.getClass().getSimpleName();
			Class<?> myClass = component.getClass();
			for (int i = 0; i < superClasses; i++) {
				myClass = myClass.getSuperclass();
				className = myClass.getSimpleName();
			}
			// safeguard
			if (className.equals("MapComponent"))
				return;

			Constructor<?> cons = null;
			for (Class<? extends MapComponentView> v : views) {
				if (v.getSimpleName().substring(0, v.getSimpleName().length() - 4).equals(className)) {
					cons = v.getConstructor(myClass);
				}
			}

			// if not in our list of added views
			if (cons == null) {
				Class<?> c = Class.forName("view.mapComponents." + className + "View");
				cons = c.getConstructor(myClass);
			}
			Object object = cons.newInstance(component);
			mapComponents.put(component, (MapComponentView) object);
			if (component instanceof Area)
				drawingOrder.add(0, component);
			else
				drawingOrder.add(component);

		} catch (ClassNotFoundException e) {
			addMapComponent(component, superClasses + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}

	/**
	 * Adds a map component view.
	 * 
	 * @param view
	 *            The view.
	 */
	public void addMapComponentView(Class<? extends MapComponentView> view) {
		views.add(view);
	}

	/**
	 * Method called when the user clicks the mouse. It is used to show about
	 * boxes for map components.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public void clicked(int x, int y) {
		Set<MapComponent> components = mapComponents.keySet();
		for (MapComponent c : components) {
			MapComponentView view = mapComponents.get(c);
			if (view.getBounds().contains(x, y)) {
				JOptionPane.showMessageDialog(this, view.getAboutString(), "Information",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

	}

	/**
	 * Indicates the coordinates that are needed for the zoom in rectangle.
	 * 
	 * @param xStart
	 *            Top left x coordinate.
	 * @param yStart
	 *            Top left y coordinate.
	 * @param xEnd
	 *            Bottom right x coordinate.
	 * @param yEnd
	 *            Bottom right y coordinate.
	 */
	public void doZoomRectangle(int xStart, int yStart, int xEnd, int yEnd) {
		zoomCoordinates = new int[] { xStart, yStart, xEnd, yEnd };
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		ShapeDrawer.updateVariables(g2, pixelRatio, offsetX, offsetY);

		// Draw each component.
		synchronized (drawingOrder) {
			for (MapComponent c : drawingOrder) {
				MapComponentView view = mapComponents.get(c);
				view.repaint();
				view.paintComponent();

			}
		}

		// Draw the rectangle that indicates zooming.
		g2.setColor(Color.BLACK);
		if (zoomCoordinates[0] != -1) {
			g2.drawRect(zoomCoordinates[0], zoomCoordinates[1], zoomCoordinates[2] - zoomCoordinates[0],
					zoomCoordinates[3] - zoomCoordinates[1]);
		}

		// Add the magnifying glass if necessary.
		if (offsetX != 0 || offsetY != 0) {
			g2.drawImage(zoomImage, 5, maxY - 51, 50, 50, null);
		}

		// Draw the time/date
		int hours = (int) (time / 3600);
		int minutes = (int) ((time % 3600) / 60);
		int seconds = (int) (time % 60);
		String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		g2.drawString("Simulation time: " + timeString, maxX - 170, 20);

		// draw fps
		numberOfCalls++;
		long currentTime = System.currentTimeMillis();
		if (currentTime - previousTime > 1000) {
			fps = numberOfCalls;
			numberOfCalls = 0;
			previousTime = currentTime;
		}
		g2.drawString("FPS: " + fps, maxX - 170, 30);

		// Hide the part that we do not want to see in case of zooming.
		g2.setColor(getParent().getBackground());
		if (maxX != 0) {
			if (maxX != getWidth())
				g2.fillRect(maxX, 0, getWidth() - maxX, getHeight());
			else
				g2.fillRect(0, maxY, getWidth(), getHeight() - maxY);
		}
		g2.dispose();
	}

	/**
	 * Removes a {@link MapComponent} from the map panel.
	 * 
	 * @param mapComponent
	 *            The map component.
	 */
	public void remove(MapComponent mapComponent) {
		synchronized (drawingOrder) {
			mapComponents.remove(mapComponent);
			drawingOrder.remove(mapComponent);
		}
	}

	/**
	 * Sets the pixel ratio.
	 * 
	 * @param pixelRatio
	 *            The pixel ratio.
	 */
	public void setPixelRatio(double pixelRatio) {
		this.pixelRatio = pixelRatio;
	}

	/**
	 * Update the map panel.
	 * 
	 * @param timeStep
	 *            The time step.
	 * @param time
	 *            The time.
	 */
	public void update(int timeStep, double time) {
		this.time = time;
		repaint();
	}

	/**
	 * Calculates the parameters needed for the user specified zoom. It ensures
	 * that the users specified ratio is respected, while making sure that the
	 * maximum dimensions are used.
	 * 
	 * @param xStart
	 *            Top left x coordinate.
	 * @param yStart
	 *            Top left y coordinate.
	 * @param xEnd
	 *            Bottom right x coordinate.
	 * @param yEnd
	 *            Bottom right y coordinate.
	 * @param mapX
	 *            The width of the map.
	 */
	public void zoom(int xStart, int yStart, int xEnd, int yEnd, double mapX) {
		// check for defaults
		if (xStart == 0 && yStart == 0 && xEnd == getWidth() && yEnd == getHeight()) {
			offsetX = 0;
			offsetY = 0;
			maxX = 0;
			maxY = 0;
		}

		double height = getHeight();
		double width = getWidth();

		double newHeight = yEnd - yStart;
		double newWidth = xEnd - xStart;

		double pixRatio = height / width;
		double newPixRatio = newHeight / newWidth;

		double oldPixelRatio = pixelRatio;

		// old height > newHeight
		if (pixRatio < newPixRatio) {
			pixelRatio = pixelRatio * (height / newHeight);

			newHeight = height;
			newWidth = newHeight * (1 / newPixRatio);
		}
		// old width > newWidth
		else {
			pixelRatio = pixelRatio * (width / newWidth);
			newWidth = width;
			newHeight = newWidth * newPixRatio;
		}

		offsetX = (int) (Math.max(0, offsetX + xStart) * (pixelRatio / oldPixelRatio));
		offsetY = (int) (Math.max(0, offsetY + yStart) * (pixelRatio / oldPixelRatio));

		maxX = (int) newWidth;
		maxY = (int) newHeight;

		repaint();
	}
}