package com.sf.jintn3270;

public abstract class TerminalCharacter {
	protected char display;
	protected short code;
	
	protected TerminalCharacter(short code) {
		this(code, (char)code);
	}
	
	protected TerminalCharacter(short code, char display) {
		this.code = code;
		this.display = display;
	}
	
	public char getDisplay() {
		return display;
	}
	
	public short getCode() {
		return code;
	}
	
	public boolean equals(Object obj) {
		TerminalCharacter that = (TerminalCharacter)obj;
		return this.display == that.display && this.code == that.code;
	}
}
