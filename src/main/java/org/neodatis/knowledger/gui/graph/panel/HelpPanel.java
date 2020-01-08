package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.neodatis.knowledger.gui.tool.Messages;


public class HelpPanel extends JDialog implements ActionListener {
	
	public HelpPanel(){
		super((JFrame)null,Messages.getString("help.title"));
		init();
		setModal(true);
	}
	
	private void init(){
		
		JLabel label = new JLabel(Messages.getString("help.title"));
		
		String s = Messages.getString("help.1")+Messages.getString("help.2")+Messages.getString("help.3")+Messages.getString("help.4")+Messages.getString("help.5");
		
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
