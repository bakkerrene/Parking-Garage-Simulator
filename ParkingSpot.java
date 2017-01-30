package parkeersimulator;

/*
 * 
 */

import java.awt.Color;

import parkeersimulator.car.AbstractCar;

public class ParkingSpot {

    public static final int TYPE_AD_HOC = 0;
	public static final int TYPE_PASS = 1;
	public static final int TYPE_HANDI = 2;
	public static final int TYPE_RES = 3;
	public static final int TYPE_COUNT = 4;

	private int type;
	private Color color;
	private AbstractCar car;

	public ParkingSpot() {
	  car = null;
	}

	public static Color getColorForType(int type) {
    	switch (type) {
    	case TYPE_AD_HOC:
    		return Color.red;
    	case TYPE_PASS:
    		return Color.blue;
    	case TYPE_HANDI:
    		return Color.green;
    	case TYPE_RES:
    		return Color.yellow;
    	}
    	return Color.black;
	}

    public void setType(int type) {
    	color = getColorForType(type);
    	this.type = type;
    }
    public int getType() {
    	return type;
    }

	public void setCar(AbstractCar new_car) {
		car = new_car;
	}
    public AbstractCar getCar() {
    	return car;
    }

    public Color getColor() {
    	if (car != null) {
    		return color;
    	} else {
    		float c[] = color.getColorComponents(null);
    		return new Color(0.6f * c[0],
    						 0.6f * c[1],
    						 0.6f * c[2]);
    	}
    }
}