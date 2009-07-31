package com.sf.jintn3270.tn3270.stream;

import com.sf.jintn3270.tn3270.TerminalModel3278;

public class ReadModifiedAll extends Command {
	public ReadModifiedAll() {
		super((short)0x6e);
	}
	
	protected int preform(TerminalModel3278 model, short[] b, int off, int len) {
		System.out.println("" + getClass().getName() + " not yet implemented.");
		return len;
	}
}
