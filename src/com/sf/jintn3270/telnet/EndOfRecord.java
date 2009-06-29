package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

import com.sf.jintn3270.TerminalModel;

public class EndOfRecord extends Option {
	public static final byte EOR = (byte)239; // End of Record.
	
	byte[] markEOR;
	
	public EndOfRecord() {
		super();
		markEOR = new byte[] {TelnetClient.IAC, EOR};
	}
	
	public String getName() {
		return "EndOfRecord";
	}
	
	public byte getCode() {
		return (byte)25;
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend) {
		byte[] ret;
		if (isEnabled() && toSend.size() > 0) {
			ret = markEOR;
		} else {
			ret = new byte[0];
		}
		return ret;
	}
	
	public int consumeIncomingBytes(byte[] incoming, TelnetClient client) {
		if (incoming[0] == TelnetClient.IAC && incoming[1] == EOR) {
			return 2;
		}
		return 0;
	}
}
