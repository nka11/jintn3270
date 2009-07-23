package com.sf.jintn3270.tn3270;

public class Viewport {
	int pid;
	
	int row;
	int col;
	
	int width;
	int height;
	
	Viewport(int pid, int col, int row, int width, int height) {
		this.pid = pid;
		
		this.row = row;
		this.col = col;
		this.width = width;
		this.height = height;
	}
	
	public Viewport(Partition p) {
		this(p.getPid(), p.getCols(), p.getRows(), p.getWindow().getWidth(), p.getWindow().getHeight());
	}
	
	public void setOrigin(int col, int row) {
		this.row = row;
		this.col = col;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}