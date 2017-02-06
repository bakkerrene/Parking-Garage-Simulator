
package parkeersimulator.controller;

import java.awt.GridLayout;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import parkeersimulator.model.Model;

/**
 * this class will mutate the tickPause in the class Model
 * 
 * @author Rene Bakker
 * @Version 2017-02-06
 *
 */
@SuppressWarnings("serial")
public class SlideController extends AbstractController{

	static final int minTick = 1;
	static final int maxTick = 999;
	private int getter = (1000 - model.getTickPause());
	private int InitTick = getter;
	
	private JSlider tickRate;
	private Hashtable<Integer, JLabel> labels;
	
	/**
	 * 
	 * @param model This is the model
	 * 
	 */
    public SlideController(Model model) {
    	super(model);

    	tickRate = new JSlider(JSlider.HORIZONTAL, minTick, maxTick,  InitTick);
    	tickRate.addChangeListener(e -> {
    		model.setTickPause(1000 - (tickRate.getValue()));
    		revalidate();
    	});

    	tickRate.setMajorTickSpacing(998);
    	tickRate.setPaintTicks(true);

    	labels = new Hashtable<Integer, JLabel>();
    	labels.put(new Integer (1), new JLabel("Langzaam"));
    	labels.put(new Integer (999), new JLabel("Snel"));
    	
    	tickRate.setLabelTable(labels);
    	tickRate.setPaintLabels(true);
    	

        setLayout(new GridLayout(2, 1));
        add(new JLabel("Snelheid", SwingConstants.CENTER));
        add(tickRate);
    }
}
