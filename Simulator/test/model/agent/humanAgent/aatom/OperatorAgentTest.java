package model.agent.humanAgent.aatom;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.tacticalLevel.activity.Activity;
import model.agent.humanAgent.aatom.tacticalLevel.activity.operator.impl.BasicOperatorCheckInActivity;
import model.environment.objects.physicalObject.Desk;
import model.environment.position.Position;
import util.math.distributions.NormalDistribution;

/**
 * Tests operator agents.
 * 
 * @author S.A.M. Janssen
 */
public class OperatorAgentTest {

	/**
	 * Tests the constructor.
	 */
	@Test
	public void testConstructor() {
		Desk desk = new Desk(10, 10, 1, 0.5, new Position(10.5, 10.5));
		Activity t = new BasicOperatorCheckInActivity(desk, new NormalDistribution(20, 4));
		OperatorAgent agent = new OperatorAgent(new Position(10, 10), 0.2, 80, t);
		Assert.assertEquals(agent.getAssignment(), t);
	}

	/**
	 * Tests the constructor exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorException() {
		new OperatorAgent(new Position(10, 10), 0.2, 80, null);
	}
}