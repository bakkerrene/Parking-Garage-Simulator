package parkeersimulator.model;


import java.awt.Dimension;
import java.util.*;

import parkeersimulator.CarQueue;
import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.car.*;
import parkeersimulator.view.AbstractView;

import java.awt.Image;

public class Model extends AbstractModel implements Runnable {

	private int counter = 0;

	private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
   	private Image carParkImage;
	private Dimension size;
	private ParkingSpot[][][] spots;
	private int[] spotCountPerType;
	private int[] carCountPerType;

	private CarQueue entranceCarQueue  = new CarQueue();
	private CarQueue entrancePassQueue = new CarQueue();
	private CarQueue paymentCarQueue = new CarQueue();
	private CarQueue paymentCarQueueExtra = new CarQueue();
	private CarQueue exitCarQueue = new CarQueue();
	private CarQueue missedCars = new CarQueue();
	private int entranceCarQueueMax = 6;

	private String[] weekDay = {"Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"};
    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    private List<Integer> moneyHourList;
    private List<Integer> moneyDayList;
    private List<Integer> moneyWeekList;
    private int moneyLastHour;
    private int moneyLastDay;
    private int moneyLastWeek;

    private int abonneesMax = 50;
    private int reserveringMax = 0;
    private int percentageHandicap = 2;
    private int abonneeTarief = 10;
    private int normaalTarief = 1;
    private int reserveringTarief = 10;

    private int tickCount = 0;
    private int tickPause = 100;

    int weekDayArrivals = 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals = 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute

	private boolean run;

	public int getSpotCountForType(int type) {
		return spotCountPerType[type];
	}

