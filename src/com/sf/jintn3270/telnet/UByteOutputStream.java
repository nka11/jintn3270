package com.sf.jintn3270.telnet;

import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

public class UByteOutputStream extends FilterOutputStream {
	public UByteOutputStream(OutputStream out) {
		super(out);
	}
	
	public void write(short[] s) {
		try {
			write(toUByte(s));
		} catch (IOException ioe) {
		}
	}
	
	public void write(short[] s, int off, int len) {
		try {
			write(toUByte(s), off, len);
		} catch (IOException ioe) {
		}
	}
	
	/**
	 * Converts Java 'short' array types to unsigned byte equivalents.
	 */
	private static byte[] toUByte(short[] s) {
		byte[] out = new byte[s.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (byte)s[i];
		}
		return out;
	}
}