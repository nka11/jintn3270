package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

/**
 * Implementation of RFC 858
 *
 * 
 * When SuppressGA is enabled, GA's are not sent.
 * When SuppressGA is disabled, GA's are sent with every outgoing frame.
 */
public class SuppressGA extends Option {
	short[] cmdGA = new short[] {TelnetClient.IAC, TelnetClient.GA};
	
	public SuppressGA() {
		super();
	}
	
	public String getName() {
		return "Suppress GA";
	}
	
	public short getCode() {
		return 3;
	}
	
	public void initiate(TelnetClient client) {
		client.sendWill(getCode());
	}
	
	public short[] outgoing(ByteArrayOutputStream queuedForSend, TelnetClient client) {
		if (!isEnabled() && queuedForSend.size() > 0) {
			return cmdGA;
		}
		return nill;
	}
}
