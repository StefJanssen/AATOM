package simulation.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.Agent;
import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import model.map.Map;
import model.map.MapComponent;
import simulation.simulation.agentGenerator.AgentGenerator;
import simulation.simulation.agentGenerator.EmptyAgentGenerator;
import simulation.simulation.endingCondition.BaseEndingConditions;
import simulation.simulation.endingCondition.EndingConditions;
import simulation.simulation.util.DirectlyUpdatable;
import simulation.simulation.util.SimulationObject;
import simulation.simulation.util.Utilities;
import util.analytics.Analyzer;
import util.analytics.AnalyzerCollection;
import util.io.logger.BaseLogger;
import util.io.logger.Logger;
import util.math.RandomPlus;
import view.GUI;
import view.mapComponents.MapComponentView;

/**
 * The Simulator simulates our system. It contains a {@link Map} and a set of
 * {@link MapComponent}. It is executed on a different thread.
 * 
 * @author S.A.M. Janssen
 *
 */
public class Simulator implements Runnable {

	/**
	 * Builder class for the simulator.
	 * 
	 * @author S.A.M. Janssen
	 * @param <T>
	 *            The subclass.
	 */
	@SuppressWarnings("unchecked")
	public static class Builder<T extends Builder<T>> {
		/**
		 * The {@link Map}.
		 */
		private Map map = new Map();
		/**
		 * The {@link GUI}.
		 */
		private boolean gui = true;
		/**
		 * The time step in milliseconds.
		 */
		private int timeStep = 100;
		/**
		 * The ending conditions.
		 */
		private EndingConditions endingConditions = new BaseEndingConditions(10000000);
		/**
		 * The agent generator.
		 */
		private AgentGenerator agentGenerator = new EmptyAgentGenerator();
		/**
		 * The {@link Logger} collects data about our simulation.
		 */
		private Logger logger;
		/**
		 * The random seed.
		 */
		private long randomSeed = System.currentTimeMillis();
		/**
		 * The name of the simulation.
		 */
		private String simulatorName = "Unnamed_Simulator";

		/**
		 * Creates the simulator.
		 * 
		 * @return The simulator.
		 */
		public Simulator build() {
			if (logger == null)
				logger = new BaseLogger();
			return new Simulator(map, gui, timeStep, endingConditions, agentGenerator, logger, randomSeed,
					simulatorName);
		}

		/**
		 * Sets the agent generator.
		 * 
		 * @param agentGenerator
		 *            The {@link AgentGenerator}.
		 * @return The builder.
		 * 
		 */
		public T setAgentGenerator(AgentGenerator agentGenerator) {
			this.agentGenerator = agentGenerator;
			return (T) this;
		}

		/**
		 * Sets the ending conditions.
		 * 
		 * @param endingConditions
		 *            The {@link EndingConditions}.
		 * @return The builder.
		 * 
		 */
		public T setEndingConditions(EndingConditions endingConditions) {
			this.endingConditions = endingConditions;
			return (T) this;
		}

		/**
		 * Sets the gui.
		 * 
		 * @param gui
		 *            The gui.
		 * @return The builder.
		 * 
		 */
		public T setGui(boolean gui) {
			this.gui = gui;
			return (T) this;
		}

		/**
		 * Sets the logger.
		 * 
		 * @param logger
		 *            The {@link Logger}.
		 * @return The builder.
		 * 
		 */
		public T setLogger(Logger logger) {
			this.logger = logger;
			return (T) this;
		}

		/**
		 * Sets the map.
		 * 
		 * @param map
		 *            The map.
		 * @return The builder.
		 */
		public T setMap(Map map) {
			this.map = map;
			return (T) this;
		}

		/**
		 * Sets the random seed.
		 * 
		 * @param randomSeed
		 *            The random seed.
		 * @return The builder.
		 * 
		 */
		public T setRandomSeed(long randomSeed) {
			this.randomSeed = randomSeed;
			return (T) this;
		}

		/**
		 * Sets the simulation name.
		 * 
		 * @param simulationName
		 *            The name.
		 * @return The builder.
		 * 
		 */
		public T setSimulationName(String simulationName) {
			this.simulatorName = simulationName;
			return (T) this;
		}

		/**
		 * Sets the time step.
		 * 
		 * @param timeStep
		 *            The time step.
		 * @return The builder.
		 * 
		 */
		public T setTimeStep(int timeStep) {
			this.timeStep = timeStep;
			return (T) this;
		}
	}

