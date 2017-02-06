package parkeersimulator.model;


import java.awt.Dimension;
import java.util.*;

import parkeersimulator.CarQueue;
import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.car.*;
import parkeersimulator.controller.AbstractController;
import parkeersimulator.exception.ParkeerException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.Image;
import java.io.File;

public class Model extends AbstractModel implements Runnable {

	private int soundSteps = 0; //steps for queue warning sound @addArrivingCars
	private int counter; //counter for handicap cars
	private double multiplier;
	
	private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
   	private Image carParkImage;
	private Dimension size;
	private ParkingSpot[][][] spots;
	private int[] spotCountPerType;
	private int[] carCountPerType;

	private String buttonOption = "cars";

	private CarQueue entranceCarQueue;
	private CarQueue entrancePassQueue;
	private CarQueue paymentCarQueue;
	private CarQueue paymentCarQueueExtra;
	private CarQueue exitCarQueue;
	private CarQueue missedCars;
	private int entranceCarQueueMax;

	private String[] weekDayStrings = {"Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"};
    private int day;
    private int hour;
    private int minute;

    private List<Integer> moneyHourList;
    private List<Integer> moneyDayList;
    private List<Integer> moneyWeekList;
    private int moneyLastHour;
    private int moneyLastDay;
    private int moneyLastWeek;

    private Random randomGen;

    class Reservation {
    	public Location location;// <--public public public
    	public int timeOfArrival;
    	public int timeOfExpiration;
    }
    private double resPercentKomtNooit = 15.0;
    private List<Integer> resTimeList;
    private List<Reservation> resList;
    private int specialResWeek;
    private int specialResDag;
    private int specialResUur;
	private int freeAdHocSpots = 0;

    private int abonneesMax = 50;
    private int reserveringMax = 250;
    private int percentageHandicap = 2;
    private int abonneeTarief = 10;
    private int normaalTarief = 1;
    private int reserveringTarief = 10;

    private int totalPassCar;
    private int totalRessCar;
    private int totalAdhocCar;
    private int totalHandiCar;

    private HashMap<String, Integer> carCounter;

    private int tickCount = 0;
    private int tickPause = 100;

    private int weekDayAdArrivals = 100; // average number of arriving cars per hour
    private int weekendAdArrivals = 70; // average number of arriving cars per hour
    private int eventAdArrivals = 300; // average number of arriving cars per hour on events
    private int weekDayPassArrivals = 50; // average number of arriving cars per hour
    private int weekendPassArrivals = 5; // average number of arriving cars per hour
    private int eventPassArrivals = 20; 
    private int weekDayResArrivals = 20;
    private int weekendResArrivals = 50;
    private int eventResArrivals = 300;

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute
    
    int selectedSpotType = ParkingSpot.TYPE_AD_HOC;

	private boolean run;
	private boolean inSim;
	
	private void init() {
		counter = 0;
		
		
		numberOfOpenSpots = numberOfFloors*numberOfRows*numberOfPlaces;

		entranceCarQueue  = new CarQueue();
		entrancePassQueue = new CarQueue();
		paymentCarQueue = new CarQueue();
		paymentCarQueueExtra = new CarQueue();
		exitCarQueue = new CarQueue();
		missedCars = new CarQueue();
		entranceCarQueueMax = 6;

	    day = hour = minute = 0;

		moneyHourList = new ArrayList<>();
		moneyDayList = new ArrayList<>();
		moneyWeekList = new ArrayList<>();

	    moneyLastHour = 0;
	    moneyLastDay = 0;
	    moneyLastWeek = 0;

	    resTimeList = new ArrayList<>();
	    resList = new ArrayList<>();

	    totalPassCar = 0;
	    totalRessCar = 0;
	    totalAdhocCar = 0;
	    totalHandiCar = 0;
	    
	    resetQueueValues();

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

        addReservering(7*specialResWeek + specialResDag, specialResUur, 0);
	}

	/**
	 * this is the constructor of the Class Model
	 * @param numberOfFloors the amount of Floors in the parking garage
	 * @param numberOfRows the amount of rows per floor in the parking garage
	 * @param numberOfPlacesthe amount of places per row per floor in the parking garage
	 */
	public Model(int numberOfFloors, int numberOfRows, int numberOfPlaces) {

		super();

		multiplier = 1;

        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;

        specialResWeek = 0;
	    specialResDag = 0;
	    specialResUur = 0;

        randomGen = new Random();
		init();
	}

