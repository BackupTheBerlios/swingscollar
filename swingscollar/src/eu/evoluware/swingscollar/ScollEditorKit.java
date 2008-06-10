//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;

@SuppressWarnings("serial")
public class ScollEditorKit extends StyledEditorKit {

	public ScollEditorKit() {
		super();
	}
	
	public Document createDefaultDocument(){
		return new ScollDocument();
	}

}
