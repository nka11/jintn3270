package com.sf.jintn3270.telnet;

import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

public class UByteInputStream extends FilterInputStream {
	public UByteInputStream(InputStream in) {
		super(in);
	}
	
	public int read(short[] s) throws IOException {
		byte[] b = new byte[s.length];
		int read = in.read(b);
		fromUByte(b, s, read);
		return read;
	}
	
	
	private static void fromUByte(byte[] b, short[] s, int length) {
		for (int i = 0; i < length; i++) {
			s[i] = (short)(b[i] & 0xff);
		}
	}
}
