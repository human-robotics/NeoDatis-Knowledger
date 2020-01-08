package org.neodatis.knowledger.example;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.odb.tool.IOUtil;


public class HelpDeskExample {

    
    public void step1() throws Exception{
        
        // Opens the helpdesk knowledge base, if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("helpdesk");
        
        // Gets the root of all concepts - If it does not exist, creates it
        Concept root = knowledgeBase.getRootObject();
        
        // Gets the HelpDesk concept as sub-concept of root
        Concept helpDesk = root.getSubConcept("HelpDesk");
        
        // Gets the Product Line concept as sub-concept of root
        Concept productLine = root.getSubConcept("Product Line");

        // Gets the Product concept as sub-concept of root
        Concept product = root.getSubConcept("Product");

        // Gets the Trend concept
        Concept trend = root.getSubConcept("Trend");
        
        // creates an instance of Trend : Palm
        Instance palm = trend.newInstance("Palm");
        

        // Creates 3 instances of Product Line : Treo, Tungsten and LifeDrive
        Instance treoProductLine = productLine.newInstance("Treo Product Line");
        Instance lifeDriveProductLine = productLine.newInstance("LifeDrive Product Line");
        Instance tungstenProductLine = productLine.newInstance("Tungsten Product Line");
        
        // Gets a connector 'is part of'. A connector is used to create a relation
        // between two entities
        Connector isPartOf = knowledgeBase.getConnectorFromName("is part of", GetMode.CREATE_IF_DOES_NOT_EXIST);

        // Tells the knowledge base that trend of product lines is Palm
        treoProductLine.connectTo(isPartOf, palm);
        tungstenProductLine.connectTo(isPartOf, palm);
        lifeDriveProductLine.connectTo(isPartOf, palm);

        // Creates the Treo and Tungsten products as instances of Product
        Instance treo650 = product.newInstance("Treo 650");
        Instance treo700p = product.newInstance("Treo 700p");
        Instance treo700w = product.newInstance("Treo 700w");
        
        Instance tungstenT3 = product.newInstance("Tunsten T3");
        Instance tungstenT5 = product.newInstance("Tunsten T5");
        
        
        // Tells the knowledge base that treo products are part of Treo product line
        treo650.connectTo(isPartOf, treoProductLine);
        treo700p.connectTo(isPartOf, treoProductLine);
        treo700w.connectTo(isPartOf, treoProductLine);

        // Tells the knowledge base that tungsten products are part of Tungsten product line
        tungstenT3.connectTo(isPartOf, tungstenProductLine);
        tungstenT5.connectTo(isPartOf, tungstenProductLine);

        // Close the knowledge base
        knowledgeBase.close();
        
    }
    
    public void step2() throws Exception{
        // Opens the helpdesk knowledge base, if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("helpdesk");
        
        // Get the trend Concept
        Concept trend = knowledgeBase.getConceptFromName("Trend", GetMode.CREATE_IF_DOES_NOT_EXIST);
        
        // Gets all the trend existing in the knowledge base
        RelationList relations = knowledgeBase.queryRelations(null, knowledgeBase.getIsInstanceOfConnector(), trend);

        // get left parts of the relations
        EntityList trends = relations.getLeftEntities();
        
        System.out.println("Trends : " + trends);

        // Get the product Concept
        Concept product = knowledgeBase.getConceptFromName("Product", GetMode.CREATE_IF_DOES_NOT_EXIST);

        // Gets all the products (instances of product) existing in the knowledge base
        relations = knowledgeBase.queryRelations(null, knowledgeBase.getIsInstanceOfConnector(), product);

        // get left parts of the relations
        EntityList products = relations.getLeftEntities();
        
        System.out.println("Products : " + products);
        

        knowledgeBase.close();
        
        
    }

