package parkeersimulator.view;


import java.awt.*;
import java.util.HashMap;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class BarGraphView extends AbstractView {

	private int totalPlaces;
	HashMap<String, Integer> carCounter;

	public BarGraphView(Model model) {
		super(model);
		totalPlaces = model.getNumberOfOpenSpots();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		carCounter = model.getTotalCars();
		
		int x = model.getNumberOfOpenSpots();

		int y = totalPlaces - x;
		double height = getHeight();

		int freeSpots = (int) ((height / totalPlaces) * x);
		int topFree = (int) (height - freeSpots);

		int nonFreeSpots = (int) ((height / totalPlaces) * y);


		int redCount = carCounter.get("adhoc");
		int blueCount = carCounter.get("pass");
		int greenCount = carCounter.get("handi");
		int yellowCount = carCounter.get("res");

		double topNonFreeRed = (height - redCount);
		int topNonFreeBlue = (int) (height - blueCount);
		int topNonFreeGreen = (int) (height - greenCount);
		int topNonFreeYellow = (int) (height - yellowCount);

		g.setColor(Color.WHITE);
		g.fillRect(20, topFree, 20, freeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_AD_HOC));
		g.fillRect(60, (int) topNonFreeRed, 20, nonFreeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_PASS));
		g.fillRect(100, topNonFreeBlue, 20, nonFreeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_HANDI));
		g.fillRect(140, topNonFreeGreen, 20, nonFreeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_RES));
		g.fillRect(180, topNonFreeYellow, 20, nonFreeSpots);
	}	
}