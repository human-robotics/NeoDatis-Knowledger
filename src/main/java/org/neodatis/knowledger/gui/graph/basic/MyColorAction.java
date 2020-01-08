package org.neodatis.knowledger.gui.graph.basic;

import prefuse.action.assignment.ColorAction;

public class MyColorAction extends ColorAction {
	private String name;
	public MyColorAction(String name,String group, String field) {
		super(group, field);
		this.name = name;
	}

	public MyColorAction(String name,String group, String field, int color) {
		super(group, field, color);
		this.name = name;
	}
	public String toString() {
		return name;
	}
}
