package util.analytics;

import java.util.ArrayList;
import java.util.List;

import model.agent.humanAgent.Passenger;
import model.environment.objects.area.QueuingArea;
import model.map.Map;

/**
 * A queue parameter tracker updates value for a queue line graph.
 * 
 * @author S.A.M. Janssen
 */
public class QueueAnalyzer extends Analyzer {

	/**
	 * The queues.
	 */
	private List<QueuingArea> queues;

	@Override
	public String[] getLineNames() {
		String[] names = new String[queues.size()];
		for (int i = 0; i < queues.size(); i++) {
			names[i] = Integer.toString(i);
		}
		return names;
	}

	/**
	 * Gets the number of passengers in a queue.
	 * 
	 * @param queue
	 *            The queuing area.
	 * @return The number of passengers.
	 */
	private int getNumberOfPassengersInQueue(QueuingArea queue) {
		int number = 0;
		for (Passenger a : map.getMapComponents(Passenger.class)) {
			if (queue.contains(a.getPosition())) {
				number++;
			}
		}
		return number;
	}

	@Override
	public String getTitle() {
		return "Passengers in Queue";
	}

	@Override
	public double[] getValues() {
		double[] values = new double[queues.size()];
		for (int i = 0; i < queues.size(); i++) {
			values[i] = getNumberOfPassengersInQueue(queues.get(i));
		}
		return values;
	}

	@Override
	public String getYAxis() {
		return "# Passengers in Queue";
	}

	@Override
	public void setMap(Map map) {
		super.setMap(map);
		queues = new ArrayList<>();
		for (QueuingArea queue : map.getMapComponents(QueuingArea.class))
			queues.add(queue);
	}

}
