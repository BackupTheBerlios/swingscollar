/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */

package eu.evoluware.swingscollar;

public class ScollContextToken extends ScollToken {
	private ScollLexer lexer;
	public ScollContextToken(int ID, String contents, int lineNumber,
			int charBegin, int charEnd, ScollLexer lexer) {
		super(ID, contents, lineNumber, charBegin, charEnd);
		this.lexer = lexer;
		this.adaptContext();
	}

	public ScollContextToken(int ID, String contents, int lineNumber,
			int charBegin, int charEnd, int state, ScollLexer lexer) {
		super(ID, contents, lineNumber, charBegin, charEnd, state);
		this.lexer = lexer;
		this.adaptContext();
	}

	public boolean isBehaviorIdentifier(){
		return this.getID() == ScollToken.BEHAVIOR_IDENTIFIER;
	}

	public void adaptContext(){
		int myState = this.getState();
		int myID = this.getID();
		switch (myID) {	
		case ScollToken.RESERVED_WORD_PERMISSION:
			if (myState == ScollToken.PERMISSION_DECLARATIONS_STATE) lexer.clearPermDecl();
			break;
		case ScollToken.RESERVED_WORD_BEHAVIOR:
			if (myState == ScollToken.BEHAVIOR_DECLARATIONS_STATE) lexer.clearBehDecl();
			if (myState == ScollToken.BEHAVIOR_STATE) lexer.clearBehaviorTypeNames();
			break;
		case ScollToken.RESERVED_WORD_KNOWLEDGE:
			if (myState == ScollToken.KNOWLEDGE_DECLARATIONS_STATE) lexer.clearKDecl();
			break;
		case ScollToken.BEHAVIOR_IDENTIFIER:
			if (myState == ScollToken.BEHAVIOR_STATE) lexer.addBehaviorTypeName(this);
			// no break
		case ScollToken.IDENTIFIER:
			switch (myState) {
			case ScollToken.PERMISSION_DECLARATIONS_STATE:
				lexer.addPermDecl(this);
				break;
			case ScollToken.BEHAVIOR_DECLARATIONS_STATE:
				lexer.addBehDecl(this);
				break;
			case ScollToken.KNOWLEDGE_DECLARATIONS_STATE:
				lexer.addKDecl(this);
				break;
			default:
				break;
			}
		}
	}

	public String getDescription(){
		if (isIdentifier()){		
			if (isBehaviorIdentifier()) return lexer.getContextBehaviorIdentifierDescription(this);
			switch (getState()) {	
			case ScollToken.SYSTEM_STATE: 
			case ScollToken.BEHAVIOR_STATE:
			case ScollToken.BEHAVIOR_RULES_STATE:
			case ScollToken.SUBJECT_STATE:
			case ScollToken.CONFIG_STATE:
			case ScollToken.GOAL_STATE: return lexer.getContextIdentifierDescription(this);
			default: return super.getDescription();	
			}
		} else {
			return super.getDescription();
		} 
	}

}
