package fr.triedge.sbi.worker.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import fr.triedge.fwk.conf.Config;
import fr.triedge.fwk.ui.UI;
import fr.triedge.sbi.worker.ctrl.Controller;
import fr.triedge.sbi.worker.ctrl.JFileTab;
import fr.triedge.sbi.worker.utils.Const;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 7682997401062409201L;

	private Controller controller;
	
	private JMenuBar bar;
	private JMenu menuFile, menuNote;
	private JMenuItem itemQuit, itemChangeWorkspace, itemNewNote, itemOpenNote;
	
	private JTabbedPane tabbedPane;
	
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
		setTabbedPane(new JTabbedPane());
		
		setBar(new JMenuBar());
		setMenuFile(new JMenu("File"));
		setMenuNote(new JMenu("Note"));
		setItemChangeWorkspace(new JMenuItem("Change Workspace"));
		setItemQuit(new JMenuItem("Exit"));
		setItemNewNote(new JMenuItem("New Note"));
		setItemOpenNote(new JMenuItem("Open Note"));
		
		getItemChangeWorkspace().addActionListener(e -> actionChangeWorkspace());
		getItemNewNote().addActionListener(e -> actionNewNote());
		getItemOpenNote().addActionListener(e -> actionOpenNote());
		
		getMenuFile().add(getItemChangeWorkspace());
		getMenuFile().add(getItemQuit());
		getMenuNote().add(getItemNewNote());
		getMenuNote().add(getItemOpenNote());
		getBar().add(getMenuFile());
		getBar().add(getMenuNote());
		setJMenuBar(getBar());
		
		setContentPane(getTabbedPane());
		
		setVisible(true);
	}
	
	public void actionOpenNote() {
		JFileChooser chooser = new JFileChooser(new File(Config.params.getProperty(Const.CONFIG_WORSPACE_LOCATION, "storage")));
		chooser.setDialogTitle("Select note file");
		chooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Worker Note .wk";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".wk"))
					return true;
				return false;
			}
		});
		int res = chooser.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (selectedFile != null) {
				JFileTab tab = new JFileTab(selectedFile, getController());
				tab.build();
				getTabbedPane().addTab(selectedFile.getName(), tab);
			}
		}
	}
	
	public void actionNewNote() {
		String storage = Config.params.getProperty(Const.CONFIG_WORSPACE_LOCATION, "storage");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
		String txt = UI.askForTextInput("File name", "Name").replaceAll(" ", "_");
		String fileName = format.format(date)+"_"+txt+".wk";
		File file = new File(storage+File.separator+fileName);
		file.getParentFile().mkdirs();
		
		JFileTab tab = new JFileTab(file, getController());
		tab.build();
		getTabbedPane().addTab(fileName, tab);
	}
	
	public void setTitleTab(JFileTab tab, String name) {
		int idx = getTabbedPane().getSelectedIndex();
		getTabbedPane().setTitleAt(idx, name);
		/*
		int count = getTabbedPane().getTabCount();
		for (int i = 0; i < count; ++i) {
			JFileTab tmpTab = (JFileTab)getTabbedPane().getTabComponentAt(i);
			if (tmpTab == null)
				continue;
			if (tmpTab.equals(tab)) {
				getTabbedPane().setTitleAt(i, name);
				System.out.println("Index: "+i);
				return;
			}
		}
		System.out.println("Cannot find element");
		*/
	}
	
	public void actionChangeWorkspace() {
		JFileChooser chooser = new JFileChooser(Config.params.getProperty(Const.CONFIG_WORSPACE_LOCATION, "storage"));
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

	public JMenuItem getItemOpenNote() {
		return itemOpenNote;
	}

	public void setItemOpenNote(JMenuItem itemOpenNote) {
		this.itemOpenNote = itemOpenNote;
	}

	public JMenuItem getItemNewNote() {
		return itemNewNote;
	}

	public void setItemNewNote(JMenuItem itemNewNote) {
		this.itemNewNote = itemNewNote;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
}
