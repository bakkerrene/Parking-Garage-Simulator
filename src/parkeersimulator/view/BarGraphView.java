package parkeersimulator.view;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;
import java.util.List;

import javax.swing.JLabel;

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
	private int totalPlaces = 0;
	private HashMap<String, Integer> carCounter;
	
    private JLabel xLbl1;
    private JLabel xLbl2;
    private JLabel xLbl3;
    private JLabel xLbl4;
    private JLabel xLbl5;
    private JLabel xLbl6;
    private JLabel xLbl7;
    private JLabel xLbl8;
    private JLabel xLbl9;
    private JLabel xLbl10;
    
    private JLabel yLbl1;
    private JLabel yLbl2;
    private JLabel yLbl3;
    private JLabel yLbl4;
    private JLabel yLbl5;
    private JLabel yLbl6;
    private JLabel yLbl7;
    
    JLabel[] valueCollection = {xLbl1, xLbl2, xLbl3, xLbl4, xLbl5, xLbl6, xLbl7, xLbl8, xLbl9, xLbl10};
    JLabel[] dayCollection = {yLbl1, yLbl2, yLbl3, yLbl4, yLbl5, yLbl6, yLbl7};
    String[] dayString = {"Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"};
    
    private int diff;
	
    private Color hocCarColor = new Color(255, 0, 0);
    private Color passCarColor = new Color(0, 0, 255);
    private Color resCarColor = new Color(255, 255, 0);
    private Color handiCarColor = new Color(0, 255, 0);
    
    private int padding= 15;
    private int labelPadding = 15;
    
    List<HashMap<String, Integer>> totalCarPerDay;
	/**
	 * 
	 * @param model This is the model
	 */
	public BarGraphView(Model model) {
		super(model);
		totalPlaces = model.getTotalSpotCount();
		
		totalCarPerDay = new ArrayList<HashMap<String, Integer>>();
		diff = 19;
		setLayout(null);
		for(int i = 0; i < valueCollection.length; i++) {
			valueCollection[i] = new JLabel("0");
			add(valueCollection[i]);
			valueCollection[i].setBounds(0, 6 + (i * diff), 30, 10 );
		}
		
		for(int i = 0; i < dayCollection.length; i++) {
			int day = 6-i;
			dayCollection[i] = new JLabel(dayString[day]);
			add(dayCollection[i]);
			dayCollection[i].setBounds(25 + padding + labelPadding + ((800 / 7 - 7) * i), 190, 70, 15);
		}
		
		for(int i = 0; i < 7; i++ ) {
			totalCarPerDay.add(model.getTotalCarsPerDay());
		}
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
		
		
        g.setColor(Color.BLACK);
        g.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
        
        diff = (getHeight() - padding - labelPadding + 10) / 10;
        
        for (int ix = 0; ix < 10; ix++) {
            g.drawLine(padding + labelPadding - 10 , ((getHeight() - padding - labelPadding) - diff * ix), getWidth() - padding ,((getHeight() - padding - labelPadding) - diff * ix));
            if (ix != 9) {
            int text = totalPlaces - (ix * (totalPlaces / 9)); 
            valueCollection[ix].setText(""+ text);
            }
            
        }
        int dayInt = model.getDayInt();
        for (int ix = 0; ix <8; ix++) {
       	 g.drawLine(padding + labelPadding + (ix * (8+(getWidth() / 8))), getHeight() - padding - labelPadding, padding + labelPadding  + (ix * (8+(getWidth() / 8))), padding);
       	 if(ix <=6) {
       	 	int day = dayInt + ix;
       	 	if (day > 6) day -= 7; 
       	 	dayCollection[ix].setText(dayString[day]);
       	 }
       }

		
		
	}
		
		private void drawBars(Graphics g, List<HashMap<String, Integer>> totalCarPerDay) {
		totalPlaces = 540;
		double height = getHeight() - padding - labelPadding;
		for (int i=0; i< totalCarPerDay.size(); i++) {
			carCounter = totalCarPerDay.get(i);	
			
		int redCount = carCounter.get("adhoc");
		int blueCount = carCounter.get("pass");
		int greenCount = carCounter.get("handi");
		int yellowCount = carCounter.get("res");
		
		int checkTotalPlaceMaxValue = redCount + blueCount + greenCount + yellowCount;
		if (checkTotalPlaceMaxValue > totalPlaces) {
			totalPlaces = checkTotalPlaceMaxValue;
		}
		
		double sizePerValue = height / totalPlaces;
		redCount *= sizePerValue;
		blueCount *= sizePerValue;
		greenCount *= sizePerValue;
		yellowCount *= sizePerValue;

		int topNonFreeRed = (int) (height - (redCount));
		int topNonFreeBlue = (int) (height - (blueCount));
		int topNonFreeGreen = (int) (height - (greenCount));
		int topNonFreeYellow = (int) (height - (yellowCount));

		int sum = padding + labelPadding + (i * ((getWidth() - padding - labelPadding - 10) / 7));
		g.setColor(hocCarColor);
		g.fillRect(sum +15, topNonFreeRed, 20, redCount);

		g.setColor(passCarColor);
		g.fillRect(sum +35, topNonFreeBlue, 20, blueCount);

		g.setColor(resCarColor);
		g.fillRect(sum +55, topNonFreeYellow, 20, yellowCount);

		g.setColor(handiCarColor);
		g.fillRect(sum +75, topNonFreeGreen, 20, greenCount);
		
		
		
			}
		}
		
}