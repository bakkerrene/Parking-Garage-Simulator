
package parkeersimulator.controller;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import parkeersimulator.model.Model;

/**
 * This class adds two radio buttons to the LineGraphView tab in the model
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 *
 */
@SuppressWarnings("serial")
public class GraphController extends AbstractController implements ActionListener {

	final static String MONEY = "money";
	final static String CARS = "cars";

	private JRadioButton showCars;
	private JRadioButton showMoney;
	private ButtonGroup buttonGroup;

	/**
	 * 
	 * @param model This is the model
	 */
    public GraphController(Model model) {

    	super(model);
    	GridLayout layout = new GridLayout();
    	this.setLayout(layout);

    	showMoney = new JRadioButton("Geld");
    	showMoney.setHorizontalAlignment(JRadioButton.LEFT);
    	showMoney.setActionCommand(MONEY);
    	showMoney.addActionListener(this);

    	showCars = new JRadioButton("Auto's");
    	showCars.setHorizontalAlignment(JRadioButton.LEFT);
    	showCars.setActionCommand(CARS);
    	showCars.addActionListener(this);

    	buttonGroup = new ButtonGroup();
    	buttonGroup.add(showMoney);
    	buttonGroup.add(showCars);

    	buttonGroup.clearSelection();
    	showCars.setSelected(true);

    	add(showMoney);
    	add(showCars);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String string = e.getActionCommand();
		if (string.equals(MONEY)) {
			model.setGraphButtonInput(MONEY);
		}
		else if (string.equals(CARS)) {
			model.setGraphButtonInput(CARS);
		}
		revalidate();
	}
}
