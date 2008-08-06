//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

@SuppressWarnings("serial")
public class ScollPatternPanel extends ScollTabPanel {
//	protected static final String newline = "\n";
//	protected final SpringLayout patternSpringlayout;
//	protected final JLabel statusLabel;
//	protected ScollTextPane textPane;
//	protected ScollListener scollListener;
//	protected final JToolBar scollBtns;
//	protected final ScollPanel mainPanel;
//	protected final JScrollPane paneScrollPane; 
//	protected final StyledDocument doc;

	public ScollPatternPanel(ScollPanel mp) {
		super(mp);
		//this.setupDocument();//		
		final String[] ButtonStrings = {
				"check",
				"fixpts", 
				"sol1",
				"sols",
				"interrupt",
				//"test",
				//"addPanel",
				//"reset"
		};
		this.scollBtns.setBtns(ButtonStrings);
	}
	
//	private void setupDocument(){	
//		final String[] initString = {
//				"declare " + newline + "     state:", //bold
//				" access/2 " + newline, //regular
//				"    behavior: ", //bold
//				" may.send/3 may.accept/1 may.get/2 may.return/2 " + newline, //regular
//				"    knowledge: ", //bold
//				" did.send/3 did.accept/2 did.get/3 did.return/2"+newline, //regular
//				"system ", //bold
//				newline + "  access(A,B) access(A,X) A:may.send(B,X) B:may.accept() =>  " + newline
//				+ "     access(B,X) A:did.send(B,X) B:did.accept(X);" + newline
//				+ "  access(A,B) access(B,X) A:may.get(B) B:may.return(X) =>  " + newline
//				+ "     access(A,X) A:did.get(B,X) B:did.return(X);" + newline, //regular
//				"behavior ", //bold
//				newline+"  BULLY{=> may.send(B,X) may.accept() may.get(B) may.return(X);}"+newline 
//				+ "  TIMID{=> may.return(X);" + newline
//				+ "        did.return(X) => may.get(X);}" + newline,//regular
//				"subject ", //bold
//				newline + "  a:BULLY" + newline
//				 + "  b:TIMID" + newline 
//				 + "  c:TIMID" + newline, //regular
//				"config ", //bold
//				newline + "  access(a,b) access(b,c)" + newline, //regular
//				"goal ", //bold
//				newline + "  !access(c,a) " + newline, //regular       
//		};
//		final String[] initStyles = { "bold", "regular", "bold", "regular",
//				"bold", "regular", "bold", "regular", "bold", "regular",
//				"bold", "regular", "bold", "regular", "bold", "regular" };
//
//		textPane.setStyledDocument(doc);
//		addStylesToDocument(doc);
//
//		try {
//			for (int i = 0, l = initString.length; i < l; i++) {
//				doc.insertString(doc.getLength(), initString[i], doc
//						.getStyle(initStyles[i]));
//			}
//		} catch (final BadLocationException ble) {
//			System.err.println("Could not insert initial text into text pane.");
//		}
//		
//	}

//	private void addStylesToDocument(final StyledDocument doc) {
//		//Initialize the needed styles.
//		final Style def = StyleContext.getDefaultStyleContext().getStyle(
//				StyleContext.DEFAULT_STYLE);
//		final Style regular = doc.addStyle("regular", def);
//		StyleConstants.setFontFamily(def, "Courier");
//		final Style bold = doc.addStyle("bold", regular);
//		StyleConstants.setBold(bold, true);
//	}

}
