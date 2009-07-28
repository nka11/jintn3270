package com.sf.jintn3270.tn3270.stream;

public class WriteControlCharacter {
	short c;
	
	private static final int RESET = 2;
	private static final int PRINTER_0 = 4;
	private static final int PRINTER_1 = 8;
	private static final int PRINTER_START = 16;
	private static final int SOUND_ALARM = 32;
	private static final int KEYBOARD_RESTORE = 64;
	private static final int RESET_MODIFIED_DATA = 128;
	
	public WriteControlCharacter(short c) {
		this.c = c;
	}
	
	public boolean reset() {
		return (c & RESET) > 0;
	}
	
	public boolean restoreKeyboard() {
		return (c & KEYBOARD_RESTORE) > 0;
	}
}