package com.sf.jintn3270.tn3270;

/**
 * Each Partition has a Window. The window defines the boundary of the 
 * presentation space to be displayed in the partitions associated Viewport.
 */
public class Window {
	private int row;
	private int col;
	
	private int height;
	private int width;
	
	
	public Window(int col, int row, int width, int height) {
		this.row = row;
		this.col = col;
		this.height = height;
		this.width = width;
	}
	
	public void scroll(int upDown, int leftRight) {
		row += upDown;
		col += leftRight;
	}
	
	public int top() {
		return row;
	}
	
	public int left() {
		return col;
	}
	
	public int bottom() {
		return row + height;
	}
	
	public int right() {
		return col + width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
}
