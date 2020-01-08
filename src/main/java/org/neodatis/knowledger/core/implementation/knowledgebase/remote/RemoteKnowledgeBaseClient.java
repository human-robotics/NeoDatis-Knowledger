/* 
 * $RCSfile: RemoteKnowledgeBaseClient.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.implementation.knowledgebase.remote;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.neodatis.knowledger.core.factory.KnowledgeBaseDescription;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.knowledgebase.InMemoryKnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.ExternalRepository;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;


/**
 * <p>
 * 
 * </p>
 * 
 * 
 */
public class RemoteKnowledgeBaseClient extends InMemoryKnowledgeBase implements ExternalRepository {
    
    private HttpCommunicator httpCommunicator;

    public RemoteKnowledgeBaseClient() throws Exception {
    	super();
    }

    public RemoteKnowledgeBaseClient(String name,String host, String port,String language) throws Exception {
    	httpCommunicator = new HttpCommunicator(host,port,name,language);
        init(name,KnowledgeBaseType.ODB);
        externalRepository = this;
    }
    
    protected void init(String name, KnowledgeBaseType knowledgeBaseType) throws Exception {
        super.init(name, knowledgeBaseType,null);
        externalRepository = this;
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#create(java.lang.Object)
     */
    public void create(Object object) throws Exception {
    	long id = httpCommunicator.createObject(object);
    	KnowledgerObject dobject = (KnowledgerObject) object;
    	dobject.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#update(java.lang.Object)
     */
    public void update(Object object) throws Exception {
    	long id = httpCommunicator.updateObject(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#delete(java.lang.Object)
     */
    public void delete(Object object) throws Exception {
    	long id = httpCommunicator.deleteObject(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#delete(java.util.List)
     */
    public void delete(List objects) throws Exception {
        for (int i = 0; i < objects.size(); i++) {
        	delete(objects.get(i));
        }
        //odb.commit();
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

    public static KnowledgeBase getInstance(String name,String host,String port) throws Exception {
    	HttpCommunicator communicator = new HttpCommunicator(host,port,name,GlobalConfiguration.getLanguage());
    	return communicator.openKnowledgeBase();
    }

    public void close() throws IOException {
    	
    }
}

class MyFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.endsWith(".odb");
    }

}
