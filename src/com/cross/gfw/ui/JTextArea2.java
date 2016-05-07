package com.cross.gfw.ui;

import javax.swing.JTextArea;

public class JTextArea2 extends JTextArea {

	public void append(String line) {

		if(null == mBuilder) {
			mBuilder = new StringBuilder();
		}
		
		mBuilder.append(line);
		mBuilder.append("\n");
		
		setText(mBuilder.toString());
		setCaretPosition(mBuilder.toString().length());
	}
	
	private StringBuilder mBuilder;

	private static final long serialVersionUID = 2426587974150869004L;
}
