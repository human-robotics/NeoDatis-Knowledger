/* 
 * $RCSfile: TestGet.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:56:11 $
 * 
 * Copyright 2003 Cetip
 */
package test;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;

public class TestGet {
    public static void main(String[] args) throws Exception {
        GlobalConfiguration.setLanguage("br");
        KnowledgeBase kb = KnowledgeBaseFactory.getInstance("Sistemas de Hipermidia");
        Concept modelo = kb.getConceptFromName("Modelo do Dominio");
        Connector connector = kb.getConnectorFromName("e primeira pagina de", GetMode.CREATE_IF_DOES_NOT_EXIST);
        Connector connector2 = kb.getConnectorFromName("e pagina de", GetMode.CREATE_IF_DOES_NOT_EXIST);
        // Query by example
        RelationList rlist = kb.queryRelations(null,connector, modelo);
        EntityList list = rlist.getLeftEntities();
        System.out.println("-------------------------------------------");
        System.out.println(rlist);
        System.out.println("------");
        System.out.println(list);
        
        RelationQuery query = new RelationQuery();
        //query.addConnectorToInclude(connector);
        query.addConnectorToExclude(connector);
        RelationList rlist2 = query.executeOn(kb.getRelationList());
        System.out.println("------");
        System.out.println("2\n"+rlist2);
        
        EntityCriteria criteria = new EntityCriteria("e primeira pagina de","Modelo do Dominio");
        EntityList rlist3 = kb.query(criteria);
        System.out.println("------");
        System.out.println("3\n"+rlist3);

        String kql = "select x where x.'e primeira pagina de' = 'Modelo do Dominio';";
        //String kql = "select x where x.name = oli";
        EntityList entityList = kb.query(kql);
        System.out.println("------");
        System.out.println("4\n"+entityList);
        
        //
        
        kb.close();
    }

}
