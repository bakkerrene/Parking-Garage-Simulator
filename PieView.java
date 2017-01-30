package parkeersimulator.view;


import java.awt.*;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class PieView extends AbstractView {

	public PieView(Model model) {
		super(model);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		int whiteCount = model.getNumberOfOpenSpots();

		int redCount = model.getCarCountForType(ParkingSpot.TYPE_AD_HOC);
		int blueCount = model.getCarCountForType(ParkingSpot.TYPE_PASS);
		int greenCount = model.getCarCountForType(ParkingSpot.TYPE_HANDI);
		int yellowCount = model.getCarCountForType(ParkingSpot.TYPE_RES);

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
		g.fillArc(10, 10, 180, 180, 90 + whiteCount + redCount + blueCount + 0, 360);

		g.setColor(Color.WHITE);
		g.fillArc(10, 10, 180, 180, 90, whiteCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_AD_HOC));
		g.fillArc(10, 10, 180, 180, 90 + whiteCount, redCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_PASS));
		g.fillArc(10, 10, 180, 180, 90 + whiteCount + redCount, blueCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_HANDI));
		g.fillArc(10, 10, 180, 180, 90 + whiteCount + redCount + blueCount, greenCount);

		g.setColor(ParkingSpot.getColorForType(ParkingSpot.TYPE_RES));
		g.fillArc(10, 10, 180, 180, 90 + whiteCount + redCount + blueCount + greenCount, yellowCount);
	}	
}