package org.neodatis.nlp.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.knowledger.gui.tool.populator.PopulatorPanel;
import org.neodatis.nlp.core.DialogyNLProcessor;
import org.neodatis.tool.DLogger;


public class NLPanel extends JPanel implements ActionListener ,Runnable{
	public static Logger logger = Logger.getLogger(NLPanel.class);
	
	public static final String ACTION_ADD_DIRECTORY = "add-directory";
	public static final String ACTION_REMOVE_DIRECTORY = "remove-directory";
	public static final String ACTION_EXECUTE = "execute";
	
	private List directories;
	private LoggingPanel loggingPanel;
	private KnowledgeBase knowledgeBase;
	
	
	public NLPanel(KnowledgeBase knowledgeBase,String defaultDirectory){
		super();
		this.knowledgeBase = knowledgeBase;
		directories = new ArrayList();
		directories.add(defaultDirectory);		
		
		init();
	}
	
	private void init(){
		JButton btAddDirectory = new JButton(Messages.getString("add directory"));
		btAddDirectory.setActionCommand(ACTION_ADD_DIRECTORY);
		btAddDirectory.addActionListener(this);
		
		JButton btExecute = new JButton(Messages.getString("execute"));
		btExecute.setActionCommand(ACTION_EXECUTE);
		btExecute.addActionListener(this);
		
		loggingPanel = new LoggingPanel(20,20);
		DLogger.register(loggingPanel);
		
		JPanel panel = new JPanel();
		panel.add(btAddDirectory);
		panel.add(btExecute);
		
		setLayout(new BorderLayout(15,15));
		
		add(panel,BorderLayout.NORTH);
		add(loggingPanel,BorderLayout.CENTER);
		
		

		
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getActionCommand().equals(ACTION_ADD_DIRECTORY)){
			addDirectory();
		}
		if(actionEvent.getActionCommand().equals(ACTION_EXECUTE)){
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	private void executeForThread() {
		DialogyNLProcessor processor = null;
		try {
			processor = new DialogyNLProcessor(knowledgeBase, false);
	        DLogger.info(processor.display());
	        
	        for(int i=0;i<directories.size();i++){
	        	processor.parseDirectory(directories.get(i).toString());
	        }
	        DLogger.info(processor.display());
	        
	        displayWizardToDefineUnknownWord(processor.getUnknowns());
	        
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayWizardToDefineUnknownWord(List unknowns) throws Exception {

		Concept base = knowledgeBase.getConceptFromName("GrammaticalEntity",GetMode.CREATE_IF_DOES_NOT_EXIST);
 
		EntityList entities = base.getSubConcepts();
		ConnectorList connectors = new ConnectorList();
		connectors.add(knowledgeBase.getIsInstanceOfConnector());
		
		JFrame frame = new JFrame("Dialogy Concept Definition");
		PopulatorPanel panel = new PopulatorPanel(knowledgeBase,unknowns,connectors, entities);
		
		frame.getContentPane().add(panel);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		frame.pack();
		frame.show();
		
	}

	private void addDirectory() {
		
	}

	public void run() {
		executeForThread();
	}
	

}
