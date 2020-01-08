package org.neodatis.knowledger.gui.tool;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.neodatis.knowledger.gui.graph.GlobalConfiguration;


public class Messages {
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

	private static ResourceBundle resourceBundle = null;
	private static String lastLanguage = "en";

	private Messages() {
	}

	public static String getString(String key) {
		if(resourceBundle==null || !lastLanguage.equals(GlobalConfiguration.getLanguage())){
			lastLanguage = GlobalConfiguration.getLanguage();
			resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME+"_"+lastLanguage);
		}
		// TODO Auto-generated method stub
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!'+":"+lastLanguage;
		}
	}
}