	/**
	 * The {@link Map}.
	 */
	private final Map map;
	/**
	 * The {@link GUI}.
	 */
	private GUI gui;
	/**
	 * The time step in milliseconds.
	 */
	private final int timeStep;
	/**
	 * The ending conditions.
	 */
	private EndingConditions endingConditions;
	/**
	 * The agent generator.
	 */
	private AgentGenerator agentGenerator;
	/**
	 * The {@link Logger} collects data about our simulation.
	 */
	private Logger logger;
	/**
	 * The analytics section of the simulation.
	 */
	private AnalyzerCollection analytics;
	/**
	 * The number of steps that we performed so far.
	 */
	private long numberOfSteps;
	/**
	 * Flag to specify if the simulation is running. Volatile for thread safety.
	 */
	private volatile boolean running;
	/**
	 * Speed up the simulation. Volatile for thread safety.
	 */
	private volatile int speedUpFactor;
	/**
	 * The random seed.
	 */
	private final long randomSeed;

	/**
	 * The name of the simulation.
	 */
	private String simulatorName;

	/**
	 * Creates a simulator with a set of properties. Use the
	 * {@link Simulator.Builder} to create the object.
	 * 
	 * @param map
	 *            The {@link Map}.
	 * @param gui
	 *            With or without {@link GUI}.
	 * @param timeStep
	 *            The time step in ms.
	 * @param endingConditions
	 *            The {@link EndingConditions}.
	 * @param agentGenerator
	 *            The {@link AgentGenerator}.
	 * @param logger
	 *            The {@link Logger}.
	 * @param randomSeed
	 *            The random seed.
	 * @param simulatorName
	 *            The name of the simulator.
	 */
	private Simulator(Map map, boolean gui, int timeStep, EndingConditions endingConditions,
			AgentGenerator agentGenerator, Logger logger, long randomSeed, String simulatorName) {
		this.map = map;
		if (gui) {
			this.gui = new GUI(map, this);
			this.gui.setControls(false);
		}
		this.timeStep = timeStep;
		this.endingConditions = endingConditions;
		this.agentGenerator = agentGenerator;
		this.logger = logger;
		this.randomSeed = randomSeed;
		Utilities.RANDOM_GENERATOR = new RandomPlus(randomSeed);
		this.simulatorName = simulatorName;
		analytics = new AnalyzerCollection();
		endingConditions.setSimulator(this);
		logger.setSimulator(this);
		speedUpFactor = 1;
		numberOfSteps = 0;
	}

	/**
	 * Add a graph to the gui.
	 * 
	 * @param analyzer
	 *            The parameter tracker.
	 */
	public void add(Analyzer analyzer) {
		add(analyzer, true);
	}

	/**
	 * Add a graph to the gui.
	 * 
	 * @param analyzer
	 *            The parameter tracker.
	 * @param visual
	 *            True if it is shown in the gui, false otherwise.
	 */
	public void add(Analyzer analyzer, boolean visual) {
		if (analyzer != null) {
			analyzer.setMap(map);
			if (gui != null && visual)
				gui.add(analyzer);

			analytics.addAnalyzer(analyzer);
		}
	}

	/**
	 * Add a map component view to the gui.
	 * 
	 * @param view
	 *            The view.
	 */
	public void add(Class<? extends MapComponentView> view) {
		if (!map.getMapComponents(MapComponent.class).isEmpty())
			System.err.println(
					"Please add all your MapComponentViews to the simulator BEFORE you add any map components. Simulation will continue, but your visualization might not show.");
		if (gui != null)
			gui.add(view);
	}

	/**
	 * Add a simulation object.
	 * 
	 * @param object
	 *            The object.
	 */
	public void add(SimulationObject object) {
		// null = done.
		if (object == null)
			return;

		if (object instanceof XRaySystem)
			map.add(((XRaySystem) object).getXRaySensor());

		if (object instanceof Passenger) {
			analytics.addPassenger((Passenger) object);
			for (Luggage l : ((Passenger) object).getLuggage())
				map.add(l);
		}

		// add to GUI
		if (gui != null && object instanceof MapComponent)
			gui.add((MapComponent) object);

		// add to map
		if (object instanceof MapComponent) {
			map.add((MapComponent) object);
		}
		if (numberOfSteps > 0 || running) {
			if (object instanceof Agent) {
				((Agent) object).init();
			}
		}

	}

	/**
	 * Add a list of {@link SimulationObject}s to the simulation.
	 * 
	 * @param components
	 *            The list of components.
	 */
	public void addAll(Collection<? extends SimulationObject> components) {
		for (SimulationObject component : components)
			add(component);
	}

	/**
	 * End the simulation.
	 */
	public void endSimulation() {

		// End logger.
		if (!logger.equals(Logger.NO_LOGGER)) {
			analytics.update(timeStep);
			logger.update(numberOfSteps * timeStep, true);
			logger.printLine(simulatorName);
			logger.closeLog();
		}

		// End GUI.
		if (gui != null)
			gui.dispose();

		// End model.
		endingConditions.forceEnd();
	}

