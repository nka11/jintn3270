package com.sf.jintn3270;

import java.util.LinkedList;

public class TnBuffer {
	int width;
	int height;
	
	LinkedList<TnField> fields;
	
	public TnBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		fields = new LinkedList<TnField>();
		clear();
	}
	
	public void clear() {
		TnField empty = new TnField(0);
		for (int i = 0; i < width * height; i++) {
			empty.addChar(new TnChar(' ', (byte)0x00));
		}
		fields.clear();
		fields.add(empty);
	}
}