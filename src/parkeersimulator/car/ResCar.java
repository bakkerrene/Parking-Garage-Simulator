
package parkeersimulator.car;

import java.awt.Color;
import java.util.Random;

import parkeersimulator.ParkingSpot;

public class ResCar extends AbstractCar {
	private static final Color COLOR=Color.yellow;
    public ResCar() {
    	super(ParkingSpot.TYPE_RES);
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
        //random nr tussen 0 en 23 ran
    }
    public Color getColor(){
    	return COLOR;
    }
}
