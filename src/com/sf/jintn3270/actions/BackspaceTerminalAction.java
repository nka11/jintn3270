package com.sf.jintn3270.actions;

import com.sf.jintn3270.TerminalModel;

public class BackspaceTerminalAction extends TerminalAction {
	public BackspaceTerminalAction() {
		super();
	}
	
	public String getName() {
		return "<backspace>";
	}
	
	public void doAction(TerminalModel model) throws Exception {
		model.eraseChar();
		model.getClient().sendCommand(model.getClient().EC);
	}
}