package view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import model.environment.map.Map;
import model.environment.map.MapComponent;
import simulation.simulation.Simulator;
import util.analytics.Analyzer;
import view.graphPanel.GraphCollectionPanel;
import view.mapComponents.MapComponentView;

/**
 * Creates the entire GUI of the program.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class GUI extends JFrame {

	/**
	 * Handles zooming of the map for improved view.
	 * 
	 * @author S.A.M. Janssen
	 */
	class ZoomListener implements MouseListener, MouseMotionListener {

		/**
		 * x starting point.
		 */
		private int xStart = -1;
		/**
		 * y starting point.
		 */
		private int yStart = -1;

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (SwingUtilities.isLeftMouseButton(arg0))
				mapPanel.clicked(arg0.getX(), arg0.getY());
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			if (SwingUtilities.isLeftMouseButton(arg0)) {
				int xEnd = arg0.getX();
				int yEnd = arg0.getY();
				int xStart = this.xStart;
				int yStart = this.yStart;

				if (arg0.getX() < xStart) {
					xEnd = xStart;
					xStart = arg0.getX();
				}
				if (arg0.getY() < yStart) {
					yEnd = yStart;
					yStart = arg0.getY();
				}
				mapPanel.doZoomRectangle(xStart, yStart, xEnd, yEnd);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			if (SwingUtilities.isRightMouseButton(arg0)) {
				Rectangle b = tabbedPane.getComponentAt(0).getBounds();
				double heightWidhtRatio = map.getHeight() / map.getWidth();
				double newHeight = b.getHeight();
				double width, height = 0;
				if (heightWidhtRatio >= (newHeight / b.getWidth())) {
					height = newHeight;
					width = newHeight * (1 / heightWidhtRatio);
				} else {
					width = b.getWidth();
					height = b.getWidth() * heightWidhtRatio;
				}
				mapPanel.setSize(new Dimension((int) width, (int) height));
				pixelRatio = height / map.getHeight();
				mapPanel.setPixelRatio(pixelRatio);
				mapPanel.zoom(0, 0, mapPanel.getWidth(), mapPanel.getHeight(), map.getWidth());
				return;
			}
			xStart = arg0.getX();
			yStart = arg0.getY();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			mapPanel.doZoomRectangle(-1, -1, -1, -1);

			if (!SwingUtilities.isRightMouseButton(arg0)) {
				if (xStart == -1 || xStart == arg0.getX())
					return;

				int xEnd = arg0.getX();
				int yEnd = arg0.getY();
				int xStart = this.xStart;
				int yStart = this.yStart;

				if (arg0.getX() < xStart) {
					xEnd = xStart;
					xStart = arg0.getX();
				}
				if (arg0.getY() < yStart) {
					yEnd = yStart;
					yStart = arg0.getY();
				}
				mapPanel.zoom(xStart, yStart, xEnd, yEnd, map.getWidth());
			}
		}
	}

	/**
	 * The {@link ControlPanel}.
	 */
	private ControlPanel controlPanel;
	/**
	 * The {@link GraphCollectionPanel}.
	 */
	private GraphCollectionPanel graphCollectionPanel;
	/**
	 * The {@link Map}.
	 */
	private Map map;
	/**
	 * The {@link MapPanel}.
	 */
	private MapPanel mapPanel;
	/**
	 * The ratio between pixels and meters in the simulation.
	 */
	private double pixelRatio;
	/**
	 * The {@link Simulator}.
	 */
	private Simulator simulator;
	/**
	 * The tabbed pane.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Initializes the GUI with a {@link Map} and a {@link Simulator}.
	 * 
	 * @param map
	 *            The map.
	 * @param simulator
	 *            The simulator.
	 */
	public GUI(Map map, Simulator simulator) {
		this.map = map;
		this.simulator = simulator;

		// Set layout
		setLayout(new BorderLayout());

		// Map panel creation.
		setMapPanel();

		// Graph panel creation.
		setGraphPanel();

		// Tabbed pane creation.
		setTabbedPane();

		// Control panel creation.
		setControlPanel();

		// Frame initializing.
		setMenuBar();
		setTitle("AATOM");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/smartsensing.png")));
		setVisible(true);
	}

	/**
	 * Adds a graph to the view.
	 * 
	 * @param tracker
	 *            The parameter for the graph.
	 */
	public void add(Analyzer tracker) {
		graphCollectionPanel.add(tracker);
	}

	/**
	 * Adds a map component view to the GUI.
	 * 
	 * @param view
	 *            The view.
	 */
	public void add(Class<? extends MapComponentView> view) {
		mapPanel.addMapComponentView(view);
	}

	/**
	 * Add a {@link MapComponent} to the GUI.
	 * 
	 * @param component
	 *            The component.
	 */
	public void add(MapComponent component) {
		mapPanel.addMapComponent(component);
	}

	/**
	 * Ends the simulation.
	 */
	private void endSimulation() {
		simulator.setRunning(false);
		String objButtons[] = { "Yes", "No" };
		int promptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", getTitle(),
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, objButtons, objButtons[1]);
		if (promptResult == 0)
			simulator.endSimulation();
		else
			simulator.setRunning(true);
	}

	/**
	 * Get the parameter trackers of the graphs of the GUI.
	 * 
	 * @return The parameter trackers of the graphs.
	 */
	public Collection<Analyzer> getParameterTrackers() {
		return graphCollectionPanel.getParameterTrackers();
	}

	/**
	 * Removes a graph from the view.
	 * 
	 * @param parameterTracker
	 *            The parameter tracker of the graph.
	 */
	public void remove(Analyzer parameterTracker) {
		graphCollectionPanel.remove(parameterTracker);
	}

	/**
	 * Removes a {@link MapComponent} from the view.
	 * 
	 * @param mapComponent
	 *            The map component.
	 */
	public void remove(MapComponent mapComponent) {
		mapPanel.remove(mapComponent);
	}

	/**
	 * Sets the {@link ControlPanel} and adds a {@link WindowListener} to the
	 * GUI.
	 * 
	 */
	private void setControlPanel() {
		controlPanel = new ControlPanel(simulator);
		add(controlPanel, BorderLayout.SOUTH);
		SwingUtilities.updateComponentTreeUI(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Window listener to prevent users from closing the simulation
		// accidently.
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {
				endSimulation();
			}
		});
	}

	/**
	 * Sets the controls the enabled or disabled.
	 * 
	 * @param b
	 *            Enabled or disabled.
	 */
	public void setControls(boolean b) {
		controlPanel.setControls(b);
	}

	/**
	 * Sets the graph panel.
	 */
	private void setGraphPanel() {
		graphCollectionPanel = new GraphCollectionPanel();
	}

	/**
	 * Sets the {@link MapPanel}.
	 */
	private void setMapPanel() {
		int width = getWidth();
		int height = getHeight() - 100;
		mapPanel = new MapPanel(pixelRatio);
		mapPanel.setPreferredSize(new Dimension(width, height));
		ZoomListener z = new ZoomListener();
		mapPanel.addMouseListener(z);
		mapPanel.addMouseMotionListener(z);
	}

	/**
	 * Sets the {@link JMenuBar} for the GUI.
	 */
	private void setMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		final JMenuItem quit = new JMenuItem("Quit");
		file.add(quit);

		JMenu help = new JMenu("Help");
		final JMenuItem about = new JMenuItem("About..");
		final JMenuItem javadoc = new JMenuItem("Javadoc");
		help.add(about);
		help.add(javadoc);

		menuBar.add(file);
		menuBar.add(help);

		/**
		 * Specifies the actions that are performed when clicking a menu item.
		 * 
		 * @author S.A.M. Janssen
		 */
		class MenuBarActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(quit)) {
					endSimulation();
				} else if (e.getSource().equals(about)) {
					JLabel l = new JLabel("AATOM - An Agent-based Airport Terminal Operations Model");
					l.setFont(new Font("Arial", Font.BOLD, 14));

					Object[] objArr = { l, new JLabel("Version 0.1"), new JLabel(""), new JLabel(""), new JLabel(""),
							new JLabel("Stef Janssen"), new JLabel("Delft University of Technology"),
							new JLabel("TODO") };
					JOptionPane.showMessageDialog(GUI.this, objArr, "About", JOptionPane.INFORMATION_MESSAGE);

				} else if (e.getSource().equals(javadoc)) {
					try {
						Desktop.getDesktop().browse(new URI("http://stefjanssen.com/doc/"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(GUI.this, "Undefined button clicked.");
				}
			}
		}

		MenuBarActionListener menuBarActionListener = new MenuBarActionListener();
		quit.addActionListener(menuBarActionListener);
		about.addActionListener(menuBarActionListener);
		javadoc.addActionListener(menuBarActionListener);
		setJMenuBar(menuBar);
	}

	/**
	 * Sets the size and ratio for the GUI.
	 */
	public void setSizeAndRatio() {
		int maxFrameSize = 800;
		double maxSize = Math.max(map.getHeight(), map.getWidth());
		double heightWidhtRatio = map.getHeight() / map.getWidth();

		double width, height = 0;
		if (heightWidhtRatio >= 1) {
			height = maxFrameSize;
			width = maxFrameSize * (1 / heightWidhtRatio);
		} else {
			width = maxFrameSize;
			height = maxFrameSize * heightWidhtRatio;
		}

		// Variable setting.
		pixelRatio = maxFrameSize / maxSize;
		setSize(new Dimension((int) width, (int) height + 120));
	}

	/**
	 * Sets the tabbed pane.
	 */
	private void setTabbedPane() {
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("MapPanel", mapPanel);
		tabbedPane.addTab("Graphs", graphCollectionPanel);
		add(tabbedPane, BorderLayout.CENTER);

		// Set window listener for resizing.
		tabbedPane.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Rectangle b = tabbedPane.getComponentAt(0).getBounds();
				double heightWidhtRatio = map.getHeight() / map.getWidth();
				double newHeight = b.getHeight();
				double width, height = 0;
				if (heightWidhtRatio >= (newHeight / b.getWidth())) {
					height = newHeight;
					width = newHeight * (1 / heightWidhtRatio);
				} else {
					width = b.getWidth();
					height = b.getWidth() * heightWidhtRatio;
				}
				mapPanel.setSize(new Dimension((int) width, (int) height));
				pixelRatio = height / map.getHeight();
				mapPanel.setPixelRatio(pixelRatio);
				mapPanel.zoom(0, 0, mapPanel.getWidth(), mapPanel.getHeight(), map.getWidth());
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}
		});
	}

	/**
	 * Update the GUI.
	 * 
	 * @param timeStep
	 *            The time step.
	 * @param time
	 *            The time.
	 */
	public void update(int timeStep, double time) {
		mapPanel.update(timeStep, time);
	}
}
