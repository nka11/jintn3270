package com.sf.jintn3270.tn3278;

import com.sf.jintn3270.telnet.Option;
import com.sf.jintn3270.telnet.TelnetClient;

import java.io.ByteArrayOutputStream;

public class Tn3270e extends Option {
	public static final byte TN3270E = (byte)40;
	
	public Tn3270e() {
		super();
	}
	
	public String getName() {
		return "Tn3270e";
	}
	
	public byte getCode() {
		return TN3270E;
	}
	
	
	public void initiate(TelnetClient client) {
		// We don't initiate.
	}
	
	public int consumeIncomingBytes(byte[] incoming, TelnetClient client) {
		return 0;
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		return nill;
	}
}
