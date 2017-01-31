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
import java.util.List;



import parkeersimulator.ParkingSpot;

import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class LineGraphView extends AbstractView {
	
	private int hour = 25;
	private int maxValue = model.getNumberOfOpenSpots();
	private int minValue = 0;
    private int padding = 25;
    private int labelPadding = 25;
    
    private Color hocCarLine = new Color(255, 0, 0);
    private Color passCarLine = new Color (0, 0, 255);
    private Color resCarLine = new Color (255, 255, 0);

    private static final Stroke GRAPH_STROKE = new BasicStroke(4f);

	List<Integer> hocCarData, passCarData, resCarData;  
	
	public LineGraphView(Model model) {
		super(model);
		hocCarData = new ArrayList<Integer>();
		passCarData = new ArrayList<Integer>();
		resCarData = new ArrayList<Integer>();
		
	}
		
    public void paintComponent(Graphics g) {
       	super.paintComponent(g);
       	Graphics2D g2 = (Graphics2D) g;
       	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       	

       	int hocCar = model.getCarCountForType(ParkingSpot.TYPE_AD_HOC);
       	int passCar = model.getCarCountForType(ParkingSpot.TYPE_PASS);
       	int resCar = model.getCarCountForType(ParkingSpot.TYPE_RES);
       	
       	
       	if (model.getHour() != hour) {
       		hocCarData.add(hocCar);
       		passCarData.add(passCar);
       		resCarData.add(resCar);
       		if (hocCarData.size() > 168) { 
       			hocCarData.remove(0);
       			passCarData.remove(0);
       			resCarData.remove(0);
       		}
       	}

       	hour = model.getHour();
       	int maxValueHoc = Collections.max(hocCarData);
       	int maxValuePass = Collections.max(passCarData);
       	int maxValueRes = Collections.max(resCarData);
       	
       	if (maxValueHoc > maxValuePass || maxValueHoc > maxValueRes) {
       		maxValue = maxValueHoc;
       	}
       	else if (maxValuePass > maxValueRes) {
       		maxValue = maxValuePass;
       	}
       	else {
       		maxValue = maxValueRes;
       	}
       	
		
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);

        
        drawGraph(g2, hocCarData, hocCarLine);
        drawGraph(g2, passCarData, passCarLine);
        drawGraph(g2, resCarData, resCarLine);
    }
        

	private void drawGraph(Graphics2D g2, List<Integer> list, Color lineColor) {
        
		double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (list.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxValue - minValue);
        	
        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((maxValue - list.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }
        
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
        
        g2.setColor(Color.BLACK);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

    }
    
}