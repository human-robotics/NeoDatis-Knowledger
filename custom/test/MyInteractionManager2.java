/* 

 */
package test;

import javax.swing.JOptionPane;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.gui.graph.MenuEntry;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager;

import prefuse.visual.VisualItem;


public class MyInteractionManager2 extends DefaultInteractionManager {

	public MyInteractionManager2() {
		super();
	}

	public MenuEntry[] buildMenuEntry(VisualItem vi, String id, int type,
			String name) {

		MenuEntry[] menuEntry = null;

		switch (type) {
		case GraphConstants.CONCEPT:
			menuEntry = new MenuEntry[3];
			menuEntry[0] = new MenuEntry(vi, id, type, name, "menu1 c",
					"action1c");
			menuEntry[1] = new MenuEntry(vi, id, type, name, "menu2 c",
					"action2c");
			menuEntry[2] = new MenuEntry(vi, id, type, name, "menu3 c",
					"action3c");
			return menuEntry;
		case GraphConstants.INSTANCE:
			if (name.equals("celula")) {
				menuEntry = new MenuEntry[1];
				menuEntry[0] = new MenuEntry(vi, id, type, name,
						"Criar página", "acao-criar-pagina");
				return menuEntry;

			} else {
				menuEntry = new MenuEntry[2];
				menuEntry[0] = new MenuEntry(vi, id, type, name, "menu1 i",
						"action1i");
				menuEntry[1] = new MenuEntry(vi, id, type, name, "menu2 i",
						"action2i");
				return menuEntry;
			}
		}
		/*
		 * case GraphConstants.CONNECTOR: menuEntry = new MenuEntry[3];
		 * menuEntry[0] = new
		 * MenuEntry(vi,id,type,name,Messages.getString("menu1
		 * co"),"action1co"); menuEntry[1] = new
		 * MenuEntry(vi,id,type,name,Messages.getString("menu2
		 * co"),"action2co"); menuEntry[2] = new
		 * MenuEntry(vi,id,type,name,Messages.getString("menu3
		 * co"),"action3co"); return menuEntry; }
		 */
		return new MenuEntry[0];
	}

	public String getToolTip(String id, int type, String name) {
		switch (type) {
		case GraphConstants.CONCEPT:
			return "Concept ola chico : " + name;
		case GraphConstants.INSTANCE:
			return "Instance " + name;
		case GraphConstants.CONNECTOR:
			return "Connector " + name;
		}
		return "unknown";
	}

	public boolean hasPopupMenu() {
		return false;
	}

	public boolean isLocalAction() {
		return false;
	}

	public long execute(MenuEntry menuEntry, int x, int y) {

		String nomeAcao = menuEntry.getAction();
		
		if(nomeAcao.equals("acao-criar-pagina")){
			try {
				criaNovaPagina(menuEntry.getObjectId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			JOptionPane.showMessageDialog(null, "Action " + menuEntry.getAction()
				+ " on " + menuEntry.getObjectName() + " at (" + x + "," + y
				+ ")");
		}
		return -1;
	}

	private void criaNovaPagina(String objectId) throws Exception {
		long oid = Long.parseLong(objectId);
		Instance instance = getPersistentManager().getInstanceById(objectId);
		
		Concept conceitoPagina = getPersistentManager().getConceptFromName("Pagina");
		InstanceList paginas = getPersistentManager().getInstancesOf(conceitoPagina,true);
		System.out.println("paginas = " + paginas);
		
		System.out.println("Criando uma nova pagina para ");
		DialogoMapeamento dialogoMapeamento = new DialogoMapeamento(getPersistentManager(),instance, paginas, "é pagina de");
		dialogoMapeamento.setVisible(true);
		
		if(dialogoMapeamento.isOk()){
			getKnowledgerGraphPanel().redraw();
		}
		
	}

	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

    

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#getFramesToUpdate(int, java.lang.String)
     */
    public String[] getFramesToUpdate(int objectType, String id) {
        String [] targets = {"target1","target2","target3"};
        return targets;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#getServletNameForFrameUpdate()
     */
    public String getServletNameForFrameUpdate() {
        return "myservlet";
    }

    public long centerOn() {
        return -1;
    }

	public boolean canDragAndDrop(String sourceObjectId) {
		// TODO Auto-generated method stub
		return false;
	}

	public ConnectorList getConnectorListOnDragOnDrop(String sourceObjectId) {
		// TODO Auto-generated method stub
		return null;
	}

}
