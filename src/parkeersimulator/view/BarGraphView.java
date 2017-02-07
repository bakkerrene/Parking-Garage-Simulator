package parkeersimulator.view;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;
import java.util.List;

/** 
 * Draws the barGraphView in the model
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 *
 */

@SuppressWarnings("serial")
public class BarGraphView extends AbstractView {

	private int day = 8;
	private int totalPlaces;
	private HashMap<String, Integer> carCounter;
	
    private Color hocCarColor = new Color(255, 0, 0);
    private Color passCarColor = new Color(0, 0, 255);
    private Color resCarColor = new Color(255, 255, 0);
    private Color handiCarColor = new Color(0, 255, 0);
    
    List<HashMap<String, Integer>> totalCarPerDay;
	/**
	 * 
	 * @param model This is the model
	 */
	public BarGraphView(Model model) {
		super(model);
		totalPlaces = model.getTotalSpotCount();
		
		totalCarPerDay = new ArrayList<HashMap<String, Integer>>();
	}
	
	/**
	 * paintComponent draws the view
	 * 
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		if(day != model.getDayInt() || totalCarPerDay.size() == 0){
			totalCarPerDay.add(model.getTotalCarsPerDay());
			if (totalCarPerDay.size() > 7) {
				totalCarPerDay.remove(0);
			}
		}
		day = model.getDayInt();
		drawBars(g, totalCarPerDay);
	}
		
		private void drawBars(Graphics g, List<HashMap<String, Integer>> totalCarPerDay) {
		
		double height = getHeight();
		
		for (int i=0; i< totalCarPerDay.size(); i++) {
			carCounter = totalCarPerDay.get(i);	
			
		int redCount = carCounter.get("adhoc");
		int blueCount = carCounter.get("pass");
		int greenCount = carCounter.get("handi");
		int yellowCount = carCounter.get("res");
		
		
		double sizePerValue = height / (redCount + blueCount + greenCount + yellowCount);
		redCount *= sizePerValue;
		blueCount *= sizePerValue;
		greenCount *= sizePerValue;
		yellowCount *= sizePerValue;

		int topNonFreeRed = (int) (height - (redCount));
		int topNonFreeBlue = (int) (height - (blueCount));
		int topNonFreeGreen = (int) (height - (greenCount));
		int topNonFreeYellow = (int) (height - (yellowCount));

		int sum = i * 100;
		g.setColor(hocCarColor);
		g.fillRect(sum +20, topNonFreeRed, 20, redCount);

		g.setColor(passCarColor);
		g.fillRect(sum +40, topNonFreeBlue, 20, blueCount);

		g.setColor(resCarColor);
		g.fillRect(sum +60, topNonFreeYellow, 20, yellowCount);

		g.setColor(handiCarColor);
		g.fillRect(sum +80, topNonFreeGreen, 20, greenCount);
			}
		}
		
}