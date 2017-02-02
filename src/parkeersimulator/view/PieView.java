package parkeersimulator.view;


import java.awt.*;
import java.util.HashMap;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class PieView extends AbstractView {
	
	HashMap<String, Integer> carCounter;

	public PieView(Model model) {
		super(model);
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

		double total = whiteCount + redCount + blueCount + greenCount + yellowCount;
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
		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_AD_HOC));
		g.fillArc(10, 10, 180, 180, sum, redCount);
		sum += redCount;
		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_PASS));
		g.fillArc(10, 10, 180, 180, sum, blueCount);
		sum += blueCount;
		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_HANDI));
		g.fillArc(10, 10, 180, 180, sum, greenCount);
		sum += greenCount;
		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_RES));
		g.fillArc(10, 10, 180, 180, sum, yellowCount);
	}	
}