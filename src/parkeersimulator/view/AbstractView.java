package parkeersimulator.view;

import javax.swing.JPanel;

import parkeersimulator.model.Model;


/**
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 * 
 *
 */

@SuppressWarnings("serial")
public abstract class AbstractView extends JPanel {

	protected Model model;

	/**
	 * 
	 * @param model This is the model
	 */
	public AbstractView(Model model) {
		this.model = model;
		model.addView(this);
	}

	/**
	 * 
	 * @param model This is the model
	 */
	public void setModel(Model model) {
		this.model=model;
	}

	/**
	 * 
	 * @return model This returns the model so it can be used in the subclasses
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Updates the Views
	 */
    public void updateView() {
    	repaint();
    }
}
