package model.environment.objects.physicalObject.luggage;

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
import model.environment.position.Position;

/**
 * Tests the luggage class.
 * 
 * @author S.A.M. Janssen
 */
public class LuggageTest {

	/**
	 * Test the constructor.
	 */
	@Test
	public void testConstructor() {
		Luggage l = new Luggage(LuggageType.CARRY_ON, 0, 0);
		Assert.assertEquals(l.getPosition(), Position.NO_POSITION);
		Assert.assertEquals(l.getComplexity(), 0, 0.001);
		Assert.assertEquals(l.getThreatLevel(), 0, 0.001);
	}

	/**
	 * Test the setting of the owner.
	 */
	@Test
	public void testOwnerSetting() {
		// create luggage
		Luggage l = new Luggage(LuggageType.CARRY_ON, 0, 0);

		// create flight
		QueuingArea checkpointQueue = new QueuingArea(10, 10, 4, 4, new Position(10, 10), new Position(14, 14));
		GateArea gate = new GateArea(1, 1, 5, 5);
		QueuingArea checkInQueue = new QueuingArea(4, 5, 3, 3, new Position(7, 8), new Position(4, 5));

		Desk d = new Desk(4, 4, 1, 0.1, new Position(4.5, 4.3));
		List<Desk> desks = new ArrayList<>();
		desks.add(d);
		Flight f = new Flight(FlightType.ARRIVING, 7200, gate, desks, checkInQueue, checkpointQueue);

		// create passenger
		Passenger p = new Passenger.Builder<>().setPosition(new Position(10, 10)).setFlight(f).build();
		l.setOwner(p);
		Assert.assertEquals(p.getPosition(), l.getPosition());
	}

	/**
	 * Test the setting of the position.
	 */
	@Test
	public void testPositionSetting() {
		Luggage l = new Luggage(LuggageType.CARRY_ON, 0, 0);
		l.setPosition(new Position(10, 12));
		Assert.assertEquals(new Position(10, 12), l.getPosition());
	}

}
