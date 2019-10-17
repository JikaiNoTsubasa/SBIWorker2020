package fr.triedge.sbi.worker.ctrl;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import fr.triedge.fwk.conf.Config;
import fr.triedge.fwk.ui.UI;
import fr.triedge.sbi.worker.utils.Const;


public class Controller {

	public void init() {
		setUpLookAndFeel();
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
		} catch (IOException e) {
			UI.error("Cannot save config", e);
			e.printStackTrace();
		}
	}
}
