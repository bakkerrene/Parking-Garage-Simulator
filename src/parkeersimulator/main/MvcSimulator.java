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
import javax.swing.JTextField;
import javax.swing.JLabel;

public class MvcSimulator  {

	private Model model;
	private SelectController selectController;
	private InitController 	initController;
	private Controller 		controller;
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
	private LegendaView legendaView;
	private LegendaView legendaView_1;
	private JTextField txtnnBlanco;

	public MvcSimulator() {

		model = new Model(3, 6, 30); // 200 is max bij 400 height, 400 / 200 = 1px voor gap en 1px voor de spot
									 // 400 /150  = ook 1px voor gap en 1px voor spot. 400 /133 is 2px voor spot 1 px voor gap
		controller = new Controller(model);
		selectController = new SelectController(model);

		model.addController(controller);// \\\ //<<\\

		legendaView = new LegendaView(model);


		screen = new JFrame("Parkeer Simulator");
		screen.setSize(1350, 925);
		screen.setResizable(false);
		screen.getContentPane().setLayout(null);

		Container contentPane = screen.getContentPane();
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBounds(10, 630, 800, 234);
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
		contentPane.add(selectController);
		controller.setBounds(10, 560, 800, 30);
		contentPane.add(legendaView);

		selectController.setBounds(1050, 145, 260, 100);
		queueView = new QueueView(model);
		queueView.setBounds(1075, 664, 200, 200);
		screen.getContentPane().add(queueView);
		initController = new InitController(model);
		initController.setBounds(1050, 290, 260, 225);
		screen.getContentPane().add(initController);
		model.addController(initController); // <<<----- wat doet dit hier  ??????????????????
		slideController = new SlideController(model);
		slideController.setBounds(10, 10, 800, 100);
		screen.getContentPane().add(slideController);
		slideController.setLayout(new GridLayout(2, 0, 0, 0));
		
				carParkView = new CarParkView(model);
				carParkView.setBounds(10, 130, 800, 400);
				screen.getContentPane().add(carParkView);
				carParkView.setBackground(Color.WHITE);
				
						carParkView.addController(selectController); // <<<----- wat doet dit hier  ????????????????
						managerView = new ManagerView(model);
						managerView.setBounds(850, 664, 200, 200);
						screen.getContentPane().add(managerView);
						managerView.setBackground(SystemColor.control);
		legendaView.setBounds(825,130 , 200, 400 );
		


		screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}
}
