package com.sf.jintn3270.actions;

import com.sf.jintn3270.TerminalModel;

import java.awt.event.ActionEvent;

public abstract class TerminalAction {
	TerminalModel model;
	
	protected TerminalAction() {
	}
	
	public void setTerminalModel(TerminalModel tm) {
		this.model = tm;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (model != null) {
			try {
				doAction(model);
			} catch (Exception ex) {ex.printStackTrace();}
		}
	}
	
	public abstract String getName();
	
	public abstract void doAction(TerminalModel model) throws Exception;
}