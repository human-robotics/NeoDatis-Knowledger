/*
 * Created on Dec 22, 2004
 */
package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.text.SimpleDateFormat;

/**
 * @author olivier
 *
 */
public interface ISerializer {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
	public static String SEPARATOR = ";";
	public String toString(Object object);
	public Object fromString(String data) throws Exception;
}
