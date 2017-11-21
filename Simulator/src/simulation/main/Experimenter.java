package simulation.main;

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
public class Experimenter extends Thread {
	/**
	 * A process task forms a simulation task for the experimenter.
	 * 
	 * @author S.A.M. Janssen
	 */
	private class ProcessTask extends Thread {

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
	private List<Future<?>> futures;

	/**
	 * The inputs.
	 */
	private List<String[]> inputs;
	/**
	 * The location of the main class.
	 */
	private String[] mainLocation;
	/**
	 * The starting time of the experiment.
	 */
	private long startTime;
	/**
	 * Success or not.
	 */
	private boolean success = false;
	/**
	 * Creates an experimenter.
	 * 
	 * @param inputs
	 *            The inputs.
	 * @param mainClass
	 *            The main class.
	 */
	public Experimenter(List<String[]> inputs, Class<?> mainClass) {
		// set the main location.
		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		String path = System.getProperty("java.home") + separator + "bin" + separator + "java";
		String className = mainClass.getName();
		mainLocation = new String[] { path, "-cp", classpath, className };

		// set the inputs.
		this.inputs = inputs;
	}

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
	private <T> T[] concatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked")
		T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	/**
	 * Execute the experiment.
	 */
	private void executeExperiment() {
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
	 * Post processing.
	 */
	private void postProcess() {
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

		if (success)
			System.out.println("Succesfully done in: " + (System.currentTimeMillis() - startTime));
		else
			System.out.println("Unsuccesfully done in: " + (System.currentTimeMillis() - startTime));

		System.exit(0);
	}

	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		executeExperiment();
		postProcess();
	}
}
