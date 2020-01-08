package org.neodatis.knowledger.gui.graph.basic.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.neodatis.knowledger.gui.graph.basic.DefaultLabelRenderer;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.basic.MyDisplay;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.force.NBodyForce;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;


public class SimpleKnowledgerGraphBuilder extends KnowledgerGraphBuilder {
	private Visualization visualization;

	private Display display;

	public static final int CONCEPT = 1;

	public static final int INSTANCE = 2;

	public static final int CONNECTOR = 3;

	public static final int UNKNOWN = 4;

	public static String GRAPH_NAME = "graph";

	public static String GRAPH_NAME_NODES = "graph.nodes";

	public static String GRAPH_NAME_EDGES = "graph.edges";

	public static String LABEL_FIELD_NAME = "label";

	// 0:showing relation connector,1:hiding
	public static int mode = 0;

	/** To pecify if edge must be drawn has curve */
	public static boolean edgeAreCurved = false;

	private LabelRenderer labelRenderer;

	private EdgeRenderer edgeRenderer;

	private ControlAdapter controlAdapter;

	private IGraphProvider graphProvider;
    private Graph graph;

	public SimpleKnowledgerGraphBuilder() {
		this.graphProvider = new MockGraphProvider(this);
		this.controlAdapter = new DefaultControlAdapter(this);
	}

    public SimpleKnowledgerGraphBuilder(Graph graph) {
        this.graph = graph;
        this.controlAdapter = new DefaultControlAdapter(this);
    }

    public void initPrefuse() {
		try {
			if(graphProvider!=null){
			    graph = graphProvider.buildNewGraph();
            }
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading graph. Exiting...");
			System.exit(1);
		}

		visualization = new Visualization();
		visualization.add(GRAPH_NAME, graph);
		visualization.addDecorators("labels", GRAPH_NAME_EDGES);

		labelRenderer = new DefaultLabelRenderer();
		edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_LINE, Constants.EDGE_ARROW_FORWARD);
		edgeRenderer.setArrowHeadSize(8, 7);
		DefaultRendererFactory rf = new DefaultRendererFactory(labelRenderer);
		rf.setDefaultEdgeRenderer(edgeRenderer);
		visualization.setRendererFactory(rf);
		rf.add(new InGroupPredicate("labels"), new MyLabeLRenderer(GraphConstants.LABEL));

		int hops = 30;
		final GraphDistanceFilter filter = new GraphDistanceFilter(GRAPH_NAME, hops);

		// create a new Display that pull from our Visualization
		display = new MyDisplay(visualization);
		display.setSize(720, 500); // set display size
		display.pan(display.getSize().getWidth() / 2, display.getSize().getHeight() / 2);

