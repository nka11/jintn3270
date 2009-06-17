package com.sf.jintn3270;

public class TnChar {
	private char display;
	private byte value;
	
	public TnChar(char display, byte value) {
		this.display = display;
		this.value = value;
	}
	
	public boolean isNumeric() {
		return display >= '0' && display <= '9';
	}
	
	public String toString() {
		return "" + display;
	}
}
