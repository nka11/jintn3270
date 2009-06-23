package com.sf.jintn3270;

/**
 * A TerminalModel is where telnet stream data goes to be rendered by a view.
 * A model maintains a list of listeners, which are notified when changes to the 
 * buffer occur.
 */
public abstract class TerminalModel {
	TerminalCharacter[][] buffer;
	
	CharacterFactory charFact;
	
	CursorPosition cursor;
	
	protected TerminalModel(int rows, int cols, CharacterFactory charFact) {
		this.charFact = charFact;
		cursor = new CursorPosition();
		initializeBuffer(rows, cols);
	}
	
	protected void initializeBuffer(int rows, int cols) {
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
		cursor.left();
		buffer[cursor.row][cursor.column] = charFact.get((byte)0);
		// TODO: Fire Buffer Changed
	}
	
	public void eraseLine() {
		for (int col = 0; col < buffer[cursor.row].length; col++) {
			buffer[cursor.row][col] = charFact.get((byte)0);
		}
		cursor.moveTo(cursor.row, 0);
		// TODO: Fire Buffer Changed
	}
	
	
	public void print(byte[] bytes) {
		print(bytes, 0, bytes.length);
		// TODO: Fire buffer changed.
	}
	
	
	public void printChar(byte b) {
		print(b);
		// TODO: Fire buffer changed.
	}
	
	
	private void print(byte b) {
		TerminalCharacter ch = charFact.get(b);
		boolean display = true;
		if (ch.getDisplay() == '\n') {
			cursor.down();
			display = false;
		}
		if (ch.getDisplay() == '\r') {
			cursor.column = 0;
			display = false;
		}
		
		if (display) {
			buffer[cursor.row][cursor.column] = ch;
			cursor.right();
		}
	}
	
	
	public void print(byte[] bytes, int offset, int length) {
		for (int pos = offset; pos < (offset + length); pos++) {
			print(bytes[pos]);
		}
		// TODO: Fire buffer changed.
	}
	
	
	
	public class CursorPosition {
		int row;
		int column;
		
		public CursorPosition() {
			this.row = 0;
			this.column = 0;
		}
		
		public void moveTo(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getColumn() {
			return column;
		}
		
		
		public void left() {
			column--;
			if (column < 0) {
				column = buffer[0].length - 1;
				up();
			}
		}
		
		
		public void up() {
			row--;
			if (row < 0) {
				row = buffer.length - 1;
			}
		}
		
		
		public void right() {
			column++;
			if (column >= buffer[0].length) {
				column = 0;
				down();
			}
		}
		
		
		public void down() {
			row++;
			if (row > buffer.length) {
				row = 0;
			}
		}
	}
}
