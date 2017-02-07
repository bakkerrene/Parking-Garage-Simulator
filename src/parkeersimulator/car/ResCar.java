package parkeersimulator.car;


import java.awt.Color;
import java.util.Random;

import parkeersimulator.ParkingSpot;

/**
 * This class is used for holding information on the reservation cars
 * 
 * @author Rick Zwaneveld
 * @version 07/02/2017
 */

public class ResCar extends AbstractCar {
	
	private static final Color COLOR=Color.yellow;
    
	/**
	 * this method holds values that are needed for the ad hoc cars
	 */
	
	public ResCar() {
    	super(ParkingSpot.TYPE_RES);
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);

    }
    
	public Color getColor(){
    	return COLOR;
    }
}