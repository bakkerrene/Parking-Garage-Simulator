package parkeersimulator;


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
    		return new Color(255, 255, 255, 255);
    	case TYPE_PASS:
    		return new Color(0, 0, 255, 64);
    	case TYPE_HANDI:
    		return new Color(0, 255, 0, 64);
    	case TYPE_RES:
    		return new Color(255, 255, 0, 64); 
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
    	return color;
    }
}