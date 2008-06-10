//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu


package eu.evoluware.swingscollar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.security.CodeSource;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class ScollAction extends AbstractAction {
	private static final int NONE=0, OPEN=1, SAVE=2, SAVEAS=3, FINDINSTALLDIR=4;
	private static ScollAction[] instances = new ScollAction[5];
	public static ScollAction none(ScollPanel mp){
		if (instances[NONE] == null) {
			instances[NONE] = new ScollAction("None", null, "Does nothing",
					null, NONE, mp);
		}
		return instances[NONE];
	}
	public static ScollAction open(ScollPanel mp){
		if (instances[OPEN] == null) {
			instances[OPEN] = new ScollAction("Open", null, "Opens a file dialog that let the user choose a pattern to load.",
					KeyEvent.VK_O, OPEN, mp);
		}
		return instances[OPEN];
	}
	public static ScollAction save(ScollPanel mp){
		if (instances[SAVE] == null) {
			instances[SAVE] = new ScollAction("Save", null, "Saves the curent pattern in the file that was last opened.",
					KeyEvent.VK_S, SAVE, mp);
		}
		return instances[SAVE];
	}
	public static ScollAction saveAs(ScollPanel mp){
		if (instances[SAVEAS] == null) {
			instances[SAVEAS] = new ScollAction("Save As", null, "Opens a file dialog that let the user choose where to save the pattern.",
					KeyEvent.VK_A, SAVEAS, mp);
		}
		return instances[SAVEAS];
	}
	public static ScollAction findInstallDir(ScollPanel mp){
		if (instances[FINDINSTALLDIR] == null) {
			instances[FINDINSTALLDIR] = new ScollAction("Set Install Directory", null, "Sets the installation directory.",
					null, FINDINSTALLDIR, mp);
		}
		return instances[FINDINSTALLDIR];
	}
	protected int code = 0;
	protected ScollPanel mp;
	private ScollAction(String text, Icon icon, String desc, Integer mnemonic, int code, ScollPanel mp)  {
		super(text, icon);
		this.code = code;
		this.mp = mp;
		putValue(SHORT_DESCRIPTION, desc);	
		putValue(MNEMONIC_KEY, mnemonic);
	}

	public void actionPerformed(ActionEvent e) {
		switch(this.code){
		case NONE: break;
		case OPEN: openPatternFile(mp); break;
		case SAVE: savePatternFile(mp); break;
		case SAVEAS: savePatternFileAs(mp); break;
		case FINDINSTALLDIR: lookForInstallDir(mp); break;
		default: break;
		}

	}

	public void openPatternFile(ScollPanel mp){
		JFileChooser fc = new JFileChooser(mp.getOpenedFilename());
		int returnVal = fc.showOpenDialog(mp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			mp.getPatternPanel().textPane.setText(getContents(file));
			this.setOpenedFilename(file);
		} else {
			;
		}
		//log.setCaretPosition(log.getDocument().getLength());
	}


	private void setOpenedFilename(File file){
		String openedFilename = "";
		try {	
			openedFilename =  file.getCanonicalPath();
		} catch (IOException e) {
			openedFilename = "";
		}
		if (! openedFilename.equals("")){	
			mp.setOpenedFilename(openedFilename);		
			mp.frame.setTitle(openedFilename);
		}
	}

	public void savePatternFileAs(ScollPanel mp){
		JFileChooser fc = new JFileChooser(mp.getOpenedFilename());
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showSaveDialog(mp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				if (file.createNewFile());
				if (this.setContents(file, mp.getPatternPanel().textPane.getText())){
					this.setOpenedFilename(file);		
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	public void savePatternFile(ScollPanel mp){
		if (mp.hasOpenedFilename()){
			File file = new File(mp.getOpenedFilename());
			if (this.setContents(file, mp.getPatternPanel().textPane.getText())){
				this.setOpenedFilename(file);
			}
		}
		else {
			this.savePatternFileAs(mp);
		}
	}

	public String getContents(File file) {
		StringBuffer contents = new StringBuffer();
		BufferedReader input = null;
		try {
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			input = new BufferedReader( new FileReader(file) );
			String line = null; //not declared within while loop
			/*
			 * readLine is a bit quirky :
			 * it returns the content of a line MINUS the newline.
			 * it returns null only for the END of the stream.
			 * it returns an empty String if two newlines appear in a row.
			 */
			while (( line = input.readLine()) != null){
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		finally {
			try {
				if (input!= null) {
					//flush and close both "input" and its underlying FileReader
					input.close();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return contents.toString();
	}

	public boolean setContents(File file, String str){
		Writer output = null;
		boolean success = true;
		try {
			if (file == null) {
//				throw new IllegalArgumentException("File should not be null.");
				success = false;
			}
			if (!file.exists()) {
//				throw new FileNotFoundException ("File does not exist: " + file);
				success =false;
			}
			if (!file.isFile()) {
//				throw new IllegalArgumentException("Should not be a directory: " + file);
				success = false;
			}
			if (!file.canWrite()) {
//				throw new IllegalArgumentException("File cannot be written: " + file);
				success = false;
			}
//			declared here only to make visible to finally clause; generic reference


//			use buffering
//			FileWriter always assumes default encoding is OK!
			if (success){
				output = new BufferedWriter( new FileWriter(file) );
				output.write( str );
			}
		}	
		catch (IOException ex){
			success = false;
			ex.printStackTrace();
		}
		finally {
//			flush and close both "output" and its underlying FileWriter
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return success;
	}

	public void lookForInstallDir(ScollPanel mp){
		try
		{
			String qualifiedClassName = "eu.evoluware.swingscoll.ScollPanel";
			Class<?> qc = Class.forName( qualifiedClassName );
			CodeSource source = qc.getProtectionDomain().getCodeSource();
			if ( source != null )
			{
				URL location = source.getLocation();
				mp.getPatternPanel().textPane.setText(location.getPath());;
			}
			else
			{
				mp.getPatternPanel().textPane.setText(qualifiedClassName + " : " + "unknown source, likely rt.jar");
			}
		}
		catch ( Exception e )
		{
			System.err.println( "Unable to locate class eu.evoluware.swingscoll.ScollPanel");
//			JFileChooser fc = new JFileChooser();
//			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//			int returnVal = fc.showOpenDialog(mp);
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//			File dir = fc.getSelectedFile();
//			mp.getPatternPanel().textPane.setText(dir.getName());
//			} else {
//			;
//			}
			//log.setCaretPosition(log.getDocument().getLength());
		}
	}
}
