/* 
 * $RCSfile: SimpleFileKnowledgeBase.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.implementation.knowledgebase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.neodatis.knowledger.core.interfaces.knowledgebase.ExternalRepository;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


/**
 * <p>
 * 
 * </p>

 * 
 */
public class SimpleFileKnowledgeBase extends KnowledgeBaseImpl implements ExternalRepository {


	public SimpleFileKnowledgeBase() throws Exception {
		super.init();
		externalRepository = this;
	}

	public SimpleFileKnowledgeBase(String name) throws Exception {
		super(name, KnowledgeBaseType.FILE);
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
		save();
	}

	private void save() throws IOException {
		FileOutputStream out = new FileOutputStream(getName()+".neodatis.knowledger");
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(this);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#update(java.lang.Object)
	 */
	public void update(Object object) throws Exception {
		save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#delete(java.lang.Object)
	 */
	public void delete(Object object) throws Exception {
		save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.knowledger.core.implementation.knowledgebase.IExternalRepository#delete(java.util.List)
	 */
	public void delete(List objects) throws Exception {
		save();
	}

	public void setKBType(String type){
		// in this case it always database
		setType(KnowledgeBaseType.FILE);
	}
	
    public void close() throws IOException{
        
    }

    public List getInitialConceptList() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public List getInitialInstanceList() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public List getInitialConnectorList() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public List getInitialRelationList() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public List getInitialPropositionList() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
	
}
