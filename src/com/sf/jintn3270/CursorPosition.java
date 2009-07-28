package com.sf.jintn3270;

/**
 * CursorPosition is closely tied to the buffer.
 */
public class CursorPosition implements Cloneable {
	int row;
	int column;
	
	private int maxRow;
	private int maxCol;
	
	public CursorPosition(int maxRow, int maxCol) {
		this.row = 0;
		this.column = 0;
		this.maxRow = maxRow;
		this.maxCol = maxCol;
	}
	
	// Copy Constructor
	CursorPosition(int row, int column, int maxRow, int maxCol) {
		this.row = row;
		this.column = column;
		this.maxRow = maxRow;
		this.maxCol = maxCol;
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
		return (row * maxCol) + column;
	}
	
	
	public void setPosition(int pos) {
		row = pos / maxCol;
		column = pos % maxCol;
	}
	
	
	public void left() {
		column--;
		if (column < 0) {
			column = maxCol - 1;
			up();
		}
	}
	
	
	public void up() {
		row--;
		if (row < 0) {
			row = maxRow - 1;
		}
	}
	
	
	public void right() {
		column++;
		if (column >= maxCol) {
			column = 0;
			down();
		}
	}
	
	
	public void down() {
		row++;
		if (row >= maxRow) {
			row = 0;
		}
	}
	
	/**
	 * Implements cloneable.
	 */
	public Object clone() {
		return new CursorPosition(row, column, maxCol, maxRow);
	}
}
