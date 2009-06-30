package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

import com.sf.jintn3270.TerminalModel;

/**
 * Implementation of RFC 885
 * 
 * When this option is enabled, each outgoing frame being sent to the remote
 * host is appended with and EOR command. Incoming frames with IAC, EOR are 
 * then consumed by this option.
 */
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
	
	public void initiate(TelnetClient client) {
		client.sendWill(getCode());
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		byte[] ret = nill;
		if (isEnabled() && toSend.size() > 0) {
			ret = markEOR;
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
