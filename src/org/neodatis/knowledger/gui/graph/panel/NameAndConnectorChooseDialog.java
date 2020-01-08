package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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


public class NameAndConnectorChooseDialog extends JDialog implements KeyListener{
	public static final int TYPE_CONCEPT = 0;
	public static final int TYPE_INSTANCE = 1;
	
	private IPersistentManager persistentManager;
	private JComboBox cbComboBox;
	private JTextField tfNewConnector;
	private JTextField tfNewObjectName;
	private String name1;
	private int objectType;
	private JButton btok;
	private boolean isOk;

	public NameAndConnectorChooseDialog(IPersistentManager persistentManager,String object1,int objectType) {
		super();
		this.persistentManager = persistentManager;
		this.name1 = object1;
		this.objectType = objectType;
		initGui();
		setModal(true);
	}

	private void initGui() {
		List l = getConnectorWrappers(persistentManager.getConnectors());
		Object[] possibilities = l.toArray(new Object[l.size()]);
		cbComboBox = new JComboBox(possibilities);
		tfNewConnector = new JTextField(12);
		Font [] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		Font font = new Font("Arial",Font.PLAIN,12);
		setTitle(Messages.getString("label.relation.between") +" "+ name1 + " "+Messages.getString("label.and")+" ?");
		JLabel label0 = null;
		if(objectType==TYPE_CONCEPT){
			label0 = new JLabel(Messages.getString("label.ask.concept.name"));
		}else{
			label0 = new JLabel(Messages.getString("label.ask.instance.name"));
		}
		JLabel label1 = new JLabel(Messages.getString("label.choose.connector.for.relation"));
		JLabel label3 = new JLabel(Messages.getString("label.or.new.connector"));

		label0.setFont(font);
		label1.setFont(font);
		label3.setFont(font);
		
		tfNewObjectName = new JTextField(12);
		tfNewConnector.setFont(font);
		cbComboBox.setFont(font);
		
		tfNewObjectName.addKeyListener(this);
		tfNewConnector.addKeyListener(this);
		cbComboBox.addKeyListener(this);
		
		JPanel panel1 = new JPanel(new GridLayout(3,2,5,5));
		panel1.add(label0);
		panel1.add(tfNewObjectName);
		panel1.add(label1);
		panel1.add(cbComboBox);
		panel1.add(label3);
		panel1.add(tfNewConnector);
		
		btok = new JButton(Messages.getString("button.ok"));
		btok.setFont(font);
		
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel3.add(btok);
		
		btok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				isOk = true;
				dispose();
			}
			
		}); 
		
		JPanel box = new JPanel(new BorderLayout(5,5));
		box.add(panel1,BorderLayout.CENTER);
		box.add(panel3,BorderLayout.SOUTH);
		box.setBorder(new EmptyBorder(5,5,5,5));
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
		String newConnectorName = tfNewConnector.getText();
		if(newConnectorName==null || newConnectorName.length()==0){
			EntityWrapper ew = (EntityWrapper) cbComboBox.getSelectedItem();
			return (Connector) ew.getDialoyObject();
		}
		
		System.out.println("Creating new connector : " + newConnectorName);
		Connector connector = persistentManager.newConnector(newConnectorName);
		return connector;
	}
	
	public String getObjectName(){
		return tfNewObjectName.getText();
	}
	
	public static void main(String[] args) throws Exception {
//		JFrame f = new JFrame("test");
		NameAndConnectorChooseDialog dialog = new NameAndConnectorChooseDialog(new ODBPersistentManager("test.odb","test"),"Animal",TYPE_CONCEPT);
		dialog.setVisible(true);
		System.out.println(dialog.getConnector());
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		setTitle(Messages.getString("label.relation.between") +" "+ name1 + " "+Messages.getString("label.and")+" " + tfNewObjectName.getText());
		String objectName = tfNewObjectName.getText();
		String connectorName = tfNewConnector.getText().length()==0 ? cbComboBox.getSelectedItem().toString() : tfNewConnector.getText(); 
		btok.setText(Messages.getString("button.create") + " "  +name1 + " " + connectorName + " " + objectName);		
		
	}

	public boolean isOk() {
		return isOk;
	}
	
}
