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
import org.neodatis.knowledger.gui.graph.basic.DefaultLineColorAction;
import org.neodatis.knowledger.gui.graph.basic.DefaultTextColorAction;
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
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.force.DragForce;
import prefuse.util.force.NBodyForce;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;


public class KnowledgerGraphBuilder {
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

	public KnowledgerGraphBuilder() {
		this.graphProvider = new MockGraphProvider(this);
		this.controlAdapter = new DefaultControlAdapter(this);
	}

	public void initPrefuse() {
		Graph graph = null;
		try {
			graph = graphProvider.buildNewGraph();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading graph. Exiting...");
			System.exit(1);
		}

		visualization = new Visualization();
		visualization.add(GRAPH_NAME, graph);
        

		labelRenderer = new DefaultLabelRenderer();
		ShapeRenderer sr = new ShapeRenderer();
		//edgeRenderer = new LabeledEdgeRenderer();
		// DefaultRendererFactory rf = new
		// DefaultRendererFactory(labelRenderer);
		DefaultRendererFactory rf = new DefaultRendererFactory(labelRenderer);
		//rf.setDefaultEdgeRenderer(edgeRenderer);
        //rf.add("labels",new DefaultLabelRenderer(GraphConstants.LABEL));
		rf.add(VisualItem.HIGHLIGHT, labelRenderer);

		visualization.setRendererFactory(rf);
        //visualization.addDecorators("labels", GRAPH_NAME_EDGES); 
		// C=1 I=2 C=3 U=4
		int[] textPalette = new int[] { Color.BLUE.getRGB(), Color.MAGENTA.getRGB(), Color.RED.getRGB(), Color.GRAY.getRGB() };
		int[] linePalette = new int[] { Color.BLUE.getRGB(), Color.MAGENTA.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB() };
		// use black for node text
		// ColorAction nodeTextColor = new DataColorAction(GRAPH_NAME_NODES,
		// "object-type", Constants.NUMERICAL, VisualItem.TEXTCOLOR,
		// textPalette);
		ColorAction nodeTextColor = new DefaultTextColorAction(null, GRAPH_NAME_NODES, "type", Constants.NUMERICAL, VisualItem.TEXTCOLOR, textPalette);
		ColorAction nodeFillColor = new ColorAction(GRAPH_NAME_NODES, VisualItem.FILLCOLOR, Color.WHITE.getRGB());
		ColorAction nodeLineColor = new DefaultLineColorAction(null, GRAPH_NAME_NODES, "type", Constants.NUMERICAL, VisualItem.STROKECOLOR, linePalette);

		// use light grey for edges
		ColorAction edgesLineColor = new ColorAction(GRAPH_NAME_EDGES, VisualItem.STROKECOLOR, Color.LIGHT_GRAY.getRGB());
		ColorAction edgesFillColor = new ColorAction(GRAPH_NAME_EDGES, VisualItem.FILLCOLOR, Color.LIGHT_GRAY.getRGB());
		ColorAction edgesTextColor = new ColorAction(GRAPH_NAME_EDGES, VisualItem.TEXTCOLOR, Color.BLACK.getRGB());

		ColorAction focusedNodes = new ColorAction(Visualization.FOCUS_ITEMS, VisualItem.TEXTCOLOR, ColorLib.gray(200));

		// create an action list containing all color assignments
		ActionList color = new ActionList();
		color.add(focusedNodes);

		//color.add(nodeTextColor);
		color.add(nodeFillColor);
		color.add(nodeLineColor);

		color.add(edgesLineColor);
		color.add(edgesFillColor);
		color.add(edgesTextColor);
        color.add(new FocusLayout(VisualItem.HIGHLIGHT));

		int hops = 30;
		final GraphDistanceFilter filter = new GraphDistanceFilter("graph", hops);

		// create an action list with an animated layout
		// the INFINITY parameter tells the action list to run indefinitely
		ActionList layout = new ActionList(Activity.INFINITY);
		ForceDirectedLayout fdl = new ForceDirectedLayout("graph");
		fdl.getForceSimulator().addForce(new NBodyForce(-50, -1, 0));
		fdl.getForceSimulator().addForce(new DragForce(-0.005f));
		layout.add(fdl);
		layout.add(new RepaintAction());
        layout.add(new LabelLayout("labels"));
		// layout.add(filter);

		// add the actions to the visualization
		visualization.putAction("color", color);
		visualization.putAction("layout", layout);

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

		visualization.runAfter("color", "layout");

		runActions();

        
		// fix selected focus nodes TupleSet focusGroup =
		TupleSet focusGroup = visualization.getGroup(Visualization.FOCUS_ITEMS);
		focusGroup.addTupleSetListener(new TupleSetListener() {
			public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
				for (int i = 0; i < rem.length; ++i){
					((VisualItem) rem[i]).setFixed(false);
                    System.out.println("removing " + ((VisualItem) add[i]).getString(GraphConstants.LABEL) +" from focus items");
                }
				for (int i = 0; i < add.length; ++i) {
					((VisualItem) add[i]).setFixed(false);
					((VisualItem) add[i]).setFixed(true);
                    System.out.println("adding " + ((VisualItem) add[i]).getString(GraphConstants.LABEL) +" to focus items");
				}
				runActions();
			}
		});
		
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
		label.setFont(Font.getFont("arial"));
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
		visualization.run("layout");
		visualization.run("color");
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
		KnowledgerGraphBuilder graph2 = new KnowledgerGraphBuilder();
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
