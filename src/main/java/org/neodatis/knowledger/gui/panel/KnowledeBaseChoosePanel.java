package org.neodatis.knowledger.gui.panel;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.neodatis.knowledger.core.factory.KnowledgeBaseDescription;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.explorer.KnowledgeExplorerPanel;
import org.neodatis.knowledger.gui.tool.Messages;


public class KnowledeBaseChoosePanel extends JPanel {

	private JComboBox comboBox;
	private KnowledgeBaseDescription knowledgeBaseDescription;

	public KnowledeBaseChoosePanel() throws Exception {
		init();
	}

	private void init() throws Exception {
		comboBox = new JComboBox(KnowledgeBaseFactory.getAllKnowledgebaseDescriptions());
		JLabel label = new JLabel(Messages.getString("choose knowledge base"));
		add(label);
		add(comboBox);
		JButton button = new JButton("choose");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setKnowledgeBaseDescription((KnowledgeBaseDescription) comboBox.getSelectedItem());
				chooseDatabase();
			};
		});

		add(button);

	}

	protected void chooseDatabase() {
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                	KnowledgeBase kb = KnowledgeBaseFactory.getInstance(getKnowledgeBaseDescription().getName(),getKnowledgeBaseDescription().getType());
	                    KnowledgeExplorerPanel.display(kb,"Dialogy");
	                } catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        });
	}

	
	public KnowledgeBaseDescription getKnowledgeBaseDescription() {
		return knowledgeBaseDescription;
	}

	public void setKnowledgeBaseDescription(KnowledgeBaseDescription knowledgeBaseDescription) {
		this.knowledgeBaseDescription = knowledgeBaseDescription;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Choose KB");
		// frame.setUndecorated(true);
		frame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(new KnowledeBaseChoosePanel());
		frame.pack();
		frame.setVisible(true);

	}

}
