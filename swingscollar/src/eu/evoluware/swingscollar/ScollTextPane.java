//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class ScollTextPane extends JTextPane {

	public ScollTextPane() {
		super();
	}

	public ScollTextPane(StyledDocument arg0) {
		super(arg0);
	}
	
	public boolean checkCaretPositions(int posA, int posB){
//		int pos1 = posA;	
//		int pos2 = posB;
//		if (pos1 > pos2) {
//			pos1 = posB;
//			pos2 = posA;
//		}
//		ScollDocument doc = (ScollDocument)(this.getDocument());
//		final AttributeSet StylePos1 = (AttributeSet) (doc.getCharacterElement(pos1).getAttributes());
//		final AttributeSet StylePos2 = (AttributeSet) (doc.getCharacterElement(pos2).getAttributes());
//		final AttributeSet StylePos1Back = (AttributeSet) (doc.getCharacterElement(pos1-1).getAttributes());
//		final AttributeSet StylePos2Back = (AttributeSet) (doc.getCharacterElement(pos2-1).getAttributes());
////		final AttributeSet StylePos1FW = (AttributeSet) (doc.getCharacterElement(pos1+1).getAttributes());
////		final AttributeSet StylePos2FW = (AttributeSet) (doc.getCharacterElement(pos2+1).getAttributes());
//		boolean bold1 = StylePos1.isDefined(StyleConstants.Bold); 
//		boolean bold1back = StylePos1Back.isDefined(StyleConstants.Bold);
////		boolean bold1fw = StylePos1FW.isDefined(StyleConstants.Bold);
//		boolean bold2 = StylePos2.isDefined(StyleConstants.Bold);
//		boolean bold2back = StylePos2Back.isDefined(StyleConstants.Bold);
////		boolean bold2fw = StylePos2FW.isDefined(StyleConstants.Bold);
//		if (bold1 || bold2){
//			if (this.isEditable()) this.setEditable(false);
//			return false;
//		}
//		else if(bold1back || bold2back){
//			if (this.isEditable()) this.setEditable(false);
//			return false;
//		}
//		else 	{
//			this.setEditable(true);
//			return true;
//		}
		return true;
	}
}