	private void initSpots() {
        for (int type = 0; type < ParkingSpot.TYPE_COUNT; type++) {
            spotCountPerType[type] = 0;
        }
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                	ParkingSpot spot = spots[floor][row][place];
                	spot.setType(ParkingSpot.TYPE_AD_HOC);
                	spotCountPerType[spot.getType()]++;
                }
            }
        }
        /* temporary test */
        for (int row = 0; row < 2; row++) {
        	for (int place = 0; place < 30; place++) {
        		ParkingSpot spot = spots[2][row][place];
        		spot.setType(ParkingSpot.TYPE_PASS);
        		spotCountPerType[spot.getType()]++;
        	}
        }
        for (int row = 2; row < 4; row++) {
        	for (int place = 0; place < 30; place++) {
        		ParkingSpot spot = spots[2][row][place];
       			spot.setType(ParkingSpot.TYPE_HANDI);
        		spotCountPerType[spot.getType()]++;
        	}
        }
        for (int row = 4; row < 6; row++) {
        	for (int place = 0; place < 30; place++) {
        		ParkingSpot spot = spots[2][row][place];
       			spot.setType(ParkingSpot.TYPE_RES);
        		spotCountPerType[spot.getType()]++;
        	}
        }
	}

	public Model(int numberOfFloors, int numberOfRows, int numberOfPlaces) {

		super();

		moneyHourList = new ArrayList<>();
		moneyDayList = new ArrayList<>();
		moneyWeekList = new ArrayList<>();

        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots = numberOfFloors*numberOfRows*numberOfPlaces;

        spotCountPerType = new int[ParkingSpot.TYPE_COUNT];
        carCountPerType = new int[ParkingSpot.TYPE_COUNT];
        spots = new ParkingSpot[numberOfFloors][numberOfRows][numberOfPlaces];

        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                	spots[floor][row][place] = new ParkingSpot();
                }
            }
        }

        initSpots();
	}

	public void reset() {
		day = hour = minute = 0;
        entranceCarQueue.clear();
        entrancePassQueue.clear();
        paymentCarQueue.clear();
        paymentCarQueueExtra.clear();
        exitCarQueue.clear();
        missedCars.clear();
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    AbstractCar car = getCarAt(location);
                    if (car != null) {
                        removeCarAt(location);
                    }
                }
            }
        }
        notifyViews();
	}

	public CarQueue getEntranceCarQueueNr() {
		return entranceCarQueue;
	}

	public CarQueue getEntrancePassQueueNr() {
		return entrancePassQueue;
	}

	public CarQueue getPaymentCarQueueNr() {
		return paymentCarQueueExtra;
	}

	public CarQueue exitCarQueueNr() {
		return exitCarQueue;
	}	

	public CarQueue getmissedCars() {
		return missedCars;
	}

	public Dimension getSize() {
		size = new Dimension(800, 400);
		return  size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public Image getCarParkImage() {
		return carParkImage;
	}

	public void setCarParkImage(Image image) {
		this.carParkImage = image;
	}

	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public int getNumberOfPlaces() {
		return numberOfPlaces;
	}

	public int getNumberOfOpenSpots() {
		return numberOfOpenSpots;
	}

	public void setSpotType(Location location, int type) {
		ParkingSpot spot = getParkingSpotAt(location);
		spotCountPerType[spot.getType()]--;
		spotCountPerType[type]++;
		spot.setType(type);
		notifyViews();
	}

	public ParkingSpot getParkingSpotAt(Location location) {
		if(!locationIsValid(location)) {
			return null;
		}
		return spots[location.getFloor()][location.getRow()][location.getPlace()];
	}

	public AbstractCar getCarAt(Location location) {
    	ParkingSpot spot = getParkingSpotAt(location);
    	if(spot == null) {
    		return null;
    	}
   		return spot.getCar();
    }

	public int getCarCountForType(int type) {
		return carCountPerType[type];
	}

    public boolean setCarAt(Location location, AbstractCar car) {
    	ParkingSpot spot = getParkingSpotAt(location);
    	if(spot == null) {
    		return false;
    	}
        AbstractCar oldCar = spot.getCar();
        if (oldCar == null) {
        	spot.setCar(car);
            car.setLocation(location);
            carCountPerType[spot.getType()]++;
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }

    public AbstractCar removeCarAt(Location location) {
    	ParkingSpot spot = getParkingSpotAt(location);
    	if(spot == null) {
    		return null;
    	}
        AbstractCar car = spot.getCar();
        if (car == null) {
            return null;
        }
        spot.setCar(null);
        car.setLocation(null);
        carCountPerType[spot.getType()]--;
        numberOfOpenSpots++;
        return car;
    }

    public void setTickPause(int tickPause){
    	this.tickPause = tickPause;
    }

    public int getTickPause(){
    	return tickPause;
    }

    public void setAbonnees(int abonnees){
    	this.abonneesMax = abonnees;
    }

    public int getAbonnees(){
		return abonneesMax;
    }

    public void setReservering(int reservering){
    	this.reserveringMax = reservering;
    }

    public int getReservering(){
		return reserveringMax;
    }

    public void setHandicapPercentage(int percentageHandicap){
    	this.percentageHandicap = percentageHandicap;
    }

    public int getHandicapPercentage(){
		return percentageHandicap;
    }

    public void setAbonneeTarief(int abonneeTarief){
    	this.abonneeTarief = abonneeTarief;
    }

    public int getAbonneeTarief(){
		return abonneeTarief;
    }

    public void setNormaalTarief(int normaalTarief){
    	this.normaalTarief = normaalTarief;
    }

    public int getNormaalTarief(){
		return normaalTarief;
    }

    public void setReserveringTarief(int reserveringTarief){
    	this.reserveringTarief = reserveringTarief;
    }

    public int getReserveringTarief(){
		return reserveringTarief;
    }

    public Location getFirstFreeTypeLocation(int type) {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    ParkingSpot spot = getParkingSpotAt(location);
                    if(spot != null && spot.getType() == type && spot.getCar() == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public Location getFirstFreeLocation() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public AbstractCar getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    AbstractCar car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    public void tick() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    AbstractCar car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    public boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }

    public void advanceTime(){
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }
    }

    public String getTime() {
    	String strMinute = ("" + minute);
    	String strHour = ("" + hour);
    	if(minute < 10) {
    		strMinute = ("0"+ strMinute);
    	}
    	if(hour < 10) {
    		strHour = ("0"+ strHour);
    	}
    	
    	return (strHour + ":" + strMinute);
    }

    public String getDay() {
    	return weekDay[day];
    }

    public void handleEntrance() {
    	carsEntering(entrancePassQueue, ParkingSpot.TYPE_PASS);
    	carsEntering(entranceCarQueue, ParkingSpot.TYPE_AD_HOC); 
    }

    private void carsArriving() {
    	int numberOfCars = 0;
    	int sum = 0;
    	if (percentageHandicap != 0) {
    		sum = 100 / percentageHandicap;
    	}
    	if (counter <= sum)   {  	
    		numberOfCars = getNumberOfCars(weekDayArrivals, weekendArrivals);
    		addArrivingCars(numberOfCars, ParkingSpot.TYPE_AD_HOC); 
    		counter++;
    	} else {
    		numberOfCars = getNumberOfCars(weekDayArrivals, weekendArrivals);
    		addArrivingCars(numberOfCars, ParkingSpot.TYPE_HANDI);
    		counter = 0;
    	}
    	if (abonneesMax > carCountPerType[ParkingSpot.TYPE_PASS]) {
    		numberOfCars = getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
    		addArrivingCars(numberOfCars, ParkingSpot.TYPE_PASS);
    	}
    }

    private void carsEntering(CarQueue queue, int type) {
        int i = 0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue() > 0 && i < enterSpeed) {
    		Location freeLocation = getFirstFreeTypeLocation(type);
    		if(freeLocation == null) {
    			break;
    		}
            AbstractCar car = queue.removeCar();
            setCarAt(freeLocation, car);
            i++;
        }
    }

    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        AbstractCar car = getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            car.setIsPaying(true);
	            paymentCarQueue.addCar(car);
	            paymentCarQueueExtra.addCar(car);
	        }
        	
        	else {
        		carLeavesSpot(car);
        	}
            car = getFirstLeavingCar();
        }
    }

    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	int moneyCounter = 0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            AbstractCar car = paymentCarQueue.removeCar();
            
            
            int totalMinutes = car.getTotalMinutes();
            int perCarPrice = totalMinutes / 30;
            
            if ((perCarPrice % 30) != 0) {
            	perCarPrice = perCarPrice + 1;
            }
            perCarPrice *= normaalTarief;
                     
            carLeavesSpot(car);
            moneyCounter += perCarPrice;
            i++;
    	}
    	setMoneyHour(moneyCounter);
    }

    private void setMoneyHour(int moneyCounter) {
    	int moneyLastMinute = moneyCounter;
    	moneyLastHour = 0;
    	//money last hour
    	if (moneyHourList.size() >= 60) {
    		moneyHourList.remove(0);
    		moneyHourList.add(moneyLastMinute);
    	}
    	else {
    		moneyHourList.add(moneyLastMinute);
    	}
    	
    	for(Integer countHour : moneyHourList) {
    		moneyLastHour += countHour;
    	}
    	
    	if (minute == 0) {
    		setMoneyDay(moneyLastHour);
    	}
    	
    }

    private void setMoneyDay(int moneyLastHourP) {
    	int moneyLastHour = moneyLastHourP;
    	moneyLastDay = 0;
    	//money last hour
    	if (moneyDayList.size() >= 24) {
    		moneyDayList.remove(0);
    		moneyDayList.add(moneyLastHour);
    	}
    	else {
    		moneyDayList.add(moneyLastHour);
    	}
    	
    	for(Integer countDay : moneyDayList) {
    		moneyLastDay += countDay;
    	}
    	
    	if (hour == 0) {
    		setMoneyWeek(moneyLastDay);
    	}
    }

    private void setMoneyWeek(int moneyLastDayP) {
    	int moneyLastDay = moneyLastDayP;
    	moneyLastWeek = 0;
    	//money last hour
    	if (moneyWeekList.size() >= 7) {
    		moneyWeekList.remove(0);
    		moneyWeekList.add(moneyLastDay);
    	}
    	else {
    		moneyWeekList.add(moneyLastDay);
    	}
    	
    	for(Integer countWeek : moneyWeekList) {
    		moneyLastWeek += countWeek;
    	}
    }

    public int getMoneyLastHour() {
    	return moneyLastHour;
    }

    public int getMoneyLastDay() {
    	return moneyLastDay;
    }

    public int getMoneyLastWeek() {
    	return moneyLastWeek;
    }

    public int getMoneyInGarage() {
    	int allCarPrice = 0;
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    AbstractCar car = getCarAt(location);
                    if (car != null) {
                    	if (car.getHasToPay() == true) {
                    		int totalMinutes = car.getTotalMinutes();
                    		int perCarPrice = totalMinutes / 30;
                        
                    		if ((perCarPrice % 30) != 0) {
                    			perCarPrice = perCarPrice + 1;
                    		}
                    		perCarPrice *= normaalTarief;
                    		allCarPrice += perCarPrice;
                    	}
                    }
                }
            }
        }
        return allCarPrice;
    }

    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
    	}	
    }
    
    private void clearExtraPaymentQueue() {
    	int i=0;
    	while (paymentCarQueueExtra.carsInQueue()>0 && i <paymentSpeed) {
    		paymentCarQueueExtra.removeCar();
    		i++;
    	}
    }

    private int getNumberOfCars(int weekDay, int weekend){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }

    private void addArrivingCars(int numberOfCars, int type){
        // Add the cars to the back of the queue.
    	switch(type) {
    	case ParkingSpot.TYPE_AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	if(entranceCarQueue.carsInQueue() < entranceCarQueueMax) {
            		entranceCarQueue.addCar(new AdHocCar());
            	}
            	else {
            		missedCars.addCar(new AdHocCar());
            	}
            }
            break;
    	case ParkingSpot.TYPE_HANDI:
    		for (int i = 0; i < numberOfCars; i++) {
        		if(entranceCarQueue.carsInQueue() < entranceCarQueueMax) {
    				entranceCarQueue.addCar(new HandiCar());
    			}
            	else {
            		missedCars.addCar(new HandiCar());
            	}
    		}
    		break;
    	case ParkingSpot.TYPE_PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
            break;	
    	case ParkingSpot.TYPE_RES:
    		for (int i = 0; i < numberOfCars; i++) {
    			entrancePassQueue.addCar(new ResCar());
    		}
    		break;
    	}
    }

    private void carLeavesSpot(AbstractCar car){
    	Location location = car.getLocation();
    	removeCarAt(location);
        exitCarQueue.addCar(car);        
    }

    public void start(int tickCount) {
		if(run) {
			System.out.println("Already running");
		} else {
			this.tickCount = tickCount;
			new Thread(this).start();
		}
	}

	public void stop() {
		run = false;
	}

	public void firstStep() {
		advanceTime();
		carsArriving();
		carsReadyToLeave();
		carsPaying();
		tick();
	}
	
	public void secondStep() {
		clearExtraPaymentQueue();
		carsLeaving();
		handleEntrance();
	}
	
	@Override
	public void run() {
		run = true;
		while(run) {
			if (tickCount > 0) {
				tickCount--; 
				if(tickCount == 0) run = false;
			}
			firstStep();
			try {
				Thread.sleep(tickPause);
			} catch (Exception e) {} 
			secondStep();
			notifyViews();
		}
	}
}