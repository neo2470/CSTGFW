package com.cross.gfw.entity;

public class Language {
	
	public String getMainWindowTitle() {
		return mainWindowTitle;
	}

	public String getBtnCheck2UpdateText() {
		return btnCheck2UpdateText;
	}
	
	public String getBtnUpdateText() {
		return btnUpdateText;
	}
	
	public String getBtnUpdatingText() {
		return btnUpdatingText;
	}

	protected String mainWindowTitle;// title of the window
	
	protected String btnCheck2UpdateText;// check to update btn text
	protected String btnUpdateText;// update btn text
	protected String btnUpdatingText;// updating btn text
}
