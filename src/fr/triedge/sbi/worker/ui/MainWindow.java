package fr.triedge.sbi.worker.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import fr.triedge.fwk.conf.Config;
import fr.triedge.fwk.ui.UI;
import fr.triedge.sbi.worker.ctrl.Controller;
import fr.triedge.sbi.worker.ctrl.JFileTab;
import fr.triedge.sbi.worker.utils.Const;
import fr.triedge.sbi.worker.utils.Icons;
import fr.triedge.sbi.worker.utils.Utils;

public class MainWindow extends JFrame implements WindowListener{

	private static final long serialVersionUID = 7682997401062409201L;

	private Controller controller;

	private JMenuBar bar;
	private JMenu menuFile, menuNote, menuTemplate;
	private JMenuItem itemQuit, itemChangeWorkspace, itemChangeTemplateLocation, itemNewNote, itemOpenNote, itemSaveAsTemplate;

	private JTabbedPane tabbedPane;

	public MainWindow(Controller controller) {
		super();
		this.controller = controller;
		addWindowListener(this);
	}

	public void build() {
		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(new File(Const.ICONS_LOCATION+"/icon_bright.png")));
			setIconImage(icon.getImage());
		} catch (IOException e) {
		}
		setTitle("SBIWorker2020");
		setSize(
				Integer.valueOf(Config.params.getProperty(Const.CONFIG_FRAME_WIDTH, "800")),
				Integer.valueOf(Config.params.getProperty(Const.CONFIG_FRAME_HEIGHT, "600")));
		setExtendedState(Integer.valueOf(Config.params.getProperty(Const.CONFIG_FRAME_FULLSCREEN, "0")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTabbedPane(new JTabbedPane());

		setBar(new JMenuBar());
		setMenuFile(new JMenu("File"));
		setMenuNote(new JMenu("Note"));
		setMenuTemplate(new JMenu("Template"));
		setItemChangeWorkspace(new JMenuItem("Change Workspace"));
		setItemQuit(new JMenuItem("Exit"));
		setItemNewNote(new JMenuItem("New Note"));
		setItemOpenNote(new JMenuItem("Open Note"));
		setItemSaveAsTemplate(new JMenuItem("Save as Template"));
		setItemChangeTemplateLocation(new JMenuItem("Change Template Folder"));

		getItemChangeWorkspace().addActionListener(e -> actionChangeWorkspace());
		getItemQuit().addActionListener(e -> actionExit());
		getItemNewNote().addActionListener(e -> actionNewNote());
		getItemOpenNote().addActionListener(e -> actionOpenNote());
		getItemSaveAsTemplate().addActionListener(e -> actionSaveAsTemplate());
		getItemChangeTemplateLocation().addActionListener(e -> actionChangeTemplateLocation());

		getMenuFile().add(getItemChangeWorkspace());
		getMenuFile().add(getItemChangeTemplateLocation());
		getMenuFile().add(getItemQuit());
		getMenuNote().add(getItemNewNote());
		getMenuNote().add(getItemOpenNote());
		getMenuTemplate().add(getItemSaveAsTemplate());
		getBar().add(getMenuFile());
		getBar().add(getMenuNote());
		getBar().add(getMenuTemplate());
		setJMenuBar(getBar());

		setContentPane(getTabbedPane());

		setVisible(true);
	}

	public void actionSaveAsTemplate() {
		Component comp = getTabbedPane().getSelectedComponent();
		if (comp instanceof JFileTab) {
			JFileTab tab = (JFileTab)comp;
			tab.saveAsTemplate();
		}else {
			UI.warn("Current tab is not instance of JFileTab, cannot save template");
		}
	}

	public void actionExit() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
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
				addTab(tab, selectedFile.getName());
				// getTabbedPane().addTab(selectedFile.getName(), tab);
				// getTabbedPane().setSelectedComponent(tab);
			}
		}
	}

	public void addTab(JFileTab tab, String title) {
		// tab.build();
		getTabbedPane().addTab(title, tab);
		getTabbedPane().setSelectedComponent(tab);

		int index = getTabbedPane().indexOfTab(title);
		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(title);
		JButton btnClose = new JButton(Icons.closeIcon);
		btnClose.setPreferredSize(new Dimension(12, 12));
		btnClose.setBorderPainted(false); 
		btnClose.setContentAreaFilled(false); 
		btnClose.setFocusPainted(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;

		pnlTab.add(lblTitle, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClose, gbc);

		getTabbedPane().setTabComponentAt(index, pnlTab);

		btnClose.addActionListener(e -> actionCloseTab(tab));
	}

	public void actionCloseTab(JFileTab tab) {
		if (tab != null) {
			getTabbedPane().remove(tab);
		}
	}

	public void actionNewNote() {
		// Get storage folder
		String storageTemplate = Config.params.getProperty(Const.CONFIG_TEMPLATE_LOCATION, "templates");
		String storage = Config.params.getProperty(Const.CONFIG_WORSPACE_LOCATION, "storage");

		// List templates
		File storageFolder = new File(storageTemplate);
		File[] files = storageFolder.listFiles(new java.io.FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".tpl"))
					return true;
				return false;
			}
		});

		String[] templateList = new String[files.length + 1];
		templateList[0] = "None";
		for (int i = 1; i < files.length +1; ++i)
			templateList[i] = files[i-1].getName();
		JComboBox<String> comboTemplate = new JComboBox<>(templateList);
		JTextField textName = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Name"),
				textName,
				new JLabel("Template"),
				comboTemplate
		};
		int result = JOptionPane.showConfirmDialog(null, inputs, "Note Name", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String txt = textName.getText();
			String template = comboTemplate.getSelectedItem().toString();
			if (txt != null && !txt.isEmpty()) {
				txt = txt.replaceAll(" ", "_");
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
				String fileName = format.format(date)+"_"+txt+".wk";
				File file = new File(storage+File.separator+fileName);
				file.getParentFile().mkdirs();

				JFileTab tab = new JFileTab(file, getController());
				tab.build();
				if (!template.equals("None")) {
					String pathToTemplate = Config.params.getProperty(Const.CONFIG_TEMPLATE_LOCATION, "templates") + File.separator + template;
					File f = new File(pathToTemplate);
					if (f.exists()) {
						try {
							String text = Utils.readFile(f);
							if (text != null) {
								tab.getEditor().setText(text);
							}
						} catch (IOException e) {
							UI.error("Cannot read file", e);
							e.printStackTrace();
						}
					}else {
						UI.warn("Template could not be loaded, verify path: "+f.getAbsolutePath());
					}
				}
				addTab(tab, fileName);
				// getTabbedPane().addTab(fileName, tab);
				// getTabbedPane().setSelectedComponent(tab);
			}else {
				UI.error("No valid name entered!");
			}
		}


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

	public void actionChangeTemplateLocation() {
		JFileChooser chooser = new JFileChooser(Config.params.getProperty(Const.CONFIG_TEMPLATE_LOCATION, "templates"));
		chooser.setDialogTitle("Select new template folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			File file = chooser.getSelectedFile();
			getController().changeTemplateLocation(file);
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

	public JMenu getMenuTemplate() {
		return menuTemplate;
	}

	public void setMenuTemplate(JMenu menuTemplate) {
		this.menuTemplate = menuTemplate;
	}

	public JMenuItem getItemSaveAsTemplate() {
		return itemSaveAsTemplate;
	}

	public void setItemSaveAsTemplate(JMenuItem itemSaveAsTemplate) {
		this.itemSaveAsTemplate = itemSaveAsTemplate;
	}

	public JMenuItem getItemChangeTemplateLocation() {
		return itemChangeTemplateLocation;
	}

	public void setItemChangeTemplateLocation(JMenuItem itemChangeTemplateLocation) {
		this.itemChangeTemplateLocation = itemChangeTemplateLocation;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		getController().closeApplication();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
