//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;
/*
 * This file is part of a syntax highlighting package
 * Copyright (C) 1999, 2000  Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Syntax+Highlighting
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * See COPYING.TXT for details.
 */

/** 
 * A ScollToken is a token that is returned by a lexer that is lexing a SCOLL
 * pattern file.  It has several attributes describing the token:
 * The type of token, the text of the token, the line number on which it
 * occurred, the number of characters into the input at which it started, and
 * similarly, the number of characters into the input at which it ended. <br>
 */ 
public class ScollToken {
	public static final int UNDEFINED_STATE = -1;
	public static final int INITIAL_STATE = ScollLexer.YYINITIAL;
	public static final int DECLARATIONS_STATE = ScollLexer.DECLARATIONS;
	public static final int STATE_DECLARATIONS_STATE = ScollLexer.STATEDECL;
	public static final int BEHAVIOR_DECLARATIONS_STATE = ScollLexer.BEHDECL;
	public static final int KNOWLEDGE_DECLARATIONS_STATE = ScollLexer.KDECL;
	public static final int SYSTEM_STATE = ScollLexer.SYSTEM;
	public static final int SYSTEM_VARS_STATE = ScollLexer.SYSTEM_VARS_AND_CONST;
	public static final int BEHAVIOR_STATE = ScollLexer.BEHAVIOR;
	public static final int BEHAVIOR_RULES_STATE = ScollLexer.BEHAVIOR_RULES;
	public static final int BEHAVIOR_RULES_VARS_STATE = ScollLexer.BEH_RULES_VARS_AND_CONST;
	public static final int SUBJECT_STATE = ScollLexer.SUBJECT;
	public static final int CONFIG_STATE = ScollLexer.CONFIG;
	public static final int GOAL_STATE = ScollLexer.GOAL;

	public final static int RESERVED_WORD_BEHAVIOR = 0x101;
	public final static int RESERVED_WORD_CONFIG = 0x102;
	public final static int RESERVED_WORD_DECLARE = 0x103;
	public final static int RESERVED_WORD_GOAL = 0x104;
	public final static int RESERVED_WORD_KNOWLEDGE = 0x105;
	public final static int RESERVED_WORD_STATE = 0x106;
	public final static int RESERVED_WORD_SUBJECT = 0x107;
	public final static int RESERVED_WORD_SYSTEM = 0x108; 

	public final static int IDENTIFIER = 0x200;
	public final static int BEHAVIOR_IDENTIFIER = 0x201;
	public final static int UNDERBAR_IDENTIFIER = 0x202;

	public final static int LITERAL_INTEGER_DECIMAL = 0x310;

	public final static int SEPARATOR_LPAREN = 0x400;
	public final static int SEPARATOR_RPAREN = 0x401;
	public final static int SEPARATOR_LBRACE = 0x410;
	public final static int SEPARATOR_RBRACE = 0x411;
	public final static int SEPARATOR_SEMICOLON = 0x430;
	public final static int SEPARATOR_COMMA = 0x440;

	public final static int OPERATOR_LOGICAL_NOT = 0x510;
	public final static int OPERATOR_DIVIDE = 0x523;
	public final static int OPERATOR_QUESTION = 0x5A0;
	public final static int OPERATOR_COLON = 0x5A1;
	public final static int OPERATOR_IMPLIES = 0x5A2;


	public final static int COMMENT_TRADITIONAL = 0xD00;
	public final static int COMMENT_END_OF_LINE = 0xD10;
	public final static int WHITE_SPACE = 0xE00;

	public final static int ERROR_IDENTIFIER = 0xF00;
	public final static int ERROR_INTEGER_DECIMIAL_SIZE = 0xF30;
	public final static int ERROR_UNCLOSED_COMMENT = 0xF40;

	private int ID;
	private String contents;
	private int lineNumber;
	private int charBegin;
	private int charEnd;
	private int state;

	/**
	 * Create a new token.
	 * The constructor is typically called by the lexer
	 *
	 * @param ID the id number of the token
	 * @param contents A string representing the text of the token
	 * @param lineNumber the line number of the input on which this token started
	 * @param charBegin the offset into the input in characters at which this token started
	 * @param charEnd the offset into the input in characters at which this token ended
	 */
	public ScollToken(int ID, String contents, int lineNumber, int charBegin, int charEnd){
		this (ID, contents, lineNumber, charBegin, charEnd, ScollToken.UNDEFINED_STATE);
	}

	/**
	 * Create a new token.
	 * The constructor is typically called by the lexer
	 *
	 * @param ID the id number of the token
	 * @param contents A string representing the text of the token
	 * @param lineNumber the line number of the input on which this token started
	 * @param charBegin the offset into the input in characters at which this token started
	 * @param charEnd the offset into the input in characters at which this token ended
	 * @param state the state the tokenizer is in after returning this token.
	 */
	public ScollToken(int ID, String contents, int lineNumber, int charBegin, int charEnd, int state){
		this.ID = ID;
		this.contents = new String(contents);
		this.lineNumber = lineNumber;
		this.charBegin = charBegin;
		this.charEnd = charEnd;
		this.state = state;
	}

	/**
	 * Get an integer representing the state the tokenizer is in after
	 * returning this token.
	 * Those who are interested in incremental tokenizing for performance
	 * reasons will want to use this method to figure out where the tokenizer
	 * may be restarted.  The tokenizer starts in Token.INITIAL_STATE, so
	 * any time that it reports that it has returned to this state, the
	 * tokenizer may be restarted from there.
	 */
	public int getState(){
		return state;
	}

