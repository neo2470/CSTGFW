package com.cross.gfw.main;

import javax.swing.SwingUtilities;

import com.cross.gfw.entity.Config;
import com.cross.gfw.entity.Language;
import com.cross.gfw.entity.LanguageCN;
import com.cross.gfw.ui.MainWindow;

public class Entrance {
	
	public static void main(String[] args) {
		buildApplication();
	}
	
	// 構建應用程序
	private static void buildApplication() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				boolean firstUse = Config.init();
				
				// 語言
				Language lan = new LanguageCN();
				
				MainWindow mainWindow = new MainWindow(lan, firstUse);
				mainWindow.setVisible(true);
			}
		});
	}
}
