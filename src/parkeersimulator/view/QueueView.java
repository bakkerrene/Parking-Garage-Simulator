package parkeersimulator.view;


import javax.swing.*;

import parkeersimulator.CarQueue;
import parkeersimulator.car.AbstractCar;
import parkeersimulator.model.Model;

import java.awt.*;

/**
 * This Class drawes the QueueView in the model and shows the missed cars info
 * 
 * @author ReneBakker
 * @version 2017-02-06
 * 
 *
 */
@SuppressWarnings("serial")
public class QueueView extends AbstractView {

	private int totalMissedMoney;
	private int totalMissedCars;

	private JLabel entrance1, entrance1Data;
	private JLabel entrance2, entrance2Data;
	private JLabel payment, paymentData;
	private JLabel exit, exitData;
	private JLabel missedCars, missedCarsNr, missedMoney, missedCarsMoney;

	private void init()
	{
		totalMissedMoney = 0;
		totalMissedCars = 0;
	}

	/**
	 * 
	 * @param model This is the model
	 */
	public QueueView(Model model) {

		super(model);

		init();

		setSize(200, 200);

		GridLayout layout = new GridLayout(0,2);

		entrance1 = new JLabel("1e Ingang:");
		entrance1Data = new JLabel("0");

		entrance2 = new JLabel("2e Ingang:");
		entrance2Data = new JLabel("0");		

		payment = new JLabel("Betaling:");
		paymentData = new JLabel("0");

		exit = new JLabel("Uitgang:");
		exitData = new JLabel("0");

		missedCars = new JLabel("Gemiste Auto's:");
		missedCarsNr = new JLabel("0");
		missedMoney = new JLabel("Gemiste Inkomsten:");
		missedCarsMoney = new JLabel("0");

		this.setLayout(layout);	
		add(entrance1);
		add(entrance1Data);
		add(entrance2);
		add(entrance2Data);
		add(payment);
		add(paymentData);
		add(exit);
		add(exitData);
		add(missedCars);
		add(missedCarsNr);
		add(missedMoney);
		add(missedCarsMoney);

	    setVisible(true);
	}

	public void paintComponent(Graphics g) {

		if(!model.isInSim()) {
			init();
		}

		super.paintComponent(g);

		setMissedCarsInfo();
		setQueueColor(g, model.getEntranceCarQueueNr());
		setQueueColor(g, model.getEntrancePassQueueNr());
		setQueueColor(g, model.getPaymentCarQueueNr());
		setQueueColor(g, model.exitCarQueueNr());

		entrance1Data.setText(""+ model.getEntranceCarQueueNr().carsInQueue());
		entrance2Data.setText(""+ model.getEntrancePassQueueNr().carsInQueue());
		paymentData.setText(""+ model.getPaymentCarQueueNr().carsInQueue());
		exitData.setText(""+ model.exitCarQueueNr().carsInQueue());
		missedCarsNr.setText(""+ totalMissedCars);
		missedCarsMoney.setText(""+ totalMissedMoney);
	}
	
	/**
	 * This method sets the amount of missed cars and the oumant of missed money
	 */
	private void setMissedCarsInfo() {
		CarQueue missedCars = model.getmissedCars();
		for(int i = 0; i < missedCars.carsInQueue(); i++) {
			totalMissedCars++;
			AbstractCar car = missedCars.removeCar();
			if (car != null) {
			int totalMinutes = car.getTotalMinutes();
			int perCarPrice = totalMinutes / 30;
            if ((perCarPrice % 30) != 0) {
            	perCarPrice = perCarPrice + 1;
            }
            totalMissedMoney += perCarPrice;
		}}
	}
	
	
	/**
	 * 
	 * @param g gets the Graphics g form the paintComponent
	 * @param entrance1Data This is a given CarQueue from the method paintComponent to get all the cars in a queue and display this on the screen
	 */
	private void setQueueColor(Graphics g, CarQueue entrance1Data) {
		int top = 0;
		if (entrance1Data == model.getEntrancePassQueueNr()) top = 0;
		if (entrance1Data == model.getEntrancePassQueueNr()) top = 33;
		if (entrance1Data == model.getPaymentCarQueueNr()) top = 66;
		if (entrance1Data == model.exitCarQueueNr()) top = 99;
		for (int i =0; i < entrance1Data.carsInQueue(); i++) {
			if (entrance1Data.peekCar(i) != null) {
				AbstractCar car = entrance1Data.peekCar(i); // <-- This gives an error on high speeds 
				g.setColor(car.getColor());
				g.fillRect(0 + (i * 21), top, 20, 10);
			}
		}
	}
}
