package com.sf.jintn3270;

public class DefaultTerminalModel extends TerminalModel {
	public DefaultTerminalModel() {
		super(24, 80, new DefaultCharacterFactory());
	}
	
	public String[] getModelName() {
		return new String[] {"UNKNOWN"};
	}
}
