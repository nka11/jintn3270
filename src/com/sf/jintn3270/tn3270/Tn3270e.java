package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.telnet.Option;
import com.sf.jintn3270.telnet.TelnetClient;
import com.sf.jintn3270.telnet.TelnetConstants;

import java.util.EnumSet;

import java.io.ByteArrayOutputStream;

public class Tn3270e extends Option implements TelnetConstants {
	public static final byte TN3270E = (byte)40;
	
	public enum Command {
		ASSOCIATE, 
		CONNECT, 
		DEVICE_TYPE, 
		FUNCTIONS, 
		IS, 
		REASON, 
		REJECT, 
		REQUEST, 	
		SEND
	};
	
	public enum Reason {
		CONN_PARTNER,
		DEVICE_IN_USE,
		INV_ASSOCIATE,
		INV_DEVICE_NAME,
		INV_DEVICE_TYPE,
		TYPE_NAME_ERROR,
		UNKNOWN_ERROR,
		UNSUPPORTED_REQ
	};
	
	public enum Function {
		BIND_IMAGE,
		DATA_STREAM_CTL,
		RESPONSES,
		SCS_CTL_CODES,
		SYSREQ
	};
	
	
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
	}
	
	public int consumeIncomingBytes(byte[] incoming, TelnetClient client) {
		return 0;
	}
	
	public int consumeIncomingSubcommand(byte[] incoming, TelnetClient client) {
		// TODO: Look for IAC, SE.
		int length = 0;
		for (int i = 0; i < incoming.length - 1; i++) {
			if (incoming[i] == IAC && incoming[i + 1] == SE) {
				length = i + 2;
			}
		}
		System.out.println("Subcommand is " + length + " bytes long.");
		if (length > 0) {
			System.out.println("Subcommand: " + resolveValue(incoming[3], Command.class) + " " + resolveValue(incoming[4], Command.class));
			
			
			
		}
		return length;
	}
	
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		return nill;
	}
	
	
	/**
	 * Resolves a byte to an Enumeration Value.
	 */
	private Enum resolveValue(byte ordinal, Class<? extends Enum> type) {
		for (Object e : EnumSet.allOf(type)) {
			if (ordinal == ((Enum)e).ordinal()) {
				return (Enum)e;
			}
		}
		return null;
	}
}
