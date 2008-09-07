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
						if (stp.getDetailsFor(column) == null){
							stp.addDetailsFor(column);
							ScollPort.getInstance().sendCmd("show "+ column + "\n");
							ScollPort.getInstance().getNextReply();
						}
						else {
							stp.setSelectedComponent(stp.getDetailsFor(column));
						}
					}
				}) ;
			}
		}
	}
}
