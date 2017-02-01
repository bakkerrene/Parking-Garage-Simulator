package parkeersimulator.model;
//test
//test
import java.awt.Dimension;
import java.util.*;

import parkeersimulator.CarQueue;
import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.car.*;
import parkeersimulator.controller.AbstractController;
import parkeersimulator.exception.ParkeerException;
import parkeersimulator.view.AbstractView;

import java.awt.Image;

public class Model extends AbstractModel implements Runnable {

	private int counter = 0;
	private double multiplier = 1;
	
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
    private int reserveringMax = 15;
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
	private boolean inSim;

	public Model(int numberOfFloors, int numberOfRows, int numberOfPlaces) {

		super();

		moneyHourList = new ArrayList<>();
		moneyDayList = new ArrayList<>();
		moneyWeekList = new ArrayList<>();

        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots = numberOfFloors*numberOfRows*numberOfPlaces;

        inSim = false;

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
        initDefaultSpots();
	}
	
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
	}

    public void initDefaultSpots() {

		int handiCount = (int)(Math.ceil(((this.getHandicapPercentage() / 100.0) * getTotalSpotCount())));

		int floor = 0, row = 0, place = 0;

		for (int x = 0; x < getAbonnees(); x++) {
			Location location = new Location(floor, row, place);
			internalSetSpotType(location, ParkingSpot.TYPE_PASS);
			place++;
			if (place >= getNumberOfPlaces()) {
				place = 0;
				row++;
				if(row >= getNumberOfRows()) {
					row = 0;
					floor++;
				}
			}
		}

		for (int x = 0; x < handiCount; x++) {
			Location location = new Location(floor, row, place);
			internalSetSpotType(location, ParkingSpot.TYPE_HANDI);
			place++;
			if (place >= getNumberOfPlaces()) {
				place = 0;
				row++;
				if(row >= getNumberOfRows()) {
					row = 0;
					floor++;
				}
			}
		}

		for (int x = 0; x < getReservering(); x++) {
			Location location = new Location(floor, row, place);
			internalSetSpotType(location, ParkingSpot.TYPE_RES);
			place++;
			if (place >= getNumberOfPlaces()) {
				place = 0;
				row++;
				if(row >= getNumberOfRows()) {
					row = 0;
					floor++;
				}
			}
		}
		for(AbstractController c: controllers) c.spotsChanged();
    }

	public void reset() {
		stop();
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
        inSim = false;
	}

	public void clearSpots() {
		initSpots();
		reset();
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

	public int getTotalSpotCount() {
		return numberOfFloors*numberOfRows*numberOfPlaces;
	}

	private void internalSetSpotType(Location location, int type) {
		ParkingSpot spot = getParkingSpotAt(location);
		spotCountPerType[spot.getType()]--;
		spotCountPerType[type]++;
		spot.setType(type);
		notifyViews();
	}

	public void setSpotType(Location location, int type) {
		internalSetSpotType(location, type);
	    abonneesMax = spotCountPerType[ParkingSpot.TYPE_PASS];
	    reserveringMax = spotCountPerType[ParkingSpot.TYPE_RES];
	    percentageHandicap = (int)(((double)spotCountPerType[ParkingSpot.TYPE_HANDI] / (double)getTotalSpotCount()) * 100.0);
		for(AbstractController c: controllers) c.spotsChanged();
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
    
    public void setMultiplier(double multiplier) {
    	this.multiplier = multiplier / 100.0;
    }
    
    public double getMultiplier() {
    	return multiplier;
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
    
    public int getHour(){
    	return hour;
    }

    public void handleEntrance() {
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue); 
    }

    private void carsArriving() {
    	int numberOfCars = 0;
    	int sum = 0;
    	if (percentageHandicap != 0) {
    		sum = 100 / percentageHandicap;
    	}
    	if (counter <= sum)   {  	
    		numberOfCars = getNumberOfCars("HOC");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_AD_HOC);
			} catch (ParkeerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    		counter++;
    	} else {
    		numberOfCars = getNumberOfCars("HOC");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_HANDI);
			} catch (ParkeerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		counter = 0;
    	}
    	if (abonneesMax > carCountPerType[ParkingSpot.TYPE_PASS]) {
    		numberOfCars = getNumberOfCars("PASS");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_PASS);
			} catch (ParkeerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	//reserveringen plaatsen toewijzen. 
    	if (reserveringMax > carCountPerType[ParkingSpot.TYPE_RES]) {
    		numberOfCars = getNumberOfCars("RES");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_RES);
			} catch (ParkeerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    private void carsEntering(CarQueue queue) {
        int i = 0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue() > 0 && i < enterSpeed) {
    		AbstractCar car = queue.removeCar();
    		Location freeLocation = getFirstFreeTypeLocation(car.getType());
    		if(freeLocation == null) {
    			queue.addCar(car);
    			break;
    		}
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

    //carType mee nemen zo dat je per auto de nummer autos kan toewijzen

    private int getNumberOfCars(String test) {

        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = 100;
        
       // alle autos voor 6 uur
       // als het voor alle autos geld hierin
        if (hour < 6) {
        	averageNumberOfCarsPerHour = 20;
        }
        else {
        	if(test == "PASS") {
        		if (day > 4) {
        			averageNumberOfCarsPerHour = 10;
        		}
        	}
        	if(test == "HOC") {
        		//Donderdag, Vrijdag, Zaterdag Avond dus normale autos & reserveringen
        		if (day >= 3 && day < 6 && hour >= 18) {
        			averageNumberOfCarsPerHour = 200;
        		}
        		// normale zaterdag uren voor normale autos
        		else if(day == 5){
        			averageNumberOfCarsPerHour = 50;
        		}
        		// theater op zondag middag dus normale autos & reserveringen
        		if (day == 6 && hour >= 12 && hour < 18) {
        			averageNumberOfCarsPerHour = 200;
        		}
        		// normale zaterdag uren voor normale autos
        		else if (day == 6 && hour >= 6) {
        			averageNumberOfCarsPerHour = 50;
        		}
        	}
        }
        averageNumberOfCarsPerHour *= multiplier;
        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }

    private void addArrivingCars(int numberOfCars, int type) throws ParkeerException{
        // Add the cars to the back of the queue.
    	switch(type) {
    	case ParkingSpot.TYPE_AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	if(entranceCarQueue.carsInQueue() < entranceCarQueueMax) {
            		entranceCarQueue.addCar(new AdHocCar());
            	}
            	else {
            		missedCars.addCar(new AdHocCar());
            		throw new ParkeerException ("Test!");
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
            		throw new ParkeerException ("Test!");
            	}
    		}
    		break;
    	case ParkingSpot.TYPE_PASS:

            for (int i = 0; i < numberOfCars; i++) {
        		if (getNumberOfCars("PASS") < abonneesMax) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
    		}
            break;	
    	case ParkingSpot.TYPE_RES:

    		for (int i = 0; i < numberOfCars; i++) {
        		if (getNumberOfCars("RES") < reserveringMax) {
    			entrancePassQueue.addCar(new ResCar());
    		}
    		break;
    	}
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
			inSim = true;
			run = true;
			new Thread(this).start();
		}
	}

	public void stop() {
		run = false;
	}

	public boolean isRunning() {
		return run;
	}
	public boolean isInSim() {
		return inSim;
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
		for(AbstractController c: controllers) c.simStarted();
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
		for(AbstractController c: controllers) c.simStopped();
	}
}
