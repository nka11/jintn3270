package com.sf.jintn3270.telnet;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class that implements a client. Yay.
 */
public class TelnetClient implements Runnable {
	String host;
	int port;
	boolean ssl;
	
	Socket sock = null;
	BufferedInputStream inStream;
	
	ByteArrayOutputStream outStream;
	
	AbstractQueue<Option> options;
	
	private static final byte IAC = (byte)255;
	
	private static final byte DONT = (byte)254;
	private static final byte DO = (byte)253;
	private static final byte WONT = (byte)252;
	private static final byte WILL = (byte)251;
	
	/**
	 * Construct a new TelnetClient that will connect to the given host, port, and use ssl or not.
	 */
	public TelnetClient(String host, int port, boolean ssl) {
		this.host = host;
		this.port = port;
		this.ssl = ssl;
		
		sock = null;
		
		options = new ConcurrentLinkedQueue<Option>();
		outStream = new ByteArrayOutputStream();
	}
	
	/**
	 * You may want to call this from another thread...
	 */
	void connect() throws UnknownHostException, IOException {
		if (isConnected()) {
			disconnect();
		}
		
		if (!ssl) {
			sock = new Socket(host, port);
		} else {
			// TODO: Implement SSL socket creation.
		}
		
		if (sock != null) {
			sock.setKeepAlive(true);
			inStream = new BufferedInputStream(sock.getInputStream(), sock.getReceiveBufferSize());
			connected();
		}
	}
	
	/**
	 * Disconnects from the host.
	 */
	public void disconnect() {
		if (sock != null) {
			try {
				sock.close();
			} catch (IOException ioe) {
			} finally {
				sock = null;
			}
		}
		disconnected();
	}
	
	/**
	 * Invoked when we're disconnected
	 */
	protected void disconnected() {
		System.out.println("Disconnected!");
	}
	
	/**
	 * Invoked when we're connected
	 */
	public void connected() {
		System.out.println("Connected!");
	}
	
	/**
	 * Determines if we have an output channel
	 */
	public boolean isConnected() {
		return sock != null;
	}
	
	
	public void addOption(Option o) {
		options.add(o);
		if (isConnected()) {
			sendWill(o.getCode());
		}
	}
	
	
	public void removeOption(Option o) {
		if (o.isEnabled()) {
			sendWont(o.getCode());
		}
	}
	
	
	
	void sendDo(byte code) {
		// IAC, DO, <code>
		try {
			outStream.write(new byte[] {IAC, DO, code});
		} catch (IOException e) {}
	}
	
	void sendWill(byte code) {
		// IAC, DO, <code>
		try {
			outStream.write(new byte[] {IAC, WILL, code});
		} catch (IOException e) {}
	}
	
	void sendDont(byte code) {
		// IAC, DONT, <code>
		try {
			outStream.write(new byte[] {IAC, DONT, code});
		} catch (IOException e) {}
	}
	
	void sendWont(byte code) {
		// IAC, WONT, <code>
		try {
			outStream.write(new byte[] {IAC, WONT, code});
		} catch (IOException e) {}
	}
	
	
	
	
	
	/**
	 * Stub for now...
	 */
	private int consumeIncomingBytes(byte[] incoming) {
		int read = 0;
		if (incoming[0] == IAC) {
			if (incoming.length >= 2) {
				switch(incoming[1]) {
					case IAC: // Handle escaped 255.
						// Trim the first byte.
						System.arraycopy(incoming, 1, incoming, 0, incoming.length - 1);
						incoming[incoming.length - 1] = (byte)0x00;
						read = 1;
						break;
					case WILL: // Option Offered! Send do or don't.
						if (incoming.length >= 3) {
							boolean dosent = false;
							for (Option o : options) {
								if (o.getCode() == incoming[2]) {
									sendDo(o.getCode());
									dosent = true;
									break;
								}
							}
							if (!dosent) {
								sendDont(incoming[2]);
							}
						}
						read = 3;
						break;
					case DO: // Option requested. Send will or wont!
						if (incoming.length >= 3) {
							boolean enabled = false;
							for (Option o : options) {
								if (o.getCode() == incoming[2]) {
									o.setEnabled(true);
									enabled = true;
									break;
								}
							}
							if (enabled) {
								sendWill(incoming[2]);
							} else {
								sendWont(incoming[2]);
							}
						}
						read = 3;
						break;
					case DONT: // Handle disable requests.
					case WONT:
						if (incoming.length >= 3) {
							for (Option o : options) {
								if (o.getCode() == incoming[2]) {
									o.setEnabled(false);
								}
							}
						}
						read = 3;
						break;
				}
			}
		}
		
		// For any enabled options, let's pass them data!
		for (Option o : options) {
			if (o.isEnabled()) {
				read += o.consumeIncomingBytes(incoming);
			}
		}
		
		// Read up to an IAC, or the end of the buffer, and dump it to screen.
		for (byte b : incoming) {
			if (b == IAC) {
				break;
			}
			read++;
		}
		if (incoming[0] != IAC) {
			System.out.write(incoming, 0, read);
		}
		
		System.out.println("consumeIncomingBytes(" + incoming.length + " bytes)");
		return read;
	}
	
	
	/**
	 * Stub for now...
	 */
	private byte[] outgoingBytes() {
		// collect all options outgoing bytes.
		for (Option o : options) {
			try {
				outStream.write(o.outgoingBytes());
			} catch (IOException ioe) {
			}
		}
		
		// append any of ours.
		byte[] out = outStream.toByteArray();
		outStream.reset();
		
		return out;
	}
	
	
	/**
	 * Start it up, baby.
	 */
	public void run() {
		try {
			connect();
		} catch (Exception ex) {
			disconnected();
		}
		
		byte[] buf;
		int available;
		int consumed;
 		while (sock != null && !sock.isClosed()) {
			try {
				consumed = 0;
				
				// Do I have data to read?
				available = inStream.available();
				if (available > 0) {
					inStream.mark(available);
					
					buf = new byte[available];
					inStream.read(buf);
					
					// Determine how many bytes (if any) we've successfully
					// consumed in this pass.
					consumed = consumeIncomingBytes(buf);
					
					// reset the stream mark, then skip past the consumed bytes.
					inStream.reset();
					if (consumed > 0) {
						inStream.skip(consumed);
					}
				}
				
				
				// Do I have data to write?
				buf = outgoingBytes();
				if (buf.length > 0) {
					System.out.println("" + buf.length + " bytes to send");
					sock.getOutputStream().write(buf);
					sock.getOutputStream().flush();
				}
			} catch (Exception ex) {
			} 
			// Play nice with thread schedulers.
			Thread.yield();
		}
	}
	
	
	/**
	 * Oh yeah, baby.
	 */
	public static void main(String[] args) {
		TelnetClient client = new TelnetClient(args[0], Integer.parseInt(args[1]), false);
		new Thread(client).start();
	}
}
