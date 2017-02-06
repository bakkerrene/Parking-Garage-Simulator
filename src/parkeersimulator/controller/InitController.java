
package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.model.Model;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

@SuppressWarnings("serial")
public class InitController extends AbstractController implements ActionListener {
	
	private JLabel filler1, filler2;
	private JLabel multiplier;
	private JLabel maxRes, maxAbo;
	private JLabel perInv, aboTa;
	private JLabel norTa, resTa;
	private JLabel queueEnter, queuePayment, queueExit;
	
	private JLabel weekDayAd, weekendAd, eventAd;
	private JLabel weekDayPass, weekendPass, eventPass;
	private JLabel weekDayRes, weekendRes, eventRes;

	private JLabel label;
	private JPanel a, b;

	private JFormattedTextField multiplierAmount;
	private JFormattedTextField aantalReserveringen;
    private JFormattedTextField aantalAbonnees;
    private JFormattedTextField percentageInvalidenplekken;
    private JFormattedTextField abonneeTarief;
    private JFormattedTextField normaalTarief;
    private JFormattedTextField reserveringsTarief;
    
    private JFormattedTextField weekDayAdArrivals, weekendAdArrivals, eventAdArrivals;
    private JFormattedTextField weekDayPassArrivals, weekendPassArrivals, eventPassArrivals;
    private JFormattedTextField weekDayResArrivals, weekendResArrivals, eventResArrivals;
    private JFormattedTextField enterSpeed, paymentSpeed, exitSpeed;
    
    private JFormattedTextField[] textFields = {multiplierAmount, aantalReserveringen, aantalAbonnees, percentageInvalidenplekken, abonneeTarief,
    											normaalTarief, reserveringsTarief, weekDayAdArrivals, weekendAdArrivals, eventAdArrivals,
    											weekDayPassArrivals, weekendPassArrivals, eventPassArrivals, weekDayResArrivals, weekendResArrivals,
    											eventResArrivals, enterSpeed, paymentSpeed, exitSpeed};
    
    
    

    private JLabel lblWeek, lblDag, lblUur;
    private JFormattedTextField resWeek;
    private JComboBox<String> resDag;
    private JComboBox<Integer> resUur;

    private JButton initButton;
    private JButton resetValues;
    
    private double defaultMultiplier = model.getMultiplier() * 100;
    private int defaultReservering = model.getReservering();
    private int defaultAbonnees = model.getAbonnees();
    private int defaultAboneeTarief = model.getAbonneeTarief();
    private int defaultNormaalTarief = model.getNormaalTarief();
    private int defaultReserveringTarief = model.getReserveringTarief();
    private int defaultHandicapPercentage = model.getHandicapPercentage();
    private int defaultEnterSpeed = model.getEnterSpeed();
    private int defaultPaymentSpeed = model.getpaymentSpeed();
    private int defaultExitSpeed = model.getExitSpeed();
    private int defaultWeekDayAdArrivals = model.getWeekDayAdArrivals();
    private int defaultWeekendAdArrivals = model.getWeekendAdArrivals();
    private int defaultEventAdArrivals = model.geteventAdArrivals();
    private int defaultWeekDayPassArrivals = model.getWeekDayPassArrivals();
    private int defaultWeekendPassArrivals = model.getWeekendPassArrivals();
    private int defaultEventPassArrivals = model.geteventPassArrivals();
    private int defaultWeekDayResArrivals = model.getWeekDayResArrivals();
    private int defaultWeekendResArrivals = model.getWeekendResArrivals();
    private int defaultEventResArrivals = model.geteventResArrivals();


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

