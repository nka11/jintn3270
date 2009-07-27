package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.telnet.UByteOutputStream;

import com.sf.jintn3270.tn3270.stream.*;

import java.io.IOException;
import java.io.OutputStream;

import java.util.HashMap;


/**
 * StreamParser reads data being written to it (it's an OutputStream, afterall)
 * as if it's a 3270 data stream, and takes appropriate action upon the 
 * TerminalModel3278 it's constructed to control.
 */
public class StreamDecoder extends UByteOutputStream {
	private TerminalModel3278 terminal;
	
	private HashMap<Short, Command> commandMap;
	
	public StreamDecoder(TerminalModel3278 terminal) {
		super(null);
		commandMap = new HashMap<Short, Command>();
		addCommand(new Write());
		addCommand(new EraseWrite());
		addCommand(new EraseWriteAlternate());
		addCommand(new ReadBuffer());
		addCommand(new ReadModified());
		addCommand(new ReadModifiedAll());
		addCommand(new EraseAllUnprotected());
		addCommand(new WriteStructuredField());
		
		this.terminal = terminal;
	}
	
	private void addCommand(Command c) {
		commandMap.put(c.getCode(), c);
	}
	
	public void close() throws IOException {
		return;
	}
	
	public void flush() throws IOException {
		return;
	}
	
	
	public void write(short[] b) {
		this.write(b, 0, b.length);
	}
	
	
	public void write(short[] b, int off, int len) {
		int nextByte = off;
		System.out.println("StreamParser received " + len  + " bytes");
		
		// We must consume the entire message.
		while (nextByte < off + len) {
			// Read the command.
			short commandCode = b[nextByte];
			Command c = commandMap.get(commandCode);
			if (c != null) {
				// Dispatch!
				nextByte += c.preform(terminal, b, off, len);
			} else {
				System.err.println("UNKNOWN COMMAND: " + Integer.toHexString(commandCode));
			}
		}
	}
}