	/**
	 * this method will paly a sound
	 * @param file path + filename or filename of the audio file to play the sound
	 */
	public void playSound(String file)
	{
	    try
	    {
	        Clip clip = AudioSystem.getClip();
	        clip.open(AudioSystem.getAudioInputStream(new File("./src/audio/" + file)));
	        clip.start();
	        //test
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
	    }
	}
	
	
	private void addSpotsOfType(int type, int count) {
		for (int x = 0; x < count; x++) {
			Location location = getFirstFreeTypeLocation(ParkingSpot.TYPE_AD_HOC);
			if (location == null) {
				break;
			}
			internalSetSpotType(location, type);
		}
	}

	private void initSpots() {

		freeAdHocSpots = numberOfOpenSpots; 

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

		int handiCount = (int)(Math.ceil(((this.getHandicapPercentage() / 100.0) * getTotalSpotCount())));
		addSpotsOfType(ParkingSpot.TYPE_HANDI, handiCount);

    	int abboCount = getAbonnees();
   		addSpotsOfType(ParkingSpot.TYPE_PASS, abboCount);

   		freeAdHocSpots = spotCountPerType[ParkingSpot.TYPE_AD_HOC];

		for(AbstractController c: controllers) c.spotsChanged();
	}
	
	public void resetQueueValues() {
		weekDayAdArrivals = 100; // average number of arriving cars per hour
	    weekendAdArrivals = 70; // average number of arriving cars per hour
	    eventAdArrivals = 300; // average number of arriving cars per hour on events
	    weekDayPassArrivals = 50; // average number of arriving cars per hour
	    weekendPassArrivals = 5; // average number of arriving cars per hour
	    eventPassArrivals = 20; 
	    weekDayResArrivals = 20;
	    weekendResArrivals = 50;
	    eventResArrivals = 300;

	    enterSpeed = 3; // number of cars that can enter per minute
	    paymentSpeed = 7; // number of cars that can pay per minute
	    exitSpeed = 5; // number of cars that can leave per minute
	}
	
	private void internalSetSpotType(Location location, int type) {
		ParkingSpot spot = getParkingSpotAt(location);
		spotCountPerType[spot.getType()]--;
		spotCountPerType[type]++;
		if(spot.getType() == ParkingSpot.TYPE_AD_HOC)
			this.freeAdHocSpots--;
		if(type == ParkingSpot.TYPE_AD_HOC)
			this.freeAdHocSpots++;
		spot.setType(type);
		notifyViews();
	}

	public void setSpotType(Location location, int type) {
		internalSetSpotType(location, type);
	    abonneesMax = spotCountPerType[ParkingSpot.TYPE_PASS];
	    percentageHandicap = (int)(((double)spotCountPerType[ParkingSpot.TYPE_HANDI] / (double)getTotalSpotCount()) * 100.0);
		for(AbstractController c: controllers) c.spotsChanged();
	}

	public ParkingSpot getParkingSpotAt(Location location) {
		if(!locationIsValid(location)) {
			return null;
		}
		return spots[location.getFloor()][location.getRow()][location.getPlace()];
	}

