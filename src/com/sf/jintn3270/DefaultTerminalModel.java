package com.sf.jintn3270;

import com.sf.jintn3270.event.TerminalEvent;

public class DefaultTerminalModel extends TerminalModel {
	TerminalCharacter[][] buffer;
	CursorPosition cur;
	
	
	public DefaultTerminalModel() {
		super(24, 80, new DefaultCharacterFactory());
	}
	
	public String[] getModelName() {
		return new String[] {"UNKNOWN"};
	}
	
	protected void initializeBuffer(int rows, int cols) {
		buffer = new TerminalCharacter[rows][cols];
		byte b = 0;
		for (int row = 0; row < buffer.length; row++) {
			for (int col = 0; col < buffer[row].length; col++) {
				buffer[row][col] = characterFactory().get(b);
			}
		}
		fire(new TerminalEvent(this, TerminalEvent.BUFFER_CHANGED));
	}
	
	protected void initializeCursor(int rows, int cols) {
		cur = new CursorPosition(rows, cols);
	}
	
	public CursorPosition cursor() {
		return cur;
	}
	
	public TerminalCharacter[][] buffer() {
		return buffer;
	}
}
