package parkeersimulator.view;


import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.car.AbstractCar;
import parkeersimulator.controller.AbstractController;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class CarParkView extends AbstractView {
	Color color = new Color(0,0,0);

	public CarParkView(Model model) {

		super(model);

	    addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	        	/*
				if (e.getButton() == MouseEvent.NOBUTTON) {
				} else if (e.getButton() == MouseEvent.BUTTON1) {
				} else if (e.getButton() == MouseEvent.BUTTON2) {
				} else if (e.getButton() == MouseEvent.BUTTON3) {
				}
				*/
				Location location = getLocationForPoint(e.getX(), e.getY());
				if (location != null) {
					for(AbstractController c: controllers) c.clickedSpot(location);
				}
			}
		});
	}

    public void paintComponent(Graphics g) {

    	super.paintComponent(g);

    	Dimension dim = model.getSize();

        if (model.getCarParkImage() == null) {
        	Image carParkImage = createImage(dim.width, dim.height);
        	model.setCarParkImage(carParkImage);
        }
        // Create a new car park image if the size has changed.
        if (!model.getSize().equals(getSize())) {
        	model.setSize(model.getSize());
            Image carParkImage = createImage(dim.width, dim.height);
            model.setCarParkImage(carParkImage);
        }

        Graphics graphics = model.getCarParkImage().getGraphics();
        graphics.clearRect(0, 0, dim.width, dim.height);
        for(int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for(int row = 0; row < model.getNumberOfRows(); row++) {
                for(int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    AbstractCar car = model.getCarAt(location);
                    if (car == null) {
                    	ParkingSpot spot = model.getParkingSpotAt(location);
                    	color = spot.getColor();
                    }
                    else {
                    	color = car.getColor();
                    }
                    drawPlace(graphics, location, color);
                }
            }
        }

        Dimension currentSize = getSize();
        if (model.getSize().equals(currentSize)) {
            g.drawImage(model.getCarParkImage(), 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(model.getCarParkImage(), 0, 0, currentSize.width, currentSize.height, null);
        }

        g.dispose();
    }

    private Location getLocationForPoint(int x, int y) {
    	int floor = (x - 40) / 260;
    	int floorX = (x - 40) % 260;
    	int block = floorX / 75; 
    	int blockX = floorX % 75;
    	int row = 2 * block + (blockX / 20);
    	int place = (y - 10) / 11;
    	Location location = new Location(floor, row, place);
    	if(getModel().locationIsValid(location))
    	{
    		int rectX0 = 40 + 260 * floor + 75 * (int)Math.floor(0.5 * row) + 20 * (row % 2);
    		int rectY0 = 10 + 11 * place;
    		int rectX1 = rectX0 + 20 - 1;
    		int rectY1 = rectY0 + 10 - 1;
    		if (rectX0 <= x && rectY0 <= y && x < rectX1 && y < rectY1)
    			return location;
    	}
    	return null;
    }

    private void drawPlace(Graphics graphics, Location location, Color color) {
        graphics.setColor(color);
        graphics.fillRect(
                40 + 260 * location.getFloor() + 75 * (int)Math.floor(0.5 * location.getRow()) + 20 * (location.getRow() % 2),
                10 + 11 * location.getPlace(),
                20 - 1,
                10 - 1); // TODO use dynamic size or constants
    }

}