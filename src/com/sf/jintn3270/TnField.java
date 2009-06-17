package com.sf.jintn3270;

import java.util.ArrayList;

/**
 * TnField - Defines a Field in the buffer.
 */
public class TnField {
	private int start;
	
	private boolean protect;
	private int type;
	private int display;
	private boolean modified;
	
	private ArrayList<TnChar> characters;
	
	/* Field Type */
	public static int NUMERIC = 1;
	public static int ALPHA = 0;
	
	/* Display Type */
	public static int NORMAL = 0;
	public static int NORMAL_DETECTABLE = 1;
	public static int INTENSE_DETECTABLE = 2;
	public static int HIDDEN = 3;
	
	private TnField(int start, boolean protect, int type, int display, boolean modified) {
		this.start = start;
		
		this.protect = protect;
		this.type = type;
		this.display = display;
		this.modified = modified;
		
		characters = new ArrayList<TnChar>();
	}
	
	public TnField(int start) {
		this(start, false, ALPHA, NORMAL, false);
	}
	
	public void addChar(TnChar c) {
		characters.add(c);
	}
	
	public ArrayList<TnChar> getChars() {
		return characters;
	}
	
	public int length() {
		return characters.size();
	}
	
	
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return start + characters.size();
	}
	
	
	
	public boolean containsAddress(int offset) {
		// TODO: This could be off by one.
		return offset >= start && offset <= (start + characters.size());
	}
}
