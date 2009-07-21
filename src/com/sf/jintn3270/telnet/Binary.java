package com.sf.jintn3270.telnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sf.jintn3270.TerminalModel;

/**
 * Implementation of RFC 856
 * 
 * When this option is enabled, all incoming non-IAC data is delivered to the
 * OutputStream specified during construction.
 */
public class Binary extends Option {
	public static final short BINARY = 0;
	
	OutputStream binaryDestination;
	
	public Binary(OutputStream destination) {
		super();
		this.binaryDestination = destination;
	}
	
	public String getName() {
		return "Binary";
	}
	
	public short getCode() {
		return BINARY;
	}
	
	/**
	 * Do nothing to initiate.
	 */
	public void initiate(TelnetClient client) {}
	
	
	public short[] outgoing(ByteArrayOutputStream queuedForSend, TelnetClient client) {
		return nill;
	}
	
	public int consumeIncoming(short[] incoming, TelnetClient client) {
		int consumed = 0;
		if (isEnabled() && incoming.length > 0) {
			for (int i = 0; i < incoming.length; i++) {
				// Default -- the data is not an IAC.
				if (incoming[i] != client.IAC) {
					try {
						binaryDestination.write((byte)incoming[i]);
						consumed++;
					} catch (IOException ioe) {
						System.err.println("Error writing binary data to destination.\n" + ioe.toString());
					}
				// If we run into IAC, IAC, deliver just the single instance of IAC as decoded binary.
				} else if (incoming[i] == client.IAC &&
					      i + 1 < incoming.length && // avoids IndexOutOfBoundsException, ensures there's at least two bytes remaining.
				           incoming[i + 1] == client.IAC) 
				{
					try {
						binaryDestination.write((byte)incoming[i]);
						consumed++;
						i++; // skip ahead.
					} catch (IOException ioe) {
						System.err.println("Error writing binary data to destination.\n" + ioe.toString());
					}
				} else { // We == IAC, but either we're at the end of the available data, or the next byte is not an escaped 255, but a real IAC.
					break;
				}
			}
		}
		System.out.println("Binary consuming " + consumed + " bytes"); 
		return consumed;
	}
}
