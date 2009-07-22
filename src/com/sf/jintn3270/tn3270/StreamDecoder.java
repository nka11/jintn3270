package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.telnet.UByteOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * StreamParser reads data being written to it (it's an OutputStream, afterall)
 * as if it's a 3270 data stream, and takes appropriate action upon the 
 * TerminalModel3278 it's constructed to control.
 */
public class StreamDecoder extends UByteOutputStream {
	private TerminalModel3278 terminal;
	
	public StreamDecoder(TerminalModel3278 terminal) {
		super(null);
		
		this.terminal = terminal;
	}
	
	public void close() throws IOException {
		return;
	}
	
	public void flush() throws IOException {
		return;
	}
	
	public void write(byte[] b, int off, int len) throws IOException {
		System.out.println("StreamParser received " + len  + " bytes");
	}
	
	public void write(byte[] b) throws IOException {
		System.out.println("StreamParser received " + b.length + " bytes");
	}
	
	public void write(int b) throws IOException {
		System.out.println("StreamParser received byte");
	}
}
