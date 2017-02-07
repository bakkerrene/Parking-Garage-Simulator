package parkeersimulator.view;


import java.awt.*;
import java.util.HashMap;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

/** 
 * Draws the barGraphView in the model
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 *
 */

@SuppressWarnings("serial")
public class BarGraphView extends AbstractView {

	private int totalPlaces;
	private HashMap<String, Integer> carCounter;

	/**
	 * 
	 * @param model This is the model
	 */
	public BarGraphView(Model model) {
		super(model);
		totalPlaces = model.getTotalSpotCount();
	}
	
	/**
	 * paintComponent draws the view
	 * 
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		carCounter = model.getTotalCars();
		double height = getHeight();
		double sizePerValue = height / totalPlaces;
		
		int x = model.getNumberOfOpenSpots();

		int y = totalPlaces - x;
		

		int freeSpots = (int) ((height / totalPlaces) * x);
		int topFree = (int) (height - freeSpots);

		int nonFreeSpots = (int) ((height / totalPlaces) * y);

		int redCount = carCounter.get("adhoc");
		int blueCount = carCounter.get("pass");
		int greenCount = carCounter.get("handi");
		int yellowCount = carCounter.get("res");
		
		System.out.println(sizePerValue);
		redCount *= sizePerValue;
		System.out.println(redCount);
		blueCount *= sizePerValue;
		System.out.println(blueCount);
		greenCount *= sizePerValue;
		System.out.println(greenCount);
		yellowCount *= sizePerValue;
		System.out.println(yellowCount);

		int topNonFreeRed = (int) (height - (redCount));
		int topNonFreeBlue = (int) (height - (blueCount));
		int topNonFreeGreen = (int) (height - (greenCount));
		int topNonFreeYellow = (int) (height - (yellowCount));

		g.setColor(Color.WHITE);
		g.fillRect(20, topFree, 20, freeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_AD_HOC));
		g.fillRect(60, topNonFreeRed, 20, redCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_PASS));
		g.fillRect(100, topNonFreeBlue, 20, blueCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_HANDI));
		g.fillRect(140, topNonFreeGreen, 20, greenCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_RES));
		g.fillRect(180, topNonFreeYellow, 20, yellowCount);
	}	
}