package util.io.logger;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import simulation.simulation.Simulator;
import simulation.simulation.util.SimulationConstants;

/**
 * The base logger is a collection of loggers that are used to log the
 * simulation.
 * 
 * @author S.A.M. Janssen
 */
public class ExtendedBaseLogger extends Logger {

	/**
	 * The {@link Logger}s.
	 */
	private List<Logger> loggerList;

	/**
	 * Creates a base logger.
	 */
	public ExtendedBaseLogger() {
		this(System.getProperty("user.dir") + File.separator + "logfiles" + File.separator);
	}

	/**
	 * Creates a base logger that logs to a specified directory.
	 * 
	 * @param folderName
	 *            The name of the folder.
	 */
	public ExtendedBaseLogger(String folderName) {
		String logLocation = initLog(folderName);
		loggerList = new ArrayList<>();
		loggerList.add(new SimulationLogger(logLocation));
		loggerList.add(new AnalyticsLogger(logLocation));
		loggerList.add(new AgentLogger(logLocation));
	}

	@Override
	public void closeLog() {
		for (Logger logger : loggerList)
			logger.closeLog();
	}

	/**
	 * Initiates log location.
	 * 
	 * @param folderName
	 *            The name of the base folder.
	 * @return The name of the log folder.
	 */
	private String initLog(String folderName) {
		File logLocation = new File(folderName);
		if (!logLocation.exists()) {
			if (!logLocation.mkdir())
				throw new RuntimeException("Log location creation failed");
		}
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		int index = jvmName.indexOf('@');
		if (index > 0)
			jvmName = jvmName.substring(0, index);
		logLocation = new File(System.getProperty("user.dir") + File.separator + "logfiles" + File.separator
				+ System.currentTimeMillis() + "_" + jvmName + "_" + SimulationConstants.randomSeed + File.separator);
		if (!logLocation.exists()) {
			if (!logLocation.mkdir())
				throw new RuntimeException("Log location creation failed");
		}
		return logLocation.toString();
	}

	/**
	 * Print a line.
	 * 
	 * @param string
	 *            The string.
	 */
	@Override
	public void printLine(String string) {
		for (Logger logger : loggerList) {
			if (logger instanceof AgentLogger) {
				logger.printLine(string);
			}
		}
	}

	@Override
	public void setSimulator(Simulator simulator) {
		super.setSimulator(simulator);
		for (Logger logger : loggerList)
			logger.setSimulator(simulator);
	}

	@Override
	public void update(long time, boolean ended) {
		for (Logger logger : loggerList)
			logger.update(time, ended);
	}
}
