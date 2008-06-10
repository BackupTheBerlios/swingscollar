/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */


package eu.evoluware.swingscollar;


	/*
	 * This file is part of the programmer editor demo
	 * Copyright (C) 2005 Stephen Ostermiller
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

	import java.awt.Color;
	import java.util.HashMap;

	import javax.swing.text.AttributeSet;
	import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

	public class ScollTokenStyles {
		private ScollTokenStyles() { } // disable constructor
		
		/**
		 * A hash table containing the text styles. Simple attribute sets are hashed
		 * by name (String)
		 */
		private static HashMap<String, SimpleAttributeSet> styles = new HashMap<String, SimpleAttributeSet>();

		/**
		 * Create the styles and place them in the hash table.
		 */
		static {
			Color maroon = new Color(0xB03060).darker();
			Color darkBlue = new Color(0x000080);
			Color darkGreen = Color.GREEN.darker().darker();
			Color darkOrange = Color.ORANGE.darker();
			Color darkPurple = new Color(0xA020F0).darker();
			addStyle("text", Color.WHITE, Color.BLACK, true, false);
			addStyle("reservedWord", Color.WHITE, Color.BLACK, true, false);
			addStyle("identifier", Color.WHITE, Color.BLACK, false, false);
			addStyle("behaviorIdentifier", Color.WHITE, darkPurple, true, false);
			addStyle("literal", Color.WHITE, Color.BLACK, false, false);
			addStyle("separator", Color.WHITE, Color.BLACK, false, false);
			addStyle("operator", Color.WHITE, Color.BLACK, true, false);
			addStyle("comment", Color.WHITE, darkOrange, false, false);
			addStyle("whitespace", Color.WHITE, Color.BLACK, false, false);
			addStyle("error", Color.WHITE, Color.RED, false, false);
			addStyle("unknown", Color.WHITE, Color.ORANGE, false, false);
			addStyle("grayedOut", Color.WHITE, Color.GRAY, false, false);
			addStyle("permissionDeclaration", Color.WHITE, maroon, false, false);
			addStyle("behaviorDeclaration", Color.WHITE, darkGreen, false, false);
			addStyle("knowledgeDeclaration", Color.WHITE, darkBlue, false, false);
			addStyle("permissionPredicate", Color.WHITE, maroon, false, false);
			addStyle("behaviorPredicate", Color.WHITE, darkGreen, false, false);
			addStyle("knowledgePredicate", Color.WHITE, darkBlue, false, false);
			
		}
		private static void addStyle(String name, Color bg, Color fg, boolean bold, boolean italic ){
			addStyle(name,bg,fg,bold,italic,false);
		}
		
		private static void addStyle(String name, Color bg, Color fg,
				boolean bold, boolean italic, boolean ul) {
			SimpleAttributeSet style = new SimpleAttributeSet();
			StyleConstants.setFontFamily(style, "Monospaced");
			StyleConstants.setFontSize(style, 12);
			StyleConstants.setBackground(style, bg);
			StyleConstants.setForeground(style, fg);
			StyleConstants.setBold(style, bold);
			StyleConstants.setItalic(style, italic);
			StyleConstants.setUnderline(style, ul);
			styles.put(name, style);
		}
		
		

		/**
		 * Retrieve the style for the given type of token.
		 * 
		 * @param styleName
		 *            the label for the type of text ("tag" for example) or null if
		 *            the styleName is not known.
		 * @return the style
		 */
		public static AttributeSet getStyle(String styleName) {
			return (AttributeSet) styles.get(styleName);
		}
	}
