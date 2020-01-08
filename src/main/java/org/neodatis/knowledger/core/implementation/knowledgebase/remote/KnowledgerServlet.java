package org.neodatis.knowledger.core.implementation.knowledgebase.remote;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.Actions;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization.DataSerializer;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;
import org.neodatis.tool.wrappers.OdbString;

import antlr.StringUtils;


public class KnowledgerServlet extends HttpServlet {
	transient protected static Logger logger = Logger.getLogger(KnowledgerServlet.class);
	
	

	public void init() throws ServletException {
		super.init();
		// Tells NeoDatis ODB not to use modified class => use user class instead of subclass (created at runtime be ASM)
		//Configuration.setUseModifiedClass(false);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			manageRequest(request,response);
		} catch (Exception e) {
			String message = OdbString.exceptionToString(e,true); 
			logger.error(message);
			response.getWriter().print("nok\n"+message);	
			throw new IOException(e.getMessage());
		}
	}

	private void manageRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		log("test");
		logger.info("action=" + action);
		logger.info("user directory = " + System.getProperty("user.dir"));
		
		if(action.equals(Actions.OPEN_BASE)){
			manageOpenAction(request,response);
			return;
		}
		if(action.equals(Actions.CREATE_OBJECT)){
			manageCreateObjectAction(request,response);
			return;
		}
		if(action.equals(Actions.UPDATE_OBJECT)){
			manageUpdateObjectAction(request,response);
			return;
		}
		
		if(action.equals(Actions.DELETE_OBJECT)){
			manageDeleteObjectAction(request,response);
			return;
		}

		response.setContentType("text/plain");
		response.getWriter().print("nok\naction " + action + " not implemented yet");
		
	}

	private void manageOpenAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String odbBaseName = request.getParameter(ActionParameters.OPEN_BASE__BASE_NAME);
		String language = request.getParameter(ActionParameters.LANGUAGE);
		response.setContentType("text/plain");
		KnowledgeBase base = null;
		try{
			GlobalConfiguration.setLanguage(language);
			base = KnowledgeBaseFactory.getInstance(odbBaseName,KnowledgeBaseType.ODB);
			logger.info(base.toString());
			response.getWriter().print("ok\n"+DataSerializer.getInstance(true).toString(base));
		}catch(Exception e){
			String message = OdbString.exceptionToString(e,true); 
			logger.error(message);
			response.getWriter().print("nok\n"+message);	
		}finally{
			if(base!=null){
				base.close();
			}
		}
	}
	
	private void manageCreateObjectAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String odbBaseName = request.getParameter(ActionParameters.OPEN_BASE__BASE_NAME);
		String data = request.getParameter(ActionParameters.CREATE_OBJECT__DATA);
		response.setContentType("text/plain");
		logger.info("create object : " + data);
		KnowledgeBase base = null;
		try{
			base = KnowledgeBaseFactory.getInstance(odbBaseName,KnowledgeBaseType.ODB);
			KnowledgerObject object = (KnowledgerObject) DataSerializer.getInstance(true).fromOneString(data);
			object = base.create(object);
			response.getWriter().print("ok\n"+object.getId());
			logger.info("insert ok : " + data  + " - id = " + object.getId());
		}catch(Exception e){
			String message = OdbString.exceptionToString(e,true); 
			logger.error("insert of " + data + " failed because :");
			logger.error(message);
			response.getWriter().print("nok\n"+message);
		}finally{
			if(base!=null){
				base.close();
			}
		}
	}
	
	private void manageUpdateObjectAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String odbBaseName = request.getParameter(ActionParameters.OPEN_BASE__BASE_NAME);
		String data = request.getParameter(ActionParameters.UPDATE_OBJECT__DATA);
		response.setContentType("text/plain");
		logger.info("update object : " + data);
		KnowledgeBase base = null;
		try{
			base = KnowledgeBaseFactory.getInstance(odbBaseName,KnowledgeBaseType.ODB);
			KnowledgerObject object = (KnowledgerObject) DataSerializer.getInstance(true).fromOneString(data);
			KnowledgerObject originalObject = base.getObjectFromId(object.getId());
			if(originalObject instanceof Relation){
				Relation relation1 = (Relation) object;
				Relation relation2 = (Relation) originalObject;
				relation2.setConnector(relation1.getConnector());
				object = base.updateRelation(relation2);
			}else{
				originalObject.setIdentifier(object.getIdentifier());
				object = base.update(originalObject);
			}
			
			
			response.getWriter().print("ok\n"+object.getId());
			logger.info("update ok : " + data);
		}catch(Exception e){
			String message = OdbString.exceptionToString(e,true); 
			logger.error("update of " + data + " failed because :");
			logger.error(message);
			response.getWriter().print("nok\n"+message);	
		}finally{
			if(base!=null){
				base.close();
			}
		}
		
	}

	private void manageDeleteObjectAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String odbBaseName = request.getParameter(ActionParameters.OPEN_BASE__BASE_NAME);
		String data = request.getParameter(ActionParameters.DELETE_OBJECT__DATA);
		response.setContentType("text/plain");
		logger.info("delete object : " + data);
		KnowledgeBase base = null;
		try{
			base = KnowledgeBaseFactory.getInstance(odbBaseName,KnowledgeBaseType.ODB);
			KnowledgerObject object = (KnowledgerObject) DataSerializer.getInstance(true).fromOneString(data);
			KnowledgerObject originalObject = base.getObjectFromId(object.getId());
			originalObject.setIdentifier(object.getIdentifier());
			object = base.delete(originalObject);
			response.getWriter().print("ok\n"+object.getId());
			logger.info("delete ok : " + data);
		}catch(Exception e){
			String message = OdbString.exceptionToString(e,true); 
			logger.error("delete of " + data + " failed because :");
			logger.error(message);
			response.getWriter().print("nok\n"+message);	
		}finally{
			if(base!=null){
				base.close();
			}
		}
	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(arg0, arg1);
	}
	
	

}
