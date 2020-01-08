package org.neodatis.knowledger.gui;

import org.neodatis.tool.ILogger;

public class GuiLogger implements ILogger {

	public void debug(Object object) {
		System.out.println(object.toString());
	}

	public void info(Object object) {
		System.out.println(object.toString());
	}

	public void error(Object object) {
		System.out.println(object.toString());
	}

}
