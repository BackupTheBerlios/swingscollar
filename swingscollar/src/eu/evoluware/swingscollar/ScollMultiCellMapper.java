//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

public class ScollMultiCellMapper {
	String[][] rows;
	int width;
	public ScollMultiCellMapper(String[][] rows){
		this.rows = rows;
		width = 0;
		for (int i=0, n=rows.length; i<n; i++){
			if (width < rows[i].length) width = rows[i].length; 
		}
	}
	public int span(int row, int column)  {
		if ((column == 1) && !rows[row][0].contains("_")) return width - 1;
		return 1;
	}
	
	public int multiCellColumnAt(int row, int column)  {
		if ((column > 1) && !rows[row][0].contains("_")) return 1;
		return column;
	}
}
