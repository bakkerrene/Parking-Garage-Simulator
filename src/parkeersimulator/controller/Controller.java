package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.model.Model;

import java.awt.GridLayout;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Controller extends AbstractController implements ActionListener {

	private JButton plus100;
	private JFormattedTextField setSteps;
	private JButton start;
	private JButton stop;
	private JButton reset;
	
	private int steps;

	public Controller(Model model) {

		super(model);
		
		GridLayout layout = new GridLayout();
		this.setLayout(layout);

		start = new JButton("Start");
    	start.addActionListener(this);
    	add(start);
    	
    	setSteps = new JFormattedTextField("0");
    	add(setSteps);

    	plus100 = new JButton("Stappen");
    	plus100.addActionListener(this);
    	add(plus100);

    	stop = new JButton("Stop");
    	stop.addActionListener(this);
    	add(stop);

    	reset = new JButton("Reset");
    	reset.addActionListener(this);
    	add(reset);

		stop.setEnabled(false);
		reset.setEnabled(true);

    	setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		Object sourceObject = e.getSource();
		steps = getSteps();


		if (sourceObject == plus100) {
			model.start(steps);
			model.playSound("button.wav");
		} else if (sourceObject == start) {
			model.start(0);
			model.playSound("button.wav");
		} else if (sourceObject == stop) {
			model.stop();
			model.playSound("button.wav");
		} else if (sourceObject == reset) {
			model.reset();
			model.playSound("button.wav");
		}
	}
	
	private int getSteps() throws NumberFormatException {
		return Integer.parseInt(setSteps.getText());
	}

	private void enableOrDisable(boolean value) {
		plus100.setEnabled(value);
		start.setEnabled(value);
		stop.setEnabled(!value);
		reset.setEnabled(value);
	}

	public void runStarted() {
		enableOrDisable(false);
	}

	public void runStopped() {
		enableOrDisable(true);
	}
}
