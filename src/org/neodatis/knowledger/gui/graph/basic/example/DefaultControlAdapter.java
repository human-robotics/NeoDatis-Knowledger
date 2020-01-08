package org.neodatis.knowledger.gui.graph.basic.example;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import prefuse.controls.ControlAdapter;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.visual.VisualItem;

/**
 * Basic key and mouse listener for Knowledger graph editor
 * 
 * <pre>
 * Warning: This listener is used for two different prefuse components :
 * 
 * 1- The prefuse.Display, which is the main visualization component
 * 2- The prefuse.Display text editor, which is a separate JTextComponenet exclusively used for node text editing.
 * 
 *  when the isEditing is true, all events calling keyPressed,keyTyped and keyReleased are from the JTextcomponent Text editor
 *  Else, events are from the prefuse display.
 * 
 * </pre>
 * @author olivier s
 *
 */
public class DefaultControlAdapter extends ControlAdapter {
	private KnowledgerGraphBuilder knowledgerGraph;
	private boolean isEditing;
	private boolean debug;

	public DefaultControlAdapter(KnowledgerGraphBuilder knowledgerGraphBuilder) {
		super();
		this.knowledgerGraph = knowledgerGraphBuilder;
	}

	
	public void keyPressed(KeyEvent e) {

		if(debug){
			System.out.println(e.getKeyCode());
		}
		
		// Manage events from the prefuse.Display key listener
		if(isEditing){
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				isEditing = false;
				knowledgerGraph.getDisplay().stopEditing();			
				return;
			}
			return;
		}
		if (e.getKeyChar() == '1') {
			System.out.println("Switching to mode 1");
			knowledgerGraph.mode = 1;
			knowledgerGraph.redraw();
			return;
		}
		if (e.getKeyChar() == '0') {
			System.out.println("Switching to mode 0");
			knowledgerGraph.mode = 0;
			knowledgerGraph.redraw();
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			knowledgerGraph.redraw();
			return;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			isEditing = false;
			knowledgerGraph.getDisplay().stopEditing();			
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F9) {
			debug = !debug;
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F10) {
			knowledgerGraph.curvedEdge(true);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F11) {
			knowledgerGraph.curvedEdge(false);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			((SimpleKnowledgerGraphBuilder)knowledgerGraph).initActions(true);
			knowledgerGraph.runActions();
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_X) {
			((SimpleKnowledgerGraphBuilder)knowledgerGraph).initActions(false);
			knowledgerGraph.runActions();
			return;
		}
	}

	public void itemKeyPressed(VisualItem item, KeyEvent e) {
		
		if(debug){
			System.out.println(e.getKeyCode());
		}

		if (e.getKeyCode() == KeyEvent.VK_INSERT) {
			Object o = item.getSourceTuple();
			Node node = (Node) o;
			Node c = knowledgerGraph.getGraphProvider().buildNode(0, "new node", KnowledgerGraphBuilder.UNKNOWN);
			Edge edge = knowledgerGraph.getGraphProvider().buildEdge(node, c, "unknown");
			knowledgerGraph.runActions();
			return;

		}
		
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			Object o = item.getSourceTuple();
			Node node = (Node) o;
			knowledgerGraph.getGraphProvider().removeNode(node);
			knowledgerGraph.runActions();
			return;
		}
		if (e.getKeyChar() == 'h') {
			item.setVisible(false);
			knowledgerGraph.runActions();
			return;
		}

		if (e.getKeyChar() == 'c') {
			Object o = item.getSourceTuple();
			Node node = (Node) o;
			// knowledgerGraph.getVisualization().get
			Point2D p = new Point2D.Double(item.getX(),item.getY());
			knowledgerGraph.getDisplay().animatePanToAbs(p, 1000);
			knowledgerGraph.runActions();
			return;
		}

		if (e.getKeyCode() == KeyEvent.VK_F2) {
			if(isEditing){
				return;
			}
			knowledgerGraph.getDisplay().editText(item,"label");
			isEditing = true;
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			isEditing = false;
			
			knowledgerGraph.getDisplay().stopEditing();
			knowledgerGraph.getDisplay().requestFocus();
			return;
		}
		
	}
	
    
	/* (non-Javadoc)
     * @see prefuse.controls.ControlAdapter#itemClicked(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemClicked(VisualItem vi, MouseEvent arg1) {
        // TODO Auto-generated method stub
        super.itemClicked(vi, arg1);
        vi.setFixed(true);
        vi.setHighlighted(true);
        vi.setHover(true);
        System.out.println("cliquei");
    }
    

    public KnowledgerGraphBuilder getKnowledgerGraph() {
		return knowledgerGraph;
	}


	public void setKnowledgerGraph(KnowledgerGraphBuilder knowledgerGraph) {
		this.knowledgerGraph = knowledgerGraph;
	}


	public String getHelpString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("Ins=create new node - del=Delete Node - c=Center - F2=rename - ");
		buffer.append("<br>");
		buffer.append("h=hide node - F5 = refresh - 0 = no connector - 1 = with connector ");
		buffer.append("<br>");
		buffer.append("F10 = curved edge - F11 = linear edge");
		buffer.append("</html>");
		return buffer.toString();
	}
	
}
