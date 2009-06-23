package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

/**
 * When SuppressGA is enabled, GA's are not sent.
 * When SuppressGA is disabled, GA's are sent with every outgoing frame.
 */
public class SuppressGA extends Option {
	byte[] empty = new byte[0];
	byte[] cmdGA = new byte[] {TelnetClient.IAC, TelnetClient.GA};
	
	public SuppressGA() {
		super();
	}
	
	public String getName() {
		return "Suppress GA";
	}
	
	public byte getCode() {
		return (byte)3;
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend) {
		if (!isEnabled() && toSend.size() > 0) {
			return cmdGA;
		}
		return empty;
	}
}
