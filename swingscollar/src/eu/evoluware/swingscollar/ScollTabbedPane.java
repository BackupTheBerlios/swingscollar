//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;


import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ScollTabbedPane extends JTabbedPane {
	ScollPanel mp;
	public ScollTabbedPane(ScollPanel mainPanel){
		super(JTabbedPane.TOP);
		mp = mainPanel;
	}

	public void addDetailsFor(int solutionNr){
		String title = Integer.toString(solutionNr);
		boolean done = false;
		int max = this.getComponentCount() - 1;
		for(int i = 3 ; i <= max; i++){
			int currentSolNr = Integer.parseInt(this.getTitleAt(i));
			if (currentSolNr == solutionNr){
				this.setSelectedIndex(i);
				done = true;
				break;
			}
			else if (currentSolNr > solutionNr){
				this.insertTab(title, null, new ScollSolutionsPanel(mp), null, i);
				this.setSelectedIndex(i);
				done = true;
				break;}
		}
		if (!done) {
			this.add(title,new ScollSolutionsPanel(mp));
			this.setSelectedIndex(max+1);
		}
	}

	public void removeAllDetails(){
		int max = this.getComponentCount() - 1;
		for (int i = 3; i <= max; i++){
			remove(3);
		}
	}

	public ScollSolutionsPanel getDetailsFor(int solutionNr){	
		int max = this.getComponentCount() - 1;
		for(int i = 3 ; i <= max; i++){
			int currentSolNr = Integer.parseInt(this.getTitleAt(i));
			if (currentSolNr == solutionNr){
				return (ScollSolutionsPanel) this.getComponentAt(i);
			}
		}
		return null;
	}
	

}
