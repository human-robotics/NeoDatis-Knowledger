package org.neodatis.nlp.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import org.neodatis.tool.ILogger;
import org.neodatis.tool.wrappers.OdbString;

public class LoggingPanel extends JPanel implements ILogger {
	private JTextArea textArea;
	public LoggingPanel(int width,int height){
		super();
		
		textArea = new JTextArea(width,height);
		setLayout(new BorderLayout(20,20));
		add(new JScrollPane(textArea),BorderLayout.CENTER);
		
		JButton clearButton = new JButton("clear");
		clearButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent arg0) 
						{
							try {
								textArea.getDocument().remove(0,textArea.getText().length());
							} catch (BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				});
		
		JButton dateTimeButton = new JButton("date/time");
		dateTimeButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent arg0) 
						{
								insertText("date/time:" , new Date());
						}
				});
		JPanel buttons = new JPanel();
		buttons.add(clearButton);
		buttons.add(dateTimeButton);
		add(buttons,BorderLayout.SOUTH);
	}
	public void debug(Object object) {
		insertText("debug:",object);
	}

	public void info(Object object) {
		insertText("info:",object);
	}

	public void error(Object object) {
		insertText("error:",object);
	}

	private void insertText(String prefix, Object object){
		int caretPosition = textArea.getCaretPosition();
		String text = object==null?"\n":object.toString()+"\n";
		try {
			textArea.getDocument().insertString(caretPosition,text, null);
			textArea.setCaretPosition(caretPosition+text.length());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Logger Panel Test");
		frame.getContentPane().add(new LoggingPanel(20,30));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.show();
	}
	public void error(Object arg0, Throwable e) {
		insertText("error:",arg0);
		insertText("error:",OdbString.exceptionToString(e, true));
		
	}
	
}