	/** 
	 * get the ID number of this token
	 * 
	 * @return the id number of the token
	 */
	public int getID(){
		return ID;
	}

	/** 
	 * get the contents of this token
	 * 
	 * @return A string representing the text of the token
	 */
	public String getContents(){
		return (new String(contents));
	}

	/** 
	 * get the line number of the input on which this token started
	 * 
	 * @return the line number of the input on which this token started
	 */
	public int getLineNumber(){
		return lineNumber;
	}

	/** 
	 * get the offset into the input in characters at which this token started
	 *
	 * @return the offset into the input in characters at which this token started
	 */
	public int getCharBegin(){
		return charBegin;
	}

	/** 
	 * get the offset into the input in characters at which this token ended
	 *
	 * @return the offset into the input in characters at which this token ended
	 */
	public int getCharEnd(){
		return charEnd;
	}

	/** 
	 * Checks this token to see if it is a reserved word.
	 * Reserved words are explained in <A Href=http://java.sun.com/docs/books/jls/html/>Java 
	 * Language Specification</A>.
	 *
	 * @return true if this token is a reserved word, false otherwise
	 */
	public boolean isReservedWord(){
		return((ID >> 8) == 0x1);
	}

	/** 
	 * Checks this token to see if it is an identifier.
	 * Identifiers are explained in <A Href=http://java.sun.com/docs/books/jls/html/>Java 
	 * Language Specification</A>.
	 *
	 * @return true if this token is an identifier, false otherwise
	 */
	public boolean isIdentifier(){
		return((ID >> 8) == 0x2);
	}

	/** 
	 * Checks this token to see if it is a literal.
	 * Literals are explained in <A Href=http://java.sun.com/docs/books/jls/html/>Java 
	 * Language Specification</A>.
	 *
	 * @return true if this token is a literal, false otherwise
	 */
	public boolean isLiteral(){
		return((ID >> 8) == 0x3);
	}

	/** 
	 * Checks this token to see if it is a Separator.
	 * Separators are explained in <A Href=http://java.sun.com/docs/books/jls/html/>Java 
	 * Language Specification</A>.
	 *
	 * @return true if this token is a Separator, false otherwise
	 */
	public boolean isSeparator(){
		return((ID >> 8) == 0x4);
	}

	/** 
	 * Checks this token to see if it is a Operator.
	 * Operators are explained in <A Href=http://java.sun.com/docs/books/jls/html/>Java 
	 * Language Specification</A>.
	 *
	 * @return true if this token is a Operator, false otherwise
	 */
	public boolean isOperator(){
		return((ID >> 8) == 0x5);
	}

	/** 
	 * Checks this token to see if it is a comment.
	 * 
	 * @return true if this token is a comment, false otherwise
	 */
	public boolean isComment(){
		return((ID >> 8) == 0xD);
	}

	/** 
	 * Checks this token to see if it is White Space.
	 * Usually tabs, line breaks, form feed, spaces, etc.
	 * 
	 * @return true if this token is White Space, false otherwise
	 */
	public boolean isWhiteSpace(){
		return((ID >> 8) == 0xE);
	}

	/** 
	 * Checks this token to see if it is an Error.
	 * Unfinished comments, numbers that are too big, unclosed strings, etc.
	 * 
	 * @return true if this token is an Error, false otherwise
	 */
	public boolean isError(){
		return((ID >> 8) == 0xF);
	}

	/**
	 * A description of this token.  The description should
	 * be appropriate for syntax highlighting.  For example
	 * "comment" is returned for a comment.
	 *
	 * @return a description of this token.
	 */
	public String getDescription(){
		if (isReservedWord()){	
			return("reservedWord");
		} else if (isIdentifier()){	
			switch (getState()) {	
			case STATE_DECLARATIONS_STATE: return ("stateDeclaration");
			case BEHAVIOR_DECLARATIONS_STATE: return ("behaviorDeclaration");
			case KNOWLEDGE_DECLARATIONS_STATE: return ("knowledgeDeclaration");
			case SYSTEM_STATE:
			case BEHAVIOR_STATE:
			case BEHAVIOR_RULES_STATE:
			case CONFIG_STATE:
			case GOAL_STATE: return("identifier");
			default: return("identifier");	
			}
		} else if (isLiteral()){
			return("literal");
		} else if (isSeparator()){
			return("separator");
		} else if (isOperator()){
			return("operator");
		} else if (isComment()){
			return("comment");
		} else if (isWhiteSpace()){
			return("whitespace");
		} else if (isError()){
			return("error");
		} else {
			return("unknown");
		}
	}

	/**
	 * get a String that explains the error, if this token is an error.
	 * 
	 * @return a  String that explains the error, if this token is an error, null otherwise.
	 */
	public String errorString(){
		String s;
		if (isError()){
			s = "Error on line " + lineNumber + ": ";
			switch (ID){
			case ERROR_IDENTIFIER:
				s += "Unrecognized Identifier: " + contents;
				break; 
			case ERROR_INTEGER_DECIMIAL_SIZE:
			case ERROR_UNCLOSED_COMMENT:
				s += "*/ expected after " + contents;
				break;
			}

		} else {
			s = null;
		}
		return (s);
	}

	/** 
	 * get a representation of this token as a human readable string.
	 * The format of this string is subject to change and should only be used
	 * for debugging purposes.
	 *
	 * @return a string representation of this token
	 */  
	public String toString() {
		return ("Token #" + Integer.toHexString(ID) + ": " + getDescription() + " Line " + 
				lineNumber + " from " +charBegin + " to " + charEnd + " : " + contents);
	}

}
