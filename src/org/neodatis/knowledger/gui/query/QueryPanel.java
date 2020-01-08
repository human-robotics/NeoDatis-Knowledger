package org.neodatis.knowledger.gui.query;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.explorer.KnowledgeExplorerPanel;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.knowledger.gui.tool.ToolsForGui;


public class QueryPanel extends JPanel implements ActionListener {

	private static final String ACTION_EXECUTE = "execute";

	private static final String ACTION_ADD = "add";

	private static final String ACTION_CLEAR = "clear";

	private static final String ACTION_GRAPH_VISUALIZATION = "graphic-visualization";

	private JTable queryTable;

	private QueryTableModel model;

	private KnowledgeBase knowledgeBase;

	// private JTextArea taResult;
	private KnowledgeExplorerPanel knowledgeExplorerPanel;

	private JLabel lbQuery;

	public QueryPanel(KnowledgeBase knowledgeBase) {
		init(knowledgeBase);
	}

	private void init(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
		model = new QueryTableModel();
		queryTable = new JTable(model);
		queryTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
		setColumnAttributes();

		queryTable.setRowHeight(20);
		queryTable.setPreferredSize(new Dimension(350, 200));

		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(7, 7, 7, 7));

		JPanel buttonPanel = new JPanel();

		JButton addButton = new JButton(ToolsForGui.createWebImageIcon("/images/newLine.gif"));
		addButton.setToolTipText(Messages.getString("tooltip.click.here.to.add.a.line.to.define.your.query"));
		addButton.setFocusable(false);
		addButton.setBorderPainted(false);
		addButton.setActionCommand(ACTION_ADD);
		addButton.addActionListener(this);

		JButton executeButton = new JButton(ToolsForGui.createWebImageIcon("/images/execute.gif"));
		executeButton.setToolTipText(Messages.getString("tooltip.click.here.to.execute.the.query"));
		executeButton.setFocusable(false);
		executeButton.setBorderPainted(false);
		executeButton.setActionCommand(ACTION_EXECUTE);
		executeButton.addActionListener(this);

		JButton clearButton = new JButton(ToolsForGui.createWebImageIcon("/images/delete.gif"));
		clearButton.setToolTipText(Messages.getString("tooltip.click.here.to.clear.the.query.table"));
		clearButton.setFocusable(false);
		clearButton.setBorderPainted(false);
		clearButton.setActionCommand(ACTION_CLEAR);
		clearButton.addActionListener(this);

		buttonPanel.add(addButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(executeButton);

		JPanel resultButtonPanel = new JPanel();

		JButton openGraphButton = new JButton(ToolsForGui.createWebImageIcon("/images/relationIcon.gif"));
		openGraphButton.setActionCommand(ACTION_GRAPH_VISUALIZATION);
		openGraphButton.addActionListener(this);
		resultButtonPanel.add(openGraphButton);

		// taResult = new JTextArea(5,5);
		try {
			knowledgeExplorerPanel = new KnowledgeExplorerPanel(knowledgeBase, "No result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lbQuery = new JLabel("?");
		lbQuery.setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

		JPanel resultPanel = new JPanel(new BorderLayout());
		resultPanel.setBorder(new TitledBorder("Result"));

		resultPanel.add(new JScrollPane(knowledgeExplorerPanel), BorderLayout.CENTER);

		resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);
		resultPanel.add(lbQuery, BorderLayout.NORTH);

		mainPanel.add(new JScrollPane(queryTable), BorderLayout.CENTER);
		mainPanel.add(resultPanel, BorderLayout.SOUTH);
		mainPanel.add(buttonPanel, BorderLayout.NORTH);

		add(mainPanel, BorderLayout.CENTER);
		// add(buttonPanel,BorderLayout.SOUTH);

	}

	private void setColumnAttributes() {
		TableColumn column = queryTable.getColumnModel().getColumn(QueryTableModel.COLUMN_LOGICAL_OPERATOR);
		String[] operators = { "-", "or", "and" };

		JComboBox comboBox = new JComboBox(operators);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		// column.setMaxWidth(35);

		//

		column = queryTable.getColumnModel().getColumn(QueryTableModel.COLUMN_COMPARE_OPERATOR);
		String[] operators2 = { "=", "like", "<", "<=", ">", ">=" };

		comboBox = new JComboBox(operators2);
		column.setCellEditor(new DefaultCellEditor(comboBox));
		// column.setMaxWidth(35);

		//

		column = queryTable.getColumnModel().getColumn(QueryTableModel.COLUMN_BINDED_OBJECT);
		column.setMaxWidth(8);

	}

	public void actionPerformed(ActionEvent actionEvent) {

		if (actionEvent.getActionCommand().equals(ACTION_EXECUTE)) {
			try {
				execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (actionEvent.getActionCommand().equals(ACTION_ADD)) {
			try {
				model.addCriteria();
				lbQuery.setText(model.getResultingCriteria().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (actionEvent.getActionCommand().equals(ACTION_CLEAR)) {
			try {
				model.clear();
				lbQuery.setText("?");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (actionEvent.getActionCommand().equals(ACTION_GRAPH_VISUALIZATION)) {
			try {
				model.clear();
				lbQuery.setText("?");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void execute() throws Exception {
		System.out.println("criterias:" + model.getCriteriaList());
		System.out.println("operators:" + model.getLogicalOperators());

		System.out.println(model.getResultingCriteria());
		lbQuery.setText(model.getResultingCriteria().toString());

		EntityList list = knowledgeBase.query(model.getResultingCriteria());
		knowledgeExplorerPanel.refresh(list, "Results of query : select x where " + model.getResultingCriteria().toString());
		System.out.println(list);
	}

}