        model.setMultiplier(defaultMultiplier);
        model.setReservering(defaultReservering);
        model.setAbonnees(defaultAbonnees);
        model.setAbonneeTarief(defaultAboneeTarief);
        model.setNormaalTarief(defaultNormaalTarief);
        model.setReserveringTarief(defaultReserveringTarief);
        model.setHandicapPercentage(defaultHandicapPercentage);
        model.setEnterSpeed(defaultEnterSpeed);
        model.setpaymentSpeed(defaultPaymentSpeed);
        model.setExitSpeed(defaultExitSpeed);
        model.setWeekDayAdArrivals(defaultWeekDayAdArrivals);
        model.setWeekendAdArrivals(defaultWeekendAdArrivals);
        model.seteventAdArrivals(defaultEventAdArrivals);
        model.setWeekDayPassArrivals(defaultWeekDayPassArrivals);
        model.setWeekendPassArrivals(defaultWeekendPassArrivals);
        model.seteventPassArrivals(defaultEventPassArrivals);
        model.setWeekDayResArrivals(defaultWeekDayResArrivals);
        model.setWeekendResArrivals(defaultWeekendResArrivals);
        model.seteventResArrivals(defaultEventResArrivals);
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
    	enterSpeed.setValue(model.getEnterSpeed());
    	paymentSpeed.setValue(model.getpaymentSpeed());
    	exitSpeed.setValue(model.getExitSpeed());
    	weekDayAdArrivals.setValue(model.getWeekDayAdArrivals());
    	weekendAdArrivals.setValue(model.getWeekendAdArrivals());
    	eventAdArrivals.setValue(model.geteventAdArrivals());
    	weekDayPassArrivals.setValue(model.getWeekDayPassArrivals());
    	weekendPassArrivals.setValue(model.getWeekendPassArrivals());
    	eventPassArrivals.setValue(model.geteventPassArrivals());
    	weekDayResArrivals.setValue(model.getWeekDayResArrivals());
    	weekendResArrivals.setValue(model.getWeekendResArrivals());
    	eventResArrivals.setValue(model.geteventResArrivals());
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
    	enterSpeed = new JFormattedTextField();
    	paymentSpeed = new JFormattedTextField();
    	exitSpeed = new JFormattedTextField();
    	weekDayAdArrivals = new JFormattedTextField();
    	weekendAdArrivals = new JFormattedTextField();
    	eventAdArrivals = new JFormattedTextField();
    	weekDayPassArrivals = new JFormattedTextField();
    	weekendPassArrivals = new JFormattedTextField();
    	eventPassArrivals = new JFormattedTextField();
    	weekDayResArrivals = new JFormattedTextField();
    	weekendResArrivals = new JFormattedTextField();
    	eventResArrivals = new JFormattedTextField();
    	

        lblWeek = new JLabel(" Reservering (W:D:H)");
        //lblDag = new JLabel("Reservering Dag");
        //lblUur = new JLabel("Reservering Uur");

        resWeek = new JFormattedTextField("0");
        String[] dagStrings = { "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag" };
        resDag = new JComboBox<String>(dagStrings);
        Integer[] uurInts = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
        resUur = new JComboBox<Integer>(uurInts);

    	initFocusListener(multiplierAmount);
    	initFocusListener(aantalReserveringen);
    	initFocusListener(aantalAbonnees);
    	initFocusListener(percentageInvalidenplekken);
    	initFocusListener(abonneeTarief);
    	initFocusListener(normaalTarief);
    	initFocusListener(reserveringsTarief);
    	initFocusListener(enterSpeed);
    	initFocusListener(paymentSpeed);
    	initFocusListener(exitSpeed);
    	initFocusListener(weekDayAdArrivals);
    	initFocusListener(weekendAdArrivals);
    	initFocusListener(eventAdArrivals);
    	initFocusListener(weekDayPassArrivals);
    	initFocusListener(weekendPassArrivals);
    	initFocusListener(eventPassArrivals);
    	initFocusListener(weekDayResArrivals);
    	initFocusListener(weekendResArrivals);
    	initFocusListener(eventResArrivals);



    	initButton = new JButton("Verstuur");
    	initButton.addActionListener(this);

    	resetValues = new JButton("Reset Waarden");
    	resetValues.addActionListener(this);

    	multiplier = new JLabel(" Vermenigvuldiger (%)");
 		maxRes = new JLabel(" Reserveringen");
 		maxAbo = new JLabel(" Abonnees");
 		perInv = new JLabel(" Invaliden (%)");
 		aboTa = new JLabel(" Abonnee Tarief");
 		norTa = new JLabel(" Normaal Tarief");
 		resTa = new JLabel(" Reservering Tarief");
 		queueEnter = new JLabel(" Enter Speed");
 		queuePayment = new JLabel(" Betaal Snelheid");
 		queueExit = new JLabel(" Exit Speed");
 		weekDayAd = new JLabel(" WeekDayAd");
 		weekendAd = new JLabel(" WeekendAd");
 		eventAd = new JLabel(" EventAd");
 		weekDayPass = new JLabel(" WeekDayPass");
 		weekendPass = new JLabel(" WeekendPass");
 		eventPass = new JLabel(" EventPass");
 		weekDayRes = new JLabel(" WeekDayRes");
 		weekendRes = new JLabel(" WeekendRes");
 		eventRes = new JLabel(" EventRes");
 		filler1 = new JLabel("");
 		filler2 = new JLabel("");

