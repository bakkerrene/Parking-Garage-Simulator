package parkeersimulator.view;
import javax.swing.*;
import parkeersimulator.model.Model;

import java.awt.Graphics;
import java.awt.event.*;

@SuppressWarnings("serial")
public class LegendaView extends AbstractView {
	public LegendaView(Model model) {
		
		super(model);
	
	
	JLabel textarea = new JLabel(": \n\n" +
	"Blanco: Lege parkeerplek. \n" +
	"Rood: Regulier. \n" +
	"Blauw: Abbonnement. \n" +
	"Groen: Reservering. \n" +
	"Geel: Gehandicapt.");
	this.setLayout(null);
	
	add(textarea);
	setVisible(true);
	}
	
	

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		
	}
	
	
	

	

}
