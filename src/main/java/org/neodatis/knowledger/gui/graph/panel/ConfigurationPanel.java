package org.neodatis.knowledger.gui.graph.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.gui.graph.Configuration;
import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.VisualizationConfiguration;
import org.neodatis.knowledger.gui.tool.Messages;


/*
 * A panel to configure to graph visualization
 */
public class ConfigurationPanel extends JPanel {

	private IPersistentManager persistentManager;

	JTabbedPane pane;

	GeneralFilterPanel generalPanel;

	EntityFilterPanel connectorPanel;

	EntityFilterPanel conceptPanel;

	EntityFilterPanel instancePanel;

	EntityFilterPanel instanceOfPanel;

	QueryFilterPanel queryFilterPanel;

	EntityColorPanel entityColorPanel;
	
	Configuration configuration ;

	public ConfigurationPanel(IPersistentManager persistentManager) {
		super();
		this.persistentManager = persistentManager;
		configuration = Configurations.getConfiguration(persistentManager.getKnowledgeBaseName());
		initGUI();
	}

	private void initGUI() {

		pane = new JTabbedPane();
		generalPanel = new GeneralFilterPanel(configuration);
		connectorPanel = new EntityFilterPanel(configuration,persistentManager.getConnectors());
		conceptPanel = new EntityFilterPanel(configuration,persistentManager.getConcepts());
		instancePanel = new EntityFilterPanel(configuration,persistentManager.getInstances());
		instanceOfPanel = new EntityFilterPanel(configuration,persistentManager.getConcepts());
		queryFilterPanel = new QueryFilterPanel();
		entityColorPanel = new EntityColorPanel(configuration,persistentManager.getConnectors());
		;

		pane.addTab(Messages.getString("FilterPanel.general"), generalPanel); //$NON-NLS-1$
		pane.addTab(Messages.getString("FilterPanel.connectors"), connectorPanel); //$NON-NLS-1$
		pane.addTab(Messages.getString("FilterPanel.concepts"), conceptPanel); //$NON-NLS-1$
		pane.addTab(Messages.getString("FilterPanel.instancesOf"), instanceOfPanel); //$NON-NLS-1$
		pane.addTab(Messages.getString("FilterPanel.instances"), instancePanel); //$NON-NLS-1$
		//pane.addTab(Messages.getString("FilterPanel.query"), queryFilterPanel); //$NON-NLS-1$
		pane.addTab(Messages.getString("FilterPanel.color"), entityColorPanel); //$NON-NLS-1$

		JPanel buttonPanel = new JPanel();

		JButton btSave = new JButton(Messages.getString("FilterPanel.save")); //$NON-NLS-1$
		btSave.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					save();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			};
		});

		buttonPanel.add(btSave);

		setLayout(new BorderLayout(5, 5));
		add(pane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void save() throws Exception {
		System.out.println(Messages.getString("FilterPanel.saveConfiguration")); //$NON-NLS-1$
		RelationQuery query = new RelationQuery();

		Set keys = connectorPanel.getEntities().keySet();
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			JCheckBox checkBox = (JCheckBox) connectorPanel.getEntities().get(entity);
			if (checkBox.isSelected()) {
				// query.addConnectorToInclude(entity);
			} else {
				query.addConnectorToExclude(entity);
			}
		}

		keys = conceptPanel.getEntities().keySet();
		iterator = keys.iterator();
		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			JCheckBox checkBox = (JCheckBox) conceptPanel.getEntities().get(entity);
			if (checkBox.isSelected()) {
				// query.addLeftEntityToInclude(entity);
			} else {
				query.addLeftEntityToExclude(entity);
				query.addRightEntityToExclude(entity);
			}
		}

		keys = instanceOfPanel.getEntities().keySet();
		iterator = keys.iterator();
		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			JCheckBox checkBox = (JCheckBox) instanceOfPanel.getEntities().get(entity);
			if (checkBox.isSelected()) {
				// query.addLeftEntityToInclude(entity);
			} else {
				InstanceList instanceList = persistentManager.getInstancesOf((Concept) entity, true);
				query.addEntitiesToExclude(instanceList);
			}
		}

		keys = instancePanel.getEntities().keySet();
		iterator = keys.iterator();
		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			JCheckBox checkBox = (JCheckBox) instancePanel.getEntities().get(entity);
			if (checkBox.isSelected()) {
				// query.addLeftEntityToInclude(entity);
			} else {
				query.addLeftEntityToExclude(entity);
				query.addRightEntityToExclude(entity);
			}
		}

		VisualizationConfiguration vc = configuration.getVisualizationConfiguration();
		vc.setRelationQuery(query);
		vc.setQuery(queryFilterPanel.getQuery());
		vc.setDisplayOrphanConcepts(generalPanel.displayOrphanConcepts());
		vc.setDisplayOrphanInstances(generalPanel.displayOrphanInstances());
		vc.setDisplayConnectorsAsNodes(generalPanel.displayConnectorAsNode());
		vc.setDisplayLabelOnEdges(generalPanel.displayLabelOnEdge());
		
		vc.setConceptHaveBox(generalPanel.conceptHaveBox());
		vc.setInstanceHaveBox(generalPanel.instanceHaveBox());
		
		configuration.getKnowledgerGraphPanel().redraw();
	}
}

