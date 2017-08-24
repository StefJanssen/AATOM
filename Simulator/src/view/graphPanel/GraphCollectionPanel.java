package view.graphPanel;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import util.analytics.Analyzer;

/**
 * A graph collection panel is a panel that holds a collection of line graphs.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class GraphCollectionPanel extends JPanel {

	/**
	 * The collection of line graph panels.
	 */
	private List<LineGraphPanel> panels;

	/**
	 * Creates an empty graph collection panel.
	 */
	public GraphCollectionPanel() {
		this(new ArrayList<Analyzer>());
	}

	/**
	 * Creates a panel with a collection of graphs.
	 * 
	 * @param parameters
	 *            The parameters for graphs.
	 */
	public GraphCollectionPanel(List<Analyzer> parameters) {
		panels = new ArrayList<>();
		for (Analyzer parameter : parameters) {
			panels.add(new LineGraphPanel(parameter));
		}
		updateLayout();
	}

	/**
	 * Add a graph.
	 * 
	 * @param parameter
	 *            The parameter tracker.
	 */
	public void add(Analyzer parameter) {
		panels.add(new LineGraphPanel(parameter));
		updateLayout();
	}

	/**
	 * Get the parameter trackers of the panel.
	 * 
	 * @return The parameter trackers.
	 */
	public Collection<Analyzer> getParameterTrackers() {
		List<Analyzer> trackers = new ArrayList<>();
		for (LineGraphPanel p : panels)
			trackers.add(p.getParameterTracker());
		return trackers;
	}

	/**
	 * Removes a graph.
	 * 
	 * @param parameterTracker
	 *            The parameter tracker.
	 */
	public void remove(Analyzer parameterTracker) {
		LineGraphPanel toRemove = null;
		for (LineGraphPanel p : panels) {
			if (p.getParameterTracker().equals(parameterTracker)) {
				toRemove = p;
				break;
			}
		}
		panels.remove(toRemove);
		updateLayout();
	}

	/**
	 * Updates the layout.
	 */
	private void updateLayout() {
		setLayout(new GridLayout(panels.size(), 1));
		if (panels.size() > 4) {
			setLayout(new GridLayout((int) Math.round(panels.size() / 2.0), 2));
		}

		for (LineGraphPanel p : panels) {
			add(p);
		}
	}

}
