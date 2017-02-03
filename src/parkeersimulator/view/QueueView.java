package parkeersimulator.view;


import javax.swing.*;

import parkeersimulator.CarQueue;
import parkeersimulator.car.AbstractCar;
import parkeersimulator.model.Model;

import java.awt.*;

@SuppressWarnings("serial")
public class QueueView extends AbstractView {

	private int totalMissedMoney = 0;
	private int totalMissedCars = 0;

	private JLabel entrance1, entrance1Data;
	private JLabel entrance2, entrance2Data;
	private JLabel payment, paymentData;
	private JLabel exit, exitData;
	private JLabel missedCars, missedCarsNr, missedMoney, missedCarsMoney;


	public QueueView(Model model) {

		super(model);

		setSize(200, 200);

		GridLayout layout = new GridLayout(0,2);

		entrance1 = new JLabel("Entrance1 ");
		entrance1Data = new JLabel("0");

		entrance2 = new JLabel("Entrance2");
		entrance2Data = new JLabel("0");		

		payment = new JLabel("Payment");
		paymentData = new JLabel("0");

		exit = new JLabel("Exit");
		exitData = new JLabel("0");

		missedCars = new JLabel("Missed Cars:");
		missedCarsNr = new JLabel("0");
		missedMoney = new JLabel("Missed Money");
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
	
	private void setMissedCarsInfo() {
		CarQueue missedCars = model.getmissedCars();
		for(int i = 0; i < missedCars.carsInQueue(); i++) {
			totalMissedCars++;
			AbstractCar car = missedCars.removeCar();
			int totalMinutes = car.getTotalMinutes();
			int perCarPrice = totalMinutes / 30;
            if ((perCarPrice % 30) != 0) {
            	perCarPrice = perCarPrice + 1;
            }
            totalMissedMoney += perCarPrice;
		}
	}
	
	private void setQueueColor(Graphics g, CarQueue entrance1Data) {
		int top = 0;
		if (entrance1Data == model.getEntrancePassQueueNr()) top = 0;
		if (entrance1Data == model.getEntrancePassQueueNr()) top = 33;
		if (entrance1Data == model.getPaymentCarQueueNr()) top = 66;
		if (entrance1Data == model.exitCarQueueNr()) top = 99;
		for (int i =0; i < entrance1Data.carsInQueue(); i++) {
			if (entrance1Data.peekCar(i) != null) {
				AbstractCar car = entrance1Data.peekCar(i);
				g.setColor(car.getColor());
				g.fillRect(0 + (i * 21), top, 20, 10);
		}
		}
		
	}
}
