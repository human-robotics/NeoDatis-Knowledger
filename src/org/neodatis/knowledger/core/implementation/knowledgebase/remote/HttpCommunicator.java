package org.neodatis.knowledger.core.implementation.knowledgebase.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neodatis.knowledger.core.implementation.knowledgebase.Actions;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization.DataSerializer;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;


public class HttpCommunicator {

	private static final String SERVLET = "request";

	private DataSerializer dataSerializer = DataSerializer.getInstance(false);

	// private AppletContext context;
	private String host;
	private String port;
	private String language;
	private String knowledgeBaseIdentifier;

	public HttpCommunicator(String host, String port,String knwoledgeBaseName,String language) {
		this.host = host;
		this.port = port;
		this.knowledgeBaseIdentifier = knwoledgeBaseName;
		this.language = language;
	}

	/*
	 */

	public KnowledgeBase openKnowledgeBase() throws Exception {
		Map map = new HashMap();
		map.put("action", Actions.OPEN_BASE);
		map.put(ActionParameters.OPEN_BASE__BASE_NAME, knowledgeBaseIdentifier);
		map.put(ActionParameters.LANGUAGE, language);

		String response = urlPost(SERVLET, map);
		if (response.startsWith("ok")) {
			int index = response.indexOf("\n");
			KnowledgeBase kb = dataSerializer.knowledgeBaseFromString(response.substring(index+1), knowledgeBaseIdentifier, host, port,GlobalConfiguration.getLanguage());
			return kb; 
		}
		throw new RuntimeException(response);

	}

	public long createObject(Object object) {
		String data = dataSerializer.toString(object);
		Map map = new HashMap();
		map.put("action", Actions.CREATE_OBJECT);
		map.put(ActionParameters.CREATE_OBJECT__DATA, data);
		map.put(ActionParameters.OPEN_BASE__BASE_NAME, knowledgeBaseIdentifier);

		try{
			String response = urlPost(SERVLET, map);
			if (response.startsWith("ok")) {
				int index = response.indexOf("\n");
				return Long.valueOf(response.substring(index+1).replaceAll("\n","")).longValue();
			}
			throw new RuntimeException(response);
		}catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long updateObject(Object object) {
		String data = dataSerializer.toString(object);
		Map map = new HashMap();
		map.put("action", Actions.UPDATE_OBJECT);
		map.put(ActionParameters.UPDATE_OBJECT__DATA, data);
		map.put(ActionParameters.OPEN_BASE__BASE_NAME, knowledgeBaseIdentifier);
		try {
			String response = urlPost(SERVLET, map);
			if (response.startsWith("ok")) {
				int index = response.indexOf("\n");
				return Long.valueOf(response.substring(index+1).replaceAll("\n","")).longValue();
			}
			throw new RuntimeException(response);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long deleteObject(Object object) {
		String data = dataSerializer.toString(object);
		Map map = new HashMap();
		map.put("action", Actions.DELETE_OBJECT);
		map.put(ActionParameters.DELETE_OBJECT__DATA, data);
		map.put(ActionParameters.OPEN_BASE__BASE_NAME, knowledgeBaseIdentifier);
		try {
			String response = urlPost(SERVLET, map);
			if (response.startsWith("ok")) {
				int index = response.indexOf("\n");
				return Long.valueOf(response.substring(index+1).replaceAll("\n","")).longValue();
			}
			throw new RuntimeException(response);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public String getServerUrl() {
		StringBuffer url = new StringBuffer();
		url.append("http://");
		url.append(host);
		url.append(":");
		url.append(port);
		url.append("/");
		return url.toString();
	}

	public String urlPost(String file, Map parameters) throws UnsupportedEncodingException {
		String urlString = getQueryString(GlobalConfiguration.getWebContext()+"/"+file, parameters);
		StringBuffer resp = new StringBuffer();
		try {
			System.out.println("posting >" + urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
				resp.append("\n");
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("< Receiving " + urlString);
		return resp.toString();
	}

	public String getQueryString(String file, Map parameters) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append(getServerUrl());
		sb.append(file);
		if (parameters.size() > 0) {
			sb.append("?");
		}
		StringBuffer params = new StringBuffer();
		for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String value = (String) parameters.get(key); 
			params.append(key);
			params.append("=");
			params.append( URLEncoder.encode(value,"UTF-8"));
			params.append("&");
		}
		if (parameters.size() > 0) {
			params.deleteCharAt(params.length() - 1);
		}
		
		//sb.append(URLEncoder.encode(params.toString(), "UTF-8"));
		sb.append(params);
		return sb.toString();
		
		
	}

	/*
	 * public void openWindow(String file, Map parameters) { String urlString =
	 * getQueryString(file, parameters); System.out.println(" calling " +
	 * urlString + " in a new window "); try { context.showDocument(new
	 * URL(urlString), "AUX"); } catch (MalformedURLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 *  }
	 */
}
