package parkeersimulator.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;


import parkeersimulator.model.Model;

/**
 * Draws the Graph in the model
 * 
 * @author Rene Bakker
 * @version 2017-02-06
 *
 */
@SuppressWarnings("serial")
public class LineGraphView extends AbstractView {
	
	private int hour;// = 25;
	private int maxValue;// = model.getNumberOfOpenSpots();
	private int minValue;// = 0;
    private int padding;// = 15;
    private int labelPadding;// = 15;

    private int diff;
    
    private JLabel lbl1;
    private JLabel lbl2;
    private JLabel lbl3;
    private JLabel lbl4;
    private JLabel lbl5;
    private JLabel lbl6;
    private JLabel lbl7;
    private JLabel lbl8;
    private JLabel lbl9;
    private JLabel lbl10;
    
    JLabel[] collection = {lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, lbl7, lbl8, lbl9, lbl10};
    HashMap<String, Integer> carCounter;
    
    private Color hocCarLine = new Color(255, 0, 0);
    private Color passCarLine = new Color(0, 0, 255);
    private Color resCarLine = new Color(255, 255, 0);
    private Color handiCarLine = new Color(0, 255, 0);

    private Color moneyLine = new Color(0, 255, 0);

    private static final Stroke GRAPH_STROKE = new BasicStroke(4f);
	final static String MONEY = "money";
	final static String CARS = "cars";

	List<Integer> hocCarData, passCarData, resCarData, handiCarData;  
	List<Integer> moneyPerHourData;

	private void init()
	{
		hocCarData = new ArrayList<Integer>();
		passCarData = new ArrayList<Integer>();
		resCarData = new ArrayList<Integer>();
		handiCarData = new ArrayList<Integer>();
		moneyPerHourData = new ArrayList<Integer>();

		hour = 25;
		maxValue = model.getNumberOfOpenSpots();
		minValue = 0;
	    padding = 15;
	    labelPadding = 15;

		//diff = (getHeight() - padding - labelPadding + 10) / 10;
		diff = 18;
	}

	/**
	 * 
	 * @param model This is the model
	 */
	public LineGraphView(Model model) {
		super(model);
		init();
		setLayout(null);
		for(int i = 0; i < collection.length; i++) {
			collection[i] = new JLabel("0");
			add(collection[i]);
			collection[i].setBounds(5, 0 + (i * diff), 35, 30 );
		}
	}

    public void paintComponent(Graphics g) {
    	if(!model.isInSim()) {
    		init();
    	}
       	super.paintComponent(g);
       	Graphics2D g2 = (Graphics2D) g;
       	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       	carCounter = model.getTotalCars();
       	String type_Graph = model.getGraphButtonInput();
              	       	
       	int hocCar = carCounter.get("adhoc");
       	int passCar = carCounter.get("pass");
       	int resCar = carCounter.get("res");
       	int handiCar = carCounter.get("handi");
       	
       	int moneyLastHour = model.getMoneyLastHour();
       	
       	if (model.getHour() != hour || hocCarData.size() == 0) {
       		hocCarData.add(hocCar);
       		passCarData.add(passCar);
       		resCarData.add(resCar);
       		handiCarData.add(handiCar);
       		moneyPerHourData.add(moneyLastHour);
       		if (hocCarData.size() > 168) { 
       			hocCarData.remove(0);
       			passCarData.remove(0);
       			resCarData.remove(0);
       			handiCarData.remove(0);
       			moneyPerHourData.remove(0);
       		}
       	}
       	hour = model.getHour();
       		
       	if (type_Graph.equals(CARS)) {
       		int maxValueHoc = Collections.max(hocCarData);
       		int maxValuePass = Collections.max(passCarData);
       		int maxValueRes = Collections.max(resCarData);
       		int maxValueHandi = Collections.max(handiCarData);
       	
       		if (maxValueHoc > maxValuePass && maxValueHoc > maxValueRes && maxValueHoc > maxValueHandi) {
       			maxValue = maxValueHoc;
       		}
       		else if (maxValuePass > maxValueRes && maxValuePass > maxValueHandi) {
       			maxValue = maxValuePass;
       		}
       		else if (maxValueRes > maxValueHandi) {
       			maxValue = maxValueRes;
       		}
       		else {
       			maxValue = maxValueHandi;
       		}
       		
       		drawGraph(g2, hocCarData, hocCarLine);
       		drawGraph(g2, passCarData, passCarLine);
       		drawGraph(g2, resCarData, resCarLine);
       		drawGraph(g2, handiCarData, handiCarLine);
       	}
       	else if(type_Graph.equals(MONEY)) {
       		if (model.getHour() != hour || moneyPerHourData.size() == 0) {
       			if (moneyPerHourData.size() > 168) {
       			}
       		}
       		hour = model.getHour();
       		maxValue = Collections.max(moneyPerHourData);
       		drawGraph(g2, moneyPerHourData, moneyLine);
       	}

        g2.setColor(Color.BLACK);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
        
        diff = (getHeight() - padding - labelPadding + 10) / 10;
        
        for (int i = 0; i < 10; i++) {
            g2.drawLine(padding + labelPadding - 10 , ((getHeight() - padding - labelPadding) - diff * i), getWidth() - padding ,((getHeight() - padding - labelPadding) - diff * i));
            if (i != 9) {
            int text = maxValue - (i * (maxValue / 9)); 
            collection[i].setText(""+ text);
            }
            
        }
        
        for (int i = 0; i <8; i++) {
        	 g2.drawLine(padding + labelPadding + (i * (8+(getWidth() / 8))), getHeight() - padding - labelPadding, padding + labelPadding  + (i * (8+(getWidth() / 8))), padding);
        }
    }

    
    /**
     * Draws the graph
     * @param g2 This is a {@link Graphics2D} Method from the java.awt package
     * @param list This is a list with data from the paintComponent method
     * @param lineColor This is the color that will be used to draw the graph
     */
	private void drawGraph(Graphics2D g2, List<Integer> list, Color lineColor) {
        
		double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (list.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxValue - minValue);
        	
        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((maxValue - list.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }
        
        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
        
        g2.setStroke(oldStroke);
        g2.setColor(lineColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - 4 / 2;
            int y = graphPoints.get(i).y - 4 / 2;
            int ovalW = 4;
            int ovalH = 4;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }
}
