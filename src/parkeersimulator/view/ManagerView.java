package parkeersimulator.view;


import javax.swing.*;

import parkeersimulator.model.Model;

import java.awt.*;
import java.util.HashMap;


/**draws the managerView
 * 
 * @author reneb
 *@version 2017-02-06
 */
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
	
	HashMap<String, Integer> carCounter;

	/**
	 * 
	 * @param model This is the model
	 */
	public ManagerView(Model model) {

		super(model);

		setSize(200, 200);

		GridLayout layout = new GridLayout(0,2);
		this.setLayout(layout);

		time = new JLabel(model.getDay());
		labelDataTijd = new JLabel("00:00");

		labelUur = new JLabel("Laatse Uur: ");
		labelDataUur = new JLabel("0");		

		labelDag = new JLabel("Laatse Dag: ");
		labelDataDag = new JLabel("0");

		labelWeek = new JLabel("Laatse Week: ");
		labelDataWeek = new JLabel("0");

		labelInGarage = new JLabel("Geld In Garage: ");
		labelDataInGarage = new JLabel("0");		

		red = new JLabel("Normaal:");
		redCar = new JLabel("0");		

		blue = new JLabel("Abonnee:");
		blueCar = new JLabel("0");

		yellow = new JLabel("Reservering:");
		yellowCar = new JLabel("0");

		green = new JLabel("Invalide:");
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

	/**
	 * This method updates the View
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		carCounter = model.getTotalCars();

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

		int redCount = carCounter.get("adhoc");
		int blueCount = carCounter.get("pass");
		int greenCount = carCounter.get("handi");
		int yellowCount = carCounter.get("res");

		redCar.setText("" + redCount);
		blueCar.setText("" + blueCount); //mag nooit groter zijn dan max abonnees
		yellowCar.setText("" + yellowCount); //mag nooit groter zijn dan aantal reserveringen en max aantal reserveringen
		greenCar.setText("" + greenCount); //is % als alle plekken vol zijn kunnen ze nog ergens anders parkeren
	}
}
