package model.agent.humanAgent.aatom;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.area.GateArea;
import model.environment.objects.area.QueuingArea;
import model.environment.objects.flight.Flight;
import model.environment.objects.flight.FlightType;
import model.environment.objects.physicalObject.Desk;
import model.environment.objects.physicalObject.luggage.Luggage;
import model.environment.position.Position;
import simulation.simulation.Simulator;

/**
 * Tests the passenger.
 * 
 * @author S.A.M. Janssen
 */
public class PassengerTest {

	/**
	 * Tests the builder class.
	 */
	@Test
	public void builderTest() {
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);

		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);

		Passenger p = new Passenger.Builder<>().setPosition(new Position(10, 10)).setFlight(f).build();
		Simulator s = new Simulator.Builder<>().setGui(false).build();
		s.add(p);

		Assert.assertEquals(p.getFlight(), f);
		Assert.assertFalse(p.isCheckedIn());
		Assert.assertNull(p.getFacilityVisit());
		Assert.assertTrue(p.getLuggage().contains(Luggage.NO_LUGGAGE));
		Assert.assertEquals(p.getLuggage().size(), 1);
	}

	/**
	 * Test exceptional cases.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void builderException() {
		new Passenger.Builder<>().build();
	}

}
