package util.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import model.agent.humanAgent.aatom.Passenger;
import model.environment.objects.area.QueuingArea;
import model.map.Map;

/**
 * A time in queue parameter tracker keeps track of the average time that
 * passengers spent in the queues.
 * 
 * @author S.A.M. Janssen
 */
public class TimeInQueueAnalyzer extends Analyzer {

	/**
	 * The queues.
	 */
	private List<QueuingArea> queues;
	/**
	 * The time in queue for each agent.
	 */
	private List<HashMap<Passenger, double[]>> times;

	@Override
	public String[] getLineNames() {
		String[] names = new String[queues.size()];
		for (int i = 0; i < queues.size(); i++) {
			names[i] = Integer.toString(i);
		}
		return names;
	}

	@Override
	public String getTitle() {
		return "Average Time in Queue";
	}

	@Override
	public double[] getValues() {
		double[] values = new double[queues.size()];
		for (int i = 0; i < queues.size(); i++) {
			int count = 0;
			Set<Passenger> agents = times.get(i).keySet();
			for (Passenger agent : agents) {
				values[i] += times.get(i).get(agent)[1];
				count++;
			}
			if (count != 0)
				values[i] /= count;
		}
		return values;
	}

	@Override
	public String getYAxis() {
		return "Average time in queue (s)";
	}

	@Override
	public void setMap(Map map) {
		super.setMap(map);
		queues = new ArrayList<>();
		times = new ArrayList<>();
		for (QueuingArea queue : map.getMapComponents(QueuingArea.class)) {
			queues.add(queue);
			times.add(new HashMap<Passenger, double[]>());
		}
	}

	@Override
	public void update(int timeStep) {
		Collection<Passenger> passengers = map.getMapComponents(Passenger.class);
		for (int i = 0; i < queues.size(); i++) {
			HashMap<Passenger, double[]> queueMap = times.get(i);
			for (Passenger a : passengers) {
				// the agents currently in the queue
				if (queues.get(i).contains(a.getPosition())) {
					// this is an old guy
					if (queueMap.containsKey(a)) {
						double[] time = queueMap.get(a);
						time[1] += timeStep / 1000.0;
						queueMap.put(a, time);
					}
					// this is a new guy
					else {
						queueMap.put(a, new double[] { map.getTime(), 0.0 });
					}
				}

			}
		}
		super.update(timeStep);
	}

}
