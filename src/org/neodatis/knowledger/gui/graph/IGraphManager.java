package org.neodatis.knowledger.gui.graph;

import java.awt.Dimension;

import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;

import prefuse.render.RendererFactory;


public interface IGraphManager {
	
	public IGraphProvider getGraphProvider();
	public RendererFactory getRendererFactory();
	public Dimension getSize();

}
