/* 
 * $RCSfile: KnowledgeExplorerPanel.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.explorer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.neodatis.knowledger.core.ObjectDoesNotExistException;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.panel.SimpleQueryPanel;
import org.neodatis.knowledger.gui.query.QueryPanel;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.knowledger.gui.tool.ToolsForGui;
import org.neodatis.tool.DLogger;



/**
 * <p>
 * 
 * </p>
 * 
 */
public class KnowledgeExplorerPanel extends JPanel implements ActionListener {

	private static final int SEARCH_TYPE_BY_NAME = 0;
	private static final int SEARCH_TYPE_BY_ID = 1;

	private static final String ACTION_FIND = "find";
	private static final String ACTION_FIND_NEXT = "find-next";
	private static final String ACTION_REFRESH = "refresh";
	public static String QUERY = "query";
	public static String SIMPLE_QUERY = "simple-query";

	private KnowledgeExplorerTree knowledgeExplorer;

	private JTextField tfSearchName;
	private JComboBox cbSearchType;
    private JTree tree;
    private JScrollPane pane;

    public KnowledgeExplorerPanel(KnowledgeBase knowledgeBase,List roots, String title) throws Exception {
		super();
		init(knowledgeBase,roots, title);
	}

    public KnowledgeExplorerPanel(KnowledgeBase knowledgeBase,String title) throws Exception {
		super();
		List roots = new ArrayList();

		roots.add(knowledgeBase.getRootObject());
    	roots.add(knowledgeBase.getRootConnector());

		init(knowledgeBase,roots,title);
	}

	private void init(KnowledgeBase knowledgeBase, List roots, String title ) throws Exception {
		knowledgeExplorer = new KnowledgeExplorerTree(knowledgeBase,roots,title);
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		JToolBar toolBar = new JToolBar();
		tfSearchName = new JTextField(10);
		tfSearchName.setMaximumSize(new Dimension(50,25));

		JButton btFind = new JButton(ToolsForGui.createWebImageIcon("/images/search.png"));
		btFind.setFocusable(false);
		btFind.setBorderPainted(false);
		btFind.setActionCommand(ACTION_FIND);
		btFind.addActionListener(this);
		btFind.setToolTipText(Messages.getString("tooltip.search.object"));

		JButton btFindNext = new JButton(ToolsForGui.createWebImageIcon("/images/search.png"));
		btFindNext.setFocusable(false);
		btFindNext.setBorderPainted(false);
		btFindNext.setActionCommand(ACTION_FIND_NEXT);
		btFindNext.addActionListener(this);
		btFindNext.setToolTipText(Messages.getString("tooltip.search.next.object"));

		JButton btRefresh = new JButton(ToolsForGui.createWebImageIcon("/images/refresh.png"));
		btRefresh.setFocusable(false);
		btRefresh.setBorderPainted(false);
		btRefresh.setActionCommand(ACTION_REFRESH);
		btRefresh.addActionListener(this);
		btRefresh.setToolTipText(Messages.getString("tooltip.reload.explorer"));

		JButton newQueryButton = new JButton(ToolsForGui.createWebImageIcon("/images/query.png"));
		newQueryButton.setFocusable(false);
		newQueryButton.setBorderPainted(false);
		newQueryButton.setActionCommand(SIMPLE_QUERY);
		newQueryButton.addActionListener(this);
		newQueryButton.setToolTipText(Messages.getString("tooltip.create.new.simple.query"));

		JButton newAdvancedQueryButton = new JButton(ToolsForGui.createWebImageIcon("/images/query.png"));
		newAdvancedQueryButton.setFocusable(false);
		newAdvancedQueryButton.setBorderPainted(false);
		newAdvancedQueryButton.setActionCommand(QUERY);
		newAdvancedQueryButton.addActionListener(this);
		newAdvancedQueryButton.setToolTipText(Messages.getString("create.new.advanced.query"));

		cbSearchType = new JComboBox();
		cbSearchType.addItem(Messages.getString("label.by.name"));
		cbSearchType.addItem(Messages.getString("label.by.id"));
		cbSearchType.setMaximumSize(new Dimension(90,25));

		toolBar.add(tfSearchName);
		toolBar.add(cbSearchType);
		toolBar.add(btFind);
		toolBar.add(btFindNext);
		toolBar.addSeparator();
		toolBar.add(btRefresh);
		toolBar.addSeparator();
		/*
		toolBar.add(newConceptButton);
		toolBar.add(newInstanceButton);
		toolBar.add(newRelationTypeButton);
		toolBar.add(newRelationButton);
		*/
		toolBar.add(newQueryButton);
		toolBar.add(newAdvancedQueryButton);

		add(toolBar, BorderLayout.NORTH);
		tree = knowledgeExplorer.getTree();
		pane = new JScrollPane(tree);
		add(pane,BorderLayout.CENTER);
	}

