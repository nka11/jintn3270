package com.sf.jintn3270.tn3278;

import com.sf.jintn3270.telnet.Binary;
import com.sf.jintn3270.telnet.EndOfRecord;
import com.sf.jintn3270.telnet.Option;
import com.sf.jintn3270.telnet.TelnetClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Regime3270 implements RFC 1041 - To do so, it encapsulates a Binary and 
 * EndOfRecord Option.
 * 
 * According to the RFC, successful negotiation of 1041 does not necessitate 
 * that BINARY or EOR actually be negotiated independently. Instead, their 
 * capability is implied by the negotiation of the REGIME option. Once the
 * Regime is successfully negotiated, BINARY and EOR is automatically enabled.
 * 
 */
public class Regime3270 extends Option {
	public static final byte REGIME = (byte)29;
	
	/** The stream where binary data is written */
	OutputStream parserStream;
	
	Binary binaryHandler;
	EndOfRecord eor;
	
	static byte IS = 0;
	static byte ARE = 1;
	
	/**
	 * Create a new Regime3270 option that writes incoming binary data to the 
	 * given destination.
	 */
	public Regime3270(OutputStream destination) {
		super();
		eor = new EndOfRecord();
		binaryHandler = null;
		parserStream = destination;
	}
	
	public String getName() {
		return "3270 Regime";
	}
	
	public byte getCode() {
		return REGIME;
	}
	
	/**
	 * We start negotiation with an IAC, DO
	 */
	public void initiate(TelnetClient client) {
		client.sendDo(getCode());
	}
	
	/**
	 * For the 3270 regime, we're looking for subcommands to kick us into and 
	 * out of 3270 data mode. In 3270 data mode, we'll pass along all data that
	 * is not a telnet IAC to a 3270 stream handler. Otherwise, we'll continue
	 * to process things as a normal NVT.
	 */
	public int consumeIncomingBytes(byte[] incoming, TelnetClient client) {
		int read = 0;
		if (incoming[0] == client.IAC &&
			incoming[1] == client.SB &&
			incoming[2] == getCode() &&
			incoming[3] == IS) 
		{
			int iacIndex = 0;
			// from [4] to the next IAC is the regime to be supported.
			for (int i = 4; i < incoming.length; i++) {
				if (incoming[i] == client.IAC) {
					iacIndex = i;
					break;
				}
			}
			
			// If we didn't find an IAC, we don't have a full data frame.
			if (iacIndex == 0) {
				read = 0;
			} else {
				byte[] regime = new byte[(iacIndex - 1) - 4];
				System.arraycopy(incoming, 4, regime, 0, regime.length);
				
				// Convert to string, without getting encoding exceptions.
				StringBuffer sb = new StringBuffer();
				for (byte b : regime) {
					sb.append((char)b);
				}
				String regimeName = sb.toString();
				System.out.println("received REGIME IS: " + regimeName);
				
				// If we got something other than an empty string back as 
				// part of our Regime negotiation, then we need to consider
				// everything coming in as 3270 binary data.
				if (!regimeName.equals("")) {
					binaryHandler = new Binary(parserStream);
					binaryHandler.setEnabled(true, client);
					eor.setEnabled(true, client);
					
					System.out.println("Created a binary handler");
				} else {
					binaryHandler.setEnabled(false, client);
					eor.setEnabled(false, client);
					
					binaryHandler = null;
					System.out.println("Disabled binary handler");
				}
				
				read = 4 + regime.length + 2;
			}
		} else if (binaryHandler != null) { // If we've installed a BINARY handler, pass along that data!
			read = binaryHandler.consumeIncomingBytes(incoming, client);
			
			byte[] remaining = new byte[incoming.length - read];
			System.arraycopy(incoming, read + 1, remaining, 0, remaining.length);
			read += eor.consumeIncomingBytes(remaining, client);
		}
		return read;
	}
	
	/**
	 * If we're enabled, defer to our parent.
	 * If we're not enabled, then we never have anything to write.
	 */
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		// If we're not enabled, we never send.
		if (!isEnabled()) {
			return nill;
		}
		return super.outgoingBytes(toSend, client);
	}
	
	
	public void setEnabled(boolean b, TelnetClient client) {
		super.setEnabled(b, client);
		
		// Once we're enabled, we send a subcommand to negotiate our regime
		if (isEnabled()) {
			try {
				out.write(new byte[] {client.IAC, client.SB, ARE});
				String[] names = client.getTerminalModel().getModelName();
				for (int i = 0; i < names.length; i++) {
					out.write(names[i].getBytes("ASCII"));
					if (i < names.length - 1) {
						out.write((byte)' ');
					}
				}
				out.write(new byte[] {client.IAC, client.SE});
			} catch (IOException ioe) {
				System.out.println("Error writing negotiation subcommand\n" + ioe.toString());
			}
		}
	}
}
