package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulation.simulation.Simulator;

/**
 * Creates the control panel, which contains buttons like "play", "pause" and
 * "step", which can start, pause or step the {@link Simulator}.
 * 
 * @author S.A.M. Janssen
 *
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

	/**
	 * pause button.
	 */
	private final JButton pause;
	/**
	 * play button.
	 */
	private final JButton play;
	/**
	 * step button.
	 */
	private final JButton step;
	/**
	 * Speed up box.
	 */
	private JComboBox<String> speedUpBox;
	/**
	 * The simulator.
	 */
	private final Simulator simulator;

	/**
	 * Creates the control panel.
	 * 
	 * @param simulator
	 *            The simulator.
	 */
	public ControlPanel(final Simulator simulator) {
		this.simulator = simulator;
		pause = new JButton("Pause");
		play = new JButton("Play");
		step = new JButton("Step");
		speedUpBox = new JComboBox<>();
		speedUpBox.addItem("1x");
		speedUpBox.addItem("2x");
		speedUpBox.addItem("3x");
		speedUpBox.addItem("4x");
		speedUpBox.addItem("5x");
		speedUpBox.addItem("10x");
		speedUpBox.addItem("max");

		play.setEnabled(false);
		step.setEnabled(false);

		add(new JLabel("Simulation speed: "));
		add(speedUpBox);
		add(new JLabel("                  "));

		add(pause);
		add(play);
		add(step);

		ItemListener itemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String item = (String) e.getItem();
				int speedUpFactor = 1;
				if (item.equals("max"))
					speedUpFactor = -1;
				else
					speedUpFactor = Integer.parseInt(item.substring(0, item.length() - 1));
				simulator.setSpeedUpFactor(speedUpFactor);
			}
		};

		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(pause)) {
					simulator.setRunning(false);
					pause.setEnabled(false);
					play.setEnabled(true);
					step.setEnabled(true);
				} else if (e.getSource().equals(play)) {
					simulator.setRunning(true);
					pause.setEnabled(true);
					step.setEnabled(false);
					play.setEnabled(false);
				} else { // step
					simulator.step();
				}

			}
		};
		pause.addActionListener(listener);
		play.addActionListener(listener);
		step.addActionListener(listener);
		speedUpBox.addItemListener(itemListener);
	}

	/**
	 * Sets the controls the enabled or disabled.
	 * 
	 * @param b
	 *            Enabled or disabled.
	 */
	public void setControls(boolean b) {
		if (!b) {
			play.setEnabled(false);
			step.setEnabled(false);
			pause.setEnabled(false);
			speedUpBox.setEnabled(false);
		} else {
			play.setEnabled(false);
			step.setEnabled(false);
			pause.setEnabled(true);
			speedUpBox.setEnabled(true);
			simulator.setRunning(true);
			String item = (String) speedUpBox.getSelectedItem();
			int speedUpFactor = 1;
			if (item.equals("max"))
				speedUpFactor = -1;
			else
				speedUpFactor = Integer.parseInt(item.substring(0, item.length() - 1));
			simulator.setSpeedUpFactor(speedUpFactor);

		}
	}
}