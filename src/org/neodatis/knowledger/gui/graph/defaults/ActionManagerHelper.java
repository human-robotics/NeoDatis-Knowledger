/* 
 * $RCSfile: ActionManagerHelper.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import javax.swing.JOptionPane;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.basic.RelationNode;
import org.neodatis.knowledger.gui.graph.panel.KnowledgerGraphPanel;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.ILogger;

import prefuse.data.Node;


public class ActionManagerHelper {
    
    public IPersistentManager persistentManager;
    public IGraphProvider graphProvider;
    public KnowledgerGraphPanel graphPanel;
    private ILogger logger;

    
    public ActionManagerHelper(IPersistentManager persistentManager, IGraphProvider graphProvider, KnowledgerGraphPanel graphPanel, ILogger logger) {
        this.persistentManager = persistentManager;
        this.graphProvider = graphProvider;
        this.graphPanel = graphPanel;
        this.logger = logger;
    }

    public void connect(Node node1, Connector connector, Node node2) throws Exception {

        Entity entity1 = persistentManager.getEntityById(node1.getString(GraphConstants.ID));
        Entity entity2 = persistentManager.getEntityById(node2.getString(GraphConstants.ID));

        Relation relation = persistentManager.newRelation(entity1, connector, entity2);

        RelationNode relationNode = graphProvider.createRelation(node1, connector, String.valueOf(relation.getId()), node2);

        graphPanel.refresh();
        logger.debug("connecting " + entity1.getIdentifier() + " to " + entity2.getIdentifier() + " using " + connector.getId() + " connector");
    }

    public void newConcept(int x, int y, boolean editing) throws Exception {

        boolean canCreateIsolatedConcept = persistentManager.getConcepts().isEmpty();
        
        if(!canCreateIsolatedConcept){
            JOptionPane.showMessageDialog(null,Messages.getString("info.create.concept.from.other.concept"));
            return;
        }
        String newConceptName = (String) JOptionPane.showInputDialog(null, Messages.getString("label.ask.concept.name"), Messages.getString("label.info"), JOptionPane.PLAIN_MESSAGE);

        if(newConceptName==null || newConceptName.length()==0){
            return;
        }

        Concept concept = persistentManager.newConcept(newConceptName);
        Node node = graphProvider.createConceptNode(String.valueOf(concept.getId()), newConceptName);
        /**
         * int row = node.getRow(); Table t =
         * (Table)graphPanel.getPrefuseGraphBuilder().getVisualization().getGroup("graph.nodes");
         * NodeItem ni = (NodeItem)t.getTuple(row);
         */
        graphPanel.refresh();
        logger.debug("creating new orphan concept with id " + concept.getId());
    }
}
