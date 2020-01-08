/* 
 * $RCSfile: ODBKnowledgeBase.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.implementation.knowledgebase;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.neodatis.knowledger.core.factory.KnowledgeBaseDescription;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.Proposition;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.interfaces.knowledgebase.ExternalRepository;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.odb.ODB;


/**
 * <p>
 * 
 * </p>
 * 
 * 
 */
public class ODBKnowledgeBase extends KnowledgeBase implements ExternalRepository {
    private transient ODB odb;

    public ODBKnowledgeBase() throws Exception {
    }

    /** Create a knowledger base using a container file
     * 
     * @param fileName The name of the container file
     * @param baseName The name of the base
     * @throws Exception
     */
    public ODBKnowledgeBase(String fileName, String baseName) throws Exception {
        odb = ODBFactory.open(fileName);
        init(baseName,KnowledgeBaseType.ODB);
        odb.store(this);
        odb.commit();
        externalRepository = this;
    }
    
    /** Create a knowledger base using a container file with the same name of the base
     * 
     * @param baseName The name of the base
     * @throws Exception
     */
    public ODBKnowledgeBase(String baseName) throws Exception {
    	this(baseName+".odb",baseName);
    }
    /*
    public ODBKnowledgeBase(String baseName, ODB odb) throws Exception{
        this.odb = odb;
        init(baseName,KnowledgeBaseType.ODB);
        externalRepository = this;
    }*/

    protected void init(String name, KnowledgeBaseType knowledgeBaseType) throws Exception {
        super.init(name, knowledgeBaseType,this);
    }
    protected void init(ODB odb) throws Exception{
        this.odb = odb;
        externalRepository = this;
        super.init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#create(java.lang.Object)
     */
    public void create(Object object) throws Exception {
        odb.store(object);
        odb.commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#update(java.lang.Object)
     */
    public void update(Object object) throws Exception {
        odb.store(object);
        odb.commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#delete(java.lang.Object)
     */
    public void delete(Object object) throws Exception {
        odb.delete(object);
        odb.commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#delete(java.util.List)
     */
    public void delete(List objects) throws Exception {
        for (int i = 0; i < objects.size(); i++) {
            odb.delete(objects.get(i));
        }
        odb.commit();
    }

    public Long getId() {
        return super.getId();
    }

    /**
     * 
     */
    public String getName() {
        return super.getName();
    }

    /**
     * 
     */
    public String getKBType() {
        return super.getType().getName();
    }

    public void setKBType(String type) {
        // in this case it always database
        setType(KnowledgeBaseType.ODB);
    }

    public static KnowledgeBaseDescription[] getAllKnowledgeBaseDescriptions() throws Exception {
        File files = new File(".");
        File[] kbFiles = files.listFiles(new MyFilter());

        KnowledgeBaseDescription[] knowledgeBaseDescriptions = new KnowledgeBaseDescription[kbFiles.length];
        for (int i = 0; i < kbFiles.length; i++) {
            knowledgeBaseDescriptions[i] = new KnowledgeBaseDescription(kbFiles[i].getName().replaceAll(".odb", ""), KnowledgeBaseType.ODB);
        }
        return knowledgeBaseDescriptions;
    }

    public static KnowledgeBase getInstance(String baseName) throws Exception {
    	String fileName = baseName+".odb";
    	return getInstance(fileName,baseName);
    }


    public static KnowledgeBase getInstance(String fileName, final String baseName) throws Exception {
        if (new File(fileName).exists()) {
            ODB odb = null;
            
            odb = ODBFactory.open(fileName);
            
            try {
				List l1 = odb.getObjects(ODBKnowledgeBase.class,true);
				// Query the database object
				IQuery query = new SimpleNativeQuery(){
					public boolean match(ODBKnowledgeBase base){
						return base.getName().equals(baseName);
					}
				};

				List l = odb.getObjects(query);
				// If the result is not empty return the base
				if(!l.isEmpty()){
				    ODBKnowledgeBase okb = (ODBKnowledgeBase) l.get(0);
				    okb.init(odb);
				    okb.optimizeStorage();
				    return okb;
				}else{
					odb.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(odb!=null){
					odb.close();
				}
				throw e;
			}finally{
			}
        }

        KnowledgeBase knowledgeBase = new ODBKnowledgeBase(fileName,baseName);
        return knowledgeBase;
    }

    public void close() throws IOException {
        odb.close();
        KnowledgeBaseFactory.remove(getIdentifier());
        clear();
    }

    public List getInitialConceptList() throws Exception{
        return odb.getObjects(new ConceptQuery(knowledgeBaseDescription.getIdentifier()),true);
    }
    public List getInitialInstanceList() throws Exception{
        return odb.getObjects(new InstanceQuery(knowledgeBaseDescription.getIdentifier()),true);
    }
    public List getInitialConnectorList() throws Exception{
    	return odb.getObjects(new ConnectorQuery(knowledgeBaseDescription.getIdentifier()),true);
    }
    public List getInitialRelationList() throws Exception{
        return odb.getObjects(new RelationQuery(knowledgeBaseDescription.getIdentifier()),true);
    }
    public List getInitialPropositionList() throws Exception{
        return odb.getObjects(new PropositionQuery(knowledgeBaseDescription.getIdentifier()),true);
    }

}

class MyFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.endsWith(".odb");
    }

}
class ConceptQuery extends SimpleNativeQuery{
	private String baseName;
	public ConceptQuery(String baseName){
		this.baseName = baseName;
	}

	public boolean match(Concept concept) {
		return baseName.equals(concept.getKnowledgeBaseIdentifier());
	}
}
class InstanceQuery extends SimpleNativeQuery{
	private String baseName;
	public InstanceQuery(String baseName){
		this.baseName = baseName;
	}

	public boolean match(Instance instance) {
		return baseName.equals(instance.getKnowledgeBaseIdentifier());
	}
}
class ConnectorQuery extends SimpleNativeQuery{
	private String baseName;
	public ConnectorQuery(String baseName){
		this.baseName = baseName;
	}

	public boolean match(Connector connector) {
		return baseName.equals(connector.getKnowledgeBaseIdentifier());
	}
}
class PropositionQuery extends SimpleNativeQuery{
	private String baseName;
	public PropositionQuery(String baseName){
		this.baseName = baseName;
	}

	public boolean match(Proposition proposition) {
		return baseName.equals(proposition.getKnowledgeBaseIdentifier());
	}
}
class RelationQuery extends SimpleNativeQuery{
	private String baseName;
	public RelationQuery(String baseName){
		this.baseName = baseName;
	}

	public boolean match(Relation relation) {
		return baseName.equals(relation.getKnowledgeBaseIdentifier());
	}
}


