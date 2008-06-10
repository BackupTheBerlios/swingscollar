//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

@SuppressWarnings("serial")
public class ScollSolutionsTable extends ScollTable {

	public ScollSolutionsTable(ScollSolutionsTableModel model) {	
		super(model);
		model.setTableHeader(this.getTableHeader());
	}
}