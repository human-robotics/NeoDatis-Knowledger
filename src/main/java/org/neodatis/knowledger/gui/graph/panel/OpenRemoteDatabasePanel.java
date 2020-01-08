package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.neodatis.knowledger.gui.tool.Messages;


public class OpenRemoteDatabasePanel extends JDialog {
	
	private JTextField tfName;
	private JTextField tfHost;
	private JTextField tfPort;
	
	public OpenRemoteDatabasePanel(Frame frame){
		super(frame);
		setModal(true);
		init();
	}

	private void init() {
		
		JPanel fieldPanel = new JPanel(new GridLayout(3,2,5,5));
		
		tfName = new JTextField("test",10);
		tfHost = new JTextField("localhost",10);
		tfPort = new JTextField("8080",10);
		
		fieldPanel.add(new JLabel(Messages.getString("label.base.name")));
		fieldPanel.add(tfName);
		fieldPanel.add(new JLabel(Messages.getString("label.host")));
		fieldPanel.add(tfHost);
		fieldPanel.add(new JLabel(Messages.getString("label.port")));
		fieldPanel.add(tfPort);
		fieldPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		setLayout(new BorderLayout(5,5));
		add(fieldPanel,BorderLayout.CENTER);
		JButton btok = new JButton(Messages.getString("button.open"));
		
		btok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ok();
			}
			
		});
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(btok);
		add(panel,BorderLayout.SOUTH);
	}
	
	public void ok(){
		this.dispose();
		
	}

	public String getHost() {
		return tfHost.getText();
	}

	public String getName() {
		return tfName.getText();
	}

	public String getPort() {
		return tfPort.getText();
	}
	
	public static void main(String[] args) {
		//JFrame f = new JFrame("test");
		OpenRemoteDatabasePanel dialog = new OpenRemoteDatabasePanel(null);
		dialog.pack();
		dialog.setVisible(true);
		System.out.println(dialog.getHost());
		
	}
	
}
