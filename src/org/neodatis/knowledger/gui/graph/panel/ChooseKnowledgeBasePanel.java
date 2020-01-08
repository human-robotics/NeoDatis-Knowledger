package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.neodatis.knowledger.gui.tool.Messages;


public class ChooseKnowledgeBasePanel extends JDialog {
	private String [] baseNameList;
	private JList gList;
	
	public ChooseKnowledgeBasePanel(String [] baseNames){
		this.baseNameList = baseNames;
		setModal(true);
		setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
		init();
	}
	
	public void init(){
		gList = new JList(baseNameList);
		gList.setBorder(new BevelBorder(BevelBorder.LOWERED));
		JButton button = new JButton(Messages.getString("button.choose.base"));
		button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel panel = new JPanel(new BorderLayout(5,10));
		panel.setBorder(new EmptyBorder(5,5,5,5));
		
		panel.add(new JLabel(Messages.getString("label.choose.base")),BorderLayout.NORTH);
		panel.add(gList,BorderLayout.CENTER);
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttons.add(button);
		panel.add(buttons,BorderLayout.SOUTH);
		
		getContentPane().add(panel);
	}
	public String getBaseName(){
		return (String) gList.getSelectedValue();
	}
	
	public static void main(String[] args) {
		String [] names = {"base1","base2","base3"};
		ChooseKnowledgeBasePanel c = new ChooseKnowledgeBasePanel(names);
		c.pack();
		c.setVisible(true);
		System.out.println(c.getBaseName());
	}
}
