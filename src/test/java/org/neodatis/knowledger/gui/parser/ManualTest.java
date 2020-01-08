/* 
 * $RCSfile: ManualTest.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.parser;

import java.io.FileNotFoundException;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class ManualTest {

    /**
     * @param args
     * @throws TokenStreamException 
     * @throws RecognitionException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws RecognitionException, TokenStreamException, FileNotFoundException {
        QueryLexer lexer = new QueryLexer(System.in);
        DialogyQueryParser parser = new DialogyQueryParser(lexer);
        parser.queries();
    }

}
