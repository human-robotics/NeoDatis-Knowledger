package org.neodatis.knowledger.gui.graph;

import java.util.HashMap;
import java.util.Map;

public class GlobalConfiguration {
	private static String language = "en";
	private static String webContext = null;
    private static String host = null;
    private static String port = null;
    private static long animationStepDuration = 50;
    private static Map map = new HashMap();

	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String language) {
		GlobalConfiguration.language = language;
	}

	public static String getWebContext() {
		return webContext;
	}

	public static void setWebContext(String webContext) {
		GlobalConfiguration.webContext = webContext;
	}

    /**
     * @return the animationStepTime
     */
    public static long getAnimationStepDuration() {
        return animationStepDuration;
    }

    /**
     * @param animationStepTime the animationStepTime to set
     */
    public static void setAnimationStepDuration(long animationStepTime) {
        GlobalConfiguration.animationStepDuration = animationStepTime;
    }

    /**
     * @return the host
     */
    public static String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public static void setHost(String host) {
        GlobalConfiguration.host = host;
    }

    /**
     * @return the port
     */
    public static String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public static void setPort(String port) {
        GlobalConfiguration.port = port;
    }
	
    public static String getParameterValue(String parameterName){
    	return (String) map.get(parameterName);
    }
    public static void setParameterValue(String parameterName,String parameterValue){
    	map.put(parameterName,parameterValue);
    }
	
	
}
