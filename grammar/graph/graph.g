header
{
package com.neodatis.knowledger.parser.graph;

import com.neodatis.knowledger.graph.*;
import java.util.List;
import java.util.ArrayList;
}

/**
[#olivier] -> (subject) -> {use} -> (object) -> [#group] -
	-> (element) -> [#racket] -> (quantity) -> [2]
	-> (element) -> [#ball] -> (quantity) -> [5]
	
	
	graphs : (oneGraph)*

	oneGraph : node arrow ( connector arrow (node | list-of-right-parts)  )*

	node : '[' ('@'|'$'|'#')? identifier ']'

	arrow : '->' | '<-'

	connector : '(' identifier ')'

	list-of-right-parts : '{'  connector arrow (node | list-of-right-parts)  '}'

*/


class GraphParser extends Parser;
{	
	protected List graphs 			= new ArrayList();
	protected Graph 				= new Graph();
	protected GraphNode lastNode	= null;
}

/*************************************************************************************/
graphs
{Graph graph=null;}
:
(graph=oneGraph {graphs.add(graph);})+ END_OF_GRAPHS{return graphs;}
;

/*************************************************************************************/
oneGraph
{
	GraphNode leftNode;
	String arrow1;
}
    : leftNode=node {lastNode=leftNode;} ( arrow1=arrow connector arrow (node | listOfRightParts)  )*
    ;

/*************************************************************************************/
completeRelation[GraphNode firstNode,boolean updateLastNode] returns [GraphRelation gr = new GraphRelation();]
{
GraphNode rightNode;
String arrow1;
ConnectorNode theConnector;
gr.add(firstNode);
}
:
	theConnector=connector {gr.add(theConnector);} 
	arrow1=arrow
   	rightNode=node {gr.add(rightNode);if(updateLastNode)lastNode=rightNode;}
;


/*************************************************************************************/
node returns [GraphNode node=null;]
: (node=object | node=connector)
;



/*************************************************************************************
 Matches an object between [] like [Man] or [#olivier]. 
 Square braces are used to indicate a concept or an instance.
  The # means instance. 
*************************************************************************************/

object returns [GraphNode node=null;]
{String nodeName=null;int nodeType=0;}
: 
	LSQUARE_BRACE 
	(INSTANCE_PREFIX {node=new InstanceNode();} | ACTION_PREFIX {node=new ActionNode();} | PROPOSITION_PREFIX {node=new PropositionNode();} )? 
	nodeName=identifier {if(node==null){node=new ConceptNode();};node.setName(nodeName);}
	RSQUARE_BRACE
;

	
/*************************************************************************************
 Matches a string between () like (subject). the () are used to indicate connectors   
 *************************************************************************************/ 

connector returns [ConnectorNode node=null]
{String nodeName=null;}
: LPAREN nodeName=identifier {node=new ConnectorNode(nodeName);} RPAREN
;



listOfParts
:
		    	LCURLY_BRACE
		    	
			    RCURLY_BRACE

;


/** Matches a valid identifier*/
identifier returns [String txt=""]
: ( l:LETTER{txt+=l.getText();} | i:INT{txt+=i.getText();} )+
;

/** Matches an arrow : "->" or "<-"*/
arrow returns [String txt=""]
: (LARROW {txt="<-";}   |  RARROW {txt="->";} | START_CONTEXT HALF_RARROW {txt=" sub";} )
;


class GraphLexer extends Lexer;

options {
    k=2; // needed for newline junk
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}

LSQUARE_BRACE:'[';
RSQUARE_BRACE:']';
LPAREN: '(' ;
RPAREN: ')' ;
LCURLY_BRACE: '{' ;
RCURLY_BRACE: '}' ;
LARROW:"<-";
RARROW:"->";
COMMA:',';
START_CONTEXT:'-';
HALF_RARROW:'>';
INSTANCE_PREFIX:'#';
ACTION_PREFIX:'@';
PROPOSITION_PREFIX:'$';
END_OF_GRAPHS:'.';

INT   : ('0'..'9')+ ;
LETTER:('a'..'z');
WS    : ( ' '
        | '\r' '\n'
        | '\n'
        | '\t'
        )
        {$setType(Token.SKIP);}
      ;