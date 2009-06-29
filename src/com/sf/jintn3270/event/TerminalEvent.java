package com.sf.jintn3270.event;

import java.util.EventObject;

import com.sf.jintn3270.TerminalModel;

public class TerminalEvent extends EventObject {
	public static final int BUFFER_CHANGED = 0;
	public static final int BUFFER_UPDATE = 1;
	public static final int CURSOR_MOVED = 2;
	
	TerminalModel.CursorPosition start;
	TerminalModel.CursorPosition end;
	
	private int id;
	
	public TerminalEvent(Object source, int id) {
		this(source, id, null, null);
	}
	
	public TerminalEvent(Object source, int id, TerminalModel.CursorPosition start, TerminalModel.CursorPosition end) {
		super(source);
		this.id = id;
		this.start = start;
		this.end = end;
	}
	
	public int getId() {
		return id;
	}
	
	public TerminalModel.CursorPosition getStart() {
		return start;
	}
	
	public TerminalModel.CursorPosition getEnd() {
		return end;
	}
}