	private String getTextToSearch() {
		return tfSearchName.getText();
	}
	private int getSearchType() {
		if (cbSearchType.getSelectedItem().toString().equals(Messages.getString("label.by.name"))) {
			return SEARCH_TYPE_BY_NAME;
		} else {
			return SEARCH_TYPE_BY_ID;
		}
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		String windowTitle = null;
		JPanel panel = null;
		boolean createFrame = false;

		if (e.getActionCommand().equals(ACTION_FIND) || e.getActionCommand().equals(ACTION_FIND_NEXT)) {
			DLogger.debug("looking for object " + getTextToSearch() + " " + getSearchType());
			int searchType = getSearchType();

			switch (searchType) {
				case SEARCH_TYPE_BY_NAME :
					knowledgeExplorer.searchNode(getTextToSearch(), true , e.getActionCommand().equals(ACTION_FIND_NEXT));
					//tree.getModel().
					
					break;
				case SEARCH_TYPE_BY_ID :
					try {
						long id = Integer.parseInt(getTextToSearch());
						knowledgeExplorer.searchNode(id, true,e.getActionCommand().equals(ACTION_FIND_NEXT));
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showConfirmDialog(this, getTextToSearch() + " " + Messages.getString("label.is.not.a.number"));
					}
					break;
			}
		}

		if (e.getActionCommand().equals(ACTION_REFRESH)) {
			try {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				knowledgeExplorer.refresh();
				//pane.repaint();
				//this.repaint();
				//frame.repaint();

			} catch (ObjectDoesNotExistException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            } catch (Exception e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            } finally {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
		/*
		if (e.getActionCommand().equals(NEW_RELATION)) {
			windowTitle = Messages.getString("relation.creator");
			try {
                panel = new NewObjectPanel(knowledgeExplorer.getknowledgeBase(),NewObjectPanel.CREATE_CONNECTOR);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}
		

		if (e.getActionCommand().equals(NEW_CONNECTOR)) {
			windowTitle = Messages.getString("connector.creator");
			try {
                panel = new NewObjectPanel(knowledgeExplorer.getknowledgeBase(),NewObjectPanel.CREATE_CONNECTOR);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}
		*/
		if (e.getActionCommand().equals(SIMPLE_QUERY)) {
			windowTitle = Messages.getString("title.advanced.query.creator");
			try {
                panel = new SimpleQueryPanel(knowledgeExplorer.getknowledgeBase());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}

		if (e.getActionCommand().equals(QUERY)) {
			windowTitle = Messages.getString("title.query.creator");
			try {
                panel = new QueryPanel(knowledgeExplorer.getknowledgeBase());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}

		if (createFrame) {
			JFrame frame = new JFrame(windowTitle);
			frame.getContentPane().add(panel);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		}

	}

	public static final void display(KnowledgeBase knowledgeBase,String title) throws Exception {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Dialogy Knowledger - Explorer - " + knowledgeBase.getName());
    	List roots = new ArrayList();
    	roots.add(knowledgeBase.getRootObject());
    	roots.add(knowledgeBase.getRootConnector());

    	JPanel panel = new KnowledgeExplorerPanel(knowledgeBase,roots,title);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static final void displayRelationTypes(KnowledgeBase knowledgeBase) throws Exception {

			
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Knowledger Relation Explorer");

		KnowledgeExplorerPanel panel = new KnowledgeExplorerPanel(knowledgeBase,"Relations");

		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
                    display(KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY),"Test");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
			}
		});
	}

	public void refresh(EntityList list, String title) {
		try {
			knowledgeExplorer.refresh(list,title);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
