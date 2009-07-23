package com.sf.jintn3270.event;

import java.util.EventObject;

import com.sf.jintn3270.CursorPosition;
import com.sf.jintn3270.TerminalModel;

public class TerminalEvent extends EventObject {
	public static final int BUFFER_CHANGED = 0;
	public static final int BUFFER_UPDATE = 1;
	public static final int CURSOR_MOVED = 2;
	
	CursorPosition start;
	CursorPosition end;
	
	private int id;
	
	public TerminalEvent(Object source, int id) {
		this(source, id, null, null);
	}
	
	public TerminalEvent(Object source, int id, CursorPosition start, CursorPosition end) {
		super(source);
		this.id = id;
		this.start = start;
		this.end = end;
	}
	
	public int getId() {
		return id;
	}
	
	public CursorPosition getStart() {
		return start;
	}
	
	public CursorPosition getEnd() {
		return end;
	}
}
