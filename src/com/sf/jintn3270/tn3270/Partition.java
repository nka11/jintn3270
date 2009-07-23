package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.CursorPosition;
import com.sf.jintn3270.TerminalCharacter;

/**
 * A 3270 Display Partition
 * 
 * Partitions are associated with a Window (which tracks the content visibleContent to
 * the target Viewport) and Viewport (which is used to display the Partition on
 * screen.
 */
public class Partition {
	private int pid;
	private int rows;
	private int cols;
	
	TerminalCharacter[][] buffer;
	Window visibleContent;
	
	Viewport view;
	
	CursorPosition cur;
	
	/**
	 * Constructs the implicit partition
	 */
	Partition(int rows, int cols) {
		this(0, rows, cols);
	}
	
	public Partition(int pid, int rows, int cols) {
		this.pid = pid;
		this.rows = rows;
		this.cols = cols;
		
		cur = new CursorPosition(rows, cols);
		
		buffer = new TerminalCharacter[rows][cols];
		visibleContent = new Window(0, 0, cols, rows);
		view = new Viewport(pid, 0, 0, cols, rows);
	}
	
	public int getPid() {
		return pid;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public TerminalCharacter[][] getBuffer() {
		return buffer;
	}
	
	public CursorPosition cursor() {
		return cur;
	}
	
	/**
	 * Apply the window bounds to the buffer and return the portion of the 
	 * buffer that is to be displayed by the viewport.
	 */
	public TerminalCharacter[][] getVisibleContentBuffer() {
		// If the window is the same size as us, then just return the buffer.
		if (getWindow().left() == 0 &&
		    getWindow().right() == cols &&
	         getWindow().top() == 0 && 
		    getWindow().bottom() == rows) 
		{
			return buffer;
		} else { // Otherwise, create a new buffer for display and copy the proper data to it.
			TerminalCharacter[][] vBuf = new TerminalCharacter[getWindow().getHeight()][getWindow().getWidth()];
			for (int r = getWindow().top(); r < getWindow().bottom(); r++) {
				System.arraycopy(buffer[r], getWindow().left(),
					            vBuf[r - getWindow().top()], 0, 
							  getWindow().getWidth());
			}
			return vBuf;
		}
	}
	
	public void setWindow(Window w) {
		this.visibleContent = w;
	}
	
	public Window getWindow() {
		return this.visibleContent;
	}
	
	public void setViewport(Viewport v) {
		this.view = v;
	}
	
	public Viewport getViewport() {
		return this.view;
	}
}
