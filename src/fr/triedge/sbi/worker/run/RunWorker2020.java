package fr.triedge.sbi.worker.run;

import fr.triedge.sbi.worker.ctrl.Controller;
import fr.triedge.sbi.worker.ui.MainWindow;

public class RunWorker2020 {

	public static void main(String[] args) {
		Controller c = new Controller();
		c.init();
		MainWindow w = new MainWindow(c);
		w.build();
	}

}
