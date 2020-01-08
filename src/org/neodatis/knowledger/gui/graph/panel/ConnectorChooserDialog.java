package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.gui.EntityWrapper;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.defaults.ODBPersistentManager;
import org.neodatis.knowledger.gui.tool.Messages;


public class ConnectorChooserDialog extends JDialog {
	
	private IPersistentManager persistentManager;
	private JComboBox cbComboBox;
	private JTextField tfNewConnector;
	private String name1;
	private String name2;
	private boolean bok;
	public ConnectorChooserDialog(ConnectorList connectorsToDisplay, IPersistentManager persistentManager,String object1,String object2){
		this.persistentManager = persistentManager;
		this.name1 = object1;
		this.name2 = object2;
		setModal(true);
		initGui(connectorsToDisplay);
	}

	private void initGui(ConnectorList connectorList) {
		List l = null;
		if(connectorList==null){
			l = getConnectorWrappers(persistentManager.getConnectors());
		}else{
			l = getConnectorWrappers(connectorList);
		}

		Object[] possibilities = l.toArray(new Object[l.size()]);
		cbComboBox = new JComboBox(possibilities);
		tfNewConnector = new JTextField(12);
		Font [] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		Font font = new Font("Arial",Font.PLAIN,12);
		JLabel label1 = new JLabel(Messages.getString("label.choose.connector.for.relation"));
		setTitle(Messages.getString("label.relation.between") +" "+ name1 + " "+Messages.getString("label.and")+" "+name2);
		JLabel label3 = new JLabel(Messages.getString("label.or.new.connector"));
		label1.setFont(font);
		//label11.setFont(font);
		label3.setFont(font);
		tfNewConnector.setFont(font);
		cbComboBox.setFont(font);
		
		JPanel panel1 = new JPanel(new GridLayout(2,2,5,5));
		panel1.add(label1);
		panel1.add(cbComboBox);
		panel1.add(label3);
		panel1.add(tfNewConnector);
		
		JButton btok = new JButton(Messages.getString("button.ok"));
		btok.setFont(font);
		
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel3.add(btok);
		
		btok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				bok = true;
				
			}
			
		}); 
		
		JPanel box = new JPanel(new BorderLayout(5,5));
		
		box.add(panel1,BorderLayout.CENTER);
		box.add(panel3,BorderLayout.SOUTH);
		box.setBorder(new EmptyBorder(10,10,10,10));
		getContentPane().add(box);
		
		
		pack();
		
	}
	
    private List getConnectorWrappers(ConnectorList connectors){
        Connector connector = null;
        EntityWrapper wrapper = null;
        // TODO : build it once only!
        List result = new ArrayList();
        
        for(int i=0;i<connectors.size();i++){
            connector = connectors.getConnector(i);
            wrapper = new EntityWrapper(connector);
            result.add(wrapper);            
        }
        return result;
        
    }
    

	public Connector getConnector() throws Exception{
		if(!bok){
			return null;
		}
		String newConnectorName = tfNewConnector.getText();
		if(newConnectorName==null || newConnectorName.length()==0){
			EntityWrapper ew = (EntityWrapper) cbComboBox.getSelectedItem();
			return (Connector) ew.getDialoyObject();
		}
		
		System.out.println("Creating new connector : " + newConnectorName);
		Connector connector = persistentManager.newConnector(newConnectorName);
		return connector;
	}
	public static void main(String[] args) throws Exception {
//		JFrame f = new JFrame("test");
		ConnectorChooserDialog dialog = new ConnectorChooserDialog(null, new ODBPersistentManager("test.odb","test"),"Animal","Human");
		dialog.setVisible(true);
		System.out.println(dialog.getConnector());
	}

}
