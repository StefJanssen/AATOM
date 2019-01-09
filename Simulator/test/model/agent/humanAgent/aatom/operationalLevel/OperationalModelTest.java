package model.agent.humanAgent.aatom.operationalLevel;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationModule;
import model.agent.humanAgent.aatom.operationalLevel.action.communication.CommunicationType;
import model.agent.humanAgent.aatom.operationalLevel.action.movement.MovementModule;
import model.agent.humanAgent.aatom.operationalLevel.observation.ObservationModule;
import model.environment.position.Vector;

/**
 * Tests the operational model.
 * 
 * @author S.A.M. Janssen
 */
public class OperationalModelTest {

	/**
	 * Test constructor.
	 */
	@Test
	public void constructorTest() {
		MovementModule movement = new MovementModule(1) {

			@Override
			public Vector getMove(int timeStep) {
				return new Vector(0, 0);
			}
		};
		ObservationModule observation = new ObservationModule() {

			@Override
			public <T> Collection<T> getObservation(Class<T> type) {
				return null;
			}
		};

		CommunicationModule communication = new CommunicationModule() {

			@Override
			public void communicate(CommunicationType type, Object communication) {
			}
		};

		OperationalModel operational = new OperationalModel(movement, observation, communication) {
		};

		Assert.assertEquals(operational.getCurrentVelocity(), new Vector(0, 0));
		Assert.assertEquals(operational.getDesiredSpeed(), 1, 0.001);
		Assert.assertEquals(operational.getMove(50), new Vector(0, 0));
	}
}