package org.neodatis.knowledger.gui.graph.panel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.ODBKnowledgeBase;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.tool.GuiUtil;
import org.neodatis.tool.wrappers.OdbString;



public class KnowledgerFrame extends JFrame implements ActionListener, Runnable, ItemListener {
	private JDesktopPane desktop;

	private final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

	private String nextThreadAction;

	private static boolean actionsInNewThread = false; 
	public KnowledgerFrame() {
		super("Knowledger Editor");

		// Make the big window be indented 50 pixels from each edge
		// of the screen.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

		// Set up the GUI.
		desktop = new JDesktopPane(); // a specialized layered pane
		// createNewKnowledgeBase(); // create first "window"
		setContentPane(desktop);
		setJMenuBar(createMenuBar());

		// Make dragging a little faster but perhaps uglier.
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		
		//setLookAndFeel();
	}

	private void setLookAndFeel() {
		try {
			String laf = UIManager.getSystemLookAndFeelClassName();				
			UIManager.setLookAndFeel(laf);	
		} catch ( Exception e ) {}
	} //
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Set up the lone menu.
		JMenu menu = new JMenu("Knowledger");
		menu.setMnemonic(KeyEvent.VK_K);
		menuBar.add(menu);

		// Set up the first menu item.
		JMenuItem menuItem = new JMenuItem(Messages.getString("menu.new.local.knowledge.base"));
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("new-local");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// Set up the second menu item.
		menuItem = new JMenuItem(Messages.getString("menu.open.local.knowledge.base"));
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("open-local");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu.addSeparator();
		
		menuItem = new JMenuItem(Messages.getString("menu.new.remote.knowledgebase"));
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("new-remote");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem(Messages.getString("menu.open.remote.knowledge.base"));
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("open-remote");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// Set up the second menu item.
		menuItem = new JMenuItem(Messages.getString("menu.quit"));
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
        menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(menu);
        
        JCheckBoxMenuItem cbmi = new JCheckBoxMenuItem(Messages.getString("menu.tolerate.inconsistency"),false);
        cbmi.setMnemonic(KeyEvent.VK_T);
        cbmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        cbmi.setActionCommand("tolerate-inconsistency");
        cbmi.addItemListener(this);
        menu.add(cbmi);


		return menuBar;
	}

	// React to menu selections.
	public void actionPerformed(ActionEvent e) {
		if ("new-local".equals(e.getActionCommand())) { // new
			if(actionsInNewThread){
				Thread t = new Thread(this);
				nextThreadAction = "new-local";
				t.start();
			}else{
				try {
					createNewLocalKnowledgeBase();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this, OdbString.exceptionToString(e1,true), "Error", JOptionPane.ERROR_MESSAGE, null);
				}
			}
			return;
		}

		if ("open-local".equals(e.getActionCommand())) { // new
			if(actionsInNewThread){
				try {
					Thread t = new Thread(this);
					nextThreadAction = "open-local";
					t.start();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, OdbString.exceptionToString(e1,true), "Error", JOptionPane.ERROR_MESSAGE, null);
				}
			}else{
				try {
					openLocalKnowledgeBase();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this, OdbString.exceptionToString(e1,true), "Error", JOptionPane.ERROR_MESSAGE, null);
				}
			}
			return;
		}
		
		if ("open-remote".equals(e.getActionCommand())) { // new
			if(actionsInNewThread){
				try {
					Thread t = new Thread(this);
					nextThreadAction = "open-remote";
					t.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(this, OdbString.exceptionToString(e1,true), "Error", JOptionPane.ERROR_MESSAGE, null);
				}
			}else{
				try {
					openRemoteKnowledgeBase();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this, OdbString.exceptionToString(e1,true), "Error", JOptionPane.ERROR_MESSAGE, null);
				}
			}
			return;
		}
		
		if ("quit".equals(e.getActionCommand())) { // new
			quit();
		}
	}

	private void openLocalKnowledgeBase() throws Exception {

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			System.out.println("Loading " + file);
			// Check if there exist more than one database in this container
			String baseName = getBaseName(file.getAbsolutePath());
			
			KnowledgerEditorPanel kep = new KnowledgerEditorPanel(file.getAbsolutePath(),baseName);

			JInternalFrame iframe = new KnowledgerEditorIFrame(file.getName(),kep);
			desktop.add(iframe);
			try {
				iframe.setSelected(true);
			} catch (java.beans.PropertyVetoException e) {
				e.printStackTrace();
			}

		} else {

		}
	}
	
	private String getBaseName(String absolutePath) throws Exception {
		ODB odb = null;
		
		try{
			odb = NeoDatis.open(absolutePath);
			Objects<ODBKnowledgeBase> bases = odb.query(ODBKnowledgeBase.class).objects();
			if(bases.size()==1){
				KnowledgeBase kb = bases.first();
				return kb.getId();				
			}
			// The container file contains more than one file
			// The user must choose 
			String [] names= new String[bases.size()];
			int i = 0;
			for(KnowledgeBase kb:bases){
				names[i++] = kb.getId();
			}
			
			ChooseKnowledgeBasePanel dialog = new ChooseKnowledgeBasePanel(names);
			dialog.pack();
			dialog.setVisible(true);
			return dialog.getBaseName();	

		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(odb!=null){
				odb.close();
			}
		}
	}

	private void openRemoteKnowledgeBase() throws Exception {

		OpenRemoteDatabasePanel dialog = new OpenRemoteDatabasePanel(null);
		dialog.pack();
		dialog.setVisible(true);
		
		String name = dialog.getName();
		String host = dialog.getHost();
		String port = dialog.getPort();

		KnowledgerEditorPanel kep = new KnowledgerEditorPanel(name,host,port);

		JInternalFrame iframe = new KnowledgerEditorIFrame(host+":"+port+"/"+name,kep);
		desktop.add(iframe);
		try {
			iframe.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}

	}

	// Create a new internal frame.
	protected void createNewLocalKnowledgeBase() throws Exception {

		InformationPanel ip = new InformationPanel();
		ip.pack();
		GuiUtil.centerScreen(ip);
		
		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			System.out.println("Creating " + file);

			KnowledgerEditorPanel kep = new KnowledgerEditorPanel(file.getAbsolutePath(),KnowledgeBaseFactory.getCleanName(file.getAbsolutePath()));

			JInternalFrame iframe = new KnowledgerEditorIFrame(file.getName(),kep);
			desktop.add(iframe);
			try {
				iframe.setSelected(true);
			} catch (java.beans.PropertyVetoException e) {
				e.printStackTrace();
			}
		}
	}

	// Quit the application.
	protected void quit() {
		System.exit(0);
	}

	public void run() {
		if ("open-local".equals(nextThreadAction)) {
			try {
				openLocalKnowledgeBase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if ("open-remote".equals(nextThreadAction)) {
			try {
				openRemoteKnowledgeBase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if ("new-local".equals(nextThreadAction)) {
			try {
				createNewLocalKnowledgeBase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void itemStateChanged(ItemEvent e) {
		tolerateInconsistency(e.getStateChange()== ItemEvent.SELECTED);
		
	}
	private void tolerateInconsistency(boolean tolerate) {
    	//Configuration.setThrowExceptionWhenInconsistencyFound(!tolerate);
	}
}

class MyFileFilter implements FileFilter{

	public boolean accept(File pathname) {
		return pathname.getName().endsWith("*.odb");
	}
	
}
