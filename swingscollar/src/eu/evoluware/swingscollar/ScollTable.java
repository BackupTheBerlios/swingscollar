//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ScollTable extends JTable {
	protected TableCellRenderer headerRenderer;
	public ScollTable(String[][] rows, String[] cols){
		this(new ScollTableModel(rows,cols));
	}
	
	public ScollTable(ScollTableModel tableModel){
		super(tableModel);
		this.initColumnSizes(); 
		this.setGridColor(java.awt.Color.black);
		this.setDefaultRenderer(String.class, 
				new ScollStringCellRenderer(true, tableModel.getRows()));
	}
				
	private void initColumnSizes() {
		ScollTableModel model = (ScollTableModel)this.getModel();
		int colcount = model.getColumnCount();
		int rowcount = model.getRowCount();
		String[][] data = model.getRows();
		TableColumn column = null;	
		Component comp = null;	
		int headerWidth = 0;
		int cellWidth = 0;
		//Object[] longValues = model.longValues;
		headerRenderer = this.getTableHeader().getDefaultRenderer();
		String[] longValues = new String[colcount];
		for (int i = 0; i < colcount ; i++) {
			String str = "";
			int len = 0;
			for (int j = 0; j < rowcount; j++){
				if (data[j][i].length() > len){
					str = data[j][i];
					len = data[j][i].length();
				}	
			}
			longValues[i] = str;
		}

		for (int i = 0; i < colcount ; i++) {
			column = this.getColumnModel().getColumn(i);

			comp = headerRenderer.getTableCellRendererComponent(
					null, column.getHeaderValue(),
					false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			comp = this.getDefaultRenderer(model.getColumnClass(i)).
			getTableCellRendererComponent(
					this, longValues[i],
					false, false, 0, i);
			cellWidth = comp.getPreferredSize().width;

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
		}
	}
}
