package parkeersimulator.main;


import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import parkeersimulator.controller.Controller;
import parkeersimulator.controller.InitController;
import parkeersimulator.controller.SlideController;
import parkeersimulator.model.Model;
import parkeersimulator.view.*;

public class MvcSimulator  {

	private Model model;
	private InitController initController;
	private Controller controller;
	private SlideController slideController;

	private JFrame screen;
	private CarParkView carParkView;
	private BarGraphView barGraphView;
	private PieView pieView;
	private ManagerView managerView;
	private QueueView queueView;

	public MvcSimulator() {

		model = new Model(3, 6, 30);

		controller = new Controller(model);
		initController = new InitController(model);
		slideController = new SlideController(model);

		carParkView = new CarParkView(model);
		barGraphView = new BarGraphView(model);
		pieView = new PieView(model);
		managerView = new ManagerView(model);
		queueView = new QueueView(model);

		carParkView.addController(initController);

		screen = new JFrame("Parking Simulator");
		screen.setSize(1600, 1000);
		screen.setResizable(false);
		screen.setLayout(null);

		Container contentPane = screen.getContentPane();
		contentPane.add(carParkView);
		contentPane.add(pieView);
		contentPane.add(managerView);
		contentPane.add(queueView);
		contentPane.add(controller);
		contentPane.add(slideController);
		contentPane.add(initController);
		contentPane.add(barGraphView);

		carParkView.setBounds(10, 50, 800, 400);
		pieView.setBounds(1030, 50, 200, 200);
		managerView.setBounds(1030, 260, 200, 200);
		queueView.setBounds(1230, 260, 200, 200);

		barGraphView.setBounds(10, 500, 402, 402);
		slideController.setBounds(10, 0, 800 , 50);
		controller.setBounds(10, 470, 800, 30);
		initController.setBounds(820, 60, 200, 400);

		screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}
}