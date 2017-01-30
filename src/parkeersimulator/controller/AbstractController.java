package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.model.Model;

public abstract class AbstractController extends JPanel {

	protected Model model;

	public AbstractController(Model model) {
		this.model = model;
	}
	
	public Model getModel() {
		return model;
	}

	public abstract void clickedSpot(Location location);
}