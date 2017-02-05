package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.model.Model;

public abstract class AbstractController extends JPanel {

	protected Model model;

	public AbstractController(Model model) {
		this.model = model;
		model.addController(this);
	}

	public void simStarted() {}
	public void simStopped() {}
	public void runStarted() {}
	public void runStopped() {}
	public void spotsChanged() {}
}
