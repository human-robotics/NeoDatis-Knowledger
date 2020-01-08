package org.neodatis.knowledger.gui.panel;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.neodatis.knowledger.core.factory.KnowledgeBaseDescription;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.tool.Messages;


public abstract class ChooseKnowledeBasePanel extends JPanel {

	private JTextField newBaseName;
	private JComboBox comboBox;
	private KnowledgeBaseDescription knowledgeBaseDescription;

	public ChooseKnowledeBasePanel() throws Exception {
		init();
	}

	private void init() throws Exception  {
		setLayout(new GridLayout(2,3));
		
		comboBox = new JComboBox(KnowledgeBaseFactory.getAllKnowledgebaseDescriptions());
		JLabel label1 = new JLabel(Messages.getString("choose knowledge base"));
		JButton button1 = new JButton("choose");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setKnowledgeBaseDescription((KnowledgeBaseDescription) comboBox.getSelectedItem());
				chooseDatabase();
			};
		});
		
		JButton button2 = new JButton(Messages.getString("Create"));
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					;
					setKnowledgeBaseDescription(createNewDatabase(newBaseName.getText()));
					chooseDatabase();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		add(label1);
		add(comboBox);
		add(button1);
		
		newBaseName = new JTextField(15);
		JLabel label2 = new JLabel(Messages.getString("Create new Database"));
		add(label2);
		add(newBaseName);
		add(button2);

	}

	private KnowledgeBaseDescription createNewDatabase(String name) throws IOException {
		KnowledgeBaseDescription description = new KnowledgeBaseDescription(name,KnowledgeBaseType.ODB);
		//ODB odb = ODB.open(name+".odb");
        //odb.commitAndClose();
        //KnowledgeBaseDescription description = new KnowledgeBaseDescription(name,KnowledgeBaseType.DB4O);
        //ObjectContainer oc = Db4o.openFile(name+".yap");
		//oc.close();
		return description;
		
	};

	
	protected void chooseDatabase() {
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                	KnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance(getKnowledgeBaseDescription().getName(),getKnowledgeBaseDescription().getType());
	                    databaseIs(knowledgeBase);
	                } catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        });
	}

	public abstract void databaseIs(KnowledgeBase knowledgeBase);
	
	public KnowledgeBaseDescription getKnowledgeBaseDescription() {
		return knowledgeBaseDescription;
	}

	public void setKnowledgeBaseDescription(KnowledgeBaseDescription knowledgeBaseDescription) {
		this.knowledgeBaseDescription = knowledgeBaseDescription;
	}

}
