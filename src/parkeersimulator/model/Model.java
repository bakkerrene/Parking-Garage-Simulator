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

	private int steps = 0;
	private int counter;
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
    	public Location location;
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

    int weekDayAdArrivals = 100; // average number of arriving cars per hour
    int weekendAdArrivals = 70; // average number of arriving cars per hour
    int eventAdArrivals = 300; // average number of arriving cars per hour on events
    int weekDayPassArrivals = 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour
    int eventPassArrivals = 20; 
    int weekDayResArrivals = 20;
    int weekendResArrivals = 50;
    int eventResArrivals = 300;

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
	
	public int getSpotCountForType(int type) {
		return spotCountPerType[type];
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

	public void reset() {
		stop();
		init();
		notifyViews();
		for(AbstractController c: controllers) c.simStopped();
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
	
	public HashMap<String, Integer> getTotalCars() {
		carCounter = new HashMap<>();
		carCounter.put("adhoc", totalAdhocCar);
		carCounter.put("pass", totalPassCar);
		carCounter.put("handi", totalHandiCar);
		carCounter.put("res", totalRessCar);
		return carCounter;
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

    public void setSelectedSpotType(int type) {
    	selectedSpotType = type;
    }
    public int getSelectedSpotType() {
    	return selectedSpotType;
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

    public void setTickPause(int tickPause) {
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
    
    public void setGraphButtonInput(String buttonOption) {
    	this.buttonOption = buttonOption;
    }
    
    public String getGraphButtonInput() {
    	return buttonOption;
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

    /*--------------------------*/

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
    	return weekDayStrings[day%7];
    }
    
    public int getHour(){
    	return hour;
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
            		if (steps > 150) { // speelt de audio file om de 150 stappen af en niet elke stap
            			playSound("queuefull.wav");
            			steps = 0;
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

    private void carLeavesSpot(AbstractCar car){
    	Location location = car.getLocation();
    	removeCarAt(location);
    	if(car.getType() == ParkingSpot.TYPE_RES) {
    		this.setSpotType(location, ParkingSpot.TYPE_AD_HOC);
    	}
        exitCarQueue.addCar(car);
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

	private void firstStep() {
		advanceTime();
		carsArriving();
		carsReadyToLeave();
		carsPaying();
		tick();
	}

    /*--------------------------*/

    private void clearPaymentQueue() {
    	int i = 0;
    	while (paymentCarQueueExtra.carsInQueue() > 0 && i < paymentSpeed) {
    		paymentCarQueueExtra.removeCar();
    		i++;
    	}
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

    private void handleEntrance() {
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue); 
    }

	private void secondStep() {
		clearPaymentQueue();
		carsLeaving();
		handleEntrance();
		checkReserveringen();
		steps++;
	}

    /*--------------------------*/

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

	public boolean isRunning() {
		return run;
	}

	public boolean isInSim() {
		return inSim;
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
}

