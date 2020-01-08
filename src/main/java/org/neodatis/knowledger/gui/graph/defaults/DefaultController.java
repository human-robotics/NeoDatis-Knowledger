package org.neodatis.knowledger.gui.graph.defaults;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.gui.graph.IManagerFactory;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.PrefuseGraphBuilder;
import org.neodatis.knowledger.gui.graph.panel.ConnectorChooserDialog;

import prefuse.Display;
import prefuse.controls.ControlAdapter;
import prefuse.data.Node;
import prefuse.visual.VisualItem;


public class DefaultController extends ControlAdapter {
    private Display display;

    private boolean isDragging;

    private int xStartDragging;

    private int yStartDragging;
    private int currentX;
    private int currentY;

    private VisualItem draggingSource;

    private boolean isTextEditing;

    private VisualItem visualItemBeingEdited;

    private IManagerFactory managerFactory;
    private PrefuseGraphBuilder pgb;

    public DefaultController(PrefuseGraphBuilder pgb, Display display, IManagerFactory managerFactory) {
    	this.pgb = pgb;
    	this.display = display;
        this.managerFactory = managerFactory;
    } //

    /*
     * (non-Javadoc)
     * 
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent event) {

        if (event.getClickCount() == 2) {
            try {
                managerFactory.getInteractionManager().getActionManager().newConcept(event.getPoint().x, event.getPoint().y, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void itemClicked(VisualItem vi, MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)) {
            String id = vi.getString(GraphConstants.ID);
            int type = vi.getInt(GraphConstants.TYPE);
            String label = vi.getString(GraphConstants.LABEL);
            int x = e.getPoint().x;
            int y = e.getPoint().y;
            managerFactory.getInteractionManager().manage(vi, id, type, label, x, y);
        } else {
            if (e.getClickCount() == 2){
                if(managerFactory.getInteractionManager().canEdit()) {
                    startEditing(vi);
                }
            } else {
                // To avoid conflict with prefuse double click editing function
                if(!managerFactory.getInteractionManager().canEdit()) {
                    int objectType = vi.getInt(GraphConstants.TYPE);
                    if(objectType==GraphConstants.CONCEPT  || objectType==GraphConstants.INSTANCE){
                        // When user clicks once on a node, the graph is centered on the
                        // clicked node
                        display.animatePanToAbs(new Point2D.Double(vi.getX(),vi.getY()), 2000);
                        if(!managerFactory.getInteractionManager().hasPopupMenu()){
                            String id = vi.getString(GraphConstants.ID);
                            int type = vi.getInt(GraphConstants.TYPE);
                            String label = vi.getString(GraphConstants.LABEL);
                            int x = e.getPoint().x;
                            int y = e.getPoint().y;
                            managerFactory.getInteractionManager().manage(vi, id, type, label, x, y);
                        }
                    }
                }
            }
        }

    }

    public void itemDragged(VisualItem gi, MouseEvent e) {
        String oid = gi.getString(GraphConstants.ID);
    	if (SwingUtilities.isRightMouseButton(e) && managerFactory.getInteractionManager().canDragAndDrop(oid)) {
            if (!isDragging) {
                xStartDragging = e.getPoint().x;
                yStartDragging = e.getPoint().y;
                isDragging = true;
                draggingSource = gi;
            } else {
           		display.repaintImmediate();
            	display.getGraphics().drawLine(xStartDragging, yStartDragging, e.getPoint().x, e.getPoint().y);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        super.mouseDragged(e);
    }

    public void itemReleased(VisualItem gi, MouseEvent e) {
        // super.itemReleased(gi,e);
        if (isDragging) {
            VisualItem vi = display.findItem(e.getPoint());
            if (vi != null) {

                Node node1 = (Node) gi.getSourceTuple();
                Node node2 = (Node) vi.getSourceTuple();
                // Prevent from connecting one node to itself
                if (node1 == node2) {
                    isDragging = false;
                    return;
                }
                try {
                    String oid = gi.getString(GraphConstants.ID);
                	Connector connector = askConnector(oid, e.getPoint(), node1.getString(GraphConstants.LABEL), node2.getString(GraphConstants.LABEL));
                	currentX = currentY = 0;
                    if (connector == null) {
                        isDragging = false;
                        return;
                    }
                    managerFactory.getInteractionManager().getActionManager().connect(node1, connector, node2);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            isDragging = false;
        }

        if (isTextEditing) {
            display.stopEditing();
            isTextEditing = false;
        }
    }

    private Connector askConnector(String objectOid, Point point, String object1Name, String object2Name) throws Exception {
    	ConnectorList connectorsToDisplay = managerFactory.getInteractionManager().getConnectorListOnDragOnDrop(objectOid);
    	ConnectorChooserDialog connectorChooserDialog = new ConnectorChooserDialog(connectorsToDisplay, managerFactory.getPersistentManager(), object1Name, object2Name);
        connectorChooserDialog.setLocationRelativeTo(null);

        connectorChooserDialog.setVisible(true);
        return connectorChooserDialog.getConnector();
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            isDragging = false;
        }
        if (isTextEditing) {
            endEditing();
        }
    } //

    /*
     * (non-Javadoc)
     * 
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#itemKeyReleased(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyReleased(VisualItem visualItem, KeyEvent keyEvent) {
        // TODO Auto-generated method stub
        super.itemKeyReleased(visualItem, keyEvent);

        if (keyEvent.getKeyCode() == '\n') {
            if (isTextEditing) {
                endEditing();
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#itemKeyTyped(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyTyped(VisualItem visualItem, KeyEvent keyEvent) {
        // TODO Auto-generated method stub
        super.itemKeyTyped(visualItem, keyEvent);
        // System.out.println(keyEvent.getKeyChar() + " - " +
        // keyEvent.getKeyCode());
        if (keyEvent.getKeyCode() == '\n') {
            if (isTextEditing) {
                endEditing();
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent event) {
        System.out.println(event.getKeyCode());

        if (event.getKeyChar() == 'c') {
            System.out.println("Center on");
            managerFactory.getInteractionManager().getKnowledgerGraphPanel().getPrefuseGraphBuilder().centerOn(1152532291262L);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent event) {

        Object src = event.getSource();
        if (src == display.getTextEditor() && event.getKeyCode() == KeyEvent.VK_ENTER) {
            endEditing();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent event) {
        // TODO Auto-generated method stub
        super.keyTyped(event);
    }

    protected void endEditing() {

        String newName = display.getTextEditor().getText();
        String id = visualItemBeingEdited.getString(GraphConstants.ID);
        int type = visualItemBeingEdited.getInt(GraphConstants.TYPE);
        try {
            switch (type) {
            case GraphConstants.CONCEPT:
                managerFactory.getPersistentManager().updateConceptName(newName, id);
                break;
            case GraphConstants.INSTANCE:
                managerFactory.getPersistentManager().updateInstanceIdentifier(newName, id);
                break;
            case GraphConstants.RELATION:
                managerFactory.getPersistentManager().updateConnectorOfRelation(id, newName);
                break;

            default:
                break;
            }
            display.stopEditing();
            isTextEditing = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startEditing(VisualItem vi) {
        visualItemBeingEdited = vi;
        vi.setFixed(true);
        String currentLabel = vi.getString(GraphConstants.LABEL);
        if (currentLabel == null || currentLabel.length() == 0 || currentLabel.equals("?")) {
            // To have more space to write
            vi.setString(GraphConstants.LABEL, "         ");
        }
        display.editText(vi, GraphConstants.LABEL);
        display.getTextEditor().selectAll();
        isTextEditing = true;
    }

    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display) e.getSource();
        String name = item.getString(GraphConstants.LABEL);
        String id = item.getString(GraphConstants.ID);
        int type = item.getInt(GraphConstants.TYPE);
        String label = managerFactory.getInteractionManager().getToolTip(id, type, name);
        d.setToolTipText(label);
    }

    public void itemExited(VisualItem item, MouseEvent e) {
        Display d = (Display) e.getSource();
        d.setToolTipText(null);
    }

    public void itemPressed(VisualItem gi, MouseEvent e) {
        super.itemPressed(gi, e);
    }

}
