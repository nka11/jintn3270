package com.sf.jintn3270;

/**
 * A TerminalModel is where telnet stream data goes to be rendered by a view.
 */
public abstract class TerminalModel {
	TerminalCharacter[][] buffer;
	
	CharacterFactory charFact;
	
	int caretRow;
	int caretCol;
	
	protected TerminalModel(int rows, int cols, CharacterFactory charFact) {
		this.caretRow = 0;
		this.caretCol = 0;
		this.charFact = charFact;
		initializeBuffer(rows, cols);
	}
	
	protected void initializeBuffer(int rows, int cols) {
		this.caretRow = 0;
		this.caretCol = 0;
		buffer = new TerminalCharacter[rows][cols];
		byte b = 0;
		for (int row = 0; row < buffer.length; row++) {
			for (int col = 0; col < buffer[row].length; col++) {
				buffer[row][col] = charFact.get(b);
			}
		}
		// TODO: Fire Buffer Changed
	}
	
	public void eraseChar() {
		caretCol--;
		
		if (caretCol < 0) {
			caretCol = buffer[0].length - 1;
			caretRow--;
		}
		if (caretRow < 0) {
			caretRow = buffer.length - 1;
		}
		
		buffer[caretRow][caretCol] = charFact.get((byte)0);
		// TODO: Fire Buffer Changed
	}
	
	public void eraseLine() {
		for (int col = 0; col < buffer[caretRow].length; col++) {
			buffer[caretRow][col] = charFact.get((byte)0);
		}
		caretCol = 0;
		// TODO: Fire Buffer Changed
	}
	
	
	public void setCursor(int row, int col) {
		caretRow = row;
		caretCol = col;
	}
	
	
	public void print(byte[] outBytes, int offset, int length) {
		TerminalCharacter ch;
		for (int pos = offset; pos < (offset + length); pos++) {
			ch = charFact.get(outBytes[pos]);
			boolean display = true;
			if (ch.getDisplay() == '\n') {
				caretRow++;
				display = false;
			}
			if (ch.getDisplay() == '\r') {
				caretCol = 0;
				display = false;
			}
			
			if (display) {
				buffer[caretRow][caretCol++] = ch;
			}
			
			// Handle carret boundry wrapping
			if (caretCol >= buffer[0].length) {
				caretCol = 0;
				caretRow++;
			}
			if (caretRow >= buffer.length) {
				caretRow = 0;
			}
		}
		// TODO: Fire buffer changed.
	}
	
	
}