	/**
	 * Gets the analytics.
	 * 
	 * @return The analytics.
	 */
	public AnalyzerCollection getAnalytics() {
		return analytics;
	}

	/**
	 * Gets the {@link Map}.
	 * 
	 * @return The map.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Gets the number of steps performed so far.
	 * 
	 * @return The number of steps.
	 */
	public long getNumberOfSteps() {
		return numberOfSteps;
	}

	/**
	 * Gets the random seed.
	 * 
	 * @return The random seed.
	 */
	public long getRandomSeed() {
		return randomSeed;
	}

	/**
	 * Gets the return values of the program.
	 * 
	 * @return The return values.
	 */
	public Object[] getReturnValues() {
		return endingConditions.getReturnValues();
	}

	/**
	 * Gets the simulation name.
	 * 
	 * @return The name.
	 */
	public String getSimulationName() {
		return simulatorName;
	}

	/**
	 * Gets the time step.
	 * 
	 * @return The time step.
	 */
	public int getTimeStep() {
		return timeStep;
	}

	/**
	 * Simulation running or not.
	 * 
	 * @return True if it is, false otherwise.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Remove a map component from the simulation.
	 * 
	 * @param mapComponent
	 *            The map component.
	 */
	private void remove(MapComponent mapComponent) {
		if (mapComponent == null)
			return;

		map.remove(mapComponent);
		if (mapComponent instanceof XRaySystem)
			map.remove(((XRaySystem) mapComponent).getXRaySensor());

		if (gui != null)
			gui.remove(mapComponent);
	}

	/**
	 * Remove a collection of agents from the simulation.
	 * 
	 * @param agents
	 *            The agents.
	 */
	private void removeAll(Collection<Agent> agents) {
		for (Agent agent : agents) {
			if (agent instanceof MapComponent)
				remove(agent);
		}
	}

	/**
	 * Start the simulation.
	 */
	@Override
	public void run() {
		if (!logger.equals(Logger.NO_LOGGER))
			logger.setSimulator(this);
		agentGenerator.setSimulator(this);

		for (Agent a : map.getMapComponents(Agent.class))
			a.init();

		if (!logger.equals(Logger.NO_LOGGER))
			logger.update(numberOfSteps * timeStep, false);

		running = true;

		if (gui != null)
			gui.setSizeAndRatio();

		List<? extends Agent> agents = agentGenerator.generateAgent(numberOfSteps, timeStep, true);
		if (agents != null) {
			for (Agent a : agents)
				add(a);
		}

		// main loop
		if (gui != null)
			gui.setControls(true);

		while (!endingConditions.hasEnded(numberOfSteps)) {
			long time = 0;
			if (running) {
				time = step();
			}
			if (gui != null) {
				if (speedUpFactor > 0)
					sleep(Math.max((timeStep - (int) time) / speedUpFactor, 1));
			}
		}
		// done with main loop
		endSimulation();
	}

	/**
	 * Set the running flag.
	 * 
	 * @param running
	 *            Running flag.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Set the speed up factor to increase the speed of a GUI simulation.
	 * 
	 * @param speedUpFactor
	 *            the factor to increase the speed with.
	 */
	public void setSpeedUpFactor(int speedUpFactor) {
		this.speedUpFactor = speedUpFactor;
	}

	/**
	 * Sleep for a specified amount of milliseconds.
	 * 
	 * @param time
	 *            The time to sleep (in milliseconds).
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Sleep failed");
		}
	}

	/**
	 * Perform a step in the simulation.
	 * 
	 * @return The time it took to perform the step.
	 */
	public long step() {
		// keep track of the time the step takes
		long startTime = System.currentTimeMillis();
		// add agents
		List<? extends Agent> agents = agentGenerator.generateAgent(numberOfSteps, timeStep, false);
		if (agents != null) {
			for (Agent a : agents)
				add(a);
		}

		// update all directly updatable items
		for (DirectlyUpdatable d : map.getMapComponents(DirectlyUpdatable.class))
			d.update(timeStep);

		// actual functionality
		List<Agent> toBeRemoved = new ArrayList<>();
		for (Agent a : map.getMapComponents(Agent.class)) {
			if (a.wantsToBeDestoryed())
				toBeRemoved.add(a);
		}

		analytics.update(timeStep);

		// update the simulation step
		numberOfSteps++;
		map.updateTime(timeStep);

		// update the log
		if (!logger.equals(Logger.NO_LOGGER))
			logger.update(numberOfSteps * timeStep, false);

		// remove agents that want to leave
		removeAll(toBeRemoved);

		// GUI update if necessary
		if (gui != null)
			gui.update();

		// return the time the step took
		return System.currentTimeMillis() - startTime;
	}
}