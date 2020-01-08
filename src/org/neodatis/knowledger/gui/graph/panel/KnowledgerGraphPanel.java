package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.IManagerFactory;
import org.neodatis.knowledger.gui.graph.basic.PrefuseGraphBuilder;
import org.neodatis.knowledger.gui.graph.defaults.DefaultManagerFactory;


public class KnowledgerGraphPanel extends JPanel implements FocusListener {
    private IManagerFactory managerFactory;
    
    private String knowledgeBaseContainerFileName;
    private String knowledgeBaseName;
    private String host;
    private String port;
    private IExternalBrowser externalBrowser;
    
    private PrefuseGraphBuilder prefuseGraphBuilder;
    
    

    public KnowledgerGraphPanel(String containerFileName, String knowledgeBaseName) throws Exception {
    	this(containerFileName,knowledgeBaseName,null,null,null);
    }
    public KnowledgerGraphPanel(String containerFileName,String knowledgeBaseName,String host, String port, IExternalBrowser externalBrowser) throws Exception {
		super();
		this.externalBrowser = externalBrowser;
        this.knowledgeBaseName = knowledgeBaseName;
        this.knowledgeBaseContainerFileName = containerFileName;
        this.host = host;
        this.port = port;
		init();
		
		Configurations.getConfiguration(knowledgeBaseName).setKnowledgerGraphPanel(this);
		setBorder(new EmptyBorder(2,2,2,2));
	}

	public void init() throws Exception {

        managerFactory = getManagerFactory(knowledgeBaseContainerFileName,knowledgeBaseName,host,port);
        prefuseGraphBuilder = new PrefuseGraphBuilder(managerFactory);
        setLayout(new BorderLayout(5,5));
        add(prefuseGraphBuilder.buildComponent());
    }

	/*
    private void testSearchPanel() {
    	String [] attributes = {"label"};
    	JFrame frame = new JFrame("Search");
    	//PrefixSearchPanel panel = new PrefixSearchPanel(attributes,getRegistry());
    	panel.addFocusListener(this);
    	frame.getContentPane().add(panel);
    	frame.pack();
    	frame.setVisible(true);
	}
	*/

  public synchronized IManagerFactory getManagerFactory(String containerFileName,String knowledgeBaseName,String host, String port) throws Exception{
        if(managerFactory==null){
        	return new DefaultManagerFactory(this,containerFileName,knowledgeBaseName,host,port);
        }
        return managerFactory;
    }
	public Dimension getMSize() {
		return new Dimension(1000, 800);
	}

	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Graph Panel");
		KnowledgerGraphPanel display = new KnowledgerGraphPanel("knowledger.odb","knowledger");
		frame.getContentPane().add(new JScrollPane(display));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public void focusGained(FocusEvent e) {
		System.out.println("Focuos gained : " + e.getSource());
		
	}

	public void focusLost(FocusEvent e) {
		System.out.println("Focuos Lost : " + e.getSource());
		
	}

	public PrefuseGraphBuilder getPrefuseGraphBuilder() {
		return prefuseGraphBuilder;
	}
	public void setPrefuseGraphBuilder(PrefuseGraphBuilder prefuseGraphBuilder) {
		this.prefuseGraphBuilder = prefuseGraphBuilder;
	}
	public void refresh() throws Exception{
		prefuseGraphBuilder.refresh();
	}
	public void redraw() throws Exception{
		prefuseGraphBuilder.redraw();
	}
	
	public void openInExternalBrowser(String relativePath,String baseName, String value1,String target){
		if(externalBrowser!=null){
			externalBrowser.openInExternalBrowser(relativePath, baseName,value1,target);
		}else{
			System.out.println("No external browser defined");
		}
		
	}
	public String getKnowledgeBaseName() {
		return knowledgeBaseName;
	}
	public IManagerFactory getManagerFactory() {
		return managerFactory;
	}
	public void close() throws IOException {
		managerFactory.getPersistentManager().close();
	}
    public void centerOn(long id){
        prefuseGraphBuilder.centerOn(id);
    }
}



