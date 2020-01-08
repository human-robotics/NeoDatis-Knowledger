package org.neodatis.knowledger.gui.graph.panel;

import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class KnowledgerEditorIFrame extends JInternalFrame implements InternalFrameListener {
	private KnowledgerEditorPanel editorPanel;
	public KnowledgerEditorIFrame(String title, KnowledgerEditorPanel panel){
		super(title);
		this.editorPanel = panel;
		addInternalFrameListener(this);
		setIconifiable(true);
		setClosable(true);
		setMaximizable(true);
		setClosable(true);
		setResizable(true);
		setTitle(title);
		setContentPane(panel);
		pack();
		setVisible(true); // necessary as of 1.3
		setLocation(15, 15);
	}
	
	public void internalFrameClosed(InternalFrameEvent e) {
		try {
			this.editorPanel.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void internalFrameClosing(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
}
