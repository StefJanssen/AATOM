package simulation.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.agent.Agent;
import model.agent.humanAgent.Passenger;
import model.environment.map.Map;
import model.environment.map.MapComponent;
import model.environment.objects.physicalObject.sensor.XRaySystem;
import simulation.simulation.agentGenerator.AgentGenerator;
import simulation.simulation.agentGenerator.EmptyAgentGenerator;
import simulation.simulation.endingCondition.BaseEndingConditions;
import simulation.simulation.endingCondition.EndingConditions;
import simulation.simulation.util.DirectlyUpdatable;
import simulation.simulation.util.SimulationObject;
import util.analytics.Analytics;
import util.analytics.Analyzer;
import util.io.logger.BaseLogger;
import util.io.logger.Logger;
import view.GUI;

/**
 * The Simulator simulates our system. It contains a {@link Map} and a set of
 * {@link MapComponent}. It is executed on a different thread.
 * 
 * @author S.A.M. Janssen
 *
 */
public class Simulator extends Thread {

	/**
	 * The {@link Map}.
	 */
	private Map map;
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
	private Analytics analytics;
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
	 * Creates a simulator with default properties.
	 */
	public Simulator() {
		this(true);
	}

	/**
	 * Creates a simulator with a set of properties.
	 * 
	 * @param gui
	 *            With or without {@link GUI}.
	 */
	public Simulator(boolean gui) {
		this(gui, 50);
	}

	/**
	 * Creates a simulator with a set of properties.
	 * 
	 * @param gui
	 *            With or without {@link GUI}.
	 * @param timeStep
	 *            The time step.
	 */
	public Simulator(boolean gui, int timeStep) {
		this(gui, timeStep, new BaseEndingConditions(10000000));
	}

	/**
	 * Creates a simulator with a set of properties.
	 * 
	 * @param gui
	 *            With or without {@link GUI}.
	 * @param timeStep
	 *            The time step.
	 * @param endingConditions
	 *            The {@link EndingConditions}.
	 */
	public Simulator(boolean gui, int timeStep, EndingConditions endingConditions) {
		this(gui, timeStep, endingConditions, new EmptyAgentGenerator());
	}

	/**
	 * Creates a simulator with a set of properties.
	 * 
	 * @param gui
	 *            With or without {@link GUI}.
	 * @param timeStep
	 *            The time step.
	 * @param endingConditions
	 *            The {@link EndingConditions}.
	 * @param agentGenerator
	 *            The {@link AgentGenerator}.
	 */
	public Simulator(boolean gui, int timeStep, EndingConditions endingConditions, AgentGenerator agentGenerator) {
		this(gui, timeStep, endingConditions, agentGenerator, new BaseLogger());
	}

	/**
	 * Creates a simulator with a set of properties.
	 * 
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
	 */
	public Simulator(boolean gui, int timeStep, EndingConditions endingConditions, AgentGenerator agentGenerator,
			Logger logger) {
		this(new Map(), gui, timeStep, endingConditions, agentGenerator, logger);
	}

	/**
	 * Creates a simulator with a set of properties.
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
	 */
	public Simulator(Map map, boolean gui, int timeStep, EndingConditions endingConditions,
			AgentGenerator agentGenerator, Logger logger) {
		this.map = map;
		if (gui) {
			this.gui = new GUI(map, this);
			this.gui.setControls(false);
		}
		this.timeStep = timeStep;
		this.endingConditions = endingConditions;
		this.agentGenerator = agentGenerator;
		this.logger = logger;
		analytics = new Analytics(this);
		endingConditions.setSimulator(this);
		logger.setSimulator(this);
		speedUpFactor = 1;
		numberOfSteps = 0;
	}

	/**
	 * Add a graph to the gui.
	 * 
	 * @param tracker
	 *            The parameter tracker.
	 */
	public void add(Analyzer tracker) {
		add(tracker, true);
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
			if (gui != null && visual)
				gui.add(analyzer);

			analytics.addAnalyzer(analyzer);
		}
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

		if (object instanceof Passenger)
			analytics.addPassenger((Passenger) object);

		// add to GUI
		if (gui != null && object instanceof MapComponent)
			gui.add((MapComponent) object);

		// add to map
		map.add(object);

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
		if (logger != null) {
			analytics.update(timeStep);
			logger.update(numberOfSteps * timeStep, true);
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
	public Analytics getAnalytics() {
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
	 * Gets the return values of the program.
	 * 
	 * @return The return values.
	 */
	public Object[] getReturnValues() {
		return endingConditions.getReturnValues();
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
	 * Remove a map component from the simulation.
	 * 
	 * @param mapComponent
	 *            The map component.
	 */
	private void remove(MapComponent mapComponent) {
		if (mapComponent == null)
			return;

		mapComponent.destroy();
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
		logger.setSimulator(this);
		agentGenerator.setSimulator(this);

		logger.update(numberOfSteps * timeStep, false);
		running = true;

		if (gui != null)
			gui.setSizeAndRatio();

		add(agentGenerator.generateAgent(numberOfSteps, timeStep, true));

		// main loop
		if (gui != null)
			gui.setControls(true);

		while (!endingConditions.isEnded(numberOfSteps)) {
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
		add(agentGenerator.generateAgent(numberOfSteps, timeStep, false));

		// update all directly updatable items
		for (DirectlyUpdatable d : map.getMapComponents(DirectlyUpdatable.class))
			d.update(timeStep);

		// actual functionality
		List<Agent> toBeRemoved = new ArrayList<>();
		for (Agent a : map.getMapComponents(Agent.class)) {
			if (a.getWantsToBeRemoved())
				toBeRemoved.add(a);
		}

		analytics.update(timeStep);

		// update the simulation step
		numberOfSteps++;

		// update the log
		if (logger != null)
			logger.update(numberOfSteps * timeStep, false);

		// remove agents that want to leave
		removeAll(toBeRemoved);

		// GUI update if necessary
		if (gui != null)
			gui.update(timeStep, numberOfSteps * (timeStep / 1000.0));

		// return the time the step took
		return System.currentTimeMillis() - startTime;
	}
}