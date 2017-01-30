package parkeersimulator.view;


import java.awt.*;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class BarGraphView extends AbstractView {

	private int totalPlaces;

	public BarGraphView(Model model) {
		super(model);
		totalPlaces = model.getNumberOfOpenSpots();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		int x = model.getNumberOfOpenSpots();

		int y = totalPlaces - x;
		double height = 400.0;

		int freeSpots = (int) ((height / totalPlaces) * x);
		int topFree = (int) (height - freeSpots);

		int nonFreeSpots = (int) ((height / totalPlaces) * y);

		int redSize = (int) ((height / totalPlaces) * y);

		int redCount = model.getCarCountForType(ParkingSpot.TYPE_AD_HOC);
		int blueCount = model.getCarCountForType(ParkingSpot.TYPE_PASS);
		int greenCount = model.getCarCountForType(ParkingSpot.TYPE_HANDI);
		int yellowCount = model.getCarCountForType(ParkingSpot.TYPE_RES);

		int topNonFreeRed = (int) (height - redCount);
		int topNonFreeBlue = (int) (height - blueCount);
		int topNonFreeGreen = (int) (height - greenCount);
		int topNonFreeYellow = (int) (height - yellowCount);

		g.setColor(Color.WHITE);
		g.fillRect(20, topFree, 20, freeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_AD_HOC));
		g.fillRect(60, topNonFreeRed, 20, redSize + 100);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_PASS));
		g.fillRect(100, topNonFreeBlue, 20, nonFreeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_HANDI));
		g.fillRect(140, topNonFreeGreen, 20, nonFreeSpots);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_RES));
		g.fillRect(180, topNonFreeYellow, 20, nonFreeSpots);
	}	
}