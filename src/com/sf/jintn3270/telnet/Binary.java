package com.sf.jintn3270.telnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sf.jintn3270.telnet.UByteOutputStream;

/**
 * Implementation of RFC 856
 * 
 * When this option is enabled, all incoming non-IAC data is delivered to the
 * OutputStream specified during construction.
 */
public class Binary extends Option {
	public static final short BINARY = 0;
	
	UByteOutputStream binaryDestination;
	
	public Binary(UByteOutputStream destination) {
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
			// Deliver as much data to the target stream at once as possible.
			// To do this, we'll search the incoming buffer for IACs.
			boolean foundCommand = false;
			int endFrame = 0;
			int trim = 0;
			for (; endFrame < (incoming.length - trim) && !foundCommand; endFrame++) {
				if (incoming[endFrame] == IAC &&
				    endFrame < incoming.length &&
			         incoming[endFrame + 1] != IAC)
				{
					// We found an IAC before the end of the buffer that's not an escaped 255.
					foundCommand = true;
				} else if (
					incoming[endFrame] == IAC &&
					endFrame < incoming.length &&
					incoming[endFrame + 1] == IAC)
				{
					// We found an IAC IAC. THis is an escaped value 255.
					System.arraycopy(incoming, endFrame + 1, incoming, endFrame, incoming.length - endFrame - 1);
					trim++;
				}
			}
			
			// If we trimmed, update the incoming buffer reference.
			if (trim > 0) {
				short[] trimmed = new short[incoming.length - trim];
				System.arraycopy(incoming, 0, trimmed, 0, trimmed.length);
				incoming = trimmed;
			}
			
			binaryDestination.write(incoming, 0, endFrame);
			
			consumed = endFrame;
		}
		return consumed;
	}
}
