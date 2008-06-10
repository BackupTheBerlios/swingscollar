//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
//import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class ScollSolutionsTableModel extends ScollTableModel {
	protected ScollTabPanel tabPanel;
	protected JTableHeader tableHeader;
	protected MouseListener mouseListener;

	public ScollSolutionsTableModel( String[][] r, String[] c, ScollTabPanel tabPanel) {
		super(r, c);
		this.tabPanel = tabPanel;
		this.mouseListener = new MouseHandler();
	}

	public JTableHeader getTableHeader() {
		return tableHeader;
	}


	public void setTableHeader(JTableHeader tableHeader) {
		if (this.tableHeader != null) {
			this.tableHeader.removeMouseListener(mouseListener);
//			TableCellRenderer defaultRenderer = this.tableHeader
//			.getDefaultRenderer();
			// if (defaultRenderer instanceof ClickableHeaderRenderer) {
			// this.tableHeader.setDefaultRenderer(((ClickableHeaderRenderer)
			// defaultRenderer).tableCellRenderer);
			// }
		}
		this.tableHeader = tableHeader;
		if (this.tableHeader != null) {
			this.tableHeader.addMouseListener(mouseListener);
			// this.tableHeader.setDefaultRenderer(
			// new
			// ClickableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
		}
	}

	// private classes

	private class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			JTableHeader h = (JTableHeader) e.getSource();
			TableColumnModel columnModel = h.getColumnModel();
			int viewColumn = columnModel.getColumnIndexAtX(e.getX());
			final int column = columnModel.getColumn(viewColumn).getModelIndex();
			if (column > 0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final ScollTabbedPane stp = tabPanel.getMainPanel().getTabbedPane();
						//stp.addDetailsFor(column);
						if (stp.getDetailsFor(column) == null){
							stp.addDetailsFor(column);
							ScollSolutionsPanel det = stp.getDetailsFor(column);
							ScollReply reply = det.getScollClient().replyTo("show "+ column + "\n");
							reply.render(det, true, false); // addButtons=false because detail should not contain buttons
							stp.setSelectedIndex(2);
						}
						else {
							stp.setSelectedComponent(stp.getDetailsFor(column));
						}
					}
				}) ;
				// int status = getSortingStatus(column);
				// if (!e.isControlDown()) {
				// cancelSorting();
				// }
				// // Cycle the sorting states through {NOT_SORTED, ASCENDING,
				// // DESCENDING} or
				// // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether
				// // shift is
				// // pressed.
				// status = status + (e.isShiftDown() ? -1 : 1);
				// status = (status + 4) % 3 - 1; // signed mod, returning {-1,
				// 0,
				// // 1}
				// setSortingStatus(column, status);
			}
		}
	}

	// private class ClickableHeaderRenderer implements TableCellRenderer {
	// private TableCellRenderer tableCellRenderer;
	//
	// public ClickableHeaderRenderer(TableCellRenderer tableCellRenderer) {
	// this.tableCellRenderer = tableCellRenderer;
	// }
	//
	// public Component getTableCellRendererComponent(JTable table,
	// Object value,
	// boolean isSelected,
	// boolean hasFocus,
	// int row,
	// int column) {
	// Component c = tableCellRenderer.getTableCellRendererComponent(table,
	// value, isSelected, hasFocus, row, column);
	// if (c instanceof JLabel) {
	// JLabel l = (JLabel) c;
	// l.setHorizontalTextPosition(JLabel.LEFT);
	// int modelColumn = table.convertColumnIndexToModel(column);
	// l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
	// }
	// return c;
	// }
	// }

}
