package com.sf.jintn3270.tn3270;

import java.io.UnsupportedEncodingException;

import com.sf.jintn3270.TerminalCharacter;

/**
 * TNCharacter - The buffer representation of a character for 3270 terminals.
 * 
 * 
 * 
 */
public class TNCharacter extends TerminalCharacter {
	public static final short ALL_ATTRIBS = 0x00;
	public static final short EXTENDED_HIGHLIGHT = 0x41;
	public static final short FOREGROUND_COLOR = 0x42;
	public static final short BACKGROUND_COLOR = 0x45;
	public static final short CHARACTER_SET = 0x43;
	public static final short TRANSPARENCY = 0x46;
	
	boolean attributesSet;
	short extendedHighlight;
	short foreground;
	short background;
	short characterSet;
	short transparency;
	
	protected TNCharacter(short code, char display) {
		super(code, display);
		attributesSet = false;
		extendedHighlight = 0;
		foreground = 0;
		background = 0;
		characterSet = 0;
		transparency = 0;
	}
	
	public TNCharacter(short code) {
		this(code, ' ');
		
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
		this((short)0, ascii);
		
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
	
	public void applyAttribute(short type, short value) {
		attributesSet = true;
		switch (type) {
			case ALL_ATTRIBS: {
				extendedHighlight = 0;
				foreground = 0;
				background = 0;
				characterSet = 0;
				transparency = 0;
				break;
			}
			case EXTENDED_HIGHLIGHT: {
				extendedHighlight = value;
				break;
			}
			case FOREGROUND_COLOR: {
				foreground = value;
				break;
			}
			case BACKGROUND_COLOR: {
				background = value;
				break;
			}
			case CHARACTER_SET: {
				characterSet = value;
				break;
			}
			case TRANSPARENCY: {
				transparency = value;
				break;
			}
		}
	}
	
	public boolean areAttributesSet() {
		return attributesSet;
	}
	
	public void copyAttributes(TNCharacter src) {
		extendedHighlight = src.extendedHighlight;
		foreground = src.foreground;
		background = src.background;
		characterSet = src.characterSet;
		transparency = src.transparency;
	}
	
	public boolean display() {
		return true;
	}
}
