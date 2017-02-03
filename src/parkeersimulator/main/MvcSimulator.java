package parkeersimulator.main;


import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import parkeersimulator.controller.Controller;
import parkeersimulator.controller.*;
import parkeersimulator.model.Model;
import parkeersimulator.view.*;

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
	private LegendaView legendaView;

	public MvcSimulator() {

		model = new Model(3, 6, 30); // 200 is max bij 400 height, 400 / 200 = 1px voor gap en 1px voor de spot
									 // 400 /150  = ook 1px voor gap en 1px voor spot. 400 /133 is 2px voor spot 1 px voor gap
		controller = new Controller(model);
		selectController = new SelectController(model);
		initController = new InitController(model);
		slideController = new SlideController(model);
		graphController = new GraphController(model);

		model.addController(controller);// \\\ //<<\
		model.addController(selectController);// <<<\
		model.addController(initController); // <<<----- wat doet dit hier  ??????????????????

		carParkView = new CarParkView(model);
		barGraphView = new BarGraphView(model);
		pieView = new PieView(model);
		managerView = new ManagerView(model);
		queueView = new QueueView(model);
		lineGraphView = new LineGraphView(model);
		legendaView = new LegendaView(model);

		carParkView.addController(selectController); // <<<----- wat doet dit hier  ????????????????

		screen = new JFrame("Parking Simulator");
		screen.setSize(1920, 1080);
		screen.setResizable(false);
		screen.setLayout(null);

		Container contentPane = screen.getContentPane();
		contentPane.add(carParkView);
		contentPane.add(pieView);
		contentPane.add(managerView);
		contentPane.add(queueView);
		contentPane.add(controller);
		contentPane.add(slideController);
		contentPane.add(selectController);
		contentPane.add(initController);
		contentPane.add(graphController);
		//contentPane.add(barGraphView);
		contentPane.add(lineGraphView);
		contentPane.add(legendaView);

		slideController.setBounds(10, 0, 800, 100);
		carParkView.setBounds(10, 100, 800, 400);
		controller.setBounds(10, 500, 800, 30);
		graphController.setBounds(0, 530, 800, 30);
		lineGraphView.setBounds(0, 560, 800, 400);

		selectController.setBounds(820, 90, 300, 101);
		initController.setBounds(820, 200, 300, 250);

		pieView.setBounds(1030 + 100, 50, 200, 200);
		managerView.setBounds(1130 + 100, 260, 200, 200);
		queueView.setBounds(1430, 260, 200, 200);
		legendaView.setBounds(1230,460 , 200, 200 );

		screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}
}
