package simulation.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An experimenter allows for multiple simulations to run in parallel.
 * 
 * @author S.A.M. Janssen
 */
public class Experimenter {
	/**
	 * A process task forms a simulation task for the experimenter.
	 * 
	 * @author S.A.M. Janssen
	 */
	private static class ProcessTask extends Thread {

		/**
		 * The commands.
		 */
		private String cmds[];

		/**
		 * Creates a process task.
		 * 
		 * @param cmds
		 *            The commands.
		 */
		public ProcessTask(String[] cmds) {
			this.cmds = cmds;
		}

		@Override
		public void run() {
			ProcessBuilder pb = new ProcessBuilder(cmds);
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
			Process process;
			try {
				process = pb.start();
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * The different processes.
	 */
	private static List<Future<?>> futures;
	/**
	 * The inputs.
	 */
	private static List<String[]> inputs;
	/**
	 * The location of the main class.
	 */
	private static String[] mainLocation;
	/**
	 * The starting time of the experiment.
	 */
	@SuppressWarnings("unused")
	private static long startTime;

	/**
	 * Success or not.
	 */

	@SuppressWarnings("unused")
	private static boolean success = false;

	/**
	 * Concatenates two arrays.
	 * 
	 * @param a
	 *            First array.
	 * @param b
	 *            Second array.
	 * @param <T>
	 *            The type.
	 * @return The concatenated array.
	 */
	private static <T> T[] concatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked")
		T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	/**
	 * Copy a string array.
	 * 
	 * @param src
	 *            The array.
	 * @return The copy.
	 */
	private static String[] copy(String[] src) {
		String[] dest = new String[src.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		return dest;
	}

	/**
	 * Execute the experiment.
	 */
	private static void executeExperiment() {
		int cores = Runtime.getRuntime().availableProcessors();
		final ExecutorService service = Executors.newFixedThreadPool(cores);

		// Prevent experiment from continuing running if this thread is
		// closed.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				service.shutdownNow();
				if (futures != null) {
					for (Future<?> future : futures) {
						future.cancel(true);
					}
				}
			}
		});

		// Add tasks and run them.
		futures = new ArrayList<>();
		for (String[] strings : inputs) {
			ProcessTask t = new ProcessTask(concatenate(mainLocation, strings));
			Future<?> f = service.submit(t);
			futures.add(f);
		}
	}

	/**
	 * Handles an input file. The first line is always the number of trials,
	 * while the second line is the arguments for the simulation.
	 * 
	 * @param fileName
	 *            The file name.
	 * @return The input parameters for the sim.
	 */
	private static List<String[]> handleInput(String fileName) {
		List<String[]> strings = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();

			while (line != null) {
				int numberOfTrials = Integer.parseInt(line);
				String s = br.readLine();
				if (s != null) {
					String[] splitted = s.split(" ");
					int mySeed = Integer.parseInt(splitted[splitted.length - 1]);
					for (int i = 0; i < numberOfTrials; i++) {
						splitted[splitted.length - 1] = Integer.toString(mySeed + i);
						strings.add(copy(splitted));
					}
					line = br.readLine();
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strings;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            The input file name.
	 */
	public static void main(String[] args) {
		preProcess(args);
		executeExperiment();
		postProcess();
	}

	/**
	 * Post processing.
	 */
	private static void postProcess() {
		int i = 0;
		for (Future<?> future : futures) {
			try {
				future.get();
				i++;
				System.out.println(i + "/" + futures.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		boolean cancelled = false;
		for (Future<?> future : futures) {
			if (future.isCancelled())
				cancelled = true;
		}
		if (!cancelled)
			success = true;
		System.exit(0);
	}

	/**
	 * pre processing for the experiment.
	 * 
	 * @param args
	 *            The input.
	 */
	private static void preProcess(String[] args) {
		// Main Location.
		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		String path = System.getProperty("java.home") + separator + "bin" + separator + "java";
		String className = Main.class.getName();
		mainLocation = new String[] { path, "-cp", classpath, className };

		// Handle input.
		if (args.length > 0)
			inputs = handleInput(args[0]);
		else {
			// Default.
			inputs = new ArrayList<>();
			inputs.add(new String[] { "50", "502323" });
			inputs.add(new String[] { "50", "5023222" });
			inputs.add(new String[] { "50", "50232343" });
			inputs.add(new String[] { "50", "502324533" });
			inputs.add(new String[] { "50", "5023213" });
			inputs.add(new String[] { "50", "50232223" });
			inputs.add(new String[] { "50", "5023243" });
		}

		startTime = System.currentTimeMillis();
	}
}
