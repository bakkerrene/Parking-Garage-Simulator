package parkeersimulator.controller;


import javax.swing.*;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class InitController extends AbstractController implements ActionListener {

	private JPanel gridPanel;

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

	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JScrollPane scrollPane;

    private void updateList() {
    	int selectedIndex = list.getSelectedIndex();
    	listModel.clear();
    	listModel.addElement("Ad Hoc (" + model.getSpotCountForType(ParkingSpot.TYPE_AD_HOC) + ")");
    	listModel.addElement("Pass (" + model.getSpotCountForType(ParkingSpot.TYPE_PASS) + ")");
    	listModel.addElement("Handi (" + model.getSpotCountForType(ParkingSpot.TYPE_HANDI) + ")");
    	listModel.addElement("Res (" + model.getSpotCountForType(ParkingSpot.TYPE_RES) + ")");
    	list.setSelectedIndex(selectedIndex);
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
    	initButton = new JButton("Submit");
    	initButton.addActionListener(this);

    	speed = new JLabel("TickPause");
 		maxRes = new JLabel("Reserveringen");
 		maxAbo = new JLabel("Abonnees");
 		perInv = new JLabel("percentage invalide");
 		aboTa = new JLabel("AbonneeTarief");
 		norTa = new JLabel("NormaalTarief");
 		resTa = new JLabel("ReserveringTarief");

		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateList();
		list.setSelectedIndex(0);
		scrollPane = new JScrollPane(list);
		add(scrollPane);

 		gridPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,2);
		gridPanel.setLayout(gridLayout);
		gridPanel.add(speed);
		gridPanel.add(tickPause);
		gridPanel.add(maxRes);
		gridPanel.add(aantalReserveringen);
		gridPanel.add(maxAbo);
		gridPanel.add(aantalAbonnees);
		gridPanel.add(perInv);
		gridPanel.add(percentageInvalidenplekken);
		gridPanel.add(aboTa);
		gridPanel.add(abonneeTarief);
		gridPanel.add(norTa);
		gridPanel.add(normaalTarief);
		gridPanel.add(resTa);
		gridPanel.add(reserveringsTarief);
		gridPanel.add(initButton);
		add(gridPanel);

    	setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
		try {
			model.setTickPause(getTickPause());
			model.setReservering(getReservering());
			model.setAbonnees(getAbonee());
			model.setHandicapPercentage(getHandiPer());
			model.setAbonneeTarief(getAboneeTarief());
			model.setNormaalTarief(getNormaalTarief());
			model.setReserveringTarief(getReserveringTarief());
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
		model.setSpotType(location, list.getSelectedIndex());
		updateList();
	}
}