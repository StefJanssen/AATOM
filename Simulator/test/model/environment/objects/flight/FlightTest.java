package model.environment.objects.flight;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;
import simulation.simulation.Simulator;
import simulation.simulation.endingCondition.BaseEndingConditions;

/**
 * The flight test class.
 * 
 * @author S.A.M. Janssen
 */
public class FlightTest {

	/**
	 * Tests the flight constructor.
	 */
	@Test
	public void testConstructor() {

		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);

		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);
		Assert.assertTrue(f.getFlightTime() == 7200);
		Assert.assertTrue(f.getFlightType().equals(FlightType.ARRIVING));
		Assert.assertTrue(f.getTimeToFlight() == 7200);

		Passenger p = null;
		Assert.assertFalse(f.alreadyCheckedIn(p));
	}

	/**
	 * Tests the flight constructor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() {
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);
		new Flight(FlightType.DEPARTING, -1, gate, desks, checkInQueue, checkpointQueue);
	}

	/**
	 * Tests the leaving condition.
	 */
	@Test
	public void testLeaving() {
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);
		Flight f = new Flight(FlightType.DEPARTING, 3600, gate, desks, checkInQueue, checkpointQueue);
		Simulator sim = new Simulator.Builder<>().setGui(false).setEndingConditions(new BaseEndingConditions(4000))
				.createSimulator();
		sim.add(checkpointQueue);
		sim.add(gate);
		sim.add(checkInQueue);
		sim.add(d);
		sim.add(f);
		Thread t = new Thread(sim);
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Assert.assertTrue(f.hasLeft());
	}

	/**
	 * Tests the leaving condition.
	 */
	@Test
	public void testLeaving2() {
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);
		Flight f = new Flight(FlightType.DEPARTING, 5000, gate, desks, checkInQueue, checkpointQueue);
		Simulator sim = new Simulator.Builder<>().setGui(false).setEndingConditions(new BaseEndingConditions(4000))
				.createSimulator();
		sim.add(checkpointQueue);
		sim.add(gate);
		sim.add(checkInQueue);
		sim.add(d);
		sim.add(f);
		Thread t = new Thread(sim);
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		Assert.assertFalse(f.hasLeft());
	}
}