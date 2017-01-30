package parkeersimulator.car;


import java.awt.Color;

import parkeersimulator.Location;

public abstract class AbstractCar {

    private Location location;
    private int minutesLeft;
    private int totalMinutes = 0;
    private boolean isPaying;
    private boolean hasToPay;

    /**
     * Constructor for objects of class Car
     */
    public AbstractCar() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }
    
    public void setTotalMinutes(int totalMinutes) {
    	this.totalMinutes = totalMinutes;
    }
    
    public int getTotalMinutes() {
    	return totalMinutes;
    }
            	
    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
    
    public boolean getIsPaying() {
        return isPaying;
    }

    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    public boolean getHasToPay() {
        return hasToPay;
    }

    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }

    public void tick() {
        minutesLeft--;
        totalMinutes++;
    }
    
    public abstract Color getColor();
}