/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */

package eu.evoluware.swingscollar;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class ScollStringCellRenderer extends JLabel
implements TableCellRenderer {
	Border unselectedBorder = null;
	Border selectedBorder = null;
	boolean isBordered = true;
	String[][] rows;

	public ScollStringCellRenderer(boolean isBordered, 	String[][] rows) {
		this.isBordered = isBordered;
		setOpaque(true); //MUST do this for background to show up.
		this.rows = rows;
	}

	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		String strval = (String)value;
		boolean inFirstCol =  (strval.contains(":")|| strval.contains("("));
		Color newColor = colorForValue(strval, inFirstCol);
		if (isSelected) newColor = new Color(0xCCCCFF); // pale blue
		setBackground(newColor);
		if (isBordered) {
			if (isSelected) {
				if (selectedBorder == null) {
					selectedBorder = BorderFactory.createMatteBorder(1,1,1,1,//2,5,2,5,
							table.getSelectionBackground());
				}
				setBorder(selectedBorder);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(1,1,1,1,//2,5,2,5,
							table.getBackground());
				}
				setBorder(unselectedBorder);
			}
		}
		if (inFirstCol) 
			setHorizontalAlignment(JLabel.LEFT);
		else 
			setHorizontalAlignment(JLabel.CENTER);
		setText(valueForValue(strval, inFirstCol));
		setTooltipForValue(strval, inFirstCol);
			return this;
		}

		private Color colorForValue(String str, boolean inFirstColumn){
			if (inFirstColumn) return Color.white;
			else	{
				if (str.startsWith("S")) return Color.red;
				if (str.startsWith("L")) return Color.green;
				return Color.white;
			}
		}

		private String valueForValue(String str, boolean inFirstColumn){
			if (inFirstColumn) return str;
			else	{
				if (str.startsWith("S")) return str.substring(1);
				if (str.startsWith("L")) return str.substring(1);
				if (str.equals("_")) return "";
				return str;
			}
		}

		private void setTooltipForValue(String str, boolean inFirstColumn){			
			if (!inFirstColumn){
				if (str.startsWith("S")) setToolTipText("safety goal");
				else if (str.startsWith("L")) setToolTipText("liveness goal");
			}
		}


	}

