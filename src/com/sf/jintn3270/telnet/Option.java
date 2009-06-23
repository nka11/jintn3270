package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;


public abstract class Option {
	protected ByteArrayOutputStream out;
	
	private boolean enabled;
	
	protected Option() {
		enabled = false;
		out = new ByteArrayOutputStream();
	}
	
	
	public abstract String getName();
	
	
	public abstract byte getCode();
	
	
	
	public int consumeIncomingBytes(byte[] incoming) {
		return 0;
	}
	
	
	public byte[] outgoingBytes() {
		byte[] ret = out.toByteArray();
		out.reset();
		return ret;
	}
	
	
	public boolean isEnabled() {
		return enabled;
	}
	
	
	void setEnabled(boolean b) {
		enabled = b;
	}
}

