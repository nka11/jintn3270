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
	/* 3270 Commands */
	private static final int READ_MODIFIED_ALL = 0x6e;
	private static final int ERASE_ALL_UNPROTECTED = 0x6f;
	private static final int ERASE_WRITE_ALTERNATE = 0x7e;
	private static final int WRITE = 0xf1;
	private static final int READ_BUFFER = 0xf2;
	private static final int WRITE_STRUCTURED_FIELD = 0xf3;
	private static final int ERASE_WRITE = 0xf5;
	private static final int READ_MODIFIED = 0xf6;
	
	
	
	private TerminalModel3278 terminal;
	
	private int currentCommand = 0;
	
	
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
		int nextByte = 0;
		System.out.println("StreamParser received " + len  + " bytes");
		
		if (currentCommand == 0) {
			currentCommand = b[nextByte++] & 0xff;
		}
		
		System.out.println("Processing data from Command: " + Integer.toHexString(currentCommand));
		switch (currentCommand) {
			case READ_MODIFIED_ALL:
				break;
			case ERASE_ALL_UNPROTECTED:
				break;
			case ERASE_WRITE_ALTERNATE:
				break;
			case WRITE:
				break;
			case READ_BUFFER:
				break;
			case WRITE_STRUCTURED_FIELD:
				break;
			case ERASE_WRITE:
				break;
			case READ_MODIFIED:
				break;
		}
	}
	
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	
	public void write(int b) throws IOException {
		write(new byte[] {(byte)b});
	}
}
