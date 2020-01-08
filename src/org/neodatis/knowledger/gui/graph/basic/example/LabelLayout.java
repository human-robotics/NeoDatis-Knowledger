/* 
 * $RCSfile: LabelLayout.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.basic.example;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import prefuse.action.layout.Layout;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualItem;
import prefuse.visual.tuple.TableEdgeItem;

public class LabelLayout extends Layout {
    public LabelLayout(String group) {
        super(group);
    }

    public void run(double frac) {
        Iterator iter = m_vis.items(m_group);
        int i=0;
        while (iter.hasNext()) {
            DecoratorItem item = (DecoratorItem) iter.next();
            VisualItem node = item.getDecoratedItem();
            TableEdgeItem tei = (TableEdgeItem) node;
            try{
                Rectangle2D bounds = tei.getBounds();
                setX(item, null, bounds.getCenterX());
                setY(item, null, bounds.getCenterY()+5);
                i++;
            }catch (RuntimeException e) {
                // TODO: handle exception
            }
        }
        //System.out.println(m_group + " size = " + i);
    }
}
