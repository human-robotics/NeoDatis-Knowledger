/*
 * Created on 26/05/2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.defaults.ODBPersistentManager;


public class DialogoMapeamento extends JDialog implements ActionListener {

	private Entity conceito;
	private InstanceList instancias;
	private IPersistentManager persistentManager;
	private String nomeRelacao;
	
	private JComboBox cbInstancias;
	private JButton btOk;
	private JButton btCancela;
	private boolean isOk;
	
	public DialogoMapeamento(IPersistentManager persistentManager , Entity base, InstanceList instancias , String nomeRelacao){
		this.conceito = base;
		this.instancias = instancias;
		this.persistentManager = persistentManager;
		this.nomeRelacao = nomeRelacao;
		
		init();
	}
	
	private void init() {
		Vector v = new Vector();
		v.addAll(instancias);
		cbInstancias = new JComboBox(v);
		
		JLabel label = new JLabel("Mapear " + conceito.getIdentifier() + " com uma das instâncias:");
		
		btOk = new JButton("Ok");
		btCancela = new JButton("Cancela");
		btOk.setActionCommand("ok");
		btCancela.setActionCommand("cancela");
		btOk.addActionListener(this);
		btCancela.addActionListener(this);
		
		getContentPane().setLayout(new BorderLayout(5,5));
		
		JPanel panel = new JPanel();
		panel.add(btOk);
		panel.add(btCancela);
		
		JPanel painelGeral = new JPanel(new BorderLayout(5,5));
		painelGeral.add(label , BorderLayout.NORTH);
		painelGeral.add(panel , BorderLayout.SOUTH);
		painelGeral.add(cbInstancias , BorderLayout.CENTER);
		painelGeral.setBorder(new EmptyBorder(10,10,10,10));
		getContentPane().add(painelGeral);
		
		pack();
		setModal(true);
	}

	

	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("ok")){
			try {
				isOk = true;
				criarMapeamento();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		dispose();
		
	}

	private void criarMapeamento() throws Exception {
		Instance instanceSelecionada = (Instance) cbInstancias.getSelectedItem();
		Connector connector = persistentManager.getConnectorFromName(nomeRelacao,GetMode.CREATE_IF_DOES_NOT_EXIST);
		
		System.out.println("Criando mapeamento entre " + conceito.getIdentifier() + " e " + instanceSelecionada.getIdentifier() + " usando " + connector.getIdentifier());
		
		persistentManager.newRelation(instanceSelecionada , connector , conceito );
		
	}

	/**
	 * @return Returns the isOk.
	 */
	public boolean isOk() {
		return isOk;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		IPersistentManager persistentManager = new ODBPersistentManager("test5.odb");
		Concept conceitoCelula = persistentManager.getConceptFromName("Celula",GetMode.CREATE_IF_DOES_NOT_EXIST);
		Instance icelula = conceitoCelula.newInstance("celula");
		
		
		Concept conceitoPagina = persistentManager.getConceptFromName("Pagina",GetMode.CREATE_IF_DOES_NOT_EXIST);
		InstanceList instancias = new  InstanceList();
		for(int i=0;i<25;i++){
			instancias.add(conceitoPagina.newInstance("pagina " + (i+1)));
		}
		
		DialogoMapeamento dialogoMapeamento = new DialogoMapeamento(persistentManager,icelula, instancias , "é página de");
		dialogoMapeamento.setVisible(true);
		if(dialogoMapeamento.isOk()){
			System.out.println("is ok");
		}else{
			System.out.println("not ok");
		}
		System.exit(0);

	}

}