class GeneralFilterPanel extends JPanel {
	private JTextField tfName;

	private JCheckBox cbDisplayOrphanConcepts;

	private JCheckBox cbDisplayOrphanInstances;

	private JCheckBox cbDisplayConnectorsasNodes;
	
	private JCheckBox cbConceptHaveBox;
	private JCheckBox cbInstanceHaveBox;	
	private JCheckBox cbDisplayLabelOnEdge;

	private JTextField tfConceptFontSize;

	private JTextField tfInstanceFontSize;

	private JTextField tfConnectorSize;

	private JButton btConceptColor;

	private JButton btConnectorColor;
	private JButton btEdgeLabelColor;

	private JButton btInstanceColor;

	private JButton btEdgeColor;
	
	protected Configuration configuration;

	public GeneralFilterPanel(Configuration configuration) {
		super();
		this.configuration = configuration;
		initGUI();
	}

	private void initGUI() {
		JLabel label1 = new JLabel(Messages.getString("FilterPanel.name")); //$NON-NLS-1$
		tfName = new JTextField(30);

		JLabel label2 = new JLabel(Messages.getString("FilterPanel.displayOrphanConcepts"));
		JLabel label3 = new JLabel(Messages.getString("FilterPanel.displayOrphanInstances"));
		JLabel label4 = new JLabel(Messages.getString("FilterPanel.displayConnectorAsNode"));
		JLabel label45 = new JLabel(Messages.getString("FilterPanel.concept.have.box"));
		JLabel label46 = new JLabel(Messages.getString("FilterPanel.instance.have.box"));
		JLabel label5 = new JLabel(Messages.getString("FilterPanel.concept.color"));
		JLabel label6 = new JLabel(Messages.getString("FilterPanel.instance.color"));
		JLabel label7 = new JLabel(Messages.getString("FilterPanel.connector.color"));
		JLabel label8 = new JLabel(Messages.getString("FilterPanel.edge.color"));
		JLabel label9 = new JLabel(Messages.getString("FilterPanel.concept.font.size"));
		JLabel label10 = new JLabel(Messages.getString("FilterPanel.instance.font.size"));
		JLabel label11 = new JLabel(Messages.getString("FilterPanel.connector.font.size"));
		JLabel label12 = new JLabel(Messages.getString("FilterPanel.display.label.on.edge"));
		JLabel label13 = new JLabel(Messages.getString("FilterPanel.edge.label.color"));
		cbDisplayOrphanConcepts = new JCheckBox();
		cbDisplayOrphanInstances = new JCheckBox();
		cbDisplayConnectorsasNodes = new JCheckBox();
		cbConceptHaveBox = new JCheckBox();
		cbInstanceHaveBox = new JCheckBox();
		cbDisplayLabelOnEdge = new JCheckBox();

		btConceptColor = new ConceptColorButton(configuration);
		btInstanceColor = new InstanceColorButton(configuration);
		btConnectorColor = new ConnectorColorButton(configuration);
		btEdgeColor = new EdgeColorButton(configuration);
		btEdgeLabelColor = new EdgeLabelColorButton(configuration);

		tfConceptFontSize = new ConceptFontSizeText(configuration);
		tfInstanceFontSize = new InstanceFontSizeText(configuration);
		tfConnectorSize = new ConnectorFontSizeText(configuration);

		cbDisplayOrphanConcepts.setSelected(configuration.getVisualizationConfiguration().displayOrphanConcepts());
		cbDisplayOrphanInstances.setSelected(configuration.getVisualizationConfiguration().displayOrphanInstances());
		cbDisplayConnectorsasNodes.setSelected(configuration.getVisualizationConfiguration().displayConnectorsAsNodes());
		cbDisplayLabelOnEdge.setSelected(configuration.getVisualizationConfiguration().displayLabelOnEdges());
		cbConceptHaveBox.setSelected(configuration.getVisualizationConfiguration().conceptHaveBox());
		cbInstanceHaveBox.setSelected(configuration.getVisualizationConfiguration().instanceHaveBox());

		setLayout(new FlowLayout(FlowLayout.TRAILING));
		JPanel panel = new JPanel(new GridLayout(15, 2, 5, 5));

		panel.add(label1);
		panel.add(tfName);
		panel.add(label2);
		panel.add(cbDisplayOrphanConcepts);
		panel.add(label3);
		panel.add(cbDisplayOrphanInstances);
		panel.add(label4);
		panel.add(cbDisplayConnectorsasNodes);
		panel.add(label45);
		panel.add(cbConceptHaveBox);
		panel.add(label46);
		panel.add(cbInstanceHaveBox);
		panel.add(label12);
		panel.add(cbDisplayLabelOnEdge);
		
		panel.add(label5);
		panel.add(btConceptColor);
		panel.add(label6);
		panel.add(btInstanceColor);
		panel.add(label7);
		panel.add(btConnectorColor);
		panel.add(label8);
		panel.add(btEdgeColor);
		panel.add(label13);
		panel.add(btEdgeLabelColor);
		panel.add(label9);
		panel.add(tfConceptFontSize);
		panel.add(label10);
		panel.add(tfInstanceFontSize);
		panel.add(label11);
		panel.add(tfConnectorSize);

		add(panel);

	}

