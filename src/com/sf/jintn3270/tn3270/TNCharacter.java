package com.sf.jintn3270.tn3270;

import java.io.UnsupportedEncodingException;

import com.sf.jintn3270.TerminalCharacter;

public class TNCharacter extends TerminalCharacter {
	public TNCharacter(short code) {
		super(code, ' ');
		
		try {
			// Convert the code (ebcdic) into a java displayable.
			display = new String(new byte[]{(byte)code}, "Cp500").charAt(0);
			if (display < 32 || display == 255) {
				display = ' ';
			}
		} catch (UnsupportedEncodingException uee) {
			display = '?';
		}
	}
	
	public TNCharacter(char ascii) {
		super((short)0, ascii);
		try {
			// Covert the displayable ascii (unicode, really) into ebcdic.
			byte[] b = new String(new byte[] {(byte)ascii}).getBytes("Cp500");
			code = (short)(b[0] & 0xff);
		} catch (UnsupportedEncodingException uee) {
			code = 0x00;
		}
	}
}
