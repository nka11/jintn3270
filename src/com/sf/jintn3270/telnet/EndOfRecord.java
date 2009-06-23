package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

public class EndOfRecord extends Option {
	byte[] markEOR;
	
	public EndOfRecord() {
		super();
		markEOR = new byte[] {TelnetClient.IAC, TelnetClient.EOR};
	}
	
	public String getName() {
		return "EndOfRecord";
	}
	
	public byte getCode() {
		return (byte)25;
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend) {
		byte[] ret;
		if (toSend.size() > 0) {
			ret = markEOR;
		} else {
			ret = new byte[0];
		}
		return ret;
	}
}
