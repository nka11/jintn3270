package com.sf.jintn3270.telnet;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.BufferedInputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;


/**
 * Class that implements a client. Yay.
 */
public class TelnetClient implements Runnable {
	String host;
	int port;
	boolean ssl;
	
	Socket sock = null;
	BufferedInputStream inStream;
	
	/**
	 * Construct a new TelnetClient that will connect to the given host, port, and use ssl or not.
	 */
	public TelnetClient(String host, int port, boolean ssl) {
		this.host = host;
		this.port = port;
		this.ssl = ssl;
		
		sock = null;
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
	
	/**
	 * Stub for now...
	 */
	private boolean actionOnBytes(byte[] incomming) {
		System.out.println("actionOnBytes(" + incomming.length + " bytes)");
		return false;
	}
	
	
	/**
	 * Stub for now...
	 */
	private byte[] outgoingBytes() {
		return new byte[0];
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
 		while (sock != null && !sock.isClosed()) {
			try {
				// Do I have data to read?
				available = inStream.available();
				if (available > 0) {
					inStream.mark(available);
					
					buf = new byte[available];
					inStream.read(buf);
					
					// If we cannot take action on the read bytes, reset the mark.
					if (!actionOnBytes(buf)) {
						inStream.reset();
					}
				}
				
				
				// Do I have data to write?
				buf = outgoingBytes();
				System.out.println("" + buf.length + " bytes to send");
				if (buf.length > 0) {
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
