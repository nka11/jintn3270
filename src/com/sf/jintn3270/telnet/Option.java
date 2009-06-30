package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

public abstract class Option {
	protected ByteArrayOutputStream out;
	
	private boolean enabled;
	
	protected byte[] nill;
	
	protected Option() {
		enabled = false;
		nill = new byte[0];
		out = new ByteArrayOutputStream();
	}
	
	
	public abstract String getName();
	
	
	public abstract byte getCode();
	
	
	public abstract void initiate(TelnetClient client);
	
	
	public int consumeIncomingBytes(byte[] incoming, TelnetClient client) {
		return 0;
	}
	
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		byte[] ret = out.toByteArray();
		out.reset();
		return ret;
	}
	
	
	public boolean isEnabled() {
		return enabled;
	}
	
	
	void setEnabled(boolean b, TelnetClient client) {
		enabled = b;
		if (enabled) {
			System.out.println("   ENABLED " + getName());
		} else {
			System.out.println("   DISABLED " + getName());
		}
	}
}
