package org.neodatis.knowledger.gui.graph;

import javax.swing.JFrame;

import org.neodatis.knowledger.gui.graph.panel.KnowledgerFrame;
import org.neodatis.tool.GuiUtil;


public class Main {
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		GuiUtil.setDefaultFont();
	    //DefaultManagerFactory.setInteractionManagerClassName("test.HelpDeskInteractionManager");
		// Make sure we have nice window decorations.
        
        //GlobalConfiguration.setLanguage("br");
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		KnowledgerFrame frame = new KnowledgerFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
