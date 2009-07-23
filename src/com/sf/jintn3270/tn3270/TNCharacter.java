package com.sf.jintn3270.tn3270;

import java.io.UnsupportedEncodingException;

import com.sf.jintn3270.TerminalCharacter;

public class TNCharacter extends TerminalCharacter {
	public TNCharacter(short code) {
		super(code, ' ');
		try {
			display = new String(new byte[]{(byte)code}, "Cp500").charAt(0);
			if (display < 32 || display == 255) {
				display = ' ';
			}
		} catch (UnsupportedEncodingException uee) {
			System.out.println("charset not supported. Awww shit.");
		}
	}
	
	public TNCharacter(char ascii) {
		super((short)0, ascii);
		try {
			byte[] b = new String(new byte[] {(byte)ascii}).getBytes("Cp500");
			code = (short)(b[0] & 0xff);
		} catch (UnsupportedEncodingException uee) {
			System.out.println("charset not supported. Awwww shit.");
		}
	}
}
