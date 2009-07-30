package com.sf.jintn3270.tn3270;

import java.io.UnsupportedEncodingException;

import com.sf.jintn3270.TerminalCharacter;

public class TNCharacter extends TerminalCharacter {
	public TNCharacter(short code) {
		super(code, ' ');
		
		switch (code) {
			case 0x00: // NUL, Null
			case 0x3f: // SUB, Substitute
			case 0x0c: // FF, Form Feed
			case 0x0d: // CR, Carriage Return
			case 0x15: // NL, New Line
			case 0x19: // EM, End Of Medium
			case 0xFF: // EO, Eight Ones
				display = ' ';
				break;
			
			case 0x1c: // DUP, Duplicate
				display = '"';
				break;
			
			case 0x1e: // FM, Field Mark
				display = ';';
				break;
				
			default: {
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
		}
	}
	
	public TNCharacter(char ascii) {
		super((short)0, ascii);
		
		switch (ascii) {
			case 0x00: 
			case 0x1c: 
			case 0x1e: 
			case 0x0c: 
			case 0x0D: 
			case 0x19: 
				code = (short)ascii;
				break;
			case 0x0a: 
				code = 0x15;
				display = ' ';
				break;
			
			default: {
				try {
					// Covert the displayable ascii (unicode, really) into ebcdic.
					byte[] b = new String(new byte[] {(byte)ascii}).getBytes("Cp500");
					code = (short)(b[0] & 0xff);
				} catch (UnsupportedEncodingException uee) {
					code = 0x00;
				}
			}
		}
	}
}
