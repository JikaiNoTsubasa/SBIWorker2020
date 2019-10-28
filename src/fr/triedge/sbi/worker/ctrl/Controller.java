package fr.triedge.sbi.worker.ctrl;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import fr.triedge.fwk.conf.Config;
import fr.triedge.fwk.ui.UI;
import fr.triedge.sbi.worker.ui.MainWindow;
import fr.triedge.sbi.worker.utils.Const;


public class Controller {
	
	private MainWindow mainWindow;

	public void init() {
		//setUpLookAndFeel();
	}
	
	public void checkConfigDefaults() {
		setupConfigItem(Const.CONFIG_FRAME_FULLSCREEN, String.valueOf(getMainWindow().getState()));
		setupConfigItem(Const.CONFIG_FRAME_HEIGHT, String.valueOf(getMainWindow().getHeight()));
		setupConfigItem(Const.CONFIG_FRAME_WIDTH, String.valueOf(getMainWindow().getWidth()));
		setupConfigItem(Const.CONFIG_WORSPACE_LOCATION, "storage");
		setupConfigItem(Const.CONFIG_TEMPLATE_LOCATION, "templates");
		setupConfigItem(Const.CONFIG_REPO_LOCATION, "storage");
	}
	
	private void setupConfigItem(String key, String defValue) {
		String value = Config.params.getProperty(key, defValue);
		Config.params.setProperty(key, value);
	}
	
	public void setUpLookAndFeel() {
		UIManager.put("MenuItem.background", Const.COLOR_BACKGROUND);
		UIManager.put("MenuItem.foreground", Const.COLOR_FORGROUND);
		UIManager.put("Menu.background", Const.COLOR_BACKGROUND);
		UIManager.put("Menu.foreground", Const.COLOR_FORGROUND);
		UIManager.put("Menu.borderPainted", false);
		UIManager.put("MenuBar.background", Const.COLOR_BACKGROUND);
		UIManager.put("MenuBar.foreground", Const.COLOR_FORGROUND);
		UIManager.put("Button.background", Const.COLOR_BACKGROUND);
		UIManager.put("Button.foreground", Const.COLOR_FORGROUND);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
	}

	public void changeWorkspace(File file) {
		if (file == null)
			return;
		Config.params.put(Const.CONFIG_WORSPACE_LOCATION, file.getAbsolutePath());
		try {
			Config.save();
			UI.info("Workspace switched to: "+file.getAbsolutePath());
		} catch (IOException e) {
			UI.error("Cannot save config", e);
			e.printStackTrace();
		}
	}
	
	public void changeTemplateLocation(File file) {
		if (file == null)
			return;
		Config.params.put(Const.CONFIG_TEMPLATE_LOCATION, file.getAbsolutePath());
		try {
			Config.save();
			UI.info("Template location switched to: "+file.getAbsolutePath());
		} catch (IOException e) {
			UI.error("Cannot save config", e);
			e.printStackTrace();
		}
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void closeApplication() {
		try {
			checkConfigDefaults();
			Config.params.setProperty(Const.CONFIG_FRAME_FULLSCREEN, String.valueOf(getMainWindow().getState()));
			Config.params.setProperty(Const.CONFIG_FRAME_HEIGHT, String.valueOf(getMainWindow().getHeight()));
			Config.params.setProperty(Const.CONFIG_FRAME_WIDTH, String.valueOf(getMainWindow().getWidth()));
			Config.save();
		} catch (IOException e1) {
			UI.error("Cannot save config", e1);
			e1.printStackTrace();
		}
	}
}
