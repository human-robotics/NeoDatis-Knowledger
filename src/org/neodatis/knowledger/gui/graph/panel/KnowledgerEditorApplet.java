package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.explorer.KnowledgeExplorerPanel;
import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;
import org.neodatis.knowledger.gui.graph.defaults.DefaultManagerFactory;
import org.neodatis.knowledger.gui.tool.Messages;



public class KnowledgerEditorApplet extends JApplet implements IExternalBrowser {

	private KnowledgerGraphPanel knowledgerGraphPanel;
	private String containerFileName;
	private String knowledgeBaseName;
	private String host;
	private String port;
	private String webContext;
	private String webUrl;
	private String language;
	

	private JButton btConfiguration;
	private JButton btExplorer;
	private boolean withToolbar;

	public void init() {
		this.knowledgeBaseName = getParameter("base.name");
		this.containerFileName = getParameter("container.name");
		if(containerFileName==null){
			containerFileName = knowledgeBaseName+".odb"; 
		}
		language = getParameter("language");
		if(language==null){
			language = "en";
		}
		GlobalConfiguration.setLanguage(language);
		
		// Gets user specific value
		String value1 = getParameter("parameter1");
		String value2 = getParameter("parameter2");
		if(value1!=null){
			GlobalConfiguration.setParameterValue("parameter1", value1);
		}
		if(value2!=null){
			GlobalConfiguration.setParameterValue("parameter2", value2);
		}

		this.host = getCodeBase().getHost();
		this.port = String.valueOf(getCodeBase().getPort());
		this.webUrl = getCodeBase().getFile();
		//System.out.println("getFile="+this.webUrl);
		//System.out.println("codebase="+getCodeBase() + "ref="+getCodeBase().getRef() + " | query="+getCodeBase().getQuery() + " path = " + getCodeBase().getPath() + " | file = " + getCodeBase().getFile());
		String path = getCodeBase().getPath();
		System.out.println("Path = " + path);
		path = path.substring(1);
		int index = path.indexOf("/");
		if(index!=-1){
			webContext = path.substring(0,index);
		}else{
			webContext = path;
		}
		GlobalConfiguration.setWebContext(webContext);
        GlobalConfiguration.setHost(host);
        GlobalConfiguration.setPort(port);        
        
		System.out.println("WebContext = " + webContext);
		
		String sWithToolbar = getParameter("with.toolbar");
		System.out.println("with toolbar ? " + sWithToolbar);
		if(sWithToolbar==null){
			withToolbar = true;
		}else{
			this.withToolbar = "true".equals(sWithToolbar);
		}
		if(this.knowledgeBaseName == null){
			this.knowledgeBaseName = "knowledger";
		}
		if(this.host==null || this.host.length()==0){
			this.host="localhost";
		}
		if(this.port==null || this.port.length()==0 || this.port.equals("-1")){
			this.port = "80";
		}
        String sAnimationStepDuration = getParameter("animation.step.duration");
        if(sAnimationStepDuration!=null && sAnimationStepDuration.length()>0){
            GlobalConfiguration.setAnimationStepDuration(Long.parseLong(sAnimationStepDuration));
        }
        String sInteractionManagerClass = getParameter("interaction.manager.class");
        if(sInteractionManagerClass!=null && sInteractionManagerClass.length()>0 && !sInteractionManagerClass.equals("default")){
            DefaultManagerFactory.setInteractionManagerClassName(sInteractionManagerClass);
        }

        // Checks if user has passed the definition of the prperty name to use for external url opening
		
		String propertyNameForExternalUrl = getParameter("property.name.for.external.url");
		if(propertyNameForExternalUrl!=null){
			Configurations.getConfiguration(knowledgeBaseName).setExternalUrlRelationName(propertyNameForExternalUrl);
		}
		try {
			showStatus("Loading Knowledger Applet with base name "+ this.knowledgeBaseName);
			initGui();
			showStatus("Knowledger Applet Loaded - connected to " + host + ":" + port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showStatus(e.getMessage());
		}
	}
	
	
	public void start() {
		super.start();
	}


	public void stop() {
		super.stop();
	}


	public KnowledgerEditorApplet() throws HeadlessException {
		super();
	}
	public KnowledgerEditorApplet(String knowledgeBaseName) throws Exception {
		super();
		this.knowledgeBaseName = knowledgeBaseName;
		init();
	}
	public KnowledgerEditorApplet(String knowledgeBaseName,String host, String port) throws Exception {
		super();
		this.knowledgeBaseName = knowledgeBaseName;
		this.host = host;
		this.port = port;
		initGui();
	}

	private void initGui() throws Exception {
		getContentPane().setLayout(new BorderLayout(5, 5));

		if(withToolbar){
			initToolbar();
		}

		initMainPanel();

		initFooter();
	}

	private void initFooter() {
		
	}

	private void initMainPanel() throws Exception {
		knowledgerGraphPanel = new KnowledgerGraphPanel(containerFileName,knowledgeBaseName,host,port,this);
		getContentPane().add(knowledgerGraphPanel,BorderLayout.CENTER);
	}

	private void initToolbar() {
		JToolBar toolBar = new JToolBar();
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
		/*
		btExplorer = new JButton("explorer");
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
		toolBar.add(btExplorer);
		*/
		toolBar.add(btConfiguration);
		
		
		getContentPane().add(toolBar,BorderLayout.NORTH);


	}

	private void displayConnectorFilterWindow() throws Exception {
		JFrame frame = new JFrame("Configuration");
		frame.getContentPane().add(new ConfigurationPanel(knowledgerGraphPanel.getManagerFactory(containerFileName,knowledgeBaseName,host,port).getPersistentManager()));
		frame.pack();
		frame.setVisible(true);

	}
	
	private void displayExplorerWindow() throws Exception {
		JFrame frame = new JFrame("Knowledger Explorer");
		KnowledgeBase knowledgeBase = (KnowledgeBase) knowledgerGraphPanel.getManagerFactory(containerFileName,knowledgeBaseName,host,port).getPersistentManager().getKnowledgeBase();
		frame.getContentPane().add(new KnowledgeExplorerPanel( knowledgeBase,"Knowledge - " + knowledgeBaseName));
		frame.pack();
		frame.setVisible(true);

	}
	
	public void openInExternalBrowser(String relativePath,String baseName, String value1,String target) {
		StringBuffer sUrl = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		parameters.append("?base.name=").append(baseName).append("&parameter=").append(value1).append("&target=").append(target);
		
		sUrl.append("http://").append(host).append(":").append(port).append("/").append(webContext).append("/").append(relativePath).append(parameters.toString());

        System.out.println("open url in external browser:"+sUrl);
		URL url;
		try {
			url = new URL(sUrl.toString());
			getAppletContext().showDocument(url,target);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
