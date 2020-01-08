// $ANTLR 2.7.5 (20050128): "query.g" -> "DialogyQueryParser.java"$

package org.neodatis.knowledger.gui.parser;

import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;


/**

select x where x.name = 'oli%'

query = 'select' variable 'where' complex-expression
complex-expression = ( or-expression | and-expression | simple-expression )
simple-expression = ( variable '.' property '=' value | variable connector value
or-expression = complex-expression 'or' complex-expression

*/
public class DialogyQueryParser extends antlr.LLkParser       implements DialogyQueryParserTokenTypes
 {


void p(Object o)
{
//System.out.println(o);
}

protected DialogyQueryParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public DialogyQueryParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected DialogyQueryParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public DialogyQueryParser(TokenStream lexer) {
  this(lexer,1);
}

public DialogyQueryParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final String  queries() throws RecognitionException, TokenStreamException {
		String queries=null;;
		
		ICriteria criteria=null;
		
		try {      // for error handling
			{
			int _cnt3=0;
			_loop3:
			do {
				if ((LA(1)==SELECT)) {
					criteria=query();
					System.out.println(criteria);
				}
				else {
					if ( _cnt3>=1 ) { break _loop3; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt3++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return queries;
	}
	
	public final ICriteria  query() throws RecognitionException, TokenStreamException {
		ICriteria criteria=null;;
		
		
			String i;
		
		
		try {      // for error handling
			match(SELECT);
			i=identifier();
			match(WHERE);
			criteria=complexExpression();
			match(SEMI);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return criteria;
	}
	
	public final String  identifier() throws RecognitionException, TokenStreamException {
		String id = null;;
		
		Token  i = null;
		
		try {      // for error handling
			i = LT(1);
			match(IDENTIFIER);
			id=i.getText();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return id;
	}
	
	public final ICriteria  complexExpression() throws RecognitionException, TokenStreamException {
		ICriteria criteria = null;;
		
		
		try {      // for error handling
			criteria=simpleExpression();
			{
			_loop7:
			do {
				if ((LA(1)==OR)) {
					criteria=orExpression(criteria);
				}
				else if ((LA(1)==AND)) {
					criteria=andExpression(criteria);
				}
				else {
					break _loop7;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return criteria;
	}
	
	public final ICriteria  simpleExpression() throws RecognitionException, TokenStreamException {
		ICriteria criteria=null;;
		
		
		String p=null,value=null;
		String connector=null,rightPart=null;
		boolean lparen=false,rparen=false;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				match(LPAREN);
				lparen=true;
				break;
			}
			case IDENTIFIER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			p=property();
			{
			switch ( LA(1)) {
			case EQUAL:
			case IS:
			{
				{
				switch ( LA(1)) {
				case EQUAL:
				{
					match(EQUAL);
					break;
				}
				case IS:
				{
					match(IS);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				value=identifier();
				criteria=new EntityCriteria(p.equals("x")?null:p,value);
				break;
			}
			case IDENTIFIER:
			{
				connector=identifier();
				rightPart=identifier();
				criteria=new EntityCriteria(connector,rightPart);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			if ((LA(1)==RPAREN)) {
				match(RPAREN);
				rparen=true;
			}
			else if ((_tokenSet_3.member(LA(1)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			
					//if(lparen!=rparen){throw new RuntimeException("asymetric left/right parent");}
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return criteria;
	}
	
	public final ICriteria  orExpression(
		ICriteria criteria
	) throws RecognitionException, TokenStreamException {
		ICriteria orICriteria=null;;
		
		ICriteria ctmp=null;
		
		try {      // for error handling
			match(OR);
			{
			if ((LA(1)==LPAREN)) {
				match(LPAREN);
			}
			else if ((LA(1)==LPAREN||LA(1)==IDENTIFIER)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			ctmp=complexExpression();
			{
			if ((LA(1)==RPAREN)) {
				match(RPAREN);
			}
			else if ((_tokenSet_3.member(LA(1)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			orICriteria = criteria.or(ctmp);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return orICriteria;
	}
	
	public final ICriteria  andExpression(
		ICriteria criteria
	) throws RecognitionException, TokenStreamException {
		ICriteria andICriteria=null;;
		
		ICriteria ctmp=null;
		
		try {      // for error handling
			match(AND);
			{
			if ((LA(1)==LPAREN)) {
				match(LPAREN);
			}
			else if ((LA(1)==LPAREN||LA(1)==IDENTIFIER)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			ctmp=complexExpression();
			{
			if ((LA(1)==RPAREN)) {
				match(RPAREN);
			}
			else if ((_tokenSet_3.member(LA(1)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			andICriteria = criteria.and(ctmp);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return andICriteria;
	}
	
	public final String  property() throws RecognitionException, TokenStreamException {
		String id = "";;
		
		Token  i = null;
		Token  i2 = null;
		String first=null,tail=null;
		
		try {      // for error handling
			i = LT(1);
			match(IDENTIFIER);
			first=i.getText();
			{
			_loop22:
			do {
				if ((LA(1)==POINT)) {
					match(POINT);
					i2 = LT(1);
					match(IDENTIFIER);
					if(tail==null){tail=i2.getText();}else{tail+="."+i2.getText();};
				}
				else {
					break _loop22;
				}
				
			} while (true);
			}
			if(tail==null){id=first;}else{id=tail;}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return id;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"select\"",
		"\"where\"",
		"SEMI",
		"\"or\"",
		"LPAREN",
		"RPAREN",
		"\"and\"",
		"EQUAL",
		"\"is\"",
		"IDENTIFIER",
		"POINT",
		"\"not\"",
		"LCURLY_BRACE",
		"RCURLY_BRACE",
		"COMMA",
		"WS"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 18L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 9952L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 1728L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 14336L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	
	}
