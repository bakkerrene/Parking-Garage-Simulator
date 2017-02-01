
package parkeersimulator.car;

import java.awt.Color;
import java.util.Random;

import parkeersimulator.ParkingSpot;

public class AdHocCar extends AbstractCar {
	
	private static final Color COLOR=Color.red;
	
    public AdHocCar() {
    	this.type = ParkingSpot.TYPE_AD_HOC;
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setTotalMinutes(stayMinutes);
        this.setHasToPay(true);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
