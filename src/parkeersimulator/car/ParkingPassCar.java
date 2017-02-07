package parkeersimulator.car;


import java.awt.Color;
import java.util.Random;

import parkeersimulator.ParkingSpot;

/**
 * This class is used for holding information on the parking pass cars
 * 
 * @author Rick Zwaneveld
 * @version 07/02/2017
 */

public class ParkingPassCar extends AbstractCar {
	
	private static final Color COLOR=Color.blue;
	
	/**
	 * this method holds values that are needed for the parking pass cars
	 */
	
	public ParkingPassCar() {
		super(ParkingSpot.TYPE_PASS);
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    
    /**
     * this method gets the color of the car
     */
	
	public Color getColor(){
    	return COLOR;
    }
}