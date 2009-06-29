package com.sf.jintn3270.actions;

import com.sf.jintn3270.TerminalModel;

public class EnterTerminalAction extends TerminalAction {
	public EnterTerminalAction() {
		super();
	}
	
	public String getName() {
		return "<enter>";
	}
	
	public void doAction(TerminalModel model) throws Exception {
		model.getClient().send((byte)('\r'));
		model.getClient().send((byte)('\n'));
	}
}