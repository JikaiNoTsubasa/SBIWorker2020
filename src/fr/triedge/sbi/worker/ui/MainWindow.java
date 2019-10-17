package fr.triedge.sbi.worker.ui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.triedge.sbi.worker.ctrl.Controller;
import fr.triedge.sbi.worker.utils.Const;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 7682997401062409201L;

	private Controller controller;
	
	private JMenuBar bar;
	private JMenu menuFile, menuNote;
	private JMenuItem itemQuit, itemChangeWorkspace;
	
	public MainWindow(Controller controller) {
		super();
		this.controller = controller;
	}
	
	public void build() {
		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(new File(Const.ICONS_LOCATION+"/icon_bright.png")));
			setIconImage(icon.getImage());
		} catch (IOException e) {
		}
		setTitle("SBIWorker2020");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setBar(new JMenuBar());
		setMenuFile(new JMenu("File"));
		setMenuNote(new JMenu("Note"));
		setItemChangeWorkspace(new JMenuItem("Change Workspace"));
		setItemQuit(new JMenuItem("Exit"));
		
		getItemChangeWorkspace().addActionListener(e -> actionChangeWorkspace());
		
		getMenuFile().add(getItemChangeWorkspace());
		getMenuFile().add(getItemQuit());
		getBar().add(getMenuFile());
		getBar().add(getMenuNote());
		setJMenuBar(getBar());
		setVisible(true);
	}
	
	public void actionChangeWorkspace() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select new workspace folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	    	File file = chooser.getSelectedFile();
	    	getController().changeWorkspace(file);
	    }
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public JMenuBar getBar() {
		return bar;
	}

	public void setBar(JMenuBar bar) {
		this.bar = bar;
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public void setMenuFile(JMenu menuFile) {
		this.menuFile = menuFile;
	}

	public JMenu getMenuNote() {
		return menuNote;
	}

	public void setMenuNote(JMenu menuNote) {
		this.menuNote = menuNote;
	}

	public JMenuItem getItemQuit() {
		return itemQuit;
	}

	public void setItemQuit(JMenuItem itemQuit) {
		this.itemQuit = itemQuit;
	}

	public JMenuItem getItemChangeWorkspace() {
		return itemChangeWorkspace;
	}

	public void setItemChangeWorkspace(JMenuItem itemChangeWorkspace) {
		this.itemChangeWorkspace = itemChangeWorkspace;
	}
}
