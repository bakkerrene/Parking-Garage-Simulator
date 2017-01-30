package parkeersimulator.view;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public abstract class AbstractView extends JPanel {

	protected Model model;
	protected List<AbstractController> controllers;

	public AbstractView(Model model) {
		this.model = model;
		model.addView(this);
		controllers = new ArrayList<AbstractController>();
	}

	public void setModel(Model model) {
		this.model=model;
	}

	public Model getModel() {
		return model;
	}

    public void updateView() {
    	repaint();
    }

    public void addController(AbstractController controller) {
    	controllers.add(controller);
    }
}