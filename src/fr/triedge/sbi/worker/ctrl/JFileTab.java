package fr.triedge.sbi.worker.ctrl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.triedge.fwk.conf.Config;
import fr.triedge.fwk.ui.UI;
import fr.triedge.sbi.worker.utils.Const;
import fr.triedge.sbi.worker.utils.Icons;
import fr.triedge.sbi.worker.utils.Utils;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;


public class JFileTab extends JPanel implements KeyListener{

	private static final long serialVersionUID = -4904346072633347314L;

	private File file;
	private Controller controller;
	
	// https://www.javatips.net/api/EKit-for-SCIL-master/src/com/hexidec/ekit/Ekit.java
	//private Ekit editor;
	
	// https://github.com/OpenIndex/OpenIndex-SHEF
	// SHEF
	private HTMLEditorPane editor;
	private JButton btnSave, btnRenameFile;
	private JPanel toolPanel;
	private JLabel labStatus;

	public JFileTab(File file, Controller controller) {
		setFile(file);
		setController(controller);
	}
	
	public void build() {
		setEditor(new HTMLEditorPane());
		loadText();
		setLayout(new BorderLayout());
		setToolPanel(new JPanel(new FlowLayout(FlowLayout.LEFT)));
		
		setLabStatus(new JLabel(""));
		
		setBtnSave(new JButton(Icons.saveIcon));
		getBtnSave().setToolTipText("Save File");
		styleBtn(getBtnSave());
		
		setBtnRenameFile(new JButton(Icons.renameIcon));
		getBtnRenameFile().setToolTipText("Rename File");
		styleBtn(btnRenameFile);
		
		getToolPanel().add(getBtnSave());
		getToolPanel().add(getBtnRenameFile());
		getToolPanel().add(getLabStatus());
		getBtnSave().addActionListener(e -> saveText());
		getBtnRenameFile().addActionListener(e -> actionRenameFile());
		
		add(getToolPanel(), BorderLayout.NORTH);
		add(getEditor(), BorderLayout.CENTER);
	}
	
	private void styleBtn(JButton btn) {
		btn.setPreferredSize(new Dimension(16, 16));
		btn.setBorderPainted(false); 
		btn.setContentAreaFilled(false); 
		btn.setFocusPainted(false);
	}
	
	public void actionRenameFile() {
		String newName = UI.askForTextInput("Edit File Name", "Name", getFile().getName());
		if (newName == null || newName == "")
			return;
		File newFile = new File(getFile().getParentFile().getAbsolutePath()+File.separator+newName);
		boolean res = Utils.renameFile(getFile(), newFile);
		if (res) {
			setFile(newFile);
			getController().getMainWindow().setTitleTab(this, newName);
			UI.info("File renamed");
		}
		else
			UI.error("Failed to rename file");
	}
	
	public void updateStatus(String status) {
		getLabStatus().setText(status);
		Tempo tmp = new Tempo();
		Thread th = new Thread(tmp);
		th.start();
	}
	
	private void loadText() {
		if (file == null)
			return;
		if (file.exists()) {
			try {
				String text = Utils.readFile(file);
				if (text != null)
					getEditor().setText(text);
			} catch (IOException e) {
				UI.error("Cannot read file", e);
				e.printStackTrace();
			}
		}
	}
	
	private void saveText() {
		String text = getEditor().getText();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(getFile().getAbsoluteFile()));
			writer.write(text);
			writer.close();
			updateStatus("Saved to:" +getFile().getAbsoluteFile());
		} catch (IOException e) {
			UI.error("Cannot save file", e);
			e.printStackTrace();
		}
	}
	
	public void saveAsTemplate() {
		String tplLoc = Config.params.getProperty(Const.CONFIG_TEMPLATE_LOCATION, "templates");
		String newName = UI.askForTextInput("Template Name", "Name", "template1.tpl");
		String pathToSave = tplLoc + File.separator+ newName;
		if (!pathToSave.endsWith(".tpl"))
			pathToSave = pathToSave+".tpl";
		File f = new File(pathToSave);
		String text = getEditor().getText();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
			writer.write(text);
			writer.close();
			updateStatus("Saved to:" +f.getAbsoluteFile());
		} catch (IOException e) {
			UI.error("Cannot save file", e);
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public HTMLEditorPane getEditor() {
		return editor;
	}

	public void setEditor(HTMLEditorPane editor) {
		this.editor = editor;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(JButton btnSave) {
		this.btnSave = btnSave;
	}

	public JPanel getToolPanel() {
		return toolPanel;
	}

	public void setToolPanel(JPanel toolPanel) {
		this.toolPanel = toolPanel;
	}

	public JLabel getLabStatus() {
		return labStatus;
	}

	public void setLabStatus(JLabel labStatus) {
		this.labStatus = labStatus;
	}

	public JButton getBtnRenameFile() {
		return btnRenameFile;
	}

	public void setBtnRenameFile(JButton btnRenameFile) {
		this.btnRenameFile = btnRenameFile;
	}

	private class Tempo implements Runnable{

		protected int tempo = 3000;
		
		@Override
		public void run() {
			try {
				Thread.sleep(tempo);
				getLabStatus().setText("");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Pressed");
		if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            saveText();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
