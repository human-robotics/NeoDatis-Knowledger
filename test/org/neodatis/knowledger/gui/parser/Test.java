/* 
 * $RCSfile: Test.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.parser;

import java.io.FileNotFoundException;

import javax.swing.JFrame;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class Test {

    /**
     * @param args
     * @throws TokenStreamException 
     * @throws RecognitionException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws RecognitionException, TokenStreamException, FileNotFoundException {
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    //KnowledgeExplorerPanel.display(KnowledgeBaseFactory.getInstance("example5",KnowledgeBaseType.DATABASE));
                	JFrame frame = new JFrame("Choose KB");
            		// frame.setUndecorated(true);
            		frame.setDefaultLookAndFeelDecorated(true);
            		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            		frame.getContentPane().add(new ChooseKnowledgeBaseToTestQuery());
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
