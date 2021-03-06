/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */
 
 /* ScollLexer.java is a generated file.  You probably want to
 * edit ScollLexer.lex to make changes.  Use JFlex to generate it.
 * To generate ScollLexer.java
 * Install <a href="http://jflex.de/">JFlex</a> v1.3.2 or later.
 * Once JFlex is in your classpath run<br>
 * <code>java JFlex.Main ScollLexer.lex</code><br>
 * You will then have a file called ScollLexer.java
 */

/*
 * This file is part of a <a href="http://ostermiller.org/syntax/">syntax
 * highlighting</a> package.
 * Copyright (C) 1999-2002 Stephen Ostermiller
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

/*
* Changes:
*
* 31-Oct-2008 fred: allow constants in rules
*/

package eu.evoluware.swingscollar;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** 
 * ScollLexer is a SCOLL lexer.  Created with JFlex.  An example of how it is used:
 *  <CODE>
 *  <PRE>
 *  ScollLexer shredder = new ScollLexer(System.in);
 *  ScollToken t;
 *  while ((t = shredder.getNextToken()) != null){
 *      System.out.println(t);
 *  }
 *  </PRE>
 *  </CODE>
 * @see ScollToken
 */ 
%%

%public
%class ScollLexer
%function getNextToken
%type ScollToken 

