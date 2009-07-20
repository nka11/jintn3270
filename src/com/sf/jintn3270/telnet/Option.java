package com.sf.jintn3270.telnet;

import java.io.ByteArrayOutputStream;

/**
 * Abstract base class for Telnet Option implementations.
 * 
 * This allows us to keep a clear separation of concerns for use in building up 
 * Telnet terminals with different capabilities. Allowing for the delegation of 
 * subcommand processing, terminal decoding, and even protocol I/O to any 
 * options which are enabled.
 */
public abstract class Option implements TelnetConstants {
	// TODO: Keep output as a Context for a TelnetClient. This is currently - NOT - thread  / multi-TelnetClient safe.
	
	/** Used to write unsigned shorts to the output buffer */
	protected UByteOutputStream out;
	/** Used to buffer the output from the Option for collection by a TelnetClient **/
	private ByteArrayOutputStream outBuf;
	
	private boolean enabled;
	
	/**
	 * Can be returned by outgoingBytes by subclasses that never send data
	 * to the remote host.
	 */
	protected short[] nill;
	
	protected Option() {
		enabled = false;
		nill = new short[0];
		outBuf = new ByteArrayOutputStream();
		out = new UByteOutputStream(outBuf);
	}
	
	
	/**
	 * A string representation of the Telnet Option
	 */
	public abstract String getName();
	
	
	/**
	 * Returns the Telnet Option Code
	 */
	public abstract short getCode();
	
	
	/**
	 * Send an IAC DO, WILL, DON't, or WON'T, depeding on how you'd like
	 * to start option negotiation.
	 */
	public abstract void initiate(TelnetClient client);
	
	
	/**
	 * Potentially handle incoming data.
	 *
	 * @param incoming The short[] array that may be an entire frame or 
	 * partial frame of data. 
	 * @param client The TelnetClient which has received the data.
	 * @return The number of shorts consumed by reading from the beginning of 
	 *         <code>incoming</code>. If no shorts were read, return 0. If the
	 *         shorts starting at <code>incoming[0]</code> are not understood
	 *         by this option, then we should return 0 and read nothing. Only 
	 *         return non-zero if the shorts starting at the beginning of the 
	 *         buffer are actionable by this Option.
	 */
	public int consumeIncoming(short[] incoming, TelnetClient client) {
		return 0;
	}
	
	/**
	 * handle the incoming data.
	 * 
	 * @param incoming The short[] array that may be an entire frame or 
	 * partial frame of data.
	 * @param client the TelnetClient which has received the data
	 * @return The number of shorts consumed by reading from the beginning of 
	 *         <code>incoming</code>. If no shorts were read, return 0.
	 */
	public int consumeIncomingSubcommand(short[] incoming, TelnetClient client) {
		return 0;
	}
	
	
	/**
	 * Retrieve the outgoing bytes from the <code>out</code> buffer and 
	 * send it along to the client.
	 */
	public short[] outgoing(ByteArrayOutputStream queuedForSend, TelnetClient client) {
		if (outBuf.size() > 0) {
			short[] ret = fromUByte(outBuf.toByteArray());
			outBuf.reset();
			return ret;
		}
		return nill;
	}
	
	/**
	 * Returns true if enabled, false if not
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Set by the TelnetClient when this option is either disabled or enabled.
	 */
	public void setEnabled(boolean b, TelnetClient client) {
		enabled = b;
		if (enabled) {
			System.out.println("   ENABLED " + getName());
		} else {
			System.out.println("   DISABLED " + getName());
		}
	}
	
	protected short[] fromUByte(byte[] b) {
		short[] ret = new short[b.length];
		for (int i = 0; i < b.length; i++) {
			ret[i] = (short)(b[i] & 0xff);
		}
		return ret;
	}
}
