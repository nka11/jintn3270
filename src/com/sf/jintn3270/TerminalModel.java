package com.sf.jintn3270;

import javax.swing.event.EventListenerList;

import com.sf.jintn3270.event.TerminalEvent;
import com.sf.jintn3270.event.TerminalEventListener;

import com.sf.jintn3270.telnet.TelnetClient;
import com.sf.jintn3270.telnet.Option;

/**
 * A TerminalModel is where telnet stream data goes to be rendered by a view.
 * A model maintains a list of listeners, which are notified when changes to the 
 * buffer occur. It also tracks a CursorPosition, which is used when printing
 * data into the buffer.
 */
public abstract class TerminalModel {
	TerminalCharacter[][] buffer;
	
	CharacterFactory charFact;
	
	CursorPosition cur;
	
	TelnetClient client;
	
	boolean localEcho;
	
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Constructs a TerminalModel with the given number of rows and cols. 
	 * The model will use the given CharacterFactory to render print() 
	 * invocations into the Buffer.
	 */
	protected TerminalModel(int rows, int cols, CharacterFactory charFact) {
		this.charFact = charFact;
		this.cur = new CursorPosition(rows, cols);
		this.client = null;
		this.localEcho = false;
		initializeBuffer(rows, cols);
	}
	
	/**
	 * Returns the model name of the Terminal. This is used by the RFC 884, 
	 * TerminalType option to report the terminal type to the remote host.
	 */
	public abstract String[] getModelName();
	
	/**
	 * Returns a list of required Telnet Option implmentations for this 
	 * TerminalModel to function as expected.
	 */
	public Option[] getRequiredOptions() {
		return new Option[0];
	}
	
	/**
	 * Initializes the buffer by allocate a new buffer array, then filling
	 * the array with the character mapped to byte 0x00
	 */
	protected void initializeBuffer(int rows, int cols) {
		buffer = new TerminalCharacter[rows][cols];
		cursor().row = 0;
		cursor().column = 0;
		byte b = 0;
		for (int row = 0; row < buffer.length; row++) {
			for (int col = 0; col < buffer[row].length; col++) {
				buffer[row][col] = charFact.get(b);
			}
		}
		fire(new TerminalEvent(this, TerminalEvent.BUFFER_CHANGED));
	}
	
	
	public void setClient(TelnetClient client) {
		this.client = client;
	}
	
	
	public TelnetClient getClient() {
		return this.client;
	}
	
	
	public boolean isConnected() {
		return this.client != null;
	}
	
	
	/**
	 * Enables / Disables local echoing.
	 */
	public void setLocalEcho(boolean localEcho) {
		this.localEcho = localEcho;
	}
	
	
	/**
	 * Adds the given Listener
	 */
	public void addTerminalEventListener(TerminalEventListener listener) {
		listenerList.add(TerminalEventListener.class, listener);
	}
	
	/**
	 * Removes the given Listener
	 */
	public void removeTerminalEventListener(TerminalEventListener listener) {
		listenerList.remove(TerminalEventListener.class, listener);
	}
	
	/**
	 * Type a character into the local buffer, and send it to the TelnetClient
	 * for transmission
	 */
	public void type(char c) {
		CursorPosition before = (CursorPosition)cursor().clone();
		
		if (localEcho) {
			buffer[cursor().row()][cursor().column()] = charFact.get(c);
			cursor().right();
		}
		if (isConnected()) {
			client.send(charFact.get(c).getCode());
		}
		
		fire(TerminalEvent.BUFFER_CHANGED, (CursorPosition)cursor().clone(), before);
	}
	
	/**
	 * Erase the last character printed. This moved the cursor to the left, 
	 * THEN sets the character to 0x00.
	 * 
	 * This would be like a 'backspace'.
	 */
	public void eraseChar() {
		CursorPosition before = (CursorPosition)cursor().clone();
		
		cursor().left();
		buffer[cursor().row()][cursor().column()] = charFact.get((byte)0);
		
		fire(TerminalEvent.BUFFER_UPDATE, (CursorPosition)cursor().clone(), before);
	}
	
	/**
	 * Erases the last line printed (Based on cursor position). This sets the 
	 * entire line to 0x00, then moves the cursor to column 0.
	 */
	public void eraseLine() {
		CursorPosition before = (CursorPosition)cursor().clone();
		before.column = buffer[0].length - 1;
		
		for (int col = 0; col < buffer[cursor().row()].length; col++) {
			buffer[cursor().row()][col] = charFact.get((byte)0);
		}
		cursor().column = 0;
		fire(TerminalEvent.BUFFER_UPDATE, (CursorPosition)cursor().clone(), before);
	}
	
	
	/**
	 * Obtains the CursorPosition object used by this TerminalModel.
	 * 
	 * Using this object, you can modify the cursor location.
	 *
	 * @return The CursorPosition used by this terminal.
	 */
	public CursorPosition cursor() {
		return cur;
	}
	
	
	/**
	 * Moves the current Cursor to the given position
	 */
	public void moveCursorTo(int row, int column) {
		CursorPosition before = (CursorPosition)cursor().clone();
		
		cursor().row = row;
		cursor().column = column;
		fire(TerminalEvent.CURSOR_MOVED, before, cursor());
	}
	
	
	/**
	 * Method that sets the buffer data at the current cursor position, then
	 * advances the cursor position (by calling cursor.right()) one position.
	 */
	protected void print(TerminalCharacter ch) {
		boolean display = true;
		if (ch.getDisplay() == '\n') {
			cursor().down();
			display = false;
		}
		if (ch.getDisplay() == '\r') {
			cursor().column = 0;
			display = false;
		}
		
		if (display) {
			buffer[cursor().row()][cursor().column()] = ch;
			cursor().right();
		}
	}
	
	/**
	 * Prints the given byte at the current cursor location.
	 */
	public void print(short b) {
		CursorPosition before = (CursorPosition)cursor().clone();
		
		print(charFact.get(b));
		fire(TerminalEvent.BUFFER_UPDATE, before, cursor());
	}
	
	/**
	 * Prints the given array of bytes, starting at offset, up to length, at 
	 * the current cursor location.
	 */
	public void print(short[] bytes, int offset, int length) {
		CursorPosition before = (CursorPosition)cursor().clone();
		
		for (int pos = offset; pos < (offset + length); pos++) {
			print(charFact.get(bytes[pos]));
		}
		fire(TerminalEvent.BUFFER_UPDATE, before, cursor());
	}
	
	/**
	 * Prints the given array of bytes at the current cursor location.
	 */
	public void print(short[] bytes) {
		print(bytes, 0, bytes.length);
	}
	
	
	/**
	 * Fire the given TerminalEvent to all registered listeners
	 */
	protected void fire(TerminalEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TerminalEventListener.class) {
				((TerminalEventListener)listeners[i + 1]).terminalChanged(evt);
			}
		}
	}
	
	
	/**
	 * Create and fire a new TerminalEvent to all registered listeners
	 */
	protected void fire(int id, CursorPosition start, CursorPosition end) {
		this.fire(new TerminalEvent(this, id, start, end));
	}
	
	
	/**
	 * Returns the height (number of rows) in the buffer
	 */
	public int getBufferHeight() {
		return buffer.length;
	}
	
	
	/**
	 * Returns the width (number of columns) in the buffer
	 */
	public int getBufferWidth() {
		return buffer[0].length;
	}
	
	
	/**
	 * Gets the given char at the given location in the buffer.
	 */
	public TerminalCharacter getChar(int row, int col) {
		return buffer[row][col];
	}
}
