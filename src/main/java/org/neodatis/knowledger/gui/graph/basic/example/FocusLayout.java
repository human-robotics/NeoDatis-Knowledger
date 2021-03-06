/* 
 * $RCSfile: FocusLayout.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.basic.example;

import java.util.Iterator;

import org.neodatis.knowledger.gui.graph.basic.GraphConstants;

import prefuse.action.layout.Layout;
import prefuse.data.Node;
import prefuse.visual.VisualItem;


public class FocusLayout extends Layout {
    public FocusLayout(String group) {
        super(group);
    }

    public void run(double frac) {
        Iterator iter = m_vis.items(m_group);
        int i=0;
        while (iter.hasNext()) {
            Node node = (Node) iter.next();
            VisualItem vi = (VisualItem) node;
            System.out.println(vi.getString(GraphConstants.LABEL));
            try{
                /*
                Rectangle2D bounds = tei.getBounds();
                setX(item, null, bounds.getCenterX());
                setY(item, null, bounds.getCenterY()+5);
                */
                i++;
            }catch (RuntimeException e) {
                // TODO: handle exception
            }
        }
        //System.out.println(m_group + " size = " + i);
    }
}
