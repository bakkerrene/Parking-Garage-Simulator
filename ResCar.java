package parkeersimulator.car;


import java.awt.Color;
import java.util.Random;

public class ResCar extends AbstractCar {
	private static final Color COLOR=Color.yellow;
    public ResCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    public Color getColor(){
    	return COLOR;
    }
}