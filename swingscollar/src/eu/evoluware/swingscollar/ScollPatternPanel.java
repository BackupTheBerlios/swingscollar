//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

@SuppressWarnings("serial")
public class ScollPatternPanel extends ScollTabPanel {
	public ScollPatternPanel(ScollPanel mp) {
		super(mp);	
		final String[] ButtonStrings = {
				"check",
				"fixpts", 
				"sol1",
				"sols",
				"interrupt"
		};
		this.scollBtns.setBtns(ButtonStrings);
	}
	
}
