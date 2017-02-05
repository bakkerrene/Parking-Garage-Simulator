
package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class InitController extends AbstractController implements ActionListener {

	private JLabel multiplier;
	private JLabel maxRes, maxAbo;
	private JLabel perInv, aboTa;
	private JLabel norTa, resTa;

	private JLabel label;
	private JPanel panel;

	private JFormattedTextField multiplierAmount;
	private JFormattedTextField aantalReserveringen;
    private JFormattedTextField aantalAbonnees;
    private JFormattedTextField percentageInvalidenplekken;
    private JFormattedTextField abonneeTarief;
    private JFormattedTextField normaalTarief;
    private JFormattedTextField reserveringsTarief;
    private JButton initButton;
    private JButton resetValues;

    private void initFocusListener(JFormattedTextField textField)
    {
    	textField.addFocusListener(new java.awt.event.FocusAdapter() {
    		public void focusGained(java.awt.event.FocusEvent evt) {
    			SwingUtilities.invokeLater(new Runnable() {
    				@Override
    				public void run() {
    					textField.selectAll();
    				}
    			});
    		}
    	});
    }

    private void setDefaultValues() {

        model.setMultiplier(100);
        model.setReservering(15);
        model.setAbonnees(50);
        model.setAbonneeTarief(10);
        model.setNormaalTarief(1);
        model.setReserveringTarief(10);
        model.setHandicapPercentage(2);

        updateTextFieldValues();
    }

    private void updateTextFieldValues() {
    	multiplierAmount.setValue(model.getMultiplier() * 100);
    	aantalReserveringen.setValue(model.getReservering());
    	aantalAbonnees.setValue(model.getAbonnees());
    	percentageInvalidenplekken.setValue(model.getHandicapPercentage());
    	abonneeTarief.setValue(model.getAbonneeTarief());
    	normaalTarief.setValue(model.getNormaalTarief());   
    	reserveringsTarief.setValue(model.getReserveringTarief());
    }

    public InitController(Model model) {

    	super(model);

    	multiplierAmount = new JFormattedTextField();
    	aantalReserveringen = new JFormattedTextField();
    	aantalAbonnees = new JFormattedTextField();
    	percentageInvalidenplekken = new JFormattedTextField();
    	abonneeTarief = new JFormattedTextField();
    	normaalTarief = new JFormattedTextField();   
    	reserveringsTarief = new JFormattedTextField();

    	initFocusListener(multiplierAmount);
    	initFocusListener(aantalReserveringen);
    	initFocusListener(aantalAbonnees);
    	initFocusListener(percentageInvalidenplekken);
    	initFocusListener(abonneeTarief);
    	initFocusListener(normaalTarief);
    	initFocusListener(reserveringsTarief);

    	setDefaultValues();

    	initButton = new JButton("Verstuur");
    	initButton.addActionListener(this);

    	resetValues = new JButton("Reset Waarden");
    	resetValues.addActionListener(this);

    	multiplier = new JLabel("Vermenigvuldiger (%)");
 		maxRes = new JLabel("Reserveringen");
 		maxAbo = new JLabel("Abonnees");
 		perInv = new JLabel("Invaliden (%)");
 		aboTa = new JLabel("Abonnee Tarief");
 		norTa = new JLabel("Normaal Tarief");
 		resTa = new JLabel("Reservering Tarief");

    	BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		label = new JLabel("Waarden");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label);

    	panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,2);
		panel.setLayout(gridLayout);

		panel.add(multiplier);
		panel.add(multiplierAmount);
		panel.add(maxRes);
		panel.add(aantalReserveringen);
		panel.add(maxAbo);
		panel.add(aantalAbonnees);
		panel.add(perInv);
		panel.add(percentageInvalidenplekken);
		panel.add(aboTa);
		panel.add(abonneeTarief);
		panel.add(norTa);
		panel.add(normaalTarief);
		panel.add(resTa);
		panel.add(reserveringsTarief);
		panel.add(initButton);
		panel.add(resetValues);

		add(panel);

    	setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
		try {
			Object sourceObject = e.getSource();
			if (sourceObject == initButton) {
				model.setMultiplier(getMultiplier());
				model.setReservering(getReservering());
				model.setAbonnees(getAbonee());
				model.setHandicapPercentage(getHandiPer());
				model.setAbonneeTarief(getAboneeTarief());
				model.setNormaalTarief(getNormaalTarief());
				model.setReserveringTarief(getReserveringTarief());
				model.initDefaultSpots();
			} else if (sourceObject == resetValues) {
				setDefaultValues();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private double getMultiplier() throws NumberFormatException {
		return Double.parseDouble(multiplierAmount.getText());
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

	private void enableOrDisable(boolean value) {
		multiplierAmount.setEnabled(value);
		aantalReserveringen.setEnabled(value);
		aantalAbonnees.setEnabled(value);
		percentageInvalidenplekken.setEnabled(value);
		abonneeTarief.setEnabled(value);
		normaalTarief.setEnabled(value);
		reserveringsTarief.setEnabled(value);
	    initButton.setEnabled(value);
	    resetValues.setEnabled(value);
	}

	public void simStarted() {
		enableOrDisable(false);
	}

	public void simStopped() {
		enableOrDisable(true);
	}

	public void spotsChanged() {
		updateTextFieldValues();
	}
}
