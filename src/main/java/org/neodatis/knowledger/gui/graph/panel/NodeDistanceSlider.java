package org.neodatis.knowledger.gui.graph.panel;

import org.neodatis.knowledger.gui.component.MyJSlider;

import prefuse.util.force.NBodyForce;


public class NodeDistanceSlider extends MyJSlider {
	private KnowledgerGraphPanel kgp;
	
	public NodeDistanceSlider(KnowledgerGraphPanel kgp, String label, int start, int end, int initialValue) {
		super(label, start, end, initialValue);
		this.kgp = kgp; 
	}

	public void newValue(int newValue) {
		System.out.println("new value is " + newValue);
		kgp.getPrefuseGraphBuilder().getBodyForce().setMinValue(NBodyForce.GRAVITATIONAL_CONST,(float)newValue);
		try {
			kgp.refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
