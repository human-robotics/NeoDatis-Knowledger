/* 
 * $RCSfile: MyLabeLRenderer.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.basic.example;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import org.neodatis.knowledger.gui.graph.basic.GraphConstants;

import prefuse.render.LabelRenderer;
import prefuse.visual.VisualItem;
import prefuse.visual.tuple.TableDecoratorItem;
import prefuse.visual.tuple.TableEdgeItem;
import prefuse.visual.tuple.TableNodeItem;


public class MyLabeLRenderer extends LabelRenderer {
	private Map labelsToHide = new HashMap(); 
	
	public MyLabeLRenderer() {
		super();
	}

	public MyLabeLRenderer(String textField, String imageField) {
		super(textField, imageField);
	}

	public MyLabeLRenderer(String textField) {
		super(textField);
		labelsToHide.put("is-subclass-of",Boolean.TRUE);
		labelsToHide.put("is-instance-of",Boolean.TRUE);
	}

	public void render(Graphics2D g, VisualItem item) {

		// System.out.println(item.getClass().getName());
		TableDecoratorItem tdi = (TableDecoratorItem) item;
		VisualItem vi = tdi.getDecoratedItem();

		String label = vi.getString(GraphConstants.LABEL);
        if(label==null){
            return ;
        }

		if (!mustDisplayLabel(label)) {
			return;
		}

		TableEdgeItem tei = (TableEdgeItem) vi;
		TableNodeItem ni1 = (TableNodeItem) tei.getSourceItem();
		TableNodeItem ni2 = (TableNodeItem) tei.getTargetItem();
		double x1 = ni1.getX();
		double y1 = ni1.getY();

		double x2 = ni2.getX();
		double y2 = ni2.getY();

		boolean arrowOnTheRight = x2 > x1;

		/*
		 * if(arrowOnTheRight){ label = label + " >"; }else{ label = "<"
		 * +label; }
		 */

		double wLabel = item.getBounds().getWidth();
		double hLabel = item.getBounds().getHeight();

		double labelSize = Math.sqrt(wLabel * wLabel + hLabel * hLabel);

		// if(label.equals("is a")){

		item.setBounds(vi.getBounds().getX(), vi.getBounds().getY(), vi.getBounds().getWidth(), vi.getBounds().getHeight());

		double arrowWidth = x2 - x1;
		double arrowHeight = y2 - y1;

		// The arrow size (diagonal)
		double arrowSize = Math.sqrt(arrowWidth * arrowWidth + arrowHeight * arrowHeight);

		double divider = arrowHeight / arrowSize * (arrowWidth < 0 ? -1 : 1);
		double angle = Math.asin(divider);// /Math.PI*180;

		// The compute the associated delta x and delta y using the angle
		double deltaX = Math.cos(angle) * labelSize / 2;
		double deltaY = Math.sin(angle) * labelSize / 2;

		AffineTransform oldTransform = g.getTransform();
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(getEdgeLabelColor());
		g.setFont(getEdgeLabelFont());
		// AffineTransform at = new AffineTransform();
		// at.rotate(10);

		// g.drawRect((int)item.getBounds().getX(),(int)item.getBounds().getY(),(int)wLabel,(int)hLabel);
		// g.drawString(""+angle2,(int)x1,(int)y1);
		// g.drawString(label,(int) (x1+x2)/2,(int) (y1+y2)/2);
		g.translate((int) (x1 + x2) / 2 - deltaX, (int) (y1 + y2) / 2 - deltaY);// -
																				// hLabel/2);
		// g.translate((int) (x1+x2)/2+deltaX,(int) (y1+y2)/2+deltaY);
		// g.translate((int) deltaX,(int) deltaY);
		// g.drawRect(0,0,10,10);
		// g.drawLine(0,0,(int)arrowWidth,(int)arrowHeight);
		// g.drawRect(0,0,15,15);
		g.rotate(angle);
		g.drawString(label, 0, 0);
		// g.drawRect(0,0,(int)wLabel,(int)hLabel);

		// super.render(g,item);
		g.setTransform(oldTransform);
		g.setColor(oldColor);
		g.setFont(oldFont);
		// System.out.println("label size = " + labelSize + " - arrow size = " +
		// arrowSize + " - deltaSize = " + deltaSize + " - x="+deltaX +
		// ","+deltaY);
		// System.out.println(vi.getString(GraphConstants.LABEL) + "
		// w="+width+",h=" +height+ " = " + angle + " - divider = " + divider);

		/*
		 * } else{ super.render(g,item); }
		 */

	}

	public Color getEdgeLabelColor() {
		return Color.BLACK;
	}

	public Font getEdgeLabelFont() {
		// Font [] fonts =
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		// int i= fonts.length;
		Font f = new Font("Arial", Font.PLAIN, getEdgeLabelFontSize());
		return f;
	}

	public int getEdgeLabelFontSize() {
		return 10;
	}

	public boolean mustDisplayLabel(String label) {
		
		Object o = labelsToHide.get(label);
		boolean display = o == null;
		//System.out.println(label + " / " + labelsToHide + " = " + display);
		return display;
	}
}
