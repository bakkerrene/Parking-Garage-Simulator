package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class InitController extends AbstractController implements ActionListener {

	private JLabel speed;
	private JLabel maxRes, maxAbo;
	private JLabel perInv, aboTa;
	private JLabel norTa, resTa;

	private JFormattedTextField tickPause;
	private JFormattedTextField aantalReserveringen;
    private JFormattedTextField aantalAbonnees;
    private JFormattedTextField percentageInvalidenplekken;
    private JFormattedTextField abonneeTarief;
    private JFormattedTextField normaalTarief;
    private JFormattedTextField reserveringsTarief;
    private JButton initButton;

    public void InitHandiCars() {
		int handiPer = getHandiPer();
		int handiCount = (int)(Math.ceil(((handiPer / 100.0) * model.getTotalSpotCount())));
		int floor = 0, row = 0, place = 0;
		for (int x = 0; x < handiCount; x++) {
			Location location = new Location(floor, row, place);
			model.setSpotType(location, ParkingSpot.TYPE_HANDI);
			place++;
			if (place >= model.getNumberOfPlaces()) {
				place = 0;
				row++;
				if(row >= model.getNumberOfRows()) {
					row = 0;
					floor++;
				}
			}
		}
    }

    public InitController(Model model) {

    	super(model);

    	tickPause = new JFormattedTextField(model.getTickPause());
    	aantalReserveringen = new JFormattedTextField(model.getReservering());
    	aantalAbonnees = new JFormattedTextField(model.getAbonnees());
    	percentageInvalidenplekken = new JFormattedTextField(model.getHandicapPercentage());
    	abonneeTarief = new JFormattedTextField(model.getAbonneeTarief());
    	normaalTarief = new JFormattedTextField(model.getNormaalTarief());   
    	reserveringsTarief = new JFormattedTextField(model.getReserveringTarief());
    	initButton = new JButton("Verstuur");
    	initButton.addActionListener(this);

    	speed = new JLabel("Snelheid");
 		maxRes = new JLabel("Reserveringen");
 		maxAbo = new JLabel("Abonnees");
 		perInv = new JLabel("Invaliden (%)");
 		aboTa = new JLabel("Abonnee Tarief");
 		norTa = new JLabel("Normaal Tarief");
 		resTa = new JLabel("Reservering Tarief");

		GridLayout gridLayout = new GridLayout(0,2);
		setLayout(gridLayout);

		add(speed);
		add(tickPause);
		add(maxRes);
		add(aantalReserveringen);
		add(maxAbo);
		add(aantalAbonnees);
		add(perInv);
		add(percentageInvalidenplekken);
		add(aboTa);
		add(abonneeTarief);
		add(norTa);
		add(normaalTarief);
		add(resTa);
		add(reserveringsTarief);
		add(initButton);

		InitHandiCars();

    	setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
		try {
			//Object sourceObject = e.getSource();
			//if(sourceObject == initButton) {
				model.setTickPause(getTickPause());
				model.setReservering(getReservering());
				model.setAbonnees(getAbonee());
				model.setHandicapPercentage(getHandiPer());
				model.setAbonneeTarief(getAboneeTarief());
				model.setNormaalTarief(getNormaalTarief());
				model.setReserveringTarief(getReserveringTarief());
				InitHandiCars();
			//}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private int getTickPause() throws NumberFormatException {
		return Integer.parseInt(tickPause.getText());
	}

	private int getReservering() throws NumberFormatException {
		return Integer.parseInt(aantalReserveringen.getText());
	}

	private int getAbonee() throws NumberFormatException {
		return Integer.parseInt(aantalAbonnees.getText());
	}

	private int getHandiPer() throws NumberFormatException {
		return Integer.parseInt(percentageInvalidenplekken.getText());
	}

	private int getAboneeTarief() throws NumberFormatException {
		return Integer.parseInt(abonneeTarief.getText());
	}

	private int getNormaalTarief() throws NumberFormatException {
		return Integer.parseInt(normaalTarief.getText());
	}

	private int getReserveringTarief() throws NumberFormatException {
		return Integer.parseInt(reserveringsTarief.getText());
	}

	public void clickedSpot(Location location) {
	}
}