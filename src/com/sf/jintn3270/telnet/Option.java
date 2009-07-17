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
public abstract class Option {
	protected ByteArrayOutputStream out;
	
	private boolean enabled;
	
	/**
	 * Can be returned by outgoingBytes by subclasses that never send data
	 * to the remote host.
	 */
	protected byte[] nill;
	
	protected Option() {
		enabled = false;
		nill = new byte[0];
		out = new ByteArrayOutputStream();
	}
	
	
	/**
	 * A string representation of the Telnet Option
	 */
	public abstract String getName();
	
	
	/**
	 * Returns the Telnet Option Code
	 */
	public abstract byte getCode();
	
	
	/**
	 * Send an IAC DO, WILL, DON't, or WON'T, depeding on how you'd like
	 * to start option negotiation.
	 */
	public abstract void initiate(TelnetClient client);
	
	
	/**
	 * Potentially handle incoming bytes.
	 *
	 * @param incoming The byte[] array that may be an entire frame or 
	 * partial frame of data. 
	 * @param client The TelnetClient which has received the data.
	 * @return The number of bytes consumed by reading from the beginning of 
	 *         <code>incoming</code>. If no bytes were read, return 0. If the
	 *         bytes starting at <code>incoming[0]</code> are not understood
	 *         by this option, then we should return 0 and read nothing. Only 
	 *         return non-zero if the bytes starting at the beginning of the 
	 *         buffer are actionable by this Option.
	 */
	public int consumeIncomingBytes(byte[] incoming, TelnetClient client) {
		return 0;
	}
	
	public int consumeIncomingSubcommand(byte[] incoming, TelnetClient client) {
		return 0;
	}
	
	
	/**
	 * Retrieve the outgoing bytes from the <code>out</code> buffer and 
	 * send it along to the client.
	 */
	public byte[] outgoingBytes(ByteArrayOutputStream toSend, TelnetClient client) {
		byte[] ret = out.toByteArray();
		out.reset();
		return ret;
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
}
