package util.analytics;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import simulation.simulation.Simulator;
import simulation.simulation.util.DirectlyUpdatable;
import simulation.simulation.util.SimulationObject;

/**
 * A parameter tracker tracks parameters.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Analyzer implements SimulationObject, DirectlyUpdatable {

	/**
	 * The collection of lines.
	 */
	private XYSeriesCollection dataset;
	/**
	 * The last update time.
	 */
	private float lastUpdate;
	/**
	 * The seconds delay.
	 */
	private float secondsDelay;
	/**
	 * The time.
	 */
	protected float time;
	/**
	 * The simulator.
	 */
	private Simulator simulator;

	/**
	 * Creates a parameter tracker without simulator access and 5 seconds delay.
	 */
	public Analyzer() {
		this(5);
	}

	/**
	 * Creates a parameter tracker.
	 * 
	 * @param secondsDelay
	 *            The seconds delay.
	 */
	public Analyzer(float secondsDelay) {
		dataset = new XYSeriesCollection();
		this.secondsDelay = secondsDelay;
		time = 0.0f;
	}

	/**
	 * exports the data of the graph.
	 * 
	 * @return The data.
	 */
	public float[][][] exportData() {
		if (dataset.getSeriesCount() == 0)
			return null;

		updateValues();

		float[][][] data = new float[dataset.getSeriesCount()][dataset.getItemCount(0)][2];
		for (int i = 0; i < dataset.getSeriesCount(); i++) {
			for (int j = 0; j < dataset.getItemCount(i); j++) {
				data[i][j][0] = (float) dataset.getXValue(i, j);
				data[i][j][1] = (float) dataset.getYValue(i, j);
			}
		}
		return data;
	}

	/**
	 * Gets the data set.
	 * 
	 * @return The data set.
	 */
	public XYSeriesCollection getDataset() {
		return dataset;
	}

	/**
	 * Gets the names of the lines.
	 * 
	 * @return The names.
	 */
	public abstract String[] getLineNames();

	/**
	 * Gets the simulator.
	 * 
	 * @return The simulator.
	 */
	public Simulator getSimulator() {
		return simulator;
	}

	/**
	 * The title of the value update.
	 * 
	 * @return The title of the value updater.
	 */
	public abstract String getTitle();

	/**
	 * Gets the current values for a graph.
	 * 
	 * @return The values.
	 */
	public abstract double[] getValues();

	/**
	 * The title of the y axis.
	 * 
	 * @return The title of the y axis.
	 */
	public abstract String getYAxis();

	/**
	 * Initialize the parameters.
	 */
	private void init() {
		for (String s : getLineNames()) {
			XYSeries series = new XYSeries(s);
			int number = 2;
			while (dataset.getSeries().contains(series)) {
				series = new XYSeries(s + " (" + number + ")");
				number++;
			}
			dataset.addSeries(series);
		}
	}

	/**
	 * Sets the simulator.
	 * 
	 * @param simulator
	 *            The simulator.
	 */
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	/**
	 * Update the graph time with a specified step. If this method is
	 * overridden, super.update(int) must be called as well.
	 * 
	 * @param timeStep
	 *            The time step.
	 */
	@Override
	public void update(int timeStep) {
		if (time == 0.0)
			init();

		time += timeStep / 1000.0;
		if (time > lastUpdate + secondsDelay - (timeStep / 1000.0)) {
			lastUpdate = time;
			updateValues();
		}
	}

	/**
	 * Updates the values.
	 */
	private void updateValues() {
		double[] values = getValues();
		for (int i = 0; i < values.length; i++) {
			dataset.getSeries(i).add(time, values[i]);
		}
	}
}