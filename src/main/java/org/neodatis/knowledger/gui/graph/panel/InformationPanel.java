package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.neodatis.knowledger.gui.tool.Messages;


public class InformationPanel extends JDialog implements ActionListener {
	
	public InformationPanel(){
		super();
		init();
		setModal(true);
	}
	
	private void init(){
		JLabel label = new JLabel(Messages.getString("Informations"));
		
		String s = Messages.getString("new.kb.info.1")+Messages.getString("new.kb.info.2")+Messages.getString("new.kb.info.3");
		
		JLabel lbInfo = new JLabel(s);
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(lbInfo);
		
		JButton button = new JButton(Messages.getString("button.ok"));
		getContentPane().add(button,BorderLayout.SOUTH);
		button.setActionCommand("ok");
		button.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();		
	}
}
