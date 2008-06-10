//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.Point;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class ScollMultiCellTable extends ScollTable {
	public ScollMultiCellMapper map;
	public ScollMultiCellTable(String[][] rows, String[] cols) {
		super(rows, cols);	
		map=new ScollMultiCellMapper(rows);
		setUI(new ScollMultiCellTableUI());
	}
	public Rectangle getCellRect(int row, int column, boolean includeSpacing){
		// required because getCellRect is used in JTable constructor
		if (map==null) return super.getCellRect(row,column, includeSpacing);
		// add widths of all spanned logical cells
		int sk=map.multiCellColumnAt(row,column);
		Rectangle r1=super.getCellRect(row,sk,includeSpacing);
		if (map.span(row,sk)!=1)
			for (int i=1; i<map.span(row,sk); i++){
				r1.width+=getColumnModel().getColumn(sk+i).getWidth();
			}
		return r1;
	}
	public int columnAtPoint(Point p) {
		int x=super.columnAtPoint(p);
		// -1 is returned by columnAtPoint if the point is not in the table
		if (x<0) return x;
		int y=super.rowAtPoint(p);
		return map.multiCellColumnAt(y,x);
	}

}
