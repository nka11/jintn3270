package com.sf.jintn3270;

public abstract class TerminalCharacter {
	char display;
	byte code;
	
	protected TerminalCharacter(byte code) {
		this(code, (char)code);
	}
	
	protected TerminalCharacter(byte code, char display) {
		this.code = code;
		this.display = display;
	}
	
	public char getDisplay() {
		return display;
	}
	
	public byte getCode() {
		return code;
	}
	
	public boolean equals(Object obj) {
		TerminalCharacter that = (TerminalCharacter)obj;
		return this.display == that.display && this.code == that.code;
	}
}
