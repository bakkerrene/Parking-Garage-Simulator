package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.model.Model;

import java.awt.GridLayout;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Controller extends AbstractController implements ActionListener {

	private JButton plus1;
	private JButton plus100;
	private JButton start;
	private JButton stop;
	private JButton reset;

	public Controller(Model model) {

		super(model);
		
		GridLayout layout = new GridLayout();
		this.setLayout(layout);

		start = new JButton("Start");
    	start.addActionListener(this);
    	add(start);
		start.setBounds(259, 10, 70, 30);

		plus1 = new JButton("1 Stap");
    	plus1.addActionListener(this);    
    	add(plus1);
    	plus1.setBounds(50, 10, 70, 30);

    	plus100 = new JButton("100 Stappen");
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
			model.start(1);
			model.playSound("button.wav");
		} else if (sourceObject == plus100) {
			model.start(100);
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

	private void enableOrDisable(boolean value) {
		plus1.setEnabled(value);
		plus100.setEnabled(value);
		start.setEnabled(value);
		stop.setEnabled(!value);
		reset.setEnabled(value);
	}

	public void simStarted() {
		enableOrDisable(false);
	}

	public void simStopped() {
		enableOrDisable(true);
	}
}
