
package parkeersimulator.model;

import java.util.*;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.view.AbstractView;

public abstract class AbstractModel {

	private List<AbstractView> views;
	protected List<AbstractController> controllers;

	public AbstractModel() {
		views = new ArrayList<AbstractView>();
		controllers = new ArrayList<AbstractController>();
	}
	
	public void addView(AbstractView view) {
		views.add(view);
	}

    public void addController(AbstractController controller) {
    	controllers.add(controller);
    }

	public void notifyViews() {
		for(AbstractView v: views) v.updateView();
	}
}
