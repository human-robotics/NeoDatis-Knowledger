package org.neodatis.knowledger.gui.graph.basic.example;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import org.neodatis.knowledger.gui.graph.basic.GraphConstants;

import prefuse.Constants;
import prefuse.render.EdgeRenderer;
import prefuse.util.GraphicsLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;


public class LabeledEdgeRenderer extends EdgeRenderer {

	private AffineTransform at;
	private Map starts = new HashMap();
	private Map ends = new HashMap();
	
	public void render(Graphics2D graphics2D, VisualItem item) {
	
		// TODO Auto-generated method stub
		super.render(graphics2D, item);
		
		drawLabel(graphics2D,item);
		
		
		
	}
	
	protected void drawLabel(Graphics2D g, VisualItem item){
        Point2D start = (Point2D) starts.get(item);
        Point2D end = (Point2D) ends.get(item);
        AffineTransform oldTransform = g.getTransform();  
        g.setTransform(m_arrowTrans);  
        float x = (float)(start.getX()-end.getX())/2+10;  
        float y = (float) -((end.getY()-start.getY())/2)+10;  
        g.rotate(HALF_PI - Math.atan2(end.getY()-start.getY(), end.getX()-start.getX()));  
        g.drawString(item.getString(GraphConstants.LABEL), x, y);  
        g.setTransform(oldTransform);  
        /*
		Point2D start = (Point2D) starts.get(item);
		Point2D end = (Point2D) ends.get(item);
		if(start!=null && end!=null){
			System.out.println("Drawing label from " + start + " to "+ end+ " | dx=" + at.getTranslateX() + ",dy=" + at.getTranslateY()+",zx="+at.getScaleX()+","+at.getScaleY() +"\n");
			//AffineTransform oldTransform = graphics2D.getTransform();  
			at.transform(start,start);  
			at.transform(end,end);
			float x = (float)(start.getX()-end.getX())/2; 
			float y = (float) ((end.getY()-start.getY())/2); 
			//graphics2D.rotate(HALF_PI - Math.atan2(end.getY()-start.getY(), end.getX()-start.getX())); 
			graphics2D.drawString(item.getString(GraphConstants.LABEL), x, y);  
			//graphics2D.setTransform(oldTransform);  
		}*/
		/*
		AffineTransform currentTransform = graphics2D.getTransform(); 
		graphics2D.setTransform(at); 
		//graphics2D.rotate(- (Math.PI/2)); 
		graphics2D.drawString(item.getString("label"), 15.0f, 15.0f); 
		graphics2D.setTransform(currentTransform); 
		//System.out.println(".");
         * 
		 */
	}
	protected Shape getRawShape(VisualItem item) {
        EdgeItem   edge = (EdgeItem)item;
        VisualItem item1 = edge.getSourceItem();
        VisualItem item2 = edge.getTargetItem();
        
        int type = m_edgeType;
        
        getAlignedPoint(m_tmpPoints[0], item1.getBounds(),
                        m_xAlign1, m_yAlign1);
        getAlignedPoint(m_tmpPoints[1], item2.getBounds(),
                        m_xAlign2, m_yAlign2);
        m_curWidth = (float)(m_width * getLineWidth(item));
        
        // create the arrow head, if needed
        EdgeItem e = (EdgeItem)item;
        if ( e.isDirected() && m_edgeArrow != Constants.EDGE_ARROW_NONE ) {
            // get starting and ending edge endpoints
            boolean forward = (m_edgeArrow == Constants.EDGE_ARROW_FORWARD);
            Point2D start = null, end = null;
            start = m_tmpPoints[forward?0:1];
            end   = m_tmpPoints[forward?1:0];
            
            // compute the intersection with the target bounding box
            VisualItem dest = forward ? e.getTargetItem() : e.getSourceItem();
            int i = GraphicsLib.intersectLineRectangle(start, end,
                    dest.getBounds(), m_isctPoints);
            if ( i > 0 ) end = m_isctPoints[0];
            
            // create the arrow head shape
            at = getArrowTrans(start, end, m_curWidth);
            m_curArrow = at.createTransformedShape(m_arrowHead);
            
            // update the endpoints for the edge shape
            // need to bias this by arrow head size
            Point2D lineEnd = m_tmpPoints[forward?1:0]; 
            lineEnd.setLocation(0, -m_arrowHeight);
            at.transform(lineEnd, lineEnd);
        } else {
            m_curArrow = null;
        }
        
        // create the edge shape
        Shape shape = null;
        double n1x = m_tmpPoints[0].getX();
        double n1y = m_tmpPoints[0].getY();
        double n2x = m_tmpPoints[1].getX();
        double n2y = m_tmpPoints[1].getY();
        
        //System.out.println("Drawing Shape from " + m_tmpPoints[0] + " to " + m_tmpPoints[1]);
        
        starts.put(item,m_tmpPoints[0]);
        ends.put(item,m_tmpPoints[1]);
        
        switch ( type ) {
            case Constants.EDGE_TYPE_LINE:          
                m_line.setLine(n1x, n1y, n2x, n2y);
                shape = m_line;
                break;
            case Constants.EDGE_TYPE_CURVE:
                getCurveControlPoints(edge, m_ctrlPoints,n1x,n1y,n2x,n2y);
                m_cubic.setCurve(n1x, n1y,
                                m_ctrlPoints[0].getX(), m_ctrlPoints[0].getY(),
                                m_ctrlPoints[1].getX(), m_ctrlPoints[1].getY(),
                                n2x, n2y);
                shape = m_cubic;
                break;
            default:
                throw new IllegalStateException("Unknown edge type");
        }
        
        // return the edge shape
        return shape;
    }

}
