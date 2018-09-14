package simulation.main;

import simulation.simulation.Simulator;
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
	private static boolean gui = false;
	/**
	 * The number of trials.
	 */
	private static int numberOfTrials = 1;
	/**
	 * Print outs or not.
	 */
	private static boolean printOuts = true;

	/**
	 * Run the program.
	 * 
	 * @param args
	 *            numberOfAgents timeStep numberOfTrials randomSeed NOGUI.
	 */
	public static void main(String[] args) {
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
	 * Perform a simulation with a specified trial number.
	 * 
	 * @param trialNumber
	 *            The trial number.
	 */
	public static void performSimulation(int trialNumber) {
		Utilities.RANDOM_GENERATOR = new RandomPlus(11111 + trialNumber);
		Simulator sim = SimulationBuilder.rotterdamTheHagueAirport(gui, 100);
		Thread t = new Thread(sim);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}