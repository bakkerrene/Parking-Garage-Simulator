
package parkeersimulator.controller;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import parkeersimulator.model.Model;
import parkeersimulator.view.LineGraphView;

@SuppressWarnings("serial")
public class TabController extends AbstractController {
	private LineGraphView graph;

	public TabController(Model model) {
		super(model);
		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon icon = createImageIcon("image/test.png");
		
        tabbedPane.addTab("Tab 1", icon, graph,
                "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);	
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = TabController.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