%{
    private int lastToken;
    private int nextState=YYINITIAL;
    private List<String> stateDecl = new ArrayList<String>(10);
    private List<String> behDecl = new ArrayList<String>(10);
    private List<String> kDecl = new ArrayList<String>(10);
    private List<String> behaviorTypeNames = new ArrayList<String>(10);
    
    
    public void clearStateDecl(){
		stateDecl.clear();
	}
	public void clearBehDecl(){
		behDecl.clear();
	}
	public void clearKDecl(){
		kDecl.clear();
	}
	public void clearBehaviorTypeNames(){
		behaviorTypeNames.clear();
	}
	public void addStateDecl(ScollContextToken tkn){
		stateDecl.add(tkn.getContents());
	}
	public void addBehDecl(ScollContextToken tkn){
		behDecl.add(tkn.getContents());
	}
	public void addKDecl(ScollContextToken tkn){
		kDecl.add(tkn.getContents());
	}
	public void addBehaviorTypeName(ScollContextToken tkn){
		behaviorTypeNames.add(tkn.getContents());
	}

	public String getContextIdentifierDescription(ScollContextToken tkn) {
		String contents = tkn.getContents();
		if (stateDecl.contains(contents)) return "statePredicate";
		if (behDecl.contains(contents)) return "behaviorPredicate";
		if (kDecl.contains(contents)) return "knowledgePredicate";
		return "identifier";
	}
	
	public String getContextBehaviorIdentifierDescription(ScollContextToken tkn) {
		if  ((tkn.getState() == ScollToken.BEHAVIOR_STATE) ||
				(behaviorTypeNames.contains(tkn.getContents())
						&& tkn.getState() == ScollToken.SUBJECT_STATE)){
			return "behaviorIdentifier";
		}
		else {
			return "error";
		}	
	}
    
    /** 
     * next Token method that allows you to control if whitespace and comments are
     * returned as tokens.
     */
    public ScollToken getNextToken(boolean returnComments, boolean returnWhiteSpace)throws IOException{
        ScollToken t = getNextToken();
        while (t != null && ((!returnWhiteSpace && t.isWhiteSpace()) || (!returnComments && t.isComment()))){
            t = getNextToken();
        }
        return (t); 
    }
        
    /**
     * Prints out tokens from a file or System.in.
     * If no arguments are given, System.in will be used for input.
     * If more arguments are given, the first argument will be used as
     * the name of the file to use as input
     *
     * @param args program arguments, of which the first is a filename
     */
    public static void main(String[] args) {
        InputStream in;
        try {
            if (args.length > 0){
                File f = new File(args[0]);
                if (f.exists()){
                    if (f.canRead()){
                        in = new FileInputStream(f);
                    } else {
                        throw new IOException("Could not open " + args[0]);
                    }
                } else {
                    throw new IOException("Could not find " + args[0]);
                }
            } else {
                in = System.in;
            }
            ScollLexer shredder = new ScollLexer(in);
            ScollToken t;
            while ((t = shredder.getNextToken()) != null) {
                if (t.getID() != ScollToken.WHITE_SPACE){
                    System.out.println(t);
                }
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }  

    /**
     * Closes the current input stream, and resets the scanner to read from a new input stream.
	 * All internal variables are reset, the old input stream  cannot be reused
	 * (content of the internal buffer is discarded and lost).
	 * The lexical state is set to the initial state.
     * Subsequent tokens read from the lexer will start with the line, char, and column
     * values given here.
     *
     * @param reader The new input.
     * @param yyline The line number of the first token.
     * @param yychar The position (relative to the start of the stream) of the first token.
     * @param yycolumn The position (relative to the line) of the first token.
     * @throws IOException if an IOExecption occurs while switching readers.
     */
    public void reset(java.io.Reader reader, int yyline, int yychar, int yycolumn) throws IOException{
        yyreset(reader);
        this.yyline = yyline;
		this.yychar = yychar;
		this.yycolumn = yycolumn;
	}
%}

%line
%char
%column
%full
%state DECLARATIONS
%state STATEDECL
%state BEHDECL
%state KDECL
%state SYSTEM
%state SYSTEM_VARS_AND_CONST
%state BEHAVIOR
%state BEHAVIOR_RULES
%state BEH_RULES_VARS_AND_CONST
%state SUBJECT
%state CONFIG
%state GOAL

Digit=([0-9])
NonZeroDigit=([1-9])
Letter=([a-zA-Z])
CapLetter=([A-Z])
LowLetter=([a-z])
Dot=([\.])
BLANK=([ ])
TAB=([\t])
FF=([\f])
CR=([\r])
LF=([\n])
EOL=({CR}|{LF}|{CR}{LF})
WhiteSpace=({BLANK}|{TAB}|{FF}|{EOL})
AnyNonSeparator=([^\t\f\r\n\ \(\)\{\}\[\]\;\,\=\>\<\!\~\?\:\+\-\*\/\&\|\^\%\"\'])
NonSeparatorInDeclarations=([^\t\f\r\n\ \:\/])
NonSeparatorInSystem=([^\t\f\r\n\ \(\;\:])
NonSeparatorInSystemVars=([^\t\f\r\n\ \)\,])
NonSeparatorInBehavior=([^\t\f\r\n\ \{])
NonSeparatorInBehaviorRules=([^\t\f\r\n\ \(\}\;\:])
NonSeparatorInBehaviorVars=([^\t\f\r\n\ \)\,])
NonSeparatorInSubject=([^\t\f\r\n\ \,\:\?])
NonSeparatorInConfig=([^\t\f\r\n\ \(\)\;\,\:])
NonSeparatorInGoal=([^\t\f\r\n\ \(\)\;\,\:\?\!])

ScollLetter=({Letter})
Identifier=({ScollLetter}({ScollLetter}|{Digit}|{Dot})*)
BehaviorTypeIdentifier=({CapLetter}({CapLetter}|{Digit}|{Dot})*)
InitLowIdentifier=({LowLetter}({ScollLetter}|{Digit}|{Dot})*)
InitCapIdentifier=({CapLetter}({ScollLetter}|{Digit}|{Dot})*)
ErrorIdentifier=({AnyNonSeparator}+)
ErrorInDeclarationsIdentifier=({NonSeparatorInDeclarations}+)
ErrorInSystemIdentifier=({NonSeparatorInSystem}+)
ErrorInSystemVarsIdentifier=({NonSeparatorInSystemVars}+)
ErrorInBehaviorIdentifier=({NonSeparatorInBehavior}+)
ErrorInBehaviorRulesIdentifier=({NonSeparatorInBehaviorRules}+) 
ErrorInBehaviorVarsIdentifier=({NonSeparatorInBehaviorVars}+) 
ErrorInSubjectIdentifier=({NonSeparatorInSubject}+)
ErrorInConfigIdentifier=({NonSeparatorInConfig}+)
ErrorInGoalIdentifier=({NonSeparatorInGoal}+)

Comment=("//"[^\r\n]*)
TradCommentBegin=("/*")
NonTermStars=([^\*\/]*[\*]+[^\*\/])
TermStars=([\*]+[\/])
CommentText=((([^\*]*[\/])|{NonTermStars})*)
CommentEnd=([^\*]*{TermStars})
TradComment=({TradCommentBegin}{CommentText}{CommentEnd})
OpenComment=({TradCommentBegin}{CommentText}([^\*]*)([\*]*))

DecimalNum=(([0]|{NonZeroDigit}{Digit}*))

%%
<YYINITIAL> "declare" { 
    nextState = DECLARATIONS;
    lastToken = ScollToken.RESERVED_WORD_DECLARE;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<YYINITIAL> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<YYINITIAL> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<YYINITIAL> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<YYINITIAL> {ErrorIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<YYINITIAL> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}



<DECLARATIONS> "state" { 
    nextState = STATEDECL;
    lastToken = ScollToken.RESERVED_WORD_STATE;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<DECLARATIONS> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<DECLARATIONS> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<DECLARATIONS> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<DECLARATIONS> {ErrorInDeclarationsIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<DECLARATIONS> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}



<STATEDECL> "/" { 
    lastToken = ScollToken.OPERATOR_DIVIDE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<STATEDECL> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<STATEDECL> "behavior" { 
    nextState = BEHDECL;
    lastToken = ScollToken.RESERVED_WORD_BEHAVIOR;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}

<STATEDECL> {InitLowIdentifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}

<STATEDECL> {DecimalNum} {
    /* At this point, the number we found could still be too large.
     * If it is too large, we need to return an error.
     * Scoll has methods built in that will decode from a string
     * and throw an exception the number is too large 
     */     
    String text = yytext();
    try { // no negative numbers supported
        Integer.decode(text);
        lastToken = ScollToken.LITERAL_INTEGER_DECIMAL;
    } catch (NumberFormatException e){
        lastToken = ScollToken.ERROR_INTEGER_DECIMIAL_SIZE;
    }
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<STATEDECL> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<STATEDECL> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<STATEDECL> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<STATEDECL> {ErrorInDeclarationsIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<STATEDECL> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}




<BEHDECL> "/" { 
    lastToken = ScollToken.OPERATOR_DIVIDE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> "knowledge" { 
    nextState = KDECL;
    lastToken = ScollToken.RESERVED_WORD_KNOWLEDGE;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<BEHDECL> {InitLowIdentifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<BEHDECL> {DecimalNum} {
    /* At this point, the number we found could still be too large.
     * If it is too large, we need to return an error.
     * Scoll has methods built in that will decode from a string
     * and throw an exception the number is too large 
     */     
    String text = yytext();
    try { // no negative numbers supported
        Integer.decode(text);
        lastToken = ScollToken.LITERAL_INTEGER_DECIMAL;
    } catch (NumberFormatException e){
        lastToken = ScollToken.ERROR_INTEGER_DECIMIAL_SIZE;
    }
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> {ErrorInDeclarationsIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHDECL> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}




<KDECL> "/" { 
    lastToken = ScollToken.OPERATOR_DIVIDE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<KDECL> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<KDECL> "system" { 
    nextState = SYSTEM;
    lastToken = ScollToken.RESERVED_WORD_SYSTEM;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}

<KDECL> {InitLowIdentifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}

<KDECL> {DecimalNum} {
    /* At this point, the number we found could still be too large.
     * If it is too large, we need to return an error.
     * Scoll has methods built in that will decode from a string
     * and throw an exception the number is too large 
     */     
    String text = yytext();
    try { // no negative numbers supported
        Integer.decode(text);
        lastToken = ScollToken.LITERAL_INTEGER_DECIMAL;
    } catch (NumberFormatException e){
        lastToken = ScollToken.ERROR_INTEGER_DECIMIAL_SIZE;
    }
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<KDECL> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<KDECL> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<KDECL> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<KDECL> {ErrorInDeclarationsIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<KDECL> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}



<SYSTEM> "(" { 
    nextState = SYSTEM_VARS_AND_CONST;
    lastToken = ScollToken.SEPARATOR_LPAREN;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
    }
<SYSTEM> ";" {
    lastToken = ScollToken.SEPARATOR_SEMICOLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM> "=>" { 
    lastToken = ScollToken.OPERATOR_IMPLIES;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM> "behavior" { 
    nextState = BEHAVIOR;
    lastToken = ScollToken.RESERVED_WORD_BEHAVIOR;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<SYSTEM> {Identifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<SYSTEM> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<SYSTEM> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM> {ErrorInSystemIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}


<SYSTEM_VARS_AND_CONST> ")" { 
    nextState = SYSTEM;
    lastToken = ScollToken.SEPARATOR_RPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    yybegin(nextState);
    return (t);
    }
<SYSTEM_VARS_AND_CONST> "_" { 
    lastToken = ScollToken.UNDERBAR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
    }
<SYSTEM_VARS_AND_CONST> "," {
    lastToken = ScollToken.SEPARATOR_COMMA;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM_VARS_AND_CONST> {Identifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<SYSTEM_VARS_AND_CONST> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM_VARS_AND_CONST> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM_VARS_AND_CONST> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM_VARS_AND_CONST> {ErrorInSystemVarsIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SYSTEM_VARS_AND_CONST> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}



<BEHAVIOR> "{" {
    nextState = BEHAVIOR_RULES;
    lastToken = ScollToken.SEPARATOR_LBRACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    yybegin(nextState);
    return (t);
}
<BEHAVIOR> "subject" { 
    nextState = SUBJECT;
    lastToken = ScollToken.RESERVED_WORD_SUBJECT;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<BEHAVIOR> {BehaviorTypeIdentifier} { 
    lastToken = ScollToken.BEHAVIOR_IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<BEHAVIOR> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR> {ErrorInBehaviorIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}



<BEHAVIOR_RULES> "(" { 
    nextState = BEH_RULES_VARS_AND_CONST;
    lastToken = ScollToken.SEPARATOR_LPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    yybegin(nextState);
    return (t);
    }
<BEHAVIOR_RULES> "}" {
    nextState = BEHAVIOR;
    lastToken = ScollToken.SEPARATOR_RBRACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    yybegin(nextState);
    return (t);
}
<BEHAVIOR_RULES> ";" {
    lastToken = ScollToken.SEPARATOR_SEMICOLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> "=>" { 
    lastToken = ScollToken.OPERATOR_IMPLIES;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> {Identifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<BEHAVIOR_RULES> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> {ErrorInBehaviorRulesIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEHAVIOR_RULES> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}



<BEH_RULES_VARS_AND_CONST> ")" {
    nextState = BEHAVIOR_RULES;
    lastToken = ScollToken.SEPARATOR_RPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    yybegin(nextState);
    return (t);
}
<BEH_RULES_VARS_AND_CONST> "_" { 
    lastToken = ScollToken.UNDERBAR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
    }
<BEH_RULES_VARS_AND_CONST> "," {
    lastToken = ScollToken.SEPARATOR_COMMA;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEH_RULES_VARS_AND_CONST> {Identifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<BEH_RULES_VARS_AND_CONST> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEH_RULES_VARS_AND_CONST> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEH_RULES_VARS_AND_CONST> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEH_RULES_VARS_AND_CONST> {ErrorInBehaviorVarsIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<BEH_RULES_VARS_AND_CONST> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}






<SUBJECT> "?" {
    lastToken = ScollToken.OPERATOR_QUESTION;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SUBJECT> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SUBJECT> "config" { 
    nextState = CONFIG;
    lastToken = ScollToken.RESERVED_WORD_CONFIG;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<SUBJECT> {BehaviorTypeIdentifier} { 
    lastToken = ScollToken.BEHAVIOR_IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<SUBJECT> {InitLowIdentifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<SUBJECT> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}

<SUBJECT> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SUBJECT> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SUBJECT> {ErrorInSubjectIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<SUBJECT> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}





<CONFIG> "(" { 
    lastToken = ScollToken.SEPARATOR_LPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
    }
<CONFIG> ")" {
    lastToken = ScollToken.SEPARATOR_RPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> "," {
    lastToken = ScollToken.SEPARATOR_COMMA;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> "?" {
    lastToken = ScollToken.OPERATOR_QUESTION;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> "goal" { 
    nextState = GOAL;
    lastToken = ScollToken.RESERVED_WORD_GOAL;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    yybegin(nextState);
    return (t);
}
<CONFIG> {InitLowIdentifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<CONFIG> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> {ErrorInConfigIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<CONFIG> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}








<GOAL> "(" { 
    lastToken = ScollToken.SEPARATOR_LPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
    }
<GOAL> ")" {
    lastToken = ScollToken.SEPARATOR_RPAREN;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> "," {
    lastToken = ScollToken.SEPARATOR_COMMA;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> "!" {
    lastToken = ScollToken.OPERATOR_LOGICAL_NOT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> ":" {
    lastToken = ScollToken.OPERATOR_COLON;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> {InitLowIdentifier} { 
    lastToken = ScollToken.IDENTIFIER;
    String text = yytext();
    ScollContextToken t = (new ScollContextToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState, this));
    return (t);
}
<GOAL> ({WhiteSpace}+) { 
    lastToken = ScollToken.WHITE_SPACE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> {Comment} { 
    lastToken = ScollToken.COMMENT_END_OF_LINE;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> {TradComment} {
    lastToken = ScollToken.COMMENT_TRADITIONAL;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> {ErrorInGoalIdentifier} { 
    lastToken = ScollToken.ERROR_IDENTIFIER;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}
<GOAL> {OpenComment} { 
    lastToken = ScollToken.ERROR_UNCLOSED_COMMENT;
    String text = yytext();
    ScollToken t = (new ScollToken(lastToken,text,yyline,yychar,yychar+text.length(),nextState));
    return (t);
}