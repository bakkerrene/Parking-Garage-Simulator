package parkeersimulator.car;


import java.awt.Color;
import java.util.Random;

import parkeersimulator.ParkingSpot;

/**
 * This class is used for holding information on the handicapped cars
 * 
 * @author Rick Zwaneveld
 * @version 07/02/2017
 */

public class HandiCar extends AbstractCar {
	
	private static final Color COLOR=Color.green;
    
	/**
	 * this method holds values that are needed for the handicapepd cars
	 */
	
	public HandiCar() {
    	super(ParkingSpot.TYPE_HANDI);
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setTotalMinutes(stayMinutes);
        this.setHasToPay(true);
    }
	
    /**
     * this method gets the color of the car
     */
    
	public Color getColor(){
    	return COLOR;
    }
}