package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.model.Model;

import java.awt.event.*;

@SuppressWarnings("serial")
public class Controller extends AbstractController implements ActionListener {

	private JButton plus1;
	private JButton plus100;
	private JButton start;
	private JButton stop;
	private JButton reset;

	private boolean inSim = false;

	public Controller(Model model) {

		super(model);

		setSize(800, 50);
		setLayout(null);

		start = new JButton("Start");
    	start.addActionListener(this);
    	add(start);
		start.setBounds(259, 10, 70, 30);

		plus1 = new JButton("1 Step");
    	plus1.addActionListener(this);    
    	add(plus1);
    	plus1.setBounds(50, 10, 70, 30);

    	plus100 = new JButton("100 Steps");
    	plus100.addActionListener(this);
    	add(plus100);
		plus100.setBounds(140, 10, 100, 30);

    	stop = new JButton("Stop");
    	stop.addActionListener(this);
    	add(stop);
		stop.setBounds(349, 10, 70, 30);

    	reset = new JButton("Reset");
    	reset.addActionListener(this);
    	add(reset);
		reset.setBounds(439, 10, 70, 30);

		stop.setEnabled(false);
		reset.setEnabled(true);

    	setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		Object sourceObject = e.getSource();

		if (sourceObject == plus1) {
			inSim = true;
			model.start(1);
		} else if (sourceObject == plus100) {
			inSim = true;
			model.start(100);
		} else if (sourceObject == start) {
			inSim = true;
			model.start(0);
		} else if (sourceObject == stop) {
			inSim = false;
			model.stop();
		} else if (sourceObject == reset) {
			model.reset();
		}

		plus1.setEnabled(!inSim);
		plus100.setEnabled(!inSim);
		start.setEnabled(!inSim);;
		stop.setEnabled(inSim);
		reset.setEnabled(!inSim);
	}

	public void clickedSpot(Location location) {
	}
}