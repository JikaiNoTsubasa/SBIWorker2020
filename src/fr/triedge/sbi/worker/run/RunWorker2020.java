package fr.triedge.sbi.worker.run;

import java.io.IOException;

import fr.triedge.fwk.conf.Config;
import fr.triedge.sbi.worker.ctrl.Controller;
import fr.triedge.sbi.worker.ui.MainWindow;

public class RunWorker2020 {

	public static void main(String[] args) {
		try {
			Config.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Controller c = new Controller();
		c.init();
		MainWindow w = new MainWindow(c);
		c.setMainWindow(w);
		w.build();
		
	}

}
