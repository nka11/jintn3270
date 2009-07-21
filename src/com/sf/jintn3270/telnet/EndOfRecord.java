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
	public static final short EOR = 239;
	public static final short[] markEOR = new short[] {IAC, EOR};
	
	public EndOfRecord() {
		super();
	}
	
	public String getName() {
		return "EndOfRecord";
	}
	
	public short getCode() {
		return 25;
	}
	
	public void initiate(TelnetClient client) {
		client.sendWill(getCode());
	}
	
	public short[] outgoing(ByteArrayOutputStream queuedForSend, TelnetClient client) {
		short[] ret = nill;
		if (isEnabled() && queuedForSend.size() > 0) {
			ret = markEOR;
		}
		return ret;
	}
	
	public int consumeIncoming(short[] incoming, TelnetClient client) {
		if (incoming[0] == TelnetClient.IAC && incoming[1] == EOR) {
			return 2;
		}
		return 0;
	}
}
