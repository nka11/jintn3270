package com.sf.jintn3270.tn3270.stream;

import com.sf.jintn3270.tn3270.TerminalModel3278;

public class ReadBuffer extends Command {
	public ReadBuffer() {
		super((short)0xf2);
	}
	
	protected int preform(TerminalModel3278 model, short[] b, int off, int len) {
		System.out.println("" + getClass().getName() + " not yet implemented.");
		return len;
	}
}