	/**
	 * sets a car at a location and returns this
	 * @param location the location of the car 
	 * @return the spot of the car
	 */
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
            carCountPerType[car.getType()]++;
            if(spot.getType() == ParkingSpot.TYPE_AD_HOC)
            	freeAdHocSpots--;
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
        carCountPerType[car.getType()]--;
        if(spot.getType() == ParkingSpot.TYPE_AD_HOC)
        	freeAdHocSpots++;
        numberOfOpenSpots++;
        return car;
    }

	public void clickedSpot(Location location) {
		if (isRunning())
			return;
		if (isInSim()) {
			ParkingSpot spot = getParkingSpotAt(location);
			if (spot.getCar() != null)
				return;
		}
		setSpotType(location, getSelectedSpotType());
	}

	public void setSpecialReservering(int resWeek, int resDag, int resUur) {
		if(resWeek < 0) resWeek = 0;
		if(resDag < 0) resDag = 0;
		if(resUur < 0) resUur = 0;
        specialResWeek = resWeek;
	    specialResDag = resDag;
	    specialResUur = resUur;
	}

    private Location getFirstFreeTypeLocation(int type) {
    	if(type == ParkingSpot.TYPE_AD_HOC && freeAdHocSpots <= resTimeList.size()) {
    		return null;
    	}
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

    public boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }

    private void addReservering(int resDay, int resHour, int resMinute) {
    	if(resTimeList.size() + this.getSpotCountForType(ParkingSpot.TYPE_RES) < reserveringMax) {
    		if(freeAdHocSpots > resTimeList.size()) {
    			int resTime = 24*60*resDay + 60*resHour + resMinute;
    			resTimeList.add(resTime);
    			
    		}
    	}
    }

    private void checkReserveringen() {

    	int curTime = 24*60*day + 60*hour + minute;

	    Iterator<Integer> it = resTimeList.iterator();
	    while(it.hasNext()) {
	    	int resTime = it.next();
	    	if(resTime <= curTime) {
	    		it.remove();

	        	Location location = getFirstFreeTypeLocation(ParkingSpot.TYPE_AD_HOC);
	            if(location != null) {
	            	setSpotType(location, ParkingSpot.TYPE_RES);

	            	Reservation res = new Reservation();
	            	res.location = location;
	            	double p = randomGen.nextDouble();
	            	if(p > 0.01 * resPercentKomtNooit) {
	            		p = randomGen.nextDouble();
	            		res.timeOfArrival = curTime + (int)(p * 30.0);
	            	} else {
	            		p = randomGen.nextDouble();
	            		res.timeOfArrival = curTime + 60;
	            	}
	            	res.timeOfExpiration = curTime + 30;
	            	resList.add(res);
	            } else {
	            	/* Als het goed is komt dit nooit voor */
	            	System.out.println("location == null");
	            }
	    	}
	    }

	    Iterator<Reservation> resIt = resList.iterator();
	    while(resIt.hasNext()) {
	    	Reservation res = resIt.next();

	    	/* Check timeOfArrival eerst, voor auto's die net op tijd komen */
	    	if(res.timeOfArrival <= curTime) {
	    	    setCarAt(res.location, new ResCar());
	    	    totalRessCar++;
				resIt.remove();
	    	} else if(res.timeOfExpiration <= curTime) {
	    		this.setSpotType(res.location, ParkingSpot.TYPE_AD_HOC);
	    		resIt.remove();
	    	}
	    }
    }
    
    
    /**
     * this method will calculate the average number of cars arriving per hour
     * @param type the type of car in string format
     * @return returns the numer of cars that will arive per type of car
     */
    private int getNumberOfCars(String type) {

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = 100;
        int weekDay = day%7;
       // alle autos voor 6 uur
       // als het voor alle autos geld hierin
        if (hour < 6) {
        	averageNumberOfCarsPerHour = 20;
        }
        else {        	
        	if(type == "PASS") {        		
        		if (weekDay <= 4) {
        			averageNumberOfCarsPerHour = weekDayPassArrivals;
        		}
        		else if ((weekDay >= 3 && weekDay < 6 && hour >= 18) || (weekDay == 6 && hour >= 12 && hour < 18)) {
        			averageNumberOfCarsPerHour = eventPassArrivals;
        		} 
        		else {
        			averageNumberOfCarsPerHour = weekendPassArrivals;
        		}
        	}        	        	
        	if(type == "HOC" || type =="HANDI") {
        		//Donderdag, Vrijdag, Zaterdag Avond dus normale autos & reserveringen
        		if ((weekDay >= 3 && weekDay < 6 && hour >= 18) || (weekDay == 6 && hour >= 12 && hour < 18)) {
        			averageNumberOfCarsPerHour = eventAdArrivals;
        		}
        		// normale zaterdag uren voor normale autos
        		else if(weekDay <= 4){
        			averageNumberOfCarsPerHour = weekDayAdArrivals;
        		}
        		else {
        			averageNumberOfCarsPerHour = weekendAdArrivals;
        		}
        	}
        	if (type == "RES") {
        		if ((weekDay >= 3 && weekDay < 6 && hour >= 18) || (weekDay == 6 && hour >= 12 && hour < 18)) {
        			averageNumberOfCarsPerHour = eventResArrivals;
        		}
        		else if(weekDay <= 4){
        			averageNumberOfCarsPerHour = weekDayResArrivals;
        		}
        		else {
        			averageNumberOfCarsPerHour = weekendResArrivals;
        		}
        	}
        }
        averageNumberOfCarsPerHour *= multiplier;
        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + randomGen.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }

    private AbstractCar getFirstLeavingCar() {
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

    private void advanceTime() {
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
        //while (day > 6) {
            //day -= 7;
        //}
    }
    
    private void tick() {
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

    private void carsArriving() {
    	int numberOfCars = 0;
    	int sum = 10;
    	if (percentageHandicap != 0) {
    		sum = 10;
    	}
    	if (counter <= sum)   {  	
    		numberOfCars = getNumberOfCars("HOC");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_AD_HOC);
			} catch (ParkeerException e) {
				e.printStackTrace();
			} 
    		counter++;
    	} else {
    		numberOfCars = getNumberOfCars("HANDI");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_HANDI);
			} catch (ParkeerException e) {
				e.printStackTrace();
			}
    		counter = 0;
    	}
    	if (abonneesMax > carCountPerType[ParkingSpot.TYPE_PASS]) {
    		numberOfCars = getNumberOfCars("PASS");
    		try {
				addArrivingCars(numberOfCars, ParkingSpot.TYPE_PASS);
			} catch (ParkeerException e) {

				e.printStackTrace();
			}
    	}

        //double p = randomGen.nextDouble();
        //if(p < 0.03) {
        for(int i = 0; i < getNumberOfCars("RES"); i++)
        	addReservering(day, hour, minute);
        //}
    }
    
	private void addArrivingCars(int numberOfCars, int type) throws ParkeerException {
        // Add the cars to the back of the queue.
    	switch(type) {
    	case ParkingSpot.TYPE_AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	if(entranceCarQueue.carsInQueue() < entranceCarQueueMax) {
            		entranceCarQueue.addCar(new AdHocCar());
            		totalAdhocCar++;
            	} else {
            		missedCars.addCar(new AdHocCar());
            		if (soundSteps > 150) { // speelt de audio file om de 150 stappen af en niet elke stap
            			playSound("queuefull.wav");
            			soundSteps = 0;
            		}
            		//throw new ParkeerException ("Test!"); <-- beetje irritant
            	}
            }
            break;
    	case ParkingSpot.TYPE_HANDI:
    		for (int i = 0; i < numberOfCars; i++) {
        		if(entranceCarQueue.carsInQueue() < entranceCarQueueMax) {
    				entranceCarQueue.addCar(new HandiCar());
    				totalHandiCar++;
    			}
            	else {
            		missedCars.addCar(new HandiCar());
            		//throw new ParkeerException ("Test!");
            	}
    		}
    		break;
    	case ParkingSpot.TYPE_PASS:
            for (int i = 0; i < numberOfCars; i++) {
        		if (totalPassCar < abonneesMax) {
        			entrancePassQueue.addCar(new ParkingPassCar());
        			totalPassCar++;
        		}
    		}
            break;	
    	}
    }
    
    private void handleEntrance() {
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue); 
    }
    
    
    /**
     * this method will place the cars in the garage and remove the car from the entrance queue's 
     * @param queue the queue of the enranceQueue's
     */
    private void carsEntering(CarQueue queue) {
        int i = 0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue() > 0 && i < enterSpeed) {
    		AbstractCar car = queue.removeCar();
    		int carType = car.getType();
    		Location freeLocation = getFirstFreeTypeLocation(carType);
    		if(freeLocation == null) {
    			/* Gebruik reguliere parkeerplaatsen als alle invalide plaatsen bezet zijn */
    			if (car.getType() == ParkingSpot.TYPE_HANDI) {
    				freeLocation = getFirstFreeTypeLocation(ParkingSpot.TYPE_AD_HOC);
    			}
    			if(freeLocation == null) {
    				queue.addCar(car);
    				/* Dat er geen plaats meer is voor dit type auto betekend niet dat er geen plaats
    				 * meer is voor andere types, dus geen break hier */
    				//break;
    			}
    		}
    		if(freeLocation != null)
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
	        } else {
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
            if (car.getType() == ParkingSpot.TYPE_RES) {
            	perCarPrice += reserveringTarief;
            }
            carLeavesSpot(car);
            moneyCounter += perCarPrice;
            i++;
    	}
    	setMoneyHour(moneyCounter);
    }
    
    private void clearPaymentQueue() {
    	int i = 0;
    	while (paymentCarQueueExtra.carsInQueue() > 0 && i < paymentSpeed) {
    		paymentCarQueueExtra.removeCar();
    		i++;
    	}
    }
    
    private void carLeavesSpot(AbstractCar car){
    	Location location = car.getLocation();
    	removeCarAt(location);
    	if(car.getType() == ParkingSpot.TYPE_RES) {
    		this.setSpotType(location, ParkingSpot.TYPE_AD_HOC);
    	}
        exitCarQueue.addCar(car);
    }
    
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            AbstractCar car = exitCarQueue.removeCar();
            if (car.getType() == ParkingSpot.TYPE_RES) {
            	totalRessCar--;
            }
            if (car.getType() == ParkingSpot.TYPE_PASS) {
            	totalPassCar--;
            }
            if (car.getType() == ParkingSpot.TYPE_AD_HOC) {
            	totalAdhocCar--;
            }
            if (car.getType() == ParkingSpot.TYPE_HANDI) {
            	totalHandiCar--;
            }
            i++;
    	}
    }

    private void firstStep() {
		advanceTime();
		carsArriving();
		carsReadyToLeave();
		carsPaying();
		tick();
	}
	
	private void secondStep() {
		clearPaymentQueue();
		carsLeaving();
		handleEntrance();
		checkReserveringen();
		soundSteps++;
	}
	
	public void reset() {
		stop();
		init();
		notifyViews();
		for(AbstractController c: controllers) c.simStopped();
	}
	
    public void start(int tickCount) {
		if(run) {
			System.out.println("Already running");
		} else {
			this.tickCount = tickCount;
			inSim = true;
			run = true;
			new Thread(this).start();
			for(AbstractController c: controllers) c.simStarted();
		}
	}
    
	public void stop() {
		run = false;
	}

	@Override
	public void run() {
		for(AbstractController c: controllers) c.runStarted();
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
		for(AbstractController c: controllers) c.runStopped();
	}
	
	
	/**
	 * This method returns the total amount of cars to other classes
	 * @return HashMap a map with the amount of total cars in the garage 
	 */
	public HashMap<String, Integer> getTotalCars() {
		carCounter = new HashMap<>();
		carCounter.put("adhoc", totalAdhocCar);
		carCounter.put("pass", totalPassCar);
		carCounter.put("handi", totalHandiCar);
		carCounter.put("res", totalRessCar);
		return carCounter;
	}
	
	/**
	 * returns the hour and minute in string format
	 * @return String returns the hour and Minute in string format 00:00
	 */
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
	
	/**
	 * this method returns the String array weekDayStrings
	 * @return String returns a day of the week in stringFormat.
	 */
    public String getDay() {
    	return weekDayStrings[day%7];
    }
    
    /**
     * this method returns the hour mainly used by the LineGraphView
     * @return int returns a int value, returns the hour of the day
     */
    public int getHour(){
    	return hour;
    }
    
    /**
     * This mehtod will calculate the amount of money made in the last Week
     * @param moneyLastDayP This parameter is the total amount of money made in the last 24 hours, and is used to calculate the amount of money per week
      */
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
    
    /**
     * This method will calculate the amount of money made in the last Day
     * @param moneyLastHourP This parameter is the total amount of money made in the last 60 minutes, and is used to calculate the amount of money per day
     */
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
    
    /**
     * This method will calculate the amount of money made in the last hour.
     * @param moneyCounter This parameter will take all the money made in the last minute and is used to calculate the amount of money per hour
     */
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
    
    /**
     * This method will return the amount of money made last Hour
     * @return int moneyLastHour is the amount of money made Last hour
     */
    public int getMoneyLastHour() {
    	return moneyLastHour;
    }
    
    /**
     * This method will retrun the amount of money made last Day
     * @return int moneyLastDay this is the mount of money made last Day
     */
    public int getMoneyLastDay() {
    	return moneyLastDay;
    }
    
    /**
     * This method will return the amount of money made last week
     * @return int moneyLastWeek this is the amount of money made last Week
     */
    public int getMoneyLastWeek() {
    	return moneyLastWeek;
    }
    
    /**
     * This method will return the amount of money that is still in the garage
     * @return int allCarPrice this will return all the money that is still in the garage
     */
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
    
    /**
     * This method will set the selected spot to a certain type
     * @param type This will set a spot to a certain type
     */
    public void setSelectedSpotType(int type) {
    	selectedSpotType = type;
    }
    
    /**
     * This method will return a selectedSpot Type
     * @return int selectedSpotType This is a int value for a certain type of car/spot
     */
    public int getSelectedSpotType() {
    	return selectedSpotType;
    }
    
    /**
     * This method will return the value of the amount of spots per type
     * @param type This is a int value to determine a type
     * @return returns the total amount of spots per type
     */
	public int getSpotCountForType(int type) {
		return spotCountPerType[type];
	}
	
	/**
	 * This method will set the amount of cars that can enter per minute per queue
	 * @param value this is a int value that will set the amount of cars that can enter per minute per queue
	 */
	public void setEnterSpeed(int value) {
		enterSpeed = value;
	}
	
	/**
	 * This method will return the enterSpeed
	 * @return This wil return the enterSpeed of the amount of cars that can enter per minute
	 */
	public int getEnterSpeed() {
		return enterSpeed;
	}
	
	/**
	 * This method will set the amount of cars that can pay per minute
	 * @param value this is a int value that will set the amount of cars that can pay per minute
	 */
	public void setpaymentSpeed(int value) {
		paymentSpeed = value;
	}
	
	/**
	 * this method will return the amount of cars that can pay per minute
	 * @return This will return the payment speed of the amount of cars that can pay per minute
	 */
	public int getpaymentSpeed() {
		return paymentSpeed;
	}
	
	/**
	 * This method will set the amount of cars that can leave per minute
	 * @param value this is a int value that will set the amount of cars that can leave per minute
	 */
	public void setExitSpeed(int value) {
		exitSpeed = value;
	}
	
	/**
	 * This method will return the amount of cars that can leave per minute
	 * @return this will return the amount of cars that can leave per minute
	 */
	public int getExitSpeed() {
		return exitSpeed;
	}
	
	/**
	 * This method will set the amount of normal cars that will arive on weekDays
	 * @param value this is a int value that will set the amount of normal cars that will arrive on weekDays
	 */
	public void setWeekDayAdArrivals(int value) {
		weekDayAdArrivals = value;
	}
	
	/**
	 * this method will return the amount of normal cars that will arrive on weekDays
	 * @return int  WeekDayAdArrivals this will return the amount of normal cars that will arrive on weekDays
	 */
	public int getWeekDayAdArrivals() {
		return weekDayAdArrivals;
	}    
	
	/**
	 * this method will set the amount of normal cars that will arrive on weekends
	 * @param value this is a int value that will set the amount of normal cars that will arrive on weekends
	 */
	public void setWeekendAdArrivals(int value) {
		weekendAdArrivals = value;
	}
	
	/**
	 * this method will return the amount of normal cars that will arrive on weekends
	 * @return int weekendADArrivals this will return the amount of normal cars that will arrive on weekends
	 */
	public int getWeekendAdArrivals() {
		return weekendAdArrivals;
	} 
	
	/**
	 * this method will set the amount of normal cars that will arrive on events (theater days, and shopping nights)
	 * @param value this is a int valueue that will set the amount of normal cars that will arrive on event days (theater days, and shopping nights)
	 */
	public void seteventAdArrivals(int value) {
		eventAdArrivals = value;
	}
	
	/**
	 * this method will return the amount of normal cars that will arrive on events (theater days, and shopping nights)
	 * @return int eventAdArrivals this will return the amount of normal cars that will arrive on event days (theater days, and shopping nights)
	 */
	public int geteventAdArrivals() {
		return eventAdArrivals;
	} 
	
	/**
	 * This method will set the amount of Subsribed Cars that will arive on weekDays
	 * @param value this is a int value that will set the amount of Subsribed cars that will arrive on weekDays
	 */
	public void setWeekDayPassArrivals(int value) {
		weekDayPassArrivals = value;
	}
	
	/**
	 * this method will return the amount of subscibed cars that will arrive on weekDays
	 * @return int  WeekDayPassArrivals this will return the amount of subscribed cars that will arrive on weekDays
	 */
	public int getWeekDayPassArrivals() {
		return weekDayPassArrivals;
	}    
	
	/**
	 * this method will set the amount of subscribed cars that will arrive on weekends
	 * @param value this is a int value that will set the amount of subscribed cars that will arrive on weekends
	 */
	public void setWeekendPassArrivals(int value) {
		weekendPassArrivals = value;
	}
	
	/**
	 * this method will return the amount of subscribed cars that will arrive on weekends
	 * @return int weekendPassArrivals this will return the amount of subscribed cars that will arrive on weekends
	 */
	public int getWeekendPassArrivals() {
		return weekendPassArrivals;
	} 
	
	/**
	 * this method will set the amount of subscribed cars that will arrive on events (theater days, and shopping nights)
	 * @param value this is a int value that will set the amount of subscribed cars that will arrive on event days (theater days, and shopping nights)
	 */
	public void seteventPassArrivals(int value) {
		eventPassArrivals = value;
	}
	
	/**
	 * this method will return the amount of subscribed cars that will arrive on events (theater days, and shopping nights)
	 * @return int eventPassArrivals this will return the amount of subscribed cars that will arrive on event days (theater days, and shopping nights)
	 */
	public int geteventPassArrivals() {
		return eventPassArrivals;
	} 
	
	/**
	 * This method will set the amount of Reservations that will arrive on weekDays
	 * @param value this is a int value that will set the amount of Reservations that will arrive on weekDays
	 */
	public void setWeekDayResArrivals(int value) {
		weekDayResArrivals = value;
	}
	
	/**
	 * this method will return the amount of Reservations that will arrive on weekDays
	 * @return int  WeekDayResArrivals this will return the amount of Reservations that will arrive on weekDays
	 */
	public int getWeekDayResArrivals() {
		return weekDayResArrivals;
	}    
	
	/**
	 * this method will set the amount of Reservations that will arrive on weekends
	 * @param value this is a int value that will set the amount of Reservations that will arrive on weekends
	 */
	public void setWeekendResArrivals(int value) {
		weekendResArrivals = value;
	}
	
	/**
	 * this method will return the amount of Reservations that will arrive on weekends
	 * @return int weekendResArrivals this will return the amount of Reservations that will arrive on weekends
	 */
	public int getWeekendResArrivals() {
		return weekendResArrivals;
	} 
	
	/**
	 * this method will set the amount of Reservations that will arrive on events (theater days, and shopping nights)
	 * @param value this is a int value that will set the amount of Reservations that will arrive on event days (theater days, and shopping nights)
	 */
	public void seteventResArrivals(int value) {
		eventResArrivals = value;
	}
	
	/**
	 * this method will return the amount of Reservations that will arrive on events (theater days, and shopping nights)
	 * @return int eventResArrivals this will return the amount of Reservations that will arrive on event days (theater days, and shopping nights)
	 */
	public int geteventResArrivals() {
		return eventResArrivals;
	} 
	
	/**
	 * This method will return the CarQueue entranceCarQueue, this is used by the QueueView to show the amount of cars in the queue
	 * @return CarQueue entranceCarQueue this is a LinkedList that is used by the QueueView
	 */
	public CarQueue getEntranceCarQueueNr() {
		return entranceCarQueue;
	}
	
	/**
	 * This method will return the CarQueue entrancePassQueue, this is used by the QueueView to show the amount of cars in the queue
	 * @return CarQueue entrancePassQueue this is a LinkedList that is used by the QueueView
	 */
	public CarQueue getEntrancePassQueueNr() {
		return entrancePassQueue;
	}
	
	/**
	 * This method will return the CarQueue paymentCarQueueExtra, this is used by the QueueView to show the amount of cars in the queue
	 * @return CarQueue paymentCarQueueExtra this is a LinkedList that is used by the QueueView
	 */
	public CarQueue getPaymentCarQueueNr() {
		return paymentCarQueueExtra;
	}
	
	/**
	 * This method will return the CarQueue exitCarQueue, this is used by the QueueView to show the amount of cars in the queue
	 * @return CarQueue exitCarQueue this is a LinkedList that is used by the QueueView
	 */
	public CarQueue exitCarQueueNr() {
		return exitCarQueue;
	}
	
	/**
	 * This method will return the CarQueue missedCars, this is used by the QueueView to calculate the amount of cars missed and money missed
	 * @return CarQueue exitCarQueue this is a LinkedList that is used by the QueueView
	 */
	public CarQueue getmissedCars() {
		return missedCars;
	}
	
	/**
	 * this method returns the Dimension ....
	 * This method is not in use.
	 * @return Dimension size the size of the model
	 */
	public Dimension getSize() {
		size = new Dimension(800, 400);
		return  size;
	}
	
	/**
	 * this method set the size of the model.
	 * this method is not in use
	 * @param size the Dimension that is given to set the size of the model
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	/**
	 * This method returns the carParkImage for the CarParkView
	 * @return Image CarParkImage the image for the carParkView
	 */
	public Image getCarParkImage() {
		return carParkImage;
	}
	
	/**
	 * This method set the CarParkImage
	 * @param image this is the CarParkImage for the carParKView
	 */
	public void setCarParkImage(Image image) {
		this.carParkImage = image;
	}
	
	/**
	 * this method will return the number of floors in the Simulator
	 * @return int NumberOfFloors this is the amount of floors in the parking garage
	 */
	public int getNumberOfFloors() {
		return numberOfFloors;
	}
	
	/**
	 * this method will get the number of Rows per floor in the simulator
	 * @return int NumberOfRows this is the amount of Rows per floor in the parking garage
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	/**
	 * This method will get the number of places per row per floor in the simulator
	 * @return int NumberOFPlaces this is the amount of places per row per floor in the parking garage
	 */
	public int getNumberOfPlaces() {
		return numberOfPlaces;
	}
	
	/**
	 * This method will get the number of spots that doesn't have a car on it
	 * @return int numberOfOpenSpots this will return the number of open spots, the spots that doesn't have a car on it
	 */
	public int getNumberOfOpenSpots() {
		return numberOfOpenSpots;
	}
	
	/**
	 * This method will return the total amount of spots in the parking garage
	 * @return int totalSpotCount this will return the total amount of places in the parking garage
	 */
	public int getTotalSpotCount() {
		return numberOfFloors*numberOfRows*numberOfPlaces;
	}
	
	/**
	 * This method will set the thickPause (0 is fast, 999 is slow)
	 * @param tickPause This is the value set by the controller; SlideController
	 */
	public void setTickPause(int tickPause) {
		this.tickPause = tickPause;
	}
	
	/**
	 * this method will get the tickpause, this will be called by the SlideController and the model.run()
	 * @return this will return the tickPause (0 is fast, 999 is slow)
	 */
	public int getTickPause(){
		return tickPause;
	}
	
	/**
	 * This will set the multiplier, this will increase the amount of total cars for every type of cars
	 * @param multiplier this will set the multiplier (180 = 180% of the normal cars, 20 = 20% of the normal cars)
	 */
	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier / 100.0;
	}
	
	/**
	 * This will return the multiplier, 
	 * @return double multiplier this will return the multiplier
	 */
	public double getMultiplier() {
		return multiplier;
	}
	
	/**
	 * This method will set the total amount of subscribers
	 * @param abonnees this will set the total amount of subscibers that are allowed to be created
	 */
	public void setAbonnees(int abonnees){
		this.abonneesMax = abonnees;
	}
	
	/**
	 * this method will return the allowed number of subscriber cars
	 * @return int abonneesMax this will return the value of the allowed number of subcriber cars
	 */
	public int getAbonnees(){
		return abonneesMax;
	}
	
	/**
	 * This method will set the total amount of reservations
	 * @param reservering this will set the total amount of reservations that are allowed to be created
	 */
	public void setReservering(int reservering){
		this.reserveringMax = reservering;
	}
	
	/**
	 * this method will return the allowed number of reservations
	 * @return int reserveringMax this will return the value of the max number of reservations that are allowed
	 */
	public int getReservering(){
		return reserveringMax;
	}
	
	/**
	 * This method will set the percentage for handicap spots.
	 * @param percentageHandicap this will set the percentage of handicap spots in the parking garage
	 */
	public void setHandicapPercentage(int percentageHandicap){
		this.percentageHandicap = percentageHandicap;
	}
	
	/**
	 * this method will return the percentage of handicap spots in the parking garage
	 * @return int percentageHandiCap this will return the percentage of handicap spots in the parking garage
	 */
	public int getHandicapPercentage(){
		return percentageHandicap;
	}
	
	/**
	 * this method will set the price that subcribers must pay per month
	 * @param abonneeTariefthis will set the price subscibers must pay per month
	 */
	public void setAbonneeTarief(int abonneeTarief){
		this.abonneeTarief = abonneeTarief;
	}
	
	/**
	 * this method will return the price that subscribers must pay per month
	 * @return this will return the price that subscribers must pay per month
	 */
	public int getAbonneeTarief(){
		return abonneeTarief;
	}
	
	/**
	 * This method will set the price that cars that has to pay must pay per half hour
	 * @param normaalTarief this will set the price per half hour that has to be paid by adHocCars, HandiCars and ResCars
	 */
	public void setNormaalTarief(int normaalTarief){
		this.normaalTarief = normaalTarief;
	}
	
	/**
	 * This method will return the price that cars has to pay per half hour
	 * @return this will return the price that cars has to pay per half hour
	 */
	public int getNormaalTarief(){
		return normaalTarief;
	}
	
	/** 
	 * this method will set the price for reservations, reservations pay the normal price + a reservation fee
	 * @param reserveringTarief this will set the amount reservations has to pay extra
	 */
	public void setReserveringTarief(int reserveringTarief){
		this.reserveringTarief = reserveringTarief;
	}
	
	/**
	 * this method will return the price reservations has to pay extra
	 * @return this will return the price that reservations has to pay extra
	 */
	public int getReserveringTarief(){
		return reserveringTarief;
	}
	
	
	public void setGraphButtonInput(String buttonOption) {
		this.buttonOption = buttonOption;
	}
	public String getGraphButtonInput() {
		return buttonOption;
	}
	public boolean isRunning() {
		return run;
	}

	public boolean isInSim() {
		return inSim;
	}
}

