
package parkeersimulator.main;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import parkeersimulator.controller.Controller;
import parkeersimulator.controller.*;
import parkeersimulator.model.Model;
import parkeersimulator.view.*;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.GridLayout;

public class MvcSimulator  {

	private Model model;
	private SelectController selectController;
	private InitController initController;
	private Controller controller;
	private SlideController slideController;
	private GraphController graphController;

	private JFrame screen;
	private CarParkView carParkView;
	private BarGraphView barGraphView;
	private PieView pieView;
	private ManagerView managerView;
	private QueueView queueView;
	private LineGraphView lineGraphView;
	private JTabbedPane tabbedPane;
	private LegendaView legenda;

	public MvcSimulator() {

		model = new Model(3, 6, 30);
		
		controller = new Controller(model);

		screen = new JFrame("Parkeer Simulator");
		screen.setSize(1350, 925);
		screen.setResizable(false);
		screen.getContentPane().setLayout(null);

		Container contentPane = screen.getContentPane();

		legenda = new LegendaView(model);
		legenda.setBounds(810, 120, 240, 380);
		screen.getContentPane().add(legenda);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBounds(10, 620, 800, 240);
		screen.getContentPane().add(tabbedPane);
				lineGraphView = new LineGraphView(model);
				tabbedPane.addTab("Lijndiagram", null, lineGraphView, null);
				graphController = new GraphController(model);
				graphController.setBounds(642, 183, 153, 23);
				lineGraphView.add(graphController);
				pieView = new PieView(model);
				tabbedPane.addTab("Cirkeldiagram", null, pieView, null);
				barGraphView = new BarGraphView(model);
				tabbedPane.addTab("Staafdiagram", null, barGraphView, null);
				
		contentPane.add(controller);
		controller.setBounds(10, 520, 800, 30);

		slideController = new SlideController(model);
		slideController.setBounds(10, 10, 800, 90);

		screen.getContentPane().add(slideController);
		slideController.setLayout(new GridLayout(2, 0, 0, 0));

						managerView = new ManagerView(model);
						managerView.setBounds(880, 650, 200, 210);

						screen.getContentPane().add(managerView);
						managerView.setBackground(SystemColor.control);
						initController = new InitController(model);
						initController.setBounds(1055, 235, 260, 225);
						screen.getContentPane().add(initController);
						queueView = new QueueView(model);
						queueView.setBounds(1080, 655, 235, 200);
						screen.getContentPane().add(queueView);
						selectController = new SelectController(model);
						selectController.setBounds(1055, 120, 260, 100);
						screen.getContentPane().add(selectController);

										carParkView = new CarParkView(model);
										carParkView.setBounds(10, 120, 800, 400);
										screen.getContentPane().add(carParkView);
										carParkView.setBackground(Color.WHITE);

		screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}
}
