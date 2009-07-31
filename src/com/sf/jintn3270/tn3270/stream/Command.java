package com.sf.jintn3270.tn3270.stream;

import com.sf.jintn3270.tn3270.TerminalModel3278;

public abstract class Command {
	private short code;
	
	protected Command(short code) {
		this.code = code;
	}
	
	public short getCode() {
		return code;
	}
	
	public int execute(TerminalModel3278 model, short[] b, int off, int len) {
		int read = preform(model, b, off, len);
		model.complete(this);
		
		return read;
	}
	
	protected abstract int preform(TerminalModel3278 model, short[] b, int off, int len);
}