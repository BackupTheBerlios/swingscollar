//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

//import java.awt.event.MouseListener;

import javax.swing.table.AbstractTableModel;
//import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class ScollTableModel extends AbstractTableModel {

	protected String[][] rows;
	protected String[] cols;    
//	private JTableHeader tableHeader;
//    private MouseListener mouseListener;
	public ScollTableModel(String[][] r, String[] c) {
		super();
		rows = r;
		cols = c;	
	}

	public int getColumnCount() {
		return cols.length ;
	}

	public int getRowCount() {
		return rows.length;
	}
	

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return String.class;	
	} 
	
	public String getColumnName(int column){
		return cols[column];
	}
	
	public boolean isCellEditable(){
		return false;
	}

	public Object getValueAt(int row, int col) {
		String val = rows[row][col];
//		if (col > 0 && (val.startsWith("S") || val.startsWith("L"))){
//			val = val.substring(1);
//		}
//		if (val.equals("_")) {
//			val = "";
//		}
		return val;
	}
	
	public String[][] getRows(){
		return rows;
	}
}

