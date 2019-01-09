package util.io.logger;

import java.io.File;

/**
 * The Return Value Logger logs the return values of the simulation.
 * 
 * @author S.A.M. Janssen
 */
public class ReturnValueLogger extends Logger {

	/**
	 * Creates a simulation logger.
	 * 
	 * @param fileName
	 *            The file name.
	 */
	public ReturnValueLogger(String fileName) {
		super(fileName + File.separator + "returnValues.txt");
	}

	@Override
	public void update(long time, boolean ended) {
		if (ended) {
			StringBuilder s = new StringBuilder();
			Object[] returnValues = simulator.getReturnValues();
			if (returnValues != null) {
				for (int i = 0; i < returnValues.length; i++) {
					if (returnValues[i] != null) {
						s.append(returnValues[i].toString());
						if (i != returnValues.length - 1)
							s.append(",");
					} else {
						s.append("NaN,");
					}
				}
				printLine(s.toString());
			}
		}
	}
}
