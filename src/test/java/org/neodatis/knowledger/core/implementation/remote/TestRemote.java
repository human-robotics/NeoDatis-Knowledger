/* 
 * $RCSfile: TestRemote.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.remote;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;

public class TestRemote {
    public static void main(String[] args) throws Exception {
        GlobalConfiguration.setWebContext("coAdapt");
        //IKnowledgeBase kb = KnowledgeBaseFactory.getInstance("TesteNavegacao", KnowledgeBaseType.REMOTE,"alessandra", "8080");
        IKnowledgeBase kb = KnowledgeBaseFactory.getInstance("TesteNavegacao");
        System.out.println(kb.getConceptList());
        Concept concept = kb.getConceptFromName("Usuario",GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
        System.out.println(concept.getInstances());
        Instance i = concept.getInstances().getFirst();
        //Instance ii = concept.getInstances().getFirst();
        System.out.println(i.getConcept() + " - " + i.getId());
        
        System.out.println(kb.getInstanceList());
        //Instance i = concept.newInstance("i4");
        for(int j=0;j<kb.getInstanceList().size();j++){
            System.out.println(kb.getInstanceList().getEntity(j));
            Instance ii = (Instance) kb.getInstanceList().getEntity(j);
            try {
                System.out.println(ii.getConcept());
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
        kb.close();
    }

}
