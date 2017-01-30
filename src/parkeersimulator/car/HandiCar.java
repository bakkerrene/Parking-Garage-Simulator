package parkeersimulator.car;


import java.awt.Color;
import java.util.Random;



public class HandiCar extends AbstractCar {
	private static final Color COLOR=Color.green;
    public HandiCar() {
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