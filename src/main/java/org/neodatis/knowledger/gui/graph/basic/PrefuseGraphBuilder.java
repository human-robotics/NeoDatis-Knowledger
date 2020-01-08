package org.neodatis.knowledger.gui.graph.basic;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JComponent;

import org.neodatis.knowledger.gui.graph.CenterAction;
import org.neodatis.knowledger.gui.graph.Configuration;
import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.GlobalConfiguration;
import org.neodatis.knowledger.gui.graph.IManagerFactory;
import org.neodatis.knowledger.gui.graph.basic.example.LabelLayout;
import org.neodatis.knowledger.gui.graph.defaults.DefaultController;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.data.Graph;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.force.NBodyForce;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;


public class PrefuseGraphBuilder implements Runnable{
	private int ANIMATION_TIME = 7000;
    private int FAST_ANIMATION_TIME = 2000;
    private Visualization visualization;

	private Display display;

	// 0:showing relation connector,1:hiding
	public static int mode = 1;

	/** To pecify if edge must be drawn has curve */
	public static boolean edgeAreCurved = false;

	private LabelRenderer labelRenderer;

	private EdgeRenderer edgeRenderer;

	private ControlAdapter controlAdapter;

	private IManagerFactory managerFactory;
	
	private NBodyForce bodyForce;

	public PrefuseGraphBuilder(IManagerFactory managerFactory) {
		this.managerFactory = managerFactory;
		init();
	}
    
	public void init() {
		Graph graph = null;
		try {
			graph = managerFactory.getGraphProvider().buildNewGraph();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading graph. Exiting...");
			System.exit(1);
		}

		visualization = new Visualization();
		visualization.add(GraphConstants.GRAPH_NAME, graph);
		labelRenderer = new DefaultLabelRenderer();
		edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_LINE, Constants.EDGE_ARROW_FORWARD);
		edgeRenderer.setArrowHeadSize(8, 7);
		DefaultRendererFactory rf = new DefaultRendererFactory(labelRenderer);
		rf.add(new InGroupPredicate("labels"), new DefaultEdgeLabeLRenderer(GraphConstants.LABEL,managerFactory.getPersistentManager().getKnowledgeBaseName()));

		visualization.setRendererFactory(rf);
		
		visualization.addDecorators("labels", GraphConstants.GRAPH_NAME_EDGES);
		
		initActions();

		// create a new Display that pull from our Visualization
		display = new MyDisplay(visualization);
		display.setSize(300, 300); // set display size
		display.pan(display.getSize().getWidth() / 2, display.getSize().getHeight() / 2);

		display.addControlListener(new FocusControl(1));
		display.addControlListener(new DragControl());
		display.addControlListener(new PanControl());
		//display.addControlListener(new ZoomControl());
		display.addControlListener(new WheelZoomControl());
		//display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new NeighborHighlightControl());
		//display.addControlListener(new ToolTipControl("tooltip"));
		display.setHighQuality(true);

		controlAdapter = new DefaultController(this,display,managerFactory);
		display.addControlListener(controlAdapter);

		display.getTextEditor().addKeyListener(controlAdapter);

		refresh();
        
