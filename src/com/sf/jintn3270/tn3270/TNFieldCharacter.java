package com.sf.jintn3270.tn3270;

import com.sf.jintn3270.TerminalCharacter;

public class TNFieldCharacter extends TNCharacter {
	short validation;
	short outline;
	
	public static final short FIELD_OUTLINE = 0xc2;
	public static final short FIELD_VALIDATION = 0xc1;
	
	public static final short DISPLAY_NO_PEN_DETECTION = 0;
	public static final short DISPLAY_PEN_DETECTION = 1;
	public static final short INTENSE_PEN_DETECTION = 2;
	public static final short NONDISPLAY_NO_PEN_DETECTION = 3;
	
	public TNFieldCharacter(short code) {
		super(code, ' ');
		validation = 0;
		outline = 0;
	}
	
	public boolean isProtected() {
		return (code & 0x20) == 0x20;
	}
	
	public void setProtected(boolean b) {
		if (b) {
			code = (short)(code | 0x20);
		} else if (isProtected() != b) { // if the state is changing....
			code = (short)(code ^ 0x20);
		}
	}
	
	public boolean isModified() {
		return (code & 0x01) == 0x01;
	}
	
	public void setModified(boolean b) {
		if (b) {
			code = (short)(code | 0x01);
		} else if (isModified() != b) {
			code = (short)(code ^ 0x01);
		}
	}
	
	public boolean isNumeric() {
		return (code & 0x10) == 0x10;
	}
	
	public void setNumeric(boolean b) {
		if (b) {
			code = (short)(code | 0x10);
		} else if (isNumeric() != b) {
			code = (short)(code ^ 0x10);
		}
	}
	
	public short getDisplayMode() {
		// shift right 4 bits, back left 4 (zero-fill low-order bits)
		// XOR that with the original value, masking out the 4 high-order bits.
		// Shift right 2 bits, leaving bits 5 and 6 of the original 8 bit value.
		// return that as a short.
		return (short)((code >> 4 << 4 ^ code) >> 2);
	}
	
	public void applyAttribute(short type, short value) {
		super.applyAttribute(type, value);
		switch (type) {
			case FIELD_OUTLINE: {
				outline = value;
				break;
			}
			case FIELD_VALIDATION: {
				validation = value;
				break;
			}
		}
	}
	
	public boolean display() {
		return false;
	}
}
