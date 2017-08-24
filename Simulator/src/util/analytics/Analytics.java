package util.analytics;

import java.util.ArrayList;
import java.util.Collection;

import model.agent.humanAgent.Passenger;
import simulation.simulation.Simulator;
import simulation.simulation.util.DirectlyUpdatable;

/**
 * Analytics module of the simulation.
 * 
 * @author S.A.M. Janssen
 */
public class Analytics implements DirectlyUpdatable {

	/**
	 * The set of analyzers.
	 */
	private Collection<Analyzer> analyzers;
	/**
	 * The simulator.
	 */
	private Simulator simulator;

	/**
	 * Creates an analytics module.
	 * 
	 * @param simulator
	 *            The simulator.
	 */
	public Analytics(Simulator simulator) {
		this.simulator = simulator;
		analyzers = new ArrayList<>();
	}

	/**
	 * Adds an analyzer.
	 * 
	 * @param analyzer
	 *            The analyzer.
	 */
	public void addAnalyzer(Analyzer analyzer) {
		if (analyzer != null) {
			analyzers.add(analyzer);
			analyzer.setSimulator(simulator);
		}
	}

	/**
	 * Adds a passenger to the analyzers.
	 * 
	 * @param passenger
	 *            The passenger.
	 */
	public void addPassenger(Passenger passenger) {
		for (Analyzer analyzer : analyzers) {
			if (analyzer instanceof PassengerAnalyzer)
				((PassengerAnalyzer) analyzer).addPassenger(passenger);
		}
	}

	/**
	 * Gets the analyzers.
	 * 
	 * @return THe analyzers.
	 */
	public Collection<Analyzer> getAnalyzers() {
		return analyzers;
	}

	/**
	 * Removes an analyzer.
	 * 
	 * @param analyzer
	 *            The analyzer.
	 */
	public void removeAnalyzer(Analyzer analyzer) {
		analyzers.remove(analyzer);
	}

	@Override
	public void update(int timeStep) {
		for (Analyzer a : analyzers)
			a.update(timeStep);
	}

}
