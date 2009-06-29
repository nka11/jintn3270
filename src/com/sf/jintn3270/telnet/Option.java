package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

import com.sf.jintn3270.TerminalModel;

public abstract class Option {
	protected ByteArrayOutputStream out;
	
	private boolean enabled;
	
	protected Option() {
		enabled = false;
		out = new ByteArrayOutputStream();
	}
	
	
	public abstract String getName();
	
	
	public abstract byte getCode();
	
	
	
	public int consumeIncomingBytes(byte[] incoming, TerminalModel model) {
		return 0;
	}
	
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend) {
		byte[] ret = out.toByteArray();
		out.reset();
		return ret;
	}
	
	
	public boolean isEnabled() {
		return enabled;
	}
	
	
	void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			System.out.println("   ENABLED " + getName());
		} else {
			System.out.println("   DISABLED " + getName());
		}
	}
}
