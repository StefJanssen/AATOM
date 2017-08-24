package util.io.logger;

import java.io.File;

import util.analytics.Analyzer;

/**
 * Logs the tracked parameters.
 * 
 * @author S.A.M. Janssen
 */
public class AnalyticsLogger extends Logger {

	/**
	 * Creates a parameter tracker logger.
	 * 
	 * @param fileName
	 *            The file name.
	 */
	public AnalyticsLogger(String fileName) {
		super(fileName + File.separator + "trackedParameters.txt");
	}

	@Override
	public void update(long time, boolean ended) {
		if (!ended)
			return;

		for (Analyzer analyzer : simulator.getAnalytics().getAnalyzers()) {
			Analyzer tracker = analyzer;
			float[][][] data = tracker.exportData();
			printLine("_graph_" + tracker.getYAxis() + "_" + tracker.getTitle());
			for (int i = 0; i < data.length; i++) {
				if (tracker.getLineNames().length > i) {
					printLine("_line_" + tracker.getLineNames()[i]);
					for (int j = 0; j < data[0].length; j++) {
						printLine(data[i][j][0] + "," + data[i][j][1]);
					}
				}
			}
		}
	}
}
