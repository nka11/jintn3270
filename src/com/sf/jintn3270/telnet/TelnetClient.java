package com.sf.jintn3270.telnet;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;

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
	
	public static final byte IAC = (byte)255;
	
	private static final byte DONT = (byte)254;
	private static final byte DO = (byte)253;
	private static final byte WONT = (byte)252;
	private static final byte WILL = (byte)251;
	
	
	
	static final byte BRK = (byte)243; // Break
	static final byte IP = (byte)244; // Interrupt Process
	static final byte AO = (byte)245; // Abort Output
	static final byte AYT = (byte)246; // Are you there
	static final byte EC = (byte)247; // Erase Character
	static final byte EL = (byte)248; // Erase Line
	static final byte GA = (byte)249; // Go Ahead
	static final byte SB = (byte)250; // Subnegotiation!
	
	/**
	 * Construct a new TelnetClient that will connect to the given host, port, and use ssl or not.
	 */
	public TelnetClient(String host, int port, boolean ssl) {
		this.host = host;
		this.port = port;
		this.ssl = ssl;
		
		sock = null;
		
		options = new ConcurrentLinkedQueue<Option>();
		options.add(new SuppressGA());
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
			sock = SSLSocketFactory.getDefault().createSocket(host, port);
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
		for (Option o : options) {
			sendWill(o.getCode());
		}
		
		System.out.println("Connected!");
	}
	
	/**
	 * Determines if we have an output channel
	 */
	public boolean isConnected() {
		return sock != null;
	}
	
	/**
	 * Add the option and send a WILL.
	 */
	public void addOption(Option o) {
		options.add(o);
		if (isConnected()) {
			sendWill(o.getCode());
		}
	}
	
	
	/**
	 * Remove the option and send a WONT
	 */
	public void removeOption(Option o) {
		if (o.isEnabled()) {
			sendWont(o.getCode());
		}
	}
	
	
	/**
	 * Writes a DO option to the output buffer
	 */
	void sendDo(byte code) {
		// IAC, DO, <code>
		try {
			outStream.write(new byte[] {IAC, DO, code});
		} catch (IOException e) {}
	}
	
	/**
	 * Writes a WILL option to the output buffer.
	 */
	void sendWill(byte code) {
		// IAC, DO, <code>
		try {
			outStream.write(new byte[] {IAC, WILL, code});
		} catch (IOException e) {}
	}
	
	/**
	 * Writes a DONT option to the output buffer.
	 */
	void sendDont(byte code) {
		// IAC, DONT, <code>
		try {
			outStream.write(new byte[] {IAC, DONT, code});
		} catch (IOException e) {}
	}
	
	/**
	 * Writes a WONT option to the output buffer.
	 */
	void sendWont(byte code) {
		// IAC, WONT, <code>
		try {
			outStream.write(new byte[] {IAC, WONT, code});
		} catch (IOException e) {}
	}
	
	
	/**
	 * The read/write loop (I/O thread) passing byte[] buffers to this method,
	 * In this implementation, we look for IAC, and other TELNET commands to 
	 * respond to. If we can handle the incoming data, we return the number of 
	 * bytes we've read & handled. These bytes are then consumed by the input 
	 * stream after this method is called. If zero bytes are consumed, the 
	 * data is considered to be an incomplete frame, and will be re-delivered 
	 * when more data is available.
	 * 
	 * @param incoming The bytes coming in from the buffer.
	 * @return The number of bytes consumed in this pass.
	 */
	private int consumeIncomingBytes(byte[] incoming) {
		int read = 0;
		if (incoming[0] == IAC) {
			if (incoming.length >= 2) {
				switch(incoming[1]) {
					case IAC: // Handle escaped 255. we leave 'read' at 0 so this will be passed along.
						// Trim the first byte.
						System.arraycopy(incoming, 1, incoming, 0, incoming.length - 1);
						incoming[incoming.length - 1] = (byte)0x00;
						break;
					case WILL: // Option Offered! Send do or don't.
						if (incoming.length >= 3) {
							System.out.println("Received WILL: " + incoming[2]);
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
							System.out.println("Received DO: " + incoming[2]);
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
							System.out.println("Received WONT/DONT: " + incoming[2]);
							for (Option o : options) {
								if (o.getCode() == incoming[2]) {
									o.setEnabled(false);
								}
							}
						}
						read = 3;
						break;
					case GA: // We got a GO-Ahead.
						System.out.println("_GA_");
						//TODO: Determine what to actually do about this.
						read = 2;
						break;
					case EL: // Erase Line
						read = 2;
						break;
					case EC: // Erase Character
						read = 2;
						break;
					
					default:
						System.out.println("UNKNOWN IAC: " + incoming[2]);
				}
			}
		}
		
		// If we didn't handle anything, we need to find something that can.
		if (read == 0) {
			// For any enabled options, let's try them.
			for (Option o : options) {
				if (o.isEnabled()) {
					read += o.consumeIncomingBytes(incoming);
				}
			}
			
			// Read up to an IAC, or the end of the incoming buffer.
			// TODO: Pass along this data to a TerminalModel.
			for (byte b : incoming) {
				if (b == IAC) {
					break;
				}
				read++;
			}
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
				outStream.write(o.outgoingBytes(outStream));
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
		TelnetClient client = new TelnetClient(args[0], Integer.parseInt(args[1]), Boolean.valueOf(args[2]).booleanValue());
		client.addOption(new EndOfRecord());
		new Thread(client).start();
	}
}
