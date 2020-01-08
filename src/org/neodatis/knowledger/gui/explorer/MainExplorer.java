/* 
 * $RCSfile: MainExplorer.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.explorer;

import javax.swing.JFrame;


/**
 * <p>
 * 
 * </p>
 *  
 */
public class MainExplorer {

    public static void main(String[] args) throws Exception {
//      use the aqua theme (stored in auquathemepack.zip)
        //SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack("./skins//aquathemepack.zip" ));
        //SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack("./skins//toxicthemepack.zip" ));
        //SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack("./skins//whistlerthemepack.zip" ));
        //SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack("./skins//macosthemepack.zip" ));
        
         
//        load L&F with the correct L&F class name
        //UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"); 
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    //KnowledgeExplorerPanel.display(KnowledgeBaseFactory.getInstance("example5",KnowledgeBaseType.DATABASE));
                	JFrame frame = new JFrame("Choose KB");
            		// frame.setUndecorated(true);
            		frame.setDefaultLookAndFeelDecorated(true);
            		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            		frame.getContentPane().add(new ChooseKnowledgeBaseForExplorer());
            		frame.pack();
            		frame.setVisible(true);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}