package com.sf.jintn3270.actions;

import com.sf.jintn3270.TerminalModel;

public class TerminalKeyTypedAction extends TerminalAction {
	char typeChar;
	
	public TerminalKeyTypedAction(char c) {
		super();
		typeChar = c;
	}
	
	public String getName() {
		return "" + typeChar;
	}
	
	public void doAction(TerminalModel model) throws Exception {
		model.type(typeChar);
	}
}
