package parkeersimulator;
 

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
    	if(((LinkedList<AbstractCar>) queue).get(i) != null) {
        return ((LinkedList<AbstractCar>) queue).get(i); //<--- niet echt optimaal met hoge snelheid...
    	} 
    	return null;
    }

    public int carsInQueue(){
    	return queue.size();
    }
}