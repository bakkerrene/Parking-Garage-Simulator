package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.model.Model;

import java.awt.GridLayout;
import java.awt.event.*;

/**
 * the main controller on the model view
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 *
 */
@SuppressWarnings("serial")
public class Controller extends AbstractController implements ActionListener {

	private JButton plus100;
	private JFormattedTextField setSteps;
	private JButton start;
	private JButton stop;
	private JButton reset;
	
	private int steps;

	/**
	 * 
	 * @param model This is the model
	 */
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
	
	/**
	 * This method will return an Int value to set in the model.
	 * @return Integer This returns the int value from the text given by setSteps
	 * @throws NumberFormatException Thrown to indicate that the application has attempted to convert a string to one of the numeric types, but that the string does not have the appropriate format.
	 */
	private int getSteps() throws NumberFormatException {
		return Integer.parseInt(setSteps.getText());
	}

	/**
	 * This method will disable the buttons when the simulation is running
	 * @param value sets the buttos to disable or enable.
	 */
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
