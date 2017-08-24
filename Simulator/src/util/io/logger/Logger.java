package util.io.logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import simulation.simulation.Simulator;

/**
 * Abstract notion of a logger.
 * 
 * @author S.A.M. Janssen
 */
public abstract class Logger {

	/**
	 * The {@link PrintWriter}.
	 */
	private PrintWriter writer;
	/**
	 * The flush counter ensures regular flushing of the writer.
	 */
	private int flushCounter;
	/**
	 * The simulator.
	 */
	protected Simulator simulator;

	/**
	 * Empty constructor to allow for joint loggers.
	 */
	public Logger() {
	}

	/**
	 * Creates a logger.
	 * 
	 * @param fileName
	 *            The file name.
	 */
	public Logger(String fileName) {
		try {
			writer = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the log.
	 */
	public void closeLog() {
		writer.close();
	}

	/**
	 * Print a line.
	 * 
	 * @param string
	 *            The string.
	 */
	public void printLine(String string) {
		if (string != null) {
			writer.println(string);
			flushCounter++;
		}

		if (flushCounter == 20)
			writer.flush();
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
	 * Updates the log.
	 * 
	 * @param time
	 *            The time.
	 * @param ended
	 *            If the simulation has ended or not.
	 */
	public abstract void update(long time, boolean ended);
}
