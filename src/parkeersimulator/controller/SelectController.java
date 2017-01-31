package parkeersimulator.controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import parkeersimulator.Location;
import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class SelectController extends AbstractController implements ActionListener {

    private JButton clearButton;

	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JScrollPane scrollPane;

    public SelectController(Model model) {

    	super(model);

    	clearButton = new JButton("Verwijder Plekken");
    	clearButton.addActionListener(this);

    	BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateList();
		list.setSelectedIndex(0);
		scrollPane = new JScrollPane(list);
		add(scrollPane);

		add(clearButton);

    	setVisible(true);
    }

    private void updateList() {
    	int selectedIndex = list.getSelectedIndex();
    	listModel.clear();
    	listModel.addElement(model.getSpotCountForType(ParkingSpot.TYPE_AD_HOC) + " Regulieren");
    	listModel.addElement(model.getSpotCountForType(ParkingSpot.TYPE_PASS) + " Abonnees");
    	listModel.addElement(model.getSpotCountForType(ParkingSpot.TYPE_HANDI) + " Invaliden");
    	listModel.addElement(model.getSpotCountForType(ParkingSpot.TYPE_RES) + " Gereserveerden");
    	list.setSelectedIndex(selectedIndex);
    }

    public void actionPerformed(ActionEvent e) {
		try {
			//Object sourceObject = e.getSource();
			//if (sourceObject == clearButton) {
				if (!model.isInSim()) {
					model.clearSpots();
					updateList();
				}
			//}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void clickedSpot(Location location) {
		if (model.isRunning()) return;
		if (model.isInSim()) {
			ParkingSpot spot = model.getParkingSpotAt(location);
			if (spot.getCar() != null) return;
		}
		model.setSpotType(location, list.getSelectedIndex());
		updateList();
	}
}