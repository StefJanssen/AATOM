package view.graphPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;

import util.analytics.Analyzer;

/**
 * A line graph panel forms a panel for a specified line graph.
 * 
 * @author S.A.M. Janssen
 */
@SuppressWarnings("serial")
public class LineGraphPanel extends ChartPanel {

	/**
	 * The parameter tracker.
	 */
	private Analyzer parameterTracker;

	/**
	 * Creates a line graph panel.
	 * 
	 * @param tracker
	 *            The parameter tracker.
	 */
	public LineGraphPanel(Analyzer tracker) {
		super(ChartFactory.createXYLineChart(tracker.getTitle(), "Time (s)", tracker.getYAxis(), tracker.getDataset()));
		this.parameterTracker = tracker;
	}

	/**
	 * Gets the parameter tracker.
	 * 
	 * @return The parameter tracker.
	 */
	public Analyzer getParameterTracker() {
		return parameterTracker;
	}
}
