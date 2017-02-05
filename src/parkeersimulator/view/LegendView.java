package parkeersimulator.view;
import javax.swing.*;
import parkeersimulator.model.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

@SuppressWarnings("serial")
public class LegendView extends AbstractView {

	/*
	 * rood is regulier
	 * blauw is abonnement
	 * groen is handi
	 * geel is reservering
     *
	 * wit is een reguliere parkeerplaats
	 * blauw is een parkeerplaats voor abonees
	 * 
	 */

	JLabel label0, label1, label2, label3;
	JLabel label4, label5, label6, label7;

	public LegendView(Model model) {

		super(model);
		this.setLayout(null);

		int y = 10;

		/* Parkeerplaatsen */
		label0 = new JLabel("Normale Plek");
		label0.setBounds(60, y, 200, 15);
		add(label0);
		y += 20;
		label1 = new JLabel("Abonnements Plek");
		label1.setBounds(60, y, 200, 15);
		add(label1);
		y += 20;
		label2 = new JLabel("Invalide Plek");
		label2.setBounds(60, y, 200, 15);
		add(label2);
		y += 20;
		label3 = new JLabel("Gereserveerde Plek");
		label3.setBounds(60, y, 200, 15);
		add(label3);
		y += 40;

		/* Auto's */
		label4 = new JLabel("Normalen");
		label4.setBounds(60, y, 200, 15);
		add(label4);
		y += 20;
		label5 = new JLabel("Abonnees");
		label5.setBounds(60, y, 200, 15);
		add(label5);
		y += 20;
		label6 = new JLabel("Invaliden");
		label6.setBounds(60, y, 200, 15);
		add(label6);
		y += 20;
		label7 = new JLabel("Reserveringen");
		label7.setBounds(60, y, 200, 15);
		add(label7);

		setVisible(true);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		/*
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        */

		int y = 10;

        /* Parkeerplaatsen */
        // case TYPE_AD_HOC
        g.setColor(new Color(255, 255, 255, 255));
        g.fillRect(10, y, 30, 15);
        y += 20;
    	// case TYPE_PASS
        g.setColor(new Color(0, 0, 255, 64));
        g.fillRect(10, y, 30, 15);
        y += 20;
    	// case TYPE_HANDI
    	g.setColor(new Color(0, 255, 0, 64));
    	g.fillRect(10, y, 30, 15);
        y += 20;
    	// case TYPE_RES
    	g.setColor(new Color(255, 255, 0, 64));
    	g.fillRect(10, y, 30, 15);
        y += 40;

		/* Auto's */
        g.setColor(Color.RED);
        g.fillRect(10,  y, 30, 15);
        y += 20;
        g.setColor(Color.BLUE);
        g.fillRect(10,  y, 30, 15);
        y += 20;
        g.setColor(Color.GREEN);
        g.fillRect(10,  y, 30,  15);
        y += 20;
        g.setColor(Color.YELLOW);
        g.fillRect(10,  y, 30,  15);
	}
}
