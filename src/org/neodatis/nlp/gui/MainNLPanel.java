package org.neodatis.nlp.gui;

import javax.swing.JFrame;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


public class MainNLPanel {

	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("Dialogy N L Processor");
		try {
			frame.getContentPane().add(new NLPanel(KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY),"C:\\Temp"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.show();
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
