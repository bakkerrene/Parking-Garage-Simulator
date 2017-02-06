package parkeersimulator.view;


import java.awt.*;
import java.util.HashMap;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;


/**
 * this Class Draws the pieChart in the view
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 *
 */
@SuppressWarnings("serial")
public class PieView extends AbstractView {
	
	private HashMap<String, Integer> carCounter;
	private int total;
	
    private Color hocCarColor = new Color(255, 0, 0);
    private Color passCarColor = new Color(0, 0, 255);
    private Color resCarColor = new Color(255, 255, 0);
    private Color handiCarColor = new Color(0, 255, 0);
	
	public PieView(Model model) {
		
		super(model);
		total = model.getNumberOfOpenSpots();
	}

	
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		carCounter = model.getTotalCars();
		
		int whiteCount = model.getNumberOfOpenSpots();

		int redCount = carCounter.get("adhoc");
		int blueCount = carCounter.get("pass");
		int greenCount = carCounter.get("handi");
		int yellowCount = carCounter.get("res");

		if ((redCount == 0) && (blueCount == 0) && (greenCount == 0) && (yellowCount == 0)) {
			whiteCount = 0;
			redCount++;
			blueCount++;
			greenCount++;
			yellowCount++;
		}

		double percent = 360.0 / total;

		whiteCount = (int) (percent * whiteCount);
		redCount = (int) (percent * redCount);
		blueCount = (int) (percent * blueCount);
		greenCount = (int) (percent * greenCount);
		yellowCount = (int) (percent * yellowCount);

		g.setColor(Color.YELLOW);
		g.fillArc(10, 10, 180, 180, 0, 360);
		int sum = 90;
		g.setColor(Color.WHITE);
		g.fillArc(10, 10, 180, 180, sum, whiteCount);
		sum += whiteCount;
		g.setColor(hocCarColor);
		g.fillArc(10, 10, 180, 180, sum, redCount);
		sum += redCount;
		g.setColor(passCarColor);
		g.fillArc(10, 10, 180, 180, sum, blueCount);
		sum += blueCount;
		g.setColor(handiCarColor);
		g.fillArc(10, 10, 180, 180, sum, greenCount);
		sum += greenCount;
		g.setColor(resCarColor);
		g.fillArc(10, 10, 180, 180, sum, yellowCount);
	}	
}