        Thread centeringThread = new Thread(this);
        centeringThread.start();
        
	}
	
	public void initActions() {
		
		Configuration configuration = Configurations.getConfiguration(managerFactory.getPersistentManager().getKnowledgeBaseName());
		
        Predicate conceptPredicate = (Predicate) ExpressionParser.parse("type=1");
        Predicate instancePredicate = (Predicate) ExpressionParser.parse("type=2");
        Predicate relationPredicate = (Predicate) ExpressionParser.parse("type=4");

        ColorAction nodeTextColorAction = new MyColorAction("nodetext",GraphConstants.GRAPH_NAME_NODES, VisualItem.TEXTCOLOR, Color.BLACK.getRGB());
		if(managerFactory.getInteractionManager().getColors()==null){
		    nodeTextColorAction.add(VisualItem.FIXED, Color.RED.getRGB());
		    nodeTextColorAction.add(VisualItem.HIGHLIGHT, Color.RED.getRGB());


		    nodeTextColorAction.add(conceptPredicate, configuration.getConceptColor().getRGB());
		    nodeTextColorAction.add(instancePredicate, configuration.getInstanceColor().getRGB());
		    nodeTextColorAction.add(relationPredicate, configuration.getConnectorColor().getRGB());

        }else{
            int [] colors = managerFactory.getInteractionManager().getColors();
            int nbColors = colors.length;
            for(int i=0;i<nbColors;i++){
                Predicate predicate = (Predicate) ExpressionParser.parse(GraphConstants.USER_KEY+"="+i);
                nodeTextColorAction.add(predicate, colors[i]);
            }
        }
        ColorAction nodeBoxColorAction = new MyColorAction("nodebox",GraphConstants.GRAPH_NAME_NODES, VisualItem.STROKECOLOR, Color.WHITE.getRGB());
        nodeBoxColorAction.add(conceptPredicate, configuration.getVisualizationConfiguration().conceptHaveBox()? configuration.getConceptColor().getRGB():Color.WHITE.getRGB());
        nodeBoxColorAction.add(instancePredicate, configuration.getVisualizationConfiguration().instanceHaveBox()? configuration.getInstanceColor().getRGB():Color.WHITE.getRGB());
        nodeBoxColorAction.add(relationPredicate, Color.WHITE.getRGB());
		
		ActionList draw = new ActionList();
		draw.add(nodeTextColorAction);
		draw.add(nodeBoxColorAction);
		draw.add(new MyColorAction("node fill color",GraphConstants.GRAPH_NAME_NODES, VisualItem.FILLCOLOR, Color.WHITE.getRGB()));
		//draw.add(new ColorAction(GraphConstants.GRAPH_NAME_NODES, VisualItem.STROKECOLOR, Color.WHITE.getRGB()));

		
		draw.add(new MyColorAction("edge fill color",GraphConstants.GRAPH_NAME_EDGES, VisualItem.FILLCOLOR, configuration.getConnectorColor().getRGB()));
		draw.add(new MyColorAction("edge stroke color",GraphConstants.GRAPH_NAME_EDGES, VisualItem.STROKECOLOR, configuration.getConnectorColor().getRGB()));
		//draw.add(new MyColorAction("edge fixed color",GraphConstants.GRAPH_NAME_EDGES, VisualItem.FIXED, ColorLib.rgb(255, 100, 100)));
		//draw.add(new MyColorAction("edge highlight color",GraphConstants.GRAPH_NAME_EDGES, VisualItem.HIGHLIGHT, ColorLib.rgb(255, 100, 100)));
	
		//ActionList animate = new ActionList(Activity.INFINITY,GlobalConfiguration.getAnimationStepDuration());
        ActionList animate = new ActionList(ANIMATION_TIME,GlobalConfiguration.getAnimationStepDuration());
        ActionList fastAnimate = new ActionList(FAST_ANIMATION_TIME,GlobalConfiguration.getAnimationStepDuration());

		ForceDirectedLayout fdl = new ForceDirectedLayout("graph");
		//fdl.getForceSimulator().addForce(new NBodyForce(-20, -1, 0));
		fdl.getForceSimulator().addForce(new NBodyForce(-50, -1, 0));
		// fdl.getForceSimulator().addForce(new DragForce(-0.005f));

        ForceDirectedLayout fdl2 = new ForceDirectedLayout("graph");
        //fdl.getForceSimulator().addForce(new NBodyForce(-20, -1, 0));
        fdl.getForceSimulator().addForce(new NBodyForce(-50, -1, 0));
        // fdl.getForceSimulator().addForce(new DragForce(-0.005f));

        animate.add(fdl);
		animate.add(nodeTextColorAction);
		//animate.add(nodeBoxColorAction);		
		animate.add(new RepaintAction());
		animate.add(new LabelLayout("labels"));
        
        fastAnimate.add(fdl);
        fastAnimate.add(nodeTextColorAction);
        //animate.add(nodeBoxColorAction);      
        fastAnimate.add(new RepaintAction());
        fastAnimate.add(new LabelLayout("labels"));


        ActionList center = new ActionList();
        center.add(new CenterAction(managerFactory.getInteractionManager()));

		// finally, we register our ActionList with the Visualization.
		// we can later execute our Actions by invoking a method on our
		// Visualization, using the name we've chosen below.
		visualization.putAction("draw", draw);

		visualization.putAction("layout", animate);
        visualization.putAction("fast-layout", fastAnimate);        

		visualization.runAfter("draw", "layout");
	}
	public JComponent buildComponent() {
		return display;
		/*
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(display, BorderLayout.CENTER);
		Color BACKGROUND = Color.WHITE;
		Color FOREGROUND = Color.DARK_GRAY;
		UILib.setColor(panel, BACKGROUND, FOREGROUND);
		return panel;*/
	}


	public void redraw() {
		try {
			visualization.reset();
			visualization.add("graph", managerFactory.getGraphProvider().buildNewGraph());
            Configuration configuration = Configurations.getConfiguration(managerFactory.getPersistentManager().getKnowledgeBaseName());
            if(configuration.getVisualizationConfiguration().displayLabelOnEdges()){
                visualization.addDecorators("labels", GraphConstants.GRAPH_NAME_EDGES);
            }
			initActions();
			refresh();
			display.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Display getDisplay() {
        return display;
	}

	public Visualization getVisualization() {
		return visualization;
	}

    public void centerOn(long id){
        if(id==-1){
            return;
        }
        Iterator iterator = visualization.visibleItems(GraphConstants.GRAPH_NAME_NODES);
        while(iterator.hasNext()){
            Object o = iterator.next();
            NodeItem ni = (NodeItem) o;
            if(Long.parseLong(ni.getString(GraphConstants.ID))==id){
                display.animatePanToAbs(new Point2D.Double(ni.getX(),ni.getY()),2000);     
                return;
            }
        }
        
    }
	public void refresh() {
		visualization.run("draw");
        visualization.run("fast-layout");
        
	}

	public void curvedEdge(boolean yes) {
		edgeAreCurved = yes;
		if (yes) {
			edgeRenderer.setEdgeType(Constants.EDGE_TYPE_CURVE);
		} else {
			edgeRenderer.setEdgeType(Constants.EDGE_TYPE_LINE);
		}
		refresh();
	}

	public IGraphProvider getGraphProvider() {
		return managerFactory.getGraphProvider();
	}

	public NBodyForce getBodyForce() {
		return bodyForce;
	}
	
    public void run() {
        try {
            Thread.sleep(ANIMATION_TIME+10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        centerOn(managerFactory.getInteractionManager().centerOn());
    }

}
