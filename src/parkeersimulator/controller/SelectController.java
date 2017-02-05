package parkeersimulator.controller;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class SelectController extends AbstractController implements ActionListener {

	private JLabel label;

	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JScrollPane scrollPane;

    private JButton clearButton;

    public SelectController(Model model) {

    	super(model);

    	BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		label = new JLabel("Parkeerplekken");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label);

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateList();
		list.setSelectedIndex(0);
		scrollPane = new JScrollPane(list);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(scrollPane);

		ListSelectionListener selectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					int spotType = ParkingSpot.TYPE_AD_HOC;
					switch (list.getSelectedIndex()) {
					case 0: spotType = ParkingSpot.TYPE_AD_HOC; break;
					case 1: spotType = ParkingSpot.TYPE_PASS; break;
					case 2: spotType = ParkingSpot.TYPE_HANDI; break;
					}
					model.setSelectedSpotType(spotType);
				}
			}
		};
		list.addListSelectionListener(selectionListener);

    	clearButton = new JButton("Verwijder Plekken");
    	clearButton.addActionListener(this);
    	clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(clearButton);

    	setVisible(true);
    }

    private void updateList() {
    	int selectedIndex = list.getSelectedIndex();
    	listModel.clear();
    	listModel.addElement("Clear");
    	listModel.addElement(model.getSpotCountForType(ParkingSpot.TYPE_PASS) + " Abonnees");
    	listModel.addElement(model.getSpotCountForType(ParkingSpot.TYPE_HANDI) + " Invaliden");
    	list.setSelectedIndex(selectedIndex);
    }

    public void actionPerformed(ActionEvent e) {
		try {
			//Object sourceObject = e.getSource();
			//if (sourceObject == clearButton) {
				if (!model.isInSim()) {
					model.clearSpots();
					model.initDefaultSpots();
					updateList();
				}
			//}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void enableOrDisable(boolean value) {
		clearButton.setEnabled(value);
		list.setEnabled(value);
		scrollPane.setEnabled(value);
	}

	public void spotsChanged() {
		updateList();
	}

	public void simStarted() {
		enableOrDisable(false);
	}

	public void simStopped() {
		enableOrDisable(true);
	}
}
