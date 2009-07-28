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
	private int bufferAddress;
	
	private boolean waiting;
	private boolean systemLock;
	private boolean implicit;
	private boolean sixteenBitAddressing;
	
	TerminalCharacter[][] buffer;
	Window visibleContent;
	
	Viewport view;
	
	CursorPosition cur;
	
	/**
	 * Constructs the implicit partition
	 */
	Partition(TerminalModel3278 model) {
		this(0, model.getBufferHeight(), model.getBufferWidth(), model);
		implicit = true;
		sixteenBitAddressing = false;
	}
	
	/**
	 * Constructs a new partition with the given PID and number of rows / cols 
	 * in it's presentation space.
	 */
	public Partition(int pid, int rows, int cols, TerminalModel3278 model) {
		this.pid = pid;
		this.rows = rows;
		this.cols = cols;
		this.waiting = false;
		this.systemLock = false;
		
		cur = new CursorPosition(rows, cols);
		erase(model);
		
		visibleContent = new Window(0, 0, cols, rows);
		view = new Viewport(pid, 0, 0, cols, rows);
		implicit = false;
		sixteenBitAddressing = false;
	}
	
	
	/**
	 * Gets the PID of this Partition
	 */
	public int getPid() {
		return pid;
	}
	
	/**
	 * Returns weather or not this partition is implicit.
	 */
	public boolean isImplicit() {
		return implicit;
	}
	
	/**
	 * Sets the addressing mode from 12/14 bits to 16 bit.
	 */
	public void setSixteenBitAddressing(boolean b) {
		sixteenBitAddressing = b;
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
	 * Prints a character and increments the buffer address
	 */
	public void print(TerminalCharacter tc) {
		buffer[bufferAddress / cols][bufferAddress % cols] = tc;
		bufferAddress++;
	}
	
	/**
	 * Gets the current buffer address
	 */
	public int getBufferAddress() {
		return bufferAddress;
	}
	
	/**
	 * Sets the current buffer address.
	 */
	public void setBufferAddress(int ba) {
		this.bufferAddress = ba;
	}
	
	/**
	 * Decodes the given two bytes of data into a buffer address according
	 * to the addressing scheme in use by this partition
	 */
	public int decodeAddress(short s1, short s2) {
		int address = ((s1 << 8) | s2);
		
		if (sixteenBitAddressing) {
			return address;
		} else if ((address >> 14) == 0) { // 14 bit, uncoded mode!
			return address;
		} else if ((address >> 14) == 1) { // 12 bit coded mode!
			return decode12bitAddress(s1, s2);
		} else if ((address >> 14) == 3) { // 12 bit coded mode!
			return decode12bitAddress(s1, s2);
		}
		return -1;
	}
	
	private int decode12bitAddress(short s1, short s2) {
		// Remove the first two bits.
		s1 = (short)(s1 >> 6 << 6 ^ s1);
		s2 = (short)(s2 >> 6 << 6 ^ s2);
		return s1 << 6 | s2;
	}
	
	
	/**
	 * Erase the buffer of the Partitions Presentation space, using the 
	 * given TerminalModel as a source for a CharacterFactory.
	 */
	public void erase(TerminalModel3278 model) {
		buffer = new TerminalCharacter[rows][cols];
		for (int r = 0; r < buffer.length; r++) {
			for (int c = 0; c < buffer[r].length; c++) {
				buffer[r][c] = model.characterFactory().get((short)0);
			}
		}
		bufferAddress = 0;
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
