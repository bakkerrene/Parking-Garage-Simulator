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

		model.addController(controller);


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
		controller.setBounds(180, 447, 800, 30);
		slideController = new SlideController(model);
		slideController.setBounds(180, 11, 800, 39);
		screen.getContentPane().add(slideController);
		slideController.setLayout(new GridLayout(2, 0, 0, 0));
						managerView = new ManagerView(model);
						managerView.setBounds(15, 49, 155, 200);
						screen.getContentPane().add(managerView);
						managerView.setBackground(SystemColor.control);
		
				carParkView = new CarParkView(model);
				carParkView.setBounds(180, 49, 800, 398);
				screen.getContentPane().add(carParkView);
				carParkView.setBackground(Color.WHITE);
						initController = new InitController(model);
						initController.setBounds(990, 363, 200, 224);
						screen.getContentPane().add(initController);
						model.addController(initController); // <<<----- wat doet dit hier  ??????????????????
						queueView = new QueueView(model);
						queueView.setBounds(990, 49, 200, 200);
						screen.getContentPane().add(queueView);
						selectController = new SelectController(model);
						selectController.setBounds(990, 260, 200, 92);
						screen.getContentPane().add(selectController);
						
								carParkView.addController(selectController);
		


		screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}
}
