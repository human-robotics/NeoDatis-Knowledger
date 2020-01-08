/* 
 * $RCSfile: ToolsForGui.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.tool;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class ToolsForGui {
	
	public static Icon createImageIcon(String path){
		Icon icon = new ImageIcon(path);
		if(icon==null){
			throw new RuntimeException("File " + path + " not found from  " + System.getProperty("user.dir"));
		}
		return icon;
	}
	
	public static Icon createWebImageIcon(String path){
		
		URL url = path.getClass().getResource(path);
		
		if(url==null){
			throw new RuntimeException("File " + path + " not found from  user path");// + System.getProperty("user.dir"));
		}
		
		Icon icon = new ImageIcon(url);
		if(icon==null){
			throw new RuntimeException("File " + path + " not found from  user path");// + System.getProperty("user.dir"));
		}
		return icon;
	}
	
}
