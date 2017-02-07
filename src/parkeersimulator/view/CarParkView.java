
package parkeersimulator.view;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.Rect;
import parkeersimulator.car.AbstractCar;
import parkeersimulator.model.Model;

/**
 * 
 * @author <<<
 * @version 2017-02-06
 *
 */
@SuppressWarnings("serial")
public class CarParkView extends AbstractView {
	Color color = new Color(0,0,0);

	private Rect getRectForLocation(Location location, boolean breed) {
    	int x = 40 + 260 * location.getFloor() + 75 * (int)Math.floor(0.5 * location.getRow()) + 20 * (location.getRow() % 2);
    	int y = 10 + 11 * location.getPlace();
    	int w = 20-1;
    	int h = 10-1;
    	if(breed) {
			w += 10;
    		if(location.getRow() % 2 == 0) {
    			x -= 10;
    		}
    	}
    	return new Rect(x, y, w, h);
	}

	private Location getLocationForPoint(int x, int y) {
        for(int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for(int row = 0; row < model.getNumberOfRows(); row++) {
                for(int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                	ParkingSpot spot = model.getParkingSpotAt(location);
                    boolean breed = (spot.getType() == ParkingSpot.TYPE_HANDI);
                    Rect rect = getRectForLocation(location, breed);
                    if(rect.x <= x && rect.y <= y && x < rect.x+rect.w && y < rect.y+rect.h)
                    	return location;
                }
            }
        }
        return null;
	}

	public CarParkView(Model model) {

		super(model);

	    addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	    		Location location = getLocationForPoint(e.getX(), e.getY());
	    		if(location != null) {
	    			model.clickedSpot(location);
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
                	ParkingSpot spot = model.getParkingSpotAt(location);
                    if (car == null) {
                    	color = spot.getColor();
                    }
                    else {
                    	color = car.getColor();
                    }
                    boolean breed = (spot.getType() == ParkingSpot.TYPE_HANDI);
                    drawPlace(graphics, location, color, breed);
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

    private void drawPlace(Graphics graphics, Location location, Color color, boolean breed) {
    	Rect rect = getRectForLocation(location, breed);
        graphics.setColor(color);
        graphics.fillRect(rect.x, rect.y, rect.w, rect.h);
    }
}