	public String getName() {
		return tfName.getText();
	}

	public boolean displayOrphanConcepts() {
		return cbDisplayOrphanConcepts.isSelected();
	}

	public boolean displayOrphanInstances() {
		return cbDisplayOrphanInstances.isSelected();
	}

	public boolean displayConnectorAsNode() {
		return cbDisplayConnectorsasNodes.isSelected();
	}
	
	public boolean displayLabelOnEdge() {
		return cbDisplayLabelOnEdge.isSelected();
	}
	public boolean conceptHaveBox() {
		return cbConceptHaveBox.isSelected();
	}
	public boolean instanceHaveBox() {
		return cbInstanceHaveBox.isSelected();
	}
}

class QueryFilterPanel extends JPanel {
	private JTextArea taQuery;

	public QueryFilterPanel() {
		super();
		initGUI();
	}

	private void initGUI() {
		JLabel label1 = new JLabel(Messages.getString("FilterPanel.name")); //$NON-NLS-1$
		taQuery = new JTextArea(4, 30);

		setLayout(new FlowLayout(FlowLayout.TRAILING));
		JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));

		panel.add(label1);
		panel.add(taQuery);

		add(panel);

	}

	public String getQuery() {
		return taQuery.getText();
	}

}

class EntityFilterPanel extends JPanel {
	private Map entities;
	protected Configuration configuration;
	public EntityFilterPanel(Configuration configuration,EntityList entityList) {
		super();
		this.configuration = configuration;
		entities = new HashMap();
		initGUI(entityList);
	}

	private void initGUI(EntityList entityList) {

		setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel checkBoxPanel = new JPanel(new GridLayout(entityList.size() / 2 + 1, 2, 5, 5));
		JPanel buttonPanel = new JPanel();
		for (int i = 0; i < entityList.size(); i++) {
			Entity entity = entityList.getEntity(i);
			boolean checked = !configuration.getVisualizationConfiguration().getRelationQuery().isExcluded(entity);
			JCheckBox checkBox = new JCheckBox(entity.getIdentifier(), checked);
			entities.put(entity, checkBox);
			checkBoxPanel.add(checkBox);
		}

		JButton btSelectAll = new JButton(Messages.getString("FilterPanel.displayAll")); //$NON-NLS-1$
		btSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				selectUnselectAll(true);
			};
		});

		JButton btHideAll = new JButton(Messages.getString("FilterPanel.hideAll")); //$NON-NLS-1$
		btHideAll.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				selectUnselectAll(false);
			};
		});

		buttonPanel.add(btSelectAll);
		buttonPanel.add(btHideAll);

		setLayout(new BorderLayout(5, 5));
		panel.add(checkBoxPanel);
		add(new JScrollPane(panel), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void save() throws Exception {
		System.out.println("Saving configuration "); //$NON-NLS-1$
		RelationQuery query = new RelationQuery();
		Set keys = entities.keySet();
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			Connector connector = (Connector) iterator.next();
			JCheckBox checkBox = (JCheckBox) entities.get(connector);
			if (checkBox.isSelected()) {
				query.addConnectorToInclude(connector);
			} else {
				query.addConnectorToExclude(connector);
			}
		}
		configuration.getVisualizationConfiguration().setRelationQuery(query);
		configuration.getKnowledgerGraphPanel().redraw();
	}

	private void selectUnselectAll(boolean select) {
		Collection collection = entities.values();
		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			JCheckBox element = (JCheckBox) iterator.next();
			element.setSelected(select);
		}
	}

	public Map getEntities() {
		return entities;
	}

}

