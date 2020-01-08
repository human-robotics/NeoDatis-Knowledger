header
{
package com.neodatis.knowledger.parser.query;

import com.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import com.neodatis.knowledger.core.interfaces.entity.ICriteria;

}

/**

select x where x.name = 'oli%'

query = 'select' variable 'where' complex-expression
complex-expression = ( or-expression | and-expression | simple-expression )
simple-expression = ( variable '.' property '=' value | variable connector value
or-expression = complex-expression 'or' complex-expression

*/


class KnowledgerQueryParser extends Parser;
{

void p(Object o)
{
//System.out.println(o);
}
}

queries returns [String queries=null;]
{ICriteria criteria=null;}
: (criteria=query {System.out.println(criteria);})+
;


query returns [ICriteria criteria=null;]
{
	String i;
}
	: SELECT i=identifier WHERE criteria=complexExpression SEMI;


complexExpression returns [ICriteria criteria = null;]
	:  criteria=simpleExpression 
	   (criteria=orExpression[criteria]|criteria=andExpression[criteria])* 
	   ;

orExpression [ICriteria criteria]returns [ICriteria orICriteria=null;]
{ICriteria ctmp=null;}

	: OR (LPAREN)? ctmp=complexExpression (RPAREN)?   {orICriteria = criteria.or(ctmp);}
	;

andExpression [ICriteria criteria]returns [ICriteria andICriteria=null;]
{ICriteria ctmp=null;}

	: AND (LPAREN)? ctmp=complexExpression (RPAREN)?   {andICriteria = criteria.and(ctmp);}
	;

simpleExpression returns [ICriteria criteria=null;]
{
String p=null,value=null;
String connector=null,rightPart=null;
boolean lparen=false,rparen=false;
}
	: (LPAREN{lparen=true;})? p=property 
	(
		(EQUAL|IS) value=identifier {criteria=new EntityCriteria(p.equals("x")?null:p,value);}
		| 
		connector=identifier rightPart=identifier {criteria=new EntityCriteria(connector,rightPart);}
	) (RPAREN{rparen=true;})? 
	
	{
		//if(lparen!=rparen){throw new RuntimeException("asymetric left/right parent");}
	}; 


identifier returns [String id = null;]
	: i:IDENTIFIER{id=i.getText();}
	;


// for property like x, return x, for properties like x.wife.name, returns wife.name
property returns [String id = "";]
{String first=null,tail=null,i2=null;}
	: 
	   first=identifier
	   (POINT i2=identifier  	
	   							{if(tail==null){tail=i2;}else{tail+="."+i2;};})*	
	   
	   {if(tail==null){id=first;}else{id=tail;}}
	;

class QueryLexer extends Lexer;

options {
    k=2; // needed for newline junk
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}

tokens {
    SELECT="select";
    WHERE="where";
    OR="or";
    AND="and";
    NOT = "not";
    IS = "is";
}

IDENTIFIER options { testLiterals=true; }
    : 
        ( 'a' .. 'z' |'A' .. 'Z' | '0' .. '9' | '_' | '-' | '$' | '#' | '%' )+
        |
        QUOTE ( 'a' .. 'z' |'A' .. 'Z' | '0' .. '9' | '_' | '-' | '$' | '#' | '%' | ' ')+ QUOTE
    ;

LPAREN: '(' ;
RPAREN: ')' ;
LCURLY_BRACE: '{' ;
RCURLY_BRACE: '}' ;
EQUAL:'=';
COMMA:',';
SEMI:';';
POINT:'.';
QUOTE: '\'';

WS    : ( ' '
        | '\r' '\n'
        | '\n'
        | '\t'
        )
        {$setType(Token.SKIP);}
     ;
     
