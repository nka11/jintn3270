package com.sf.jintn3270.util;

import java.io.*;
import java.net.Socket;

public class SocketDumper implements Runnable {
	String host;
	int port;
	String fileName;
	
	public SocketDumper(String host, int port, String fileName) {
		this.host = host;
		this.port = port;
		this.fileName = fileName;
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new SocketDumper(args[0], Integer.parseInt(args[1]), args[2]));
		t.start();
	}
	
	public void run() {
		Socket sock = null;
		DataInputStream istream = null;
		DataOutputStream ostream = null;
		FileOutputStream fos = null;
		try {
			sock = new Socket(host, port);
			istream = new DataInputStream(sock.getInputStream());
			ostream = new DataOutputStream(sock.getOutputStream());
			fos = new FileOutputStream(fileName);
			
			if (sock.isConnected()) {
				// Not sure why I send this...
				ostream.write((byte)0xCC);
				ostream.flush();
				
				try {
					byte b;
					while (true) {
						b = istream.readByte();
						
						fos.write(b);
						System.out.write(b);
					}
				} catch (IOException io) {
					System.out.println("" + io);
				} finally {
					if (istream != null) {
						try {
							istream.close();
						} catch (IOException ioe) {
						}
					}
				}
			} else {
				System.out.println("Not Connected.");
			}
		} catch (Exception ex) {
			System.out.println("" + ex);
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException ioe) {
				} finally {
					istream = null;
				}
			}
			
			if (!sock.isClosed()) {
				try {
					sock.close();
				} catch (IOException ioe) {
				}
			}
			
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException ioe) {
				} finally {
					fos = null;
				}
			}
		}
	}
}