    public void step3() throws Exception{
        // Opens the helpdesk knowledge base, if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("helpdesk");
        
        // Get the trend Concept
        Concept trend = knowledgeBase.getConceptFromName("Trend", GetMode.CREATE_IF_DOES_NOT_EXIST);
        
        // Builds a query that will include relations with connector
        // is instance of , and right part Trend.
        RelationQuery rquery = new RelationQuery();
        rquery.addConnectorToInclude(knowledgeBase.getIsInstanceOfConnector());
        rquery.addRightEntityToInclude(trend);
        
        // Execute the query on all relations
        RelationList relations = rquery.executeOn(knowledgeBase.getRelationList());

        // get left parts of the relations
        EntityList trends = relations.getLeftEntities();
        
        System.out.println("Trends : " + trends);

        // Get the product Concept
        Concept product = knowledgeBase.getConceptFromName("Product", GetMode.CREATE_IF_DOES_NOT_EXIST);

        rquery = new RelationQuery();
        rquery.addConnectorToInclude(knowledgeBase.getIsInstanceOfConnector());
        rquery.addRightEntityToInclude(product);
        // Gets all the products (instances of product) existing in the knowledge base
        relations = rquery.executeOn(knowledgeBase.getRelationList());

        // get left parts of the relations
        EntityList products = relations.getLeftEntities();
        
        System.out.println("Products : " + products);
        

        knowledgeBase.close();
        
        
    }
    
    public void step4() throws Exception{
        // Opens the helpdesk knowledge base, if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("helpdesk");
        
        // Get the product Concept
        Concept product = knowledgeBase.getConceptFromName("Product", GetMode.CREATE_IF_DOES_NOT_EXIST);
        
        // Builds a query that will include relations with connector
        // is instance of , and right part Trend.
        RelationQuery rquery = new RelationQuery();
        rquery.addConnectorToExclude(knowledgeBase.getIsInstanceOfConnector());
        rquery.addConnectorToExclude(knowledgeBase.getIsSubRelationOfConnector());
        
        // Execute the query on all relations
        RelationList relations = rquery.executeOn(knowledgeBase.getRelationList());

        // get left parts of the relations
        EntityList trends = relations.getLeftEntities();
        
        System.out.println("Trends : " + trends);

        knowledgeBase.close();
        
        
    }
    
    public void step5() throws Exception{
        // Opens the helpdesk knowledge base, if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("helpdesk");
        
        // Get the product Concept
        Concept product = knowledgeBase.getConceptFromName("Product", GetMode.CREATE_IF_DOES_NOT_EXIST);
        
        // Create an Operational System Concept
        Concept os = knowledgeBase.getRootObject().getSubConcept("Operating System");
        
        // Creates two instances of OS
        Instance palmOs = os.newInstance();
        Instance windowsMobileOs = os.newInstance();
        
        Concept name = knowledgeBase.getRootObject().getSubConcept("Name");
        
        // Then set the property name
        palmOs.setProperty("name", name.newInstance("Palm OS"));
        windowsMobileOs.setProperty("name", name.newInstance("Windows Mobile OS"));
        
        // Gets all instances of Product
        InstanceList products = product.getInstances();
        
        Instance instance = null;
        for(int i=0;i<products.size();i++){
            instance = products.getInstance(i);
            // The Treo 700w uses Window Mobile
            if(instance.getIdentifier().equals("Treo 700w")){
                instance.connectTo("os", windowsMobileOs);
            }else{
                instance.connectTo("os", palmOs);
            }
        }
        
        
        EntityCriteria criteria = new EntityCriteria("os.name","Palm OS");
        EntityList palmOsProducts = knowledgeBase.query(criteria);

        System.out.println("Palm OS products : " + palmOsProducts);
        
        criteria = new EntityCriteria("os.name","Windows Mobile OS");
        EntityList windowsOsProducts = knowledgeBase.query(criteria);

        System.out.println("Widows Mobile OS products : " + windowsOsProducts);
        knowledgeBase.close();
        
        
    }
    
    public void step6() throws Exception{
        // Opens the helpdesk knowledge base, if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("helpdesk");
        
        // All product with the OS equal to Palm OS
        String kql = "select x where x.os.name is 'Palm OS';";
        EntityList palmOsProducts = knowledgeBase.query(kql);

        Instance i = (Instance) palmOsProducts.get(0);
        System.out.println(i.getConcept());
        // All product with the OS equal to Palm OS or starting with Windo
        kql = "select x where x.os.name is 'Palm OS' or x.os.name is 'Windo%';";
        EntityList allProducts = knowledgeBase.query(kql);
        System.out.println("All products : " + allProducts);

        knowledgeBase.close();        
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Knowledger API HelpDesk Example");
        IOUtil.deleteFile("helpdesk.knowledger");
        HelpDeskExample helpdesk = new HelpDeskExample();
        
        helpdesk.step1();
        helpdesk.step2();
        helpdesk.step3();
        helpdesk.step4();
        helpdesk.step5();
        helpdesk.step6();
        
        
        
    }
    
}
