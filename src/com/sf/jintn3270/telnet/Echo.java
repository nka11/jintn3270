package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;


import com.sf.jintn3270.TerminalModel;

public class Echo extends Option {
	public static final byte ECHO = (byte)1; // Echo option ID.
	
	public Echo() {
		super();
	}
	
	public String getName() {
		return "Echo";
	}
	
	public byte getCode() {
		return ECHO;
	}
	
	public void initiate(TelnetClient client) {
		client.sendDo(getCode());
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		return nill;
	}
	
	public int consumeIncomingBytes(byte[] needToEcho, TelnetClient client) {
		return 0;
	}
	
	void setEnabled(boolean b, TelnetClient client) {
		super.setEnabled(b, client);
		client.getTerminalModel().setLocalEcho(!b);
	}
}
