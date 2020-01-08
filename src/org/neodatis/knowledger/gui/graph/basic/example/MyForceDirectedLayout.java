package org.neodatis.knowledger.gui.graph.basic.example;

import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

public class MyForceDirectedLayout extends ForceDirectedLayout {

	private static ForceSimulator emptyForceSimulator = null;
	private static ForceSimulator nBodyForceSimulator = null;
	public static boolean withNBodyForce = true;
	
	public MyForceDirectedLayout(String group, boolean enforceBounds, boolean runonce) {
		super(group, enforceBounds, runonce);
		// TODO Auto-generated constructor stub
	}

	public MyForceDirectedLayout(String group, boolean enforceBounds) {
		super(group, enforceBounds);
		// TODO Auto-generated constructor stub
	}

	public MyForceDirectedLayout(String group, ForceSimulator fsim, boolean enforceBounds, boolean runonce) {
		super(group, fsim, enforceBounds, runonce);
		// TODO Auto-generated constructor stub
	}

	public MyForceDirectedLayout(String group, ForceSimulator fsim, boolean enforceBounds) {
		super(group, fsim, enforceBounds);
		// TODO Auto-generated constructor stub
	}

	public MyForceDirectedLayout(String graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	protected float getMassValue(VisualItem n) {
		float mass = super.getMassValue(n);
		/*
		if(n.isHighlighted()){
			mass = 10;
		}
		String label = n.getString(GraphConstants.LABEL);
		if(label.equals("Object")){
			mass = 10;
		}
		//System.out.println("mass of " + label + " is " + mass);
		 * */
		 
		return mass*2;
	}

	public ForceSimulator getForceSimulator() {
		if(nBodyForceSimulator==null){
			nBodyForceSimulator = new ForceSimulator();
			nBodyForceSimulator.addForce(new NBodyForce(-100, -1, 0));
			emptyForceSimulator = new ForceSimulator();
		}
		if(withNBodyForce){
			return nBodyForceSimulator;
		}
		return emptyForceSimulator;
	}

	protected float getSpringCoefficient(EdgeItem e) {
		// TODO Auto-generated method stub
		return super.getSpringCoefficient(e);
	}

	protected float getSpringLength(EdgeItem e) {
		// TODO Auto-generated method stub
		return super.getSpringLength(e);
	}

}