		display.addControlListener(new FocusControl(1));
		display.addControlListener(new DragControl());
		display.addControlListener(new PanControl());
		display.addControlListener(new ZoomControl());
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new NeighborHighlightControl());
		display.addControlListener(new ToolTipControl("tooltip"));

		// controlAdapter = new DefaultControlAdapter(this);
		display.addControlListener(controlAdapter);

		display.getTextEditor().addKeyListener(controlAdapter);
		display.setHighQuality(true);

		initActions(false);
		runActions();

		/*
		 * // fix selected focus nodes TupleSet focusGroup = TupleSet focusGroup =
		 * visualization.getGroup(Visualization.FOCUS_ITEMS);
		 * focusGroup.addTupleSetListener(new TupleSetListener() { public void
		 * tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) { for (int i =
		 * 0; i < rem.length; ++i){ ((VisualItem) rem[i]).setFixed(false);
		 * System.out.println("removing " + ((VisualItem)
		 * add[i]).getString(GraphConstants.LABEL) +" from focus items"); } for
		 * (int i = 0; i < add.length; ++i) { ((VisualItem)
		 * add[i]).setFixed(false); ((VisualItem) add[i]).setFixed(true);
		 * System.out.println("adding " + ((VisualItem)
		 * add[i]).getString(GraphConstants.LABEL) +" to focus items"); }
		 * runActions(); } });
		 */
		/*
		 * SearchTupleSet search = new PrefixSearchTupleSet();
		 * visualization.addFocusGroup(Visualization.SEARCH_ITEMS, search);
		 * search.addTupleSetListener(new TupleSetListener() { public void
		 * tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
		 * //visualization.cancel("animatePaint");
		 * System.out.println("tuplesetChanged - " + t); runActions(); } });
		 */
		/*
		 * FontAction fonts = new FontAction("graph.nodes",
		 * FontLib.getFont("Tahoma", 10)); fonts.add("ingroup('_focus_')",
		 * FontLib.getFont("Tahoma", 11));
		 */
		// The same listener is used to interact with prefuse display and
		// display Text editor!
	}

	public void initActions(boolean withBoxes) {
		ColorAction nodeTextColorAction = new ColorAction(GRAPH_NAME_NODES, VisualItem.TEXTCOLOR, ColorLib.rgb(200, 200, 255));
		nodeTextColorAction.add(VisualItem.FIXED, ColorLib.rgb(255, 100, 100));
		nodeTextColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));

		Predicate conceptPredicate = (Predicate) ExpressionParser.parse("type=1");
		Predicate instancePredicate = (Predicate) ExpressionParser.parse("type=2");
		Predicate relationPredicate = (Predicate) ExpressionParser.parse("type=4");

		nodeTextColorAction.add(conceptPredicate, Color.BLUE.getRGB());
		nodeTextColorAction.add(instancePredicate, Color.RED.getRGB());
		nodeTextColorAction.add(instancePredicate, Color.ORANGE.getRGB());
		nodeTextColorAction.add(relationPredicate, Color.BLACK.getRGB());

		ActionList draw = new ActionList();
		draw.add(nodeTextColorAction);
		if(withBoxes){
			draw.add(new ColorAction(GRAPH_NAME_NODES, VisualItem.STROKECOLOR, Color.BLACK.getRGB()));
		}else{
			draw.add(new ColorAction(GRAPH_NAME_NODES, VisualItem.STROKECOLOR, Color.WHITE.getRGB()));
		}
		draw.add(new ColorAction(GRAPH_NAME_EDGES, VisualItem.FILLCOLOR, ColorLib.gray(200)));
		draw.add(new ColorAction(GRAPH_NAME_EDGES, VisualItem.STROKECOLOR, ColorLib.gray(200)));

		ActionList animate = new ActionList(Activity.INFINITY);

		ForceDirectedLayout fdl = new ForceDirectedLayout("graph");
		fdl.getForceSimulator().addForce(new NBodyForce(-20, -1, 0));
		// fdl.getForceSimulator().addForce(new NBodyForce(-100, -1, 0));
		// fdl.getForceSimulator().addForce(new DragForce(-0.005f));

		animate.add(fdl);
		animate.add(nodeTextColorAction);
		animate.add(new RepaintAction());
		//animate.add(new LabelLayout("labels"));

		// finally, we register our ActionList with the Visualization.
		// we can later execute our Actions by invoking a method on our
		// Visualization, using the name we've chosen below.
		visualization.putAction("draw", draw);

		visualization.putAction("layout", animate);

		visualization.runAfter("draw", "layout");
	}

	public JPanel buildPanel(final String searchLabel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(display, BorderLayout.CENTER);
		Color BACKGROUND = Color.WHITE;
		Color FOREGROUND = Color.DARK_GRAY;
		UILib.setColor(panel, BACKGROUND, FOREGROUND);
		return panel;
	}

	public JPanel buildPanelTest(final String searchLabel) {
		// create a search panel for the tree map
		/*
		 * SearchQueryBinding sq = new SearchQueryBinding((Table)
		 * visualization.getGroup("graph.nodes"), searchLabel, (SearchTupleSet)
		 * visualization.getGroup(Visualization.SEARCH_ITEMS)); JSearchPanel
		 * search = sq.createSearchPanel(); search.setShowResultCount(true);
		 * search.setBorder(BorderFactory.createEmptyBorder(5, 5, 4, 0));
		 * search.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));
		 * 
		 * final JFastLabel title = new JFastLabel(" ");
		 * title.setPreferredSize(new Dimension(350, 20));
		 * title.setVerticalAlignment(SwingConstants.BOTTOM);
		 * title.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		 * title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
		 */
		/*
		 * display.addControlListener(new ControlAdapter() { public void
		 * itemEntered(VisualItem item, MouseEvent e) { if
		 * (item.canGetString(searchLabel))
		 * title.setText(item.getString(searchLabel)); }
		 * 
		 * public void itemExited(VisualItem item, MouseEvent e) {
		 * title.setText(null); } });
		 */
		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createHorizontalStrut(10));
		JLabel label = new JLabel("controlAdapter.getHelpString()");
		label.setFont(Font.getFont("Arial"));
		box.add(label);
		box.add(Box.createHorizontalGlue());
		// box.add(search);
		box.add(Box.createHorizontalStrut(3));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(display, BorderLayout.CENTER);
		panel.add(box, BorderLayout.SOUTH);

		Color BACKGROUND = Color.WHITE;
		Color FOREGROUND = Color.DARK_GRAY;
		UILib.setColor(panel, BACKGROUND, FOREGROUND);

		return panel;
	}

	public void redraw() {
		try {
			visualization.reset();
			visualization.add("graph", graphProvider.buildNewGraph());
			runActions();
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

	public void runActions() {
		visualization.run("draw");
	}

	public void curvedEdge(boolean yes) {
		edgeAreCurved = yes;
		if (yes) {
			edgeRenderer.setEdgeType(Constants.EDGE_TYPE_CURVE);
		} else {
			edgeRenderer.setEdgeType(Constants.EDGE_TYPE_LINE);
		}
		runActions();
	}

	public IGraphProvider getGraphProvider() {
		return graphProvider;
	}

	public static void main(String[] args) {
		SimpleKnowledgerGraphBuilder graph2 = new SimpleKnowledgerGraphBuilder();
		graph2.initPrefuse();
		JPanel panel = graph2.buildPanel("label");
		// create a new window to hold the visualization
		JFrame frame = new JFrame("prefuse example");
		// ensure application exits when window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack(); // layout components in window
		frame.setVisible(true); // show the window
	}
}
