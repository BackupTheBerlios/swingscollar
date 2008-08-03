//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

//import java.awt.MenuBar;
//import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

@SuppressWarnings("serial")
public class ScollPanel extends JPanel {


	protected final ScollClient scollClient;
	protected final ScollTabbedPane tabbedPane;
	protected final ScollPatternPanel patternPanel;
	protected final ScollFixptPanel fixptPanel;
	protected final ScollSolutionsPanel solutionsPanel;
	protected final SpringLayout springlayout;
	protected String openedFilename;
	protected boolean openFilename;
	protected JFrame frame;
	//protected final JMenuBar menubar;

	public ScollPanel(String[] args) {
		scollClient = new ScollClient(Integer.parseInt(args[0]));
		tabbedPane = new ScollTabbedPane(this);
		patternPanel = new ScollPatternPanel(this);
		fixptPanel = new ScollFixptPanel(this);
		solutionsPanel = new ScollSolutionsPanel(this);
		tabbedPane.addTab("Pattern", patternPanel);
		tabbedPane.addTab("Fixpoint", fixptPanel);
		tabbedPane.addTab("Solutions", solutionsPanel);
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		openedFilename = "";
		openFilename = false;
		//	menubar = new JMenuBar();


		this.add(tabbedPane);
		springlayout = new SpringLayout();
		springlayout.putConstraint(SpringLayout.WEST, tabbedPane, 5,
				SpringLayout.WEST, this);
		springlayout.putConstraint(SpringLayout.EAST, this, 5,
				SpringLayout.EAST, tabbedPane);
		springlayout.putConstraint(SpringLayout.NORTH, tabbedPane, 5,
				SpringLayout.NORTH, this);
		springlayout.putConstraint(SpringLayout.SOUTH, this, 5,
				SpringLayout.SOUTH, tabbedPane);
		this.setLayout(springlayout);
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI(final String[] args) {
		final JFrame frame = new JFrame("Scollar");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//testInstallations(frame);	
		ScollPanel.createAndShowScollarGui(frame, args);
	}	
		
	private static void createAndShowScollarGui(final JFrame frame, final String[] args){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//Add content to the window.
		ScollPanel sp = new ScollPanel(args);
		sp.setFrame(frame);
		frame.add(sp);

		//Add menus
		sp.addMenusTo(frame);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public void setFrame(JFrame frame){
		this.frame = frame;
	}
	public JFrame getFrame(){
		return this.frame;
	}
	public void addMenusTo(JFrame frame){
		JMenuBar menubar = new JMenuBar();
		JMenu menu; //, submenu;
		JMenuItem menuItem;
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Open and save SCOLL patterns");
		menubar.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem(ScollAction.open(this));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(menuItem);

		menuItem = new JMenuItem(ScollAction.save(this));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(menuItem);
		//menuItem.setEnabled(! this.hasOpenedFilename());

		menuItem = new JMenuItem(ScollAction.saveAs(this));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(menuItem);
		
		menuItem = new JMenuItem(ScollAction.findInstallDir(this));
		menu.add(menuItem);

		menubar.add(menu);
		frame.setJMenuBar(menubar);
	}

	public static void main(final String[] args) {
		//Set up communication with Scoll process 
		// communication pattern:
		// send request [read status update]* [send interrupt] read response 
		
		//Schedule a job for the event dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI(args);
			}
		});
	}

	public ScollPatternPanel getPatternPanel(){
		return patternPanel;
	}
	public ScollFixptPanel getFixptPanel(){
		return fixptPanel;
	}
	public ScollSolutionsPanel getSolutionsPanel(){
		return solutionsPanel;
	}
	public ScollSolutionsPanel getDetailPanel(int nr){
		return (ScollSolutionsPanel) getTabbedPane().getComponentAt(nr+2);
	}
	public ScollTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	public String getOpenedFilename(){
		return openedFilename;
	}
	public boolean hasOpenedFilename(){
		return openFilename;
	}
	public void setOpenedFilename(String fileName){
		openedFilename = fileName;
		openFilename = true;
		this.frame.setTitle(openedFilename);
		//menuBar.getMenu(0).setEnabled(true);
	}
}