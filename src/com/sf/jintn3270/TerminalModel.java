package com.sf.jintn3270;

/**
 * A TerminalModel is where telnet stream data goes to be rendered by a view.
 * A model maintains a list of listeners, which are notified when changes to the 
 * buffer occur. It also tracks a CursorPosition, which is used when printing
 * data into the buffer.
 */
public abstract class TerminalModel {
	TerminalCharacter[][] buffer;
	
	CharacterFactory charFact;
	
	CursorPosition cursor;
	
	/**
	 * Constructs a TerminalModel with the given number of rows and cols. 
	 * The model will use the given CharacterFactory to render print() 
	 * invocations into the Buffer.
	 */
	protected TerminalModel(int rows, int cols, CharacterFactory charFact) {
		this.charFact = charFact;
		cursor = new CursorPosition();
		initializeBuffer(rows, cols);
	}
	
	/**
	 * Initializes the buffer by allocate a new buffer array, then filling
	 * the array with the character mapped to byte 0x00
	 */
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
	
	/**
	 * Erase the last character printed. This moved the cursor to the left, 
	 * THEN sets the character to 0x00.
	 * 
	 * This would be like a 'backspace'.
	 */
	public void eraseChar() {
		cursor.left();
		buffer[cursor.row()][cursor.column()] = charFact.get((byte)0);
		// TODO: Fire Buffer Changed
	}
	
	/**
	 * Erases the last line printed (Based on cursor position). This sets the 
	 * entire line to 0x00, then moves the cursor to column 0.
	 */
	public void eraseLine() {
		for (int col = 0; col < buffer[cursor.row()].length; col++) {
			buffer[cursor.row()][col] = charFact.get((byte)0);
		}
		cursor.moveTo(cursor.row(), 0);
		// TODO: Fire Buffer Changed
	}
	
	/**
	 * Gets the current CursorPosition used by this TerminalModel.
	 */
	public CursorPosition getCursor() {
		return cursor;
	}
	
	/**
	 * Method that sets the buffer data at the current cursor position, then
	 * advances the cursor position (by calling cursor.right()) one position.
	 */
	protected void print(TerminalCharacter ch) {
		boolean display = true;
		if (ch.getDisplay() == '\n') {
			cursor.down();
			display = false;
		}
		if (ch.getDisplay() == '\r') {
			cursor.moveTo(cursor.row(), 0);
			display = false;
		}
		
		if (display) {
			buffer[cursor.row()][cursor.column()] = ch;
			cursor.right();
		}
	}
	
	/**
	 * Prints the given byte at the current cursor location.
	 */
	public void print(byte b) {
		print(charFact.get(b));
		// TODO: Fire buffer changed.
	}
	
	/**
	 * Prints the given array of bytes, starting at offset, up to length, at 
	 * the current cursor location.
	 */
	public void print(byte[] bytes, int offset, int length) {
		for (int pos = offset; pos < (offset + length); pos++) {
			print(charFact.get(bytes[pos]));
		}
		// TODO: Fire buffer changed.
	}
	
	/**
	 * Prints the given array of bytes at the current cursor location.
	 */
	public void print(byte[] bytes) {
		print(bytes, 0, bytes.length);
		// TODO: Fire buffer changed.
	}
	
	
	/**
	 * CursorPosition is closely tied to the buffer.
	 */
	public class CursorPosition {
		private int row;
		private int column;
		
		public CursorPosition() {
			this.row = 0;
			this.column = 0;
		}
		
		public void moveTo(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
		public int row() {
			return row;
		}
		
		public int column() {
			return column;
		}
		
		/**
		 * Gets the position as if it were being used to index a linear buffer.
		 */
		public int getPosition() {
			return (row * buffer[0].length) + column;
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
