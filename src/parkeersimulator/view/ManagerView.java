package parkeersimulator.view;


import javax.swing.*;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

import java.awt.*;

@SuppressWarnings("serial")
public class ManagerView extends AbstractView {

	private JLabel time, labelDataTijd;
	private JLabel labelUur, labelDataUur;
	private JLabel labelDag, labelDataDag;
	private JLabel labelWeek, labelDataWeek;
	private JLabel labelInGarage, labelDataInGarage;
	private JLabel red, redCar;
	private JLabel blue, blueCar;
	private JLabel yellow, yellowCar;
	private JLabel green, greenCar;

	public ManagerView(Model model) {

		super(model);

		setSize(200, 200);

		GridLayout layout = new GridLayout(0,2);
		this.setLayout(layout);

		time = new JLabel(model.getDay());
		labelDataTijd = new JLabel("00:00");

		labelUur = new JLabel("Laatse uur: ");
		labelDataUur = new JLabel("0");		

		labelDag = new JLabel("Laatse dag: ");
		labelDataDag = new JLabel("0");

		labelWeek = new JLabel("Laatse week: ");
		labelDataWeek = new JLabel("0");

		labelInGarage = new JLabel("In garage: ");
		labelDataInGarage = new JLabel("0");		

		red = new JLabel("ADHOC");
		redCar = new JLabel("0");		

		blue = new JLabel("PASS");
		blueCar = new JLabel("0");

		yellow = new JLabel("RES");
		yellowCar = new JLabel("0");

		green = new JLabel("INVA");
		greenCar = new JLabel("0");	

		add(time);
		add(labelDataTijd);
		add(labelUur);
		add(labelDataUur);
		add(labelDag);
		add(labelDataDag);
		add(labelWeek);
		add(labelDataWeek);
		add(labelInGarage);
		add(labelDataInGarage);	
		add(red);
		add(redCar);
		add(blue);
		add(blueCar);
		add(yellow);
		add(yellowCar);
		add(green);
		add(greenCar);	

	    setVisible(true);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		int hour = model.getMoneyLastHour();
		int day = model.getMoneyLastDay();
		int week = model.getMoneyLastWeek();
		int inGarage = model.getMoneyInGarage();

		time.setText(model.getDay());
		labelDataTijd.setText(model.getTime());
		labelDataUur.setText(""+hour);
		labelDataDag.setText(""+day);
		labelDataWeek.setText(""+week);
		labelDataInGarage.setText(""+inGarage);

		int redCount = model.getCarCountForType(ParkingSpot.TYPE_AD_HOC);
		int blueCount = model.getCarCountForType(ParkingSpot.TYPE_PASS);
		int greenCount = model.getCarCountForType(ParkingSpot.TYPE_HANDI);
		int yellowCount = model.getCarCountForType(ParkingSpot.TYPE_RES);

		redCar.setText("" + redCount);
		blueCar.setText("" + blueCount); //mag nooit groter zijn dan max abonnees
		yellowCar.setText("" + yellowCount); //mag nooit groter zijn dan aantal reserveringen en max aantal reserveringen
		greenCar.setText("" + greenCount); //is % als alle plekken vol zijn kunnen ze nog ergens anders parkeren
	}
}