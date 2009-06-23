package com.sf.jintn3270.event;

import java.util.EventObject;

import com.sf.jintn3270.TerminalModel;

public class TerminalEvent extends EventObject {
	public static int BUFFER_CHANGED;
	public static int BUFFER_UPDATE;
	public static int CURSOR_MOVED;
	
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