    	BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);



		
		
		a = new JPanel();
		b = new JPanel();
		GridLayout gridLayout1 = new GridLayout(0,6);
		GridLayout gridLayout2 = new GridLayout(0,2);
		
		a.setLayout(gridLayout1);

		a.add(multiplier); a.add(multiplierAmount); a.add(queueEnter); a.add(enterSpeed);  a.add(weekDayPass); a.add(weekDayPassArrivals);
		a.add(maxRes); a.add(aantalReserveringen); a.add(queuePayment); a.add(paymentSpeed); a.add(weekendPass); a.add(weekendPassArrivals);
		a.add(maxAbo); a.add(aantalAbonnees); a.add(queueExit); a.add(exitSpeed);  a.add(eventPass); a.add(eventPassArrivals);
		a.add(perInv); a.add(percentageInvalidenplekken); a.add(weekDayAd); a.add(weekDayAdArrivals); a.add(weekDayRes); a.add(weekDayResArrivals);
		a.add(norTa); a.add(normaalTarief); a.add(weekendAd); a.add(weekendAdArrivals);  a.add(weekendRes); a.add(weekendResArrivals);
		a.add(resTa); a.add(reserveringsTarief); a.add(eventAd); a.add(eventAdArrivals);  a.add(eventRes); a.add(eventResArrivals);
		a.add(aboTa); a.add(abonneeTarief); a.add(lblWeek); a.add(resWeek); a.add(resDag); a.add(resUur);
		
		 
		 
		b.setLayout(gridLayout2);
		b.add(initButton); b.add(resetValues);
		add(a);
		add(b);
    	setDefaultValues();

    	setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
		try {
			Object sourceObject = e.getSource();
			if (sourceObject == initButton) {
				model.playSound("button.wav");
				
				model.setMultiplier(getMultiplier());
				model.setReservering(getReservering());
				model.setAbonnees(getAbonee());
				model.setHandicapPercentage(getHandiPer());
				model.setAbonneeTarief(getAboneeTarief());
				model.setNormaalTarief(getNormaalTarief());
				model.setReserveringTarief(getReserveringTarief());
				model.setEnterSpeed(getEnterSpeed());
				model.setpaymentSpeed(getPaymentSpeed());
				model.setExitSpeed(getExitSpeed());
				model.setWeekDayAdArrivals(getWeekDayAd());
				model.setWeekendAdArrivals(getWeekendAd());
				model.seteventAdArrivals(getEventAd());
				model.setWeekDayPassArrivals(getWeekDayPass());
				model.setWeekendPassArrivals(getWeekendPass());
				model.seteventPassArrivals(getEventPass());
				model.setWeekDayResArrivals(getWeekDayRes());
				model.setWeekendResArrivals(getWeekendRes());
				model.seteventResArrivals(getEventRes());
				
				int dag = resDag.getSelectedIndex();
				int uur = resUur.getSelectedIndex();
				model.setSpecialReservering(getResWeek(), dag, uur);
			} else if (sourceObject == resetValues) {
				model.playSound("button.wav");
				
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

	private int getResWeek() throws NumberFormatException {
		return Integer.parseInt(resWeek.getText());
	}
	private int getEnterSpeed() throws NumberFormatException {
		return Integer.parseInt(enterSpeed.getText());
	}

	private int getPaymentSpeed() throws NumberFormatException {
		return Integer.parseInt(paymentSpeed.getText());
	}

	private int getExitSpeed() throws NumberFormatException {
		return Integer.parseInt(exitSpeed.getText());
	}
	
	private int getWeekDayAd() throws NumberFormatException {
		return Integer.parseInt(weekDayAdArrivals.getText());
	}
	private int getWeekendAd() throws NumberFormatException {
		return Integer.parseInt(weekendAdArrivals.getText());
	}
	private int getEventAd() throws NumberFormatException {
		return Integer.parseInt(eventAdArrivals.getText());
	}
	private int getWeekDayPass() throws NumberFormatException {
		return Integer.parseInt(weekDayPassArrivals.getText());
	}
	private int getWeekendPass() throws NumberFormatException {
		return Integer.parseInt(weekendPassArrivals.getText());
	}
	private int getEventPass() throws NumberFormatException {
		return Integer.parseInt(eventPassArrivals.getText());
	}
	private int getWeekDayRes() throws NumberFormatException {
		return Integer.parseInt(weekDayResArrivals.getText());
	}
	private int getWeekendRes() throws NumberFormatException {
		return Integer.parseInt(weekendResArrivals.getText());
	}
	private int getEventRes() throws NumberFormatException {
		return Integer.parseInt(eventResArrivals.getText());
	}

	/**
	private void enableOrDisable(boolean value) {
		multiplier.setEnabled(value);
		aantalReserveringen.setEnabled(value);
		aantalAbonnees.setEnabled(value);
		percentageInvalidenplekken.setEnabled(value);
		abonneeTarief.setEnabled(value);
		normaalTarief.setEnabled(value);
		reserveringsTarief.setEnabled(value);
	    //initButton.setEnabled(value);

	    resWeek.setEnabled(value);;
	    resDag.setEnabled(value);;
	    resUur.setEnabled(value);;
	}
	

	public void simStarted() {
		enableOrDisable(false);
	}

	public void simStopped() {
		enableOrDisable(true);
	}
	*/


}
