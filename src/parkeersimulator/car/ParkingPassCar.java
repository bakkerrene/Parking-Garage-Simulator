
package parkeersimulator.car;

import java.awt.Color;
import java.util.Random;

import parkeersimulator.ParkingSpot;

public class ParkingPassCar extends AbstractCar {
	private static final Color COLOR=Color.blue;
	public ParkingPassCar() {
		this.type = ParkingSpot.TYPE_PASS;
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    public Color getColor(){
    	return COLOR;
    }
}
