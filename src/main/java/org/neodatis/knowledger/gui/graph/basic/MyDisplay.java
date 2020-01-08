package org.neodatis.knowledger.gui.graph.basic;

import java.awt.Rectangle;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.data.expression.Predicate;
import prefuse.visual.VisualItem;

public class MyDisplay extends Display {
	
	private VisualItem editingItem;
	private String editingAttribute;
	
	public MyDisplay() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyDisplay(Visualization arg0, Predicate arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	public MyDisplay(Visualization arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	public MyDisplay(Visualization arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	public void editText(VisualItem item, String attribute, Rectangle r) {
		super.editText(item,attribute,r);
		editingItem = item;
		editingAttribute = attribute;
	}
	/**
     * Stops text editing on the display, hiding the text editing widget. If
     * the text editor was associated with a specific VisualItem (ie one of the
     * editText() methods which include a VisualItem as an argument was called),
     * the item is updated with the edited text.
     */
    public void stopEditing() {
        String newText = getTextEditor().getText();
        if(editingItem!=null){
            editingItem.set(editingAttribute,newText);
            editingItem = null;
            editingAttribute = null;
        	try{
        		// the super method has a bug
        		super.stopEditing();
        	}catch (Exception e) {
    		}
        }
    }

}