class EntityColorPanel extends JPanel {
	private Map entities;
	protected Configuration configuration;

	public EntityColorPanel(Configuration configuration,EntityList entityList) {
		super();
		this.configuration = configuration;
		entities = new HashMap();
		initGUI(entityList);
	}

	private void initGUI(EntityList entityList) {

		setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel checkBoxPanel = new JPanel(new GridLayout(entityList.size() / 2 + 1, 2, 5, 5));
		JPanel buttonPanel = new JPanel();
		for (int i = 0; i < entityList.size(); i++) {
			Entity entity = entityList.getEntity(i);
			JButton button = new EntityColorButton(configuration,entity);
			entities.put(entity, button);
			checkBoxPanel.add(button);
		}

		setLayout(new BorderLayout(5, 5));
		panel.add(checkBoxPanel);
		add(new JScrollPane(panel), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void save() throws Exception {
		System.out.println("Saving configuration "); //$NON-NLS-1$
		RelationQuery query = new RelationQuery();
		Set keys = entities.keySet();
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			Connector connector = (Connector) iterator.next();
			JCheckBox checkBox = (JCheckBox) entities.get(connector);
			if (checkBox.isSelected()) {
				query.addConnectorToInclude(connector);
			} else {
				query.addConnectorToExclude(connector);
			}
		}
		configuration.getVisualizationConfiguration().setRelationQuery(query);
		configuration.getKnowledgerGraphPanel().refresh();
	}

	private void selectUnselectAll(boolean select) {
		Collection collection = entities.values();
		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			JCheckBox element = (JCheckBox) iterator.next();
			element.setSelected(select);
		}
	}

	public Map getEntities() {
		return entities;
	}

}

abstract class FontSizeText extends JTextField implements KeyListener {
	protected Configuration configuration;
	public FontSizeText(Configuration configuration,int i) {
		super("" + i);
		addKeyListener(this);
		this.configuration = configuration;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}
}

class ConceptFontSizeText extends FontSizeText {
	public ConceptFontSizeText(Configuration configuration) {
		super(configuration,configuration.getConceptFontSize());
	}

	public void keyReleased(KeyEvent e) {
		configuration.setConceptFontSize(Integer.parseInt(getText()));
	}
}

class ConnectorFontSizeText extends FontSizeText {
	public ConnectorFontSizeText(Configuration configuration) {
		super(configuration,configuration.getConnectorFontSize());
	}

	public void keyReleased(KeyEvent e) {
		configuration.setConnectorFontSize(Integer.parseInt(getText()));
	}
}

class InstanceFontSizeText extends FontSizeText {
	public InstanceFontSizeText(Configuration configuration) {
		super(configuration,configuration.getInstanceFontSize());
	}

	public void keyReleased(KeyEvent e) {
		configuration.setInstanceFontSize(Integer.parseInt(getText()));
	}
}

class ColorButton extends JButton implements ActionListener {
	private Color color;
	protected Configuration configuration;

	public ColorButton(Configuration configuration,Color color) {
		this.color = color;
		setBackground(color);
		addActionListener(this);
		this.configuration = configuration;
	}

	public void actionPerformed(ActionEvent e) {
		color = JColorChooser.showDialog(this, Messages.getString("Choose color"), getBackground());
		setBackground(color);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}

class ConceptColorButton extends ColorButton {
	public ConceptColorButton(Configuration configuration) {
		super(configuration,configuration.getConceptColor());
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		configuration.setConceptColor(getColor());
	}
}

class EdgeColorButton extends ColorButton {
	public EdgeColorButton(Configuration configuration) {
		super(configuration,configuration.getEdgeColor());
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		configuration.setEdgeColor(getColor());
	}
}
class EdgeLabelColorButton extends ColorButton {
	public EdgeLabelColorButton(Configuration configuration) {
		super(configuration,configuration.getConnectorLabelColor());
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		configuration.setConnectorLabelColor(getColor());
	}
}

class ConnectorColorButton extends ColorButton {
	public ConnectorColorButton(Configuration configuration) {
		super(configuration,configuration.getConnectorColor());
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		configuration.setConnectorColor(getColor());
	}
}

class InstanceColorButton extends ColorButton {
	public InstanceColorButton(Configuration configuration) {
		super(configuration,configuration.getInstanceColor());
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		configuration.setInstanceColor(getColor());
	}
}

class EntityColorButton extends ColorButton {
	private Entity entity;

	public EntityColorButton(Configuration configuration,Entity entity) {
		super(configuration,configuration.getVisualizationConfiguration().getColorOf(entity));
		setText(entity.getIdentifier());
		this.entity = entity;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		configuration.getVisualizationConfiguration().setColorOf(entity, getColor());
	}
}
