package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.explorer.KnowledgeExplorerPanel;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.GuiUtil;



public class KnowledgerEditorPanel extends JPanel implements IExternalBrowser{

	private KnowledgerGraphPanel knowledgerGraphPanel;
	private String knowledgeBaseFullPath;
	private String knowledgeBaseName;
	private String host;
	private String port;

	private JButton btConfiguration;
	private JButton btExplorer;
	private JButton btHelp;

	public KnowledgerEditorPanel(String knowledgeBaseFullPath,String baseName) throws Exception {
		super();
		this.knowledgeBaseFullPath = knowledgeBaseFullPath;
		this.knowledgeBaseName = baseName;
		
		init();
	}
	public KnowledgerEditorPanel(String knowledgeBaseFullPath,String host, String port) throws Exception {
		super();
		this.knowledgeBaseFullPath = knowledgeBaseFullPath;
		this.knowledgeBaseName = KnowledgeBaseFactory.getCleanName(knowledgeBaseFullPath);
		this.host = host;
		this.port = port;
		init();
	}

	private void init() throws Exception {
		setLayout(new BorderLayout(0, 0));

		

		initMainPanel();
		initToolbar();
		initFooter();
	}

	private void initFooter() {
		
	}

	private void initMainPanel() throws Exception {
		knowledgerGraphPanel = new KnowledgerGraphPanel(knowledgeBaseFullPath, knowledgeBaseName,host,port,this);
		add(knowledgerGraphPanel,BorderLayout.CENTER);
	}

	private void initToolbar() {
		//JToolBar toolBar = new JToolBar();
		JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toolBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		btConfiguration = new JButton(Messages.getString("button.configuration"));
		btConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					displayConnectorFilterWindow();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			};
		});
		btExplorer = new JButton(Messages.getString("button.explorer"));
		btExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					displayExplorerWindow();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			};
		});
		btHelp = new JButton(Messages.getString("button.help"));
		btHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					displayHelpWindow();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private void displayHelpWindow() {
				HelpPanel helpPanel = new HelpPanel();
				helpPanel.pack();
				GuiUtil.centerScreen(helpPanel);

				
			};
		});
		
		toolBar.add(btConfiguration);
		toolBar.add(btHelp);
		//toolBar.add(btExplorer);
		//toolBar.add(new NodeDistanceSlider(knowledgerGraphPanel,Messages.getString("label.distance"),-100,0,-25));
		
		add(toolBar,BorderLayout.NORTH);


	}

	private void displayConnectorFilterWindow() throws Exception {
		JFrame frame = new JFrame(Messages.getString("button.configuration"));
		frame.getContentPane().add(new ConfigurationPanel(knowledgerGraphPanel.getManagerFactory(knowledgeBaseFullPath,knowledgeBaseName,host,port).getPersistentManager()));
		frame.pack();
		frame.setVisible(true);

	}
	
	private void displayExplorerWindow() throws Exception {
		JFrame frame = new JFrame("Knowledger Explorer");
		KnowledgeBase knowledgeBase = (KnowledgeBase) knowledgerGraphPanel.getManagerFactory(knowledgeBaseFullPath,knowledgeBaseName,host,port).getPersistentManager().getKnowledgeBase();
		frame.getContentPane().add(new KnowledgeExplorerPanel( knowledgeBase,"Knowledge - " + knowledgeBaseFullPath));
		frame.pack();
		frame.setVisible(true);

	}
	public void close() throws IOException {
		knowledgerGraphPanel.close();
		
	}
    public void openInExternalBrowser(String relativePath, String baseName, String value1, String target) {
        System.out.println("Calling external browser for path " + relativePath + " for base " + baseName + " and value1=" + value1+" on target="+target);
    }

}
