
package parkeersimulator.controller;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import parkeersimulator.ParkingSpot;
import parkeersimulator.model.Model;

@SuppressWarnings("serial")
public class SelectController extends AbstractController {

	private JLabel label;

	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JScrollPane scrollPane;

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

    	setVisible(true);
    }

    private void updateList() {
    	int selectedIndex = list.getSelectedIndex();
    	listModel.clear();
    	listModel.addElement("Normaal");
    	listModel.addElement("Abonnees");
    	listModel.addElement("Invaliden");
    	list.setSelectedIndex(selectedIndex);
    }

	private void enableOrDisable(boolean value) {
		list.setEnabled(value);
		scrollPane.setEnabled(value);
	}

	public void spotsChanged() {
//		updateList();
	}

	public void simStarted() {
		enableOrDisable(false);
	}

	public void simStopped() {
		enableOrDisable(true);
	}
}
