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
	
	private boolean waiting;
	private boolean systemLock;
	
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
	
	/**
	 * Constructs a new partition with the given PID and number of rows / cols 
	 * in it's presentation space.
	 */
	public Partition(int pid, int rows, int cols) {
		this.pid = pid;
		this.rows = rows;
		this.cols = cols;
		this.waiting = false;
		this.systemLock = false;
		
		cur = new CursorPosition(rows, cols);
		
		buffer = new TerminalCharacter[rows][cols];
		visibleContent = new Window(0, 0, cols, rows);
		view = new Viewport(pid, 0, 0, cols, rows);
	}
	
	/**
	 * Gets the PID of this Partition
	 */
	public int getPid() {
		return pid;
	}
	
	/**
	 * Gets the number of rows in the presentation space of this partition
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Gets the number of columns in the presentation space of this partition
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * Is this Partition in a PWAIT?
	 */
	public boolean isWaiting() {
		return waiting;
	}
	
	/**
	 * Set the PWAIT state of the partition.
	 */
	void setWaiting(boolean b) {
		this.waiting = b;
	}
	
	/**
	 * Is the Partition in a System Lock state?
	 */
	public boolean isSystemLocked() {
		return systemLock;
	}
	
	/**
	 * Set the system lock state of the partition
	 */
	void setSystemLock(boolean b) {
		systemLock = b;
	}
	
	/**
	 * Gets the buffer of the Partitions Presentation space.
	 */
	public TerminalCharacter[][] getBuffer() {
		return buffer;
	}
	
	/**
	 * Gets the cursor location of this Partition
	 */
	public CursorPosition cursor() {
		return cur;
	}
	
	/**
	 * Gets the visible buffer (as viewed through the Window of the Partition 
	 * presentation space.
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
	
	/**
	 * Sets the Window object to be used when calculating visible portions of
	 * the presentation space.
	 */
	public void setWindow(Window w) {
		this.visibleContent = w;
	}
	
	/**
	 * Gets the window currently used to calculate and track the visible 
	 * portion of the presentation space.
	 */
	public Window getWindow() {
		return this.visibleContent;
	}
	
	/**
	 * Sets the Viewport of the Partition. This is the location of the final 
	 * display out where the contents of the Window will be displayed.
	 */
	public void setViewport(Viewport v) {
		this.view = v;
	}
	
	/**
	 * Gets the current Viewport associated with the Partition.
	 */
	public Viewport getViewport() {
		return this.view;
	}
}
