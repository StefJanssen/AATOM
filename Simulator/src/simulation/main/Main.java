package simulation.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulation.simulation.Simulator;
import simulation.simulation.util.SimulationConstants;
import simulation.simulation.util.Utilities;
import simulation.simulationBuilder.SimulationBuilder;
import util.math.RandomPlus;

/**
 * Main class.
 * 
 * @author S.A.M. Janssen
 */
public class Main {

	/**
	 * GUI or not.
	 */
	private static boolean gui = true;
	/**
	 * The number of trials.
	 */
	private static int numberOfTrials = 1;
	/**
	 * Print outs or not.
	 */
	private static boolean printOuts = false;

	/**
	 * Handles the input provided by the user. It passes each argument to the
	 * static fields declared in the {@link SimulationConstants} class. The last
	 * argument is always the random seed, an integer. The one before that is
	 * always the timeStep, an integer.
	 * 
	 * @param args
	 *            input arguments alphabetically ordered. random seed as the
	 *            last input argument, time step as the one before that.
	 */
	private static void handleInput(String[] args) {
		if (args.length == 0)
			return;

		Field[] declaredFields = SimulationConstants.class.getDeclaredFields();
		List<Field> staticFields = new ArrayList<Field>();
		List<String> staticFieldNames = new ArrayList<String>();

		for (Field field : declaredFields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				staticFields.add(field);
				staticFieldNames.add(field.getName());
			}
		}

		Collections.sort(staticFieldNames);
		staticFieldNames.remove("timeStep");
		staticFieldNames.add("timeStep");
		staticFieldNames.remove("randomSeed");
		staticFieldNames.add("randomSeed");

		Field[] sortedFields = new Field[staticFields.size()];
		for (Field field : staticFields) {
			sortedFields[staticFieldNames.indexOf(field.getName())] = field;
		}

		if (args.length > 0) {
			if (args[0].contains("-help") || args[0].contains("-?")) {
				System.out.println(staticFieldNames);
				return;
			}
		}

		if (args.length > 0) {
			// gui = true;
			numberOfTrials = 1;
		}

		try {
			for (int i = 0; i < args.length - 2; i++) {
				Class<?> type = sortedFields[i].getType();
				sortedFields[i].set(null, parseString(type, args[i]));
			}
			sortedFields[sortedFields.length - 1].set(null,
					parseString(sortedFields[sortedFields.length - 1].getType(), args[args.length - 1]));
			if (sortedFields.length > 1 && args.length > 1)
				sortedFields[sortedFields.length - 2].set(null,
						parseString(sortedFields[sortedFields.length - 2].getType(), args[args.length - 2]));

		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run the program.
	 * 
	 * @param args
	 *            numberOfAgents timeStep numberOfTrials randomSeed NOGUI.
	 */
	public static void main(String[] args) {
		// handle the inputs of the simulation.
		handleInput(args);
		// log the time.
		long time = System.currentTimeMillis();
		// print start of simulation
		if (printOuts)
			System.out.println("Starting simulations..");
		// perform numberOfTrials simulation runs
		for (int i = 0; i < numberOfTrials; i++) {
			if (printOuts)
				System.out.print(i + 1 + "/" + numberOfTrials);
			performSimulation(i);
			if (printOuts)
				System.out.println(" DONE.");
		}
		// finalize.
		if (printOuts) {
			System.out.print("DONE in ");
			System.out.println(System.currentTimeMillis() - time);
		}
	}

	/**
	 * Parse a string to a different type.
	 * 
	 * @param type
	 *            The type.
	 * @param value
	 *            The string value.
	 * @return The parsed string.
	 */
	private static Object parseString(Class<?> type, String value) {
		if ("boolean" == type.getName())
			return Boolean.parseBoolean(value);
		if ("byte" == type.getName())
			return Byte.parseByte(value);
		if ("short" == type.getName())
			return Short.parseShort(value);
		if ("int" == type.getName())
			return Integer.parseInt(value);
		if ("long" == type.getName())
			return Long.parseLong(value);
		if ("float" == type.getName())
			return Float.parseFloat(value);
		if ("double" == type.getName())
			return Double.parseDouble(value);
		return value;
	}

	/**
	 * Perform a simulation with a specified trial number.
	 * 
	 * @param trialNumber
	 *            The trial number.
	 */
	public static void performSimulation(int trialNumber) {
		Utilities.RANDOM_GENERATOR = new RandomPlus(SimulationConstants.randomSeed + trialNumber);
		Simulator sim = SimulationBuilder.eindhovenAirport(gui, SimulationConstants.timeStep);
		sim.start();
		try {
			sim.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}