package parkeersimulator;
 
/*
 * l
 * 
 */
import java.util.LinkedList;
import java.util.Queue;

import parkeersimulator.car.AbstractCar;

public class CarQueue {

    private Queue<AbstractCar> queue = new LinkedList<>();

    public void clear() {
    	queue.clear();
    }

    public boolean addCar(AbstractCar car) {
        return queue.add(car);
    }

    public AbstractCar removeCar() {
        return queue.poll();
    }

    public AbstractCar peekCar(int i) {
        return ((LinkedList<AbstractCar>) queue).get(i);
    }

    public int carsInQueue(){
    	return queue.size();
    }